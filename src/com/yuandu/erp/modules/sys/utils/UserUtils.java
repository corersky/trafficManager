package com.yuandu.erp.modules.sys.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.google.common.collect.Maps;
import com.yuandu.erp.common.service.BaseService;
import com.yuandu.erp.common.utils.CacheUtils;
import com.yuandu.erp.common.utils.DateUtils;
import com.yuandu.erp.common.utils.SpringContextHolder;
import com.yuandu.erp.modules.business.dao.RechargeDao;
import com.yuandu.erp.modules.business.entity.Recharge;
import com.yuandu.erp.modules.sys.dao.AreaDao;
import com.yuandu.erp.modules.sys.dao.MenuDao;
import com.yuandu.erp.modules.sys.dao.OfficeDao;
import com.yuandu.erp.modules.sys.dao.RoleDao;
import com.yuandu.erp.modules.sys.dao.UserDao;
import com.yuandu.erp.modules.sys.dao.UserRechargeDao;
import com.yuandu.erp.modules.sys.entity.Area;
import com.yuandu.erp.modules.sys.entity.Menu;
import com.yuandu.erp.modules.sys.entity.Office;
import com.yuandu.erp.modules.sys.entity.Role;
import com.yuandu.erp.modules.sys.entity.Tactics;
import com.yuandu.erp.modules.sys.entity.User;
import com.yuandu.erp.modules.sys.entity.UserRecharge;
import com.yuandu.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yuandu.erp.webservice.utils.ProductCacheUtil;

/**
 * 用户工具类
 */
