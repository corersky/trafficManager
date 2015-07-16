package com.yuandu.erp.modules.sys.utils;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.yuandu.erp.common.service.BaseService;
import com.yuandu.erp.common.utils.CacheUtils;
import com.yuandu.erp.common.utils.SpringContextHolder;
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
	public static String updateBalance(User user,String orderNo,String partnerOrderNo, String status) throws Exception{
		//判断工单是否已经扣费
		Recharge recharge = ProductCacheUtil.getRecharge(partnerOrderNo);
		
		if(recharge!=null && orderNo.equals(recharge.getOrderNo())){
			if(!"1".equals(status)&&!"4".equals(status)){//状态不为1 (1:成功  已经扣款) 不为4 (4:处理中  还在处理)
				
				if("1".equals(recharge.getIsRefund())){
					throw new RuntimeException("订单["+partnerOrderNo+"] 已经退款，无法更新状态");
				}
				//更新退款余额
				double balance = recharge.getBalance();
				user.setBalance(balance);
				userDao.updateBlance(user);
				UserUtils.clearCache(user);//清除缓存
				
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
	public static void updateBalance(User user,Double balance,String partnerOrderNo) throws Exception{
		//更新余额
		double update = -balance;
		user.setBalance(update);
		
		userDao.updateBlance(user);
		UserUtils.clearCache(user);//清楚缓存
		
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
		}
		return officeList;
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
//			subject.logout();
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
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
//		Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
//		getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
//		getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}

}
