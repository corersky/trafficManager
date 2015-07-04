package com.yuandu.erp.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.modules.sys.entity.User;

/**
 * Service基类
 */
@Transactional(readOnly = true)
public abstract class BaseService {
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param officeAlias 机构表别名，多个用“,”逗号隔开。
	 * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String dataScopeFilter(User user, String officeAlias, String userAlias) {

		StringBuilder sqlString = new StringBuilder();
		
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		
		// 超级管理员，跳过权限过滤
		if (!user.isAdmin()){
			boolean isDataScopeAll = false;
			//获取用户权限
			for (String oa : StringUtils.split(officeAlias, ",")){
				if (!dataScope.contains(user.getDataScope()) && StringUtils.isNotBlank(oa)){
					if (User.DATA_SCOPE_ALL.equals(user.getDataScope())){
						isDataScopeAll = true;
					}
					else if (User.DATA_SCOPE_COMPANY_AND_CHILD.equals(user.getDataScope())){
						sqlString.append(" OR " + oa + ".id = '" + user.getCompany().getId() + "'");
						sqlString.append(" OR " + oa + ".parent_ids LIKE '" + user.getCompany().getParentIds() + user.getCompany().getId() + ",%'");
					}
					else if (User.DATA_SCOPE_COMPANY.equals(user.getDataScope())){
						sqlString.append(" OR " + oa + ".id = '" + user.getCompany().getId() + "'");
						// 包括本公司下的部门 （type=1:公司；type=2：部门）
						sqlString.append(" OR (" + oa + ".parent_id = '" + user.getCompany().getId() + "' AND " + oa + ".type = '2')");
					}
					else if (User.DATA_SCOPE_OFFICE_AND_CHILD.equals(user.getDataScope())){
						sqlString.append(" OR " + oa + ".id = '" + user.getOffice().getId() + "'");
						sqlString.append(" OR " + oa + ".parent_ids LIKE '" + user.getOffice().getParentIds() + user.getOffice().getId() + ",%'");
					}
					else if (User.DATA_SCOPE_OFFICE.equals(user.getDataScope())){
						sqlString.append(" OR " + oa + ".id = '" + user.getOffice().getId() + "'");
					}
					else if (User.DATA_SCOPE_CUSTOM.equals(user.getDataScope())){
						String officeIds =  StringUtils.join(user.getOfficeIdList(), "','");
						if (StringUtils.isNotEmpty(officeIds)){
							sqlString.append(" OR " + oa + ".id IN ('" + officeIds + "')");
						}
					}
					dataScope.add(user.getDataScope());
				}
			}
			// 如果没有全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
			if (!isDataScopeAll){
				if (StringUtils.isNotBlank(userAlias)){
					for (String ua : StringUtils.split(userAlias, ",")){
						sqlString.append(" OR " + ua + ".id = '" + user.getId() + "'");
					}
				}else {
					for (String oa : StringUtils.split(officeAlias, ",")){
						//sqlString.append(" OR " + oa + ".id  = " + user.getOffice().getId());
						sqlString.append(" OR " + oa + ".id IS NULL");
					}
				}
			}else{
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param officeAlias 机构表别名，多个用“,”逗号隔开。
	 * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String linkScopeFilter(User user, String officeAlias) {

		StringBuilder sqlString = new StringBuilder();
		
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		
		// 超级管理员，跳过权限过滤
		if (!user.isAdmin()){
			boolean isDataScopeAll = false;
			//获取用户权限
			if (!dataScope.contains(user.getDataScope()) && StringUtils.isNotBlank(officeAlias)){
				if (User.DATA_SCOPE_ALL.equals(user.getDataScope())){
					isDataScopeAll = true;
				}
				else if (User.DATA_SCOPE_COMPANY_AND_CHILD.equals(user.getDataScope())){
					sqlString.append(" OR " + officeAlias + ".id = '" + user.getCompany().getId() + "'");
					sqlString.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getCompany().getParentIds() + user.getCompany().getId() + ",%'");
				}
				else if (User.DATA_SCOPE_COMPANY.equals(user.getDataScope())){
					sqlString.append(" OR " + officeAlias + ".id = '" + user.getCompany().getId() + "'");
					// 包括本公司下的部门 （type=1:公司；type=2：部门）
					sqlString.append(" OR (" + officeAlias + ".parent_id = '" + user.getCompany().getId() + "' AND " + officeAlias + ".type = '2')");
				}
				else if (User.DATA_SCOPE_OFFICE_AND_CHILD.equals(user.getDataScope())){
					sqlString.append(" OR " + officeAlias + ".id = '" + user.getOffice().getId() + "'");
					sqlString.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getOffice().getParentIds() + user.getOffice().getId() + ",%'");
				}
				else if (User.DATA_SCOPE_OFFICE.equals(user.getDataScope())){
					sqlString.append(" OR " + officeAlias + ".id = '" + user.getOffice().getId() + "'");
				}
				else if (User.DATA_SCOPE_CUSTOM.equals(user.getDataScope())){
					String officeIds =  StringUtils.join(user.getOfficeIdList(), "','");
					if (StringUtils.isNotEmpty(officeIds)){
						sqlString.append(" OR " + officeAlias + ".id IN ('" + officeIds + "')");
					}
				}else if(User.DATA_SCOPE_SELF.equals(user.getDataScope())){ //个人数据
					sqlString.append(" OR a.create_by = '" + user.getId() + "'");
				}
				dataScope.add(user.getDataScope());
			}
			// 如果没有全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
			if (isDataScopeAll){
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}else{
				sqlString.append(" OR a.office_id = '' OR a.office_id IS NULL");
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}
}