public class UserUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static UserRechargeDao userRechargeDao = SpringContextHolder.getBean(UserRechargeDao.class);
	private static RechargeDao rechargeDao = SpringContextHolder.getBean(RechargeDao.class);

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";
	public static final String USER_CACHE_NO_ = "no_";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user ==  null){
			user = userDao.get(id);
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 根据编号获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User getByNo(String no){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_NO_ + no);
		if (user ==  null){
			user = userDao.getByNo(no);
			if (user == null){
				return null;
			}
			CacheUtils.put(USER_CACHE, USER_CACHE_NO_ + user.getNo(), user);
		}
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null){
			user = userDao.getByLoginName(new User(null, loginName));
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 退款余额(web 用户调用)  先扣费 后 退款
	 * @return 扣款状态
	 * @throws Exception 
	 */
	public static String refundBalance(User user,String orderNo,String partnerOrderNo, String status) throws Exception{
		//判断工单是否已经扣费
		Recharge recharge = ProductCacheUtil.getRecharge(partnerOrderNo);
		if(recharge!=null && orderNo.equals(recharge.getOrderNo())){
			if(!"1".equals(status)&&!"4".equals(status)){//状态不为1 (1:成功  已经扣款) 不为4 (4:处理中  还在处理)
				
				if("1".equals(recharge.getIsRefund())){
					throw new RuntimeException("订单["+partnerOrderNo+"] 已经退款，无法更新状态");
				}
				//更新退款余额
				double balance = recharge.getBalance();
				User updateUser = new User();
				updateUser.setId(user.getId());
				updateUser.setBalance(balance);
				userDao.updateBlance(updateUser);
				
				Double adminFee = recharge.getAdminFee();
				updateAdminBalance(adminFee);
				//不清楚缓存  重新获取用户的  策略
				initFeeRate(user,balance,recharge.getCreateDate());
				//保存消费记录
				UserRecharge rechargeLog = new UserRecharge();
				rechargeLog.setBalance(user.getBalance());
				rechargeLog.setSupplier(user);
				rechargeLog.setRemarks("订单："+partnerOrderNo+"  充值失败退款！");
				rechargeLog.preInsert();
				rechargeLog.setCreateBy(user);
				rechargeLog.setUpdateBy(user);
				userRechargeDao.insert(rechargeLog);
				
				return "1";//扣款状态
			}
		}else{
			throw new RuntimeException("订单：["+partnerOrderNo+"] 不存在");
		}
		return "0";
	}
	
	private static void updateAdminBalance(Double adminFee) {
		//更新
		if(adminFee!=null){
			User admin = UserUtils.get("1");
			admin.setBalance(adminFee);
			userDao.updateBlance(admin);
			
			UserUtils.clearCache(admin);
		}
	}
	
	//0：电信 1：移动 2：联通
	public static String getCommonFeeRate(String operate){
		String feeRate = "1";
		
		if("0".equals(operate)){
			feeRate = DictUtils.getDictValue("电信商务汇率", "company_rate", "1");
		}else if("1".equals(operate)){
			feeRate = DictUtils.getDictValue("移动商务汇率", "company_rate", "1");
		}else if("2".equals(operate)){
			feeRate = DictUtils.getDictValue("联通商务汇率", "company_rate", "1");
		}
		return feeRate;
	}

	/**
	 * 判断用户余额是否充足
	 * @param user
	 * @param balance
	 * @return
	 */
	public static boolean isEnoughBalance(User user,Double balance){
		if(balance!=null&&user!=null){
			Double userbalance = user.getBalance();
			if(userbalance.compareTo(balance)>=0)
				return true;
		}
		return false;
	}
	
	/**
	 * 更新余额  购买后直接扣款
	 * @return
	 * @throws Exception 
	 */
	public static void purchaseBalance(User user,Double balance,Double adminFee,String partnerOrderNo) throws Exception{
		//更新余额
		double update = -balance;
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setBalance(update);
		userDao.updateBlance(updateUser);
		
		updateAdminBalance(-adminFee);//更新管理员价格
		//不清楚缓存  重新获取用户的  策略
		initFeeRate(user,update,new Date());
		//保存消费记录
		UserRecharge rechargeLog = new UserRecharge();
		rechargeLog.setBalance(user.getBalance());
		rechargeLog.setSupplier(user);
		rechargeLog.setRemarks("订单："+partnerOrderNo+"  购买扣款！");
		rechargeLog.preInsert();
		rechargeLog.setCreateBy(user);
		rechargeLog.setUpdateBy(user);
		userRechargeDao.insert(rechargeLog);
	}
	
	public static void initFeeRate(User user,double balance,Date dateTime){
		//判断是否是当月（如果不是 则不更新）
		if(!DateUtils.isSameMonth(dateTime,new Date())){return;}
		
		// 查询数据库
		Double monthConsume = user.getMonthConsume();
		if(monthConsume==null){
			String timeBegin = DateUtils.getDateTime0();
			String timeEnd = DateUtils.getDateTime23();
			monthConsume = rechargeDao.getMonthConsume(user.getId(),timeBegin,timeEnd);
			
			if(monthConsume==null){
				monthConsume = 0d;
			}
		}
		user.setMonthConsume(monthConsume-balance);
		user.setBalance(user.getBalance()+balance);
		//获取消费策略
		List<Tactics> racticsList = user.getTacticsList();
		Map<String,Tactics> tacticsMap = Maps.newHashMap();
		
		for(Tactics tc:racticsList){
			if(tc.getMinConsume()!=null && monthConsume.compareTo(tc.getMinConsume())>=0){
				if(tc.getMaxConsume()==null||tc.getMaxConsume()==0){
					tacticsMap.put(tc.getFeeType(), tc);
				}else if(monthConsume.compareTo(tc.getMaxConsume())<0){
					tacticsMap.put(tc.getFeeType(), tc);
				}
			}
		}
		//更新消费模式
		for(Tactics tactics:tacticsMap.values()){
			String feeType = tactics.getFeeType();
			Double feeRate = tactics.getFeeRate();
			
			if("0".equals(feeType)){
				user.setFeeRateDx(feeRate);
			}else if("1".equals(feeType)){
				user.setFeeRateYd(feeRate);
			}else if("2".equals(feeType)){
				user.setFeeRateLt(feeRate);
			}
		}
		userDao.updateFeeRate(user);//更新汇率
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		UserUtils.clearCache(getUser());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		user = UserUtils.get(user.getId());
		
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
		if (user.getOffice() != null && user.getOffice().getId() != null){
			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
		}
		CacheUtils.remove(USER_CACHE, USER_CACHE_NO_ + user.getNo());//清楚编码
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser(){
		Principal principal = getPrincipal();
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (roleList == null){
			User user = getUser();
			if (user.isAdmin()){
				roleList = roleDao.findAllList(new Role());
			}else{
				Role role = new Role();
				role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
				roleList = roleDao.findList(role);
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao.findAllList(new Menu());
			}else{
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao.findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){
			User user = getUser();
			if (user.isAdmin()){
				officeList = officeDao.findAllList(new Office());
			}else{
				Office office = new Office();
				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
				officeList = officeDao.findList(office);
			}
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeAllList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);
		if (officeList == null){
			officeList = officeDao.findAllList(new Office());
			putCache(CACHE_OFFICE_ALL_LIST, officeList);
		}
		return officeList;
	}
	
	/**
	 * 重新计算用户汇率
	 * @param user
	 * @param operators
	 * @return
	 */
	public static Double validateUserRate(User user, String operators) {
		//判断月消费是否为空
		Double monthConsume = user.getMonthConsume();
		if(monthConsume==null){
			// 查询数据库
			String timeBegin = DateUtils.getDateTime0();
			String timeEnd = DateUtils.getDateTime23();
			monthConsume = rechargeDao.getMonthConsume(user.getId(),timeBegin,timeEnd);
			if(monthConsume==null){
				monthConsume = 0d;
			}
			user.setMonthConsume(monthConsume);
		}
		//获取消费策略
		List<Tactics> racticsList = user.getTacticsList();
		Tactics current = null;
		for(Tactics tc:racticsList){
			if(!operators.equals(tc.getFeeType())){ continue; }//类型统一
			if(tc.getMinConsume()!=null && monthConsume.compareTo(tc.getMinConsume())>=0){
				if(tc.getMaxConsume()==null||tc.getMaxConsume()==0){
					current = tc;
					break;
				}else if(monthConsume.compareTo(tc.getMaxConsume())<0){
					current = tc;
					break;
				}
			}
		}
		if(current!=null){
			if("0".equals(operators)){
				user.setFeeRateDx(current.getFeeRate());
			}else if("1".equals(operators)){
				user.setFeeRateYd(current.getFeeRate());
			}else if("2".equals(operators)){
				user.setFeeRateLt(current.getFeeRate());
			}
			return current.getFeeRate();
		}
		return 1d;
	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}

}
