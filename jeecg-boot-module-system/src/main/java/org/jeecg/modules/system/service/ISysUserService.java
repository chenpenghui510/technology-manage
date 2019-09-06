package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryCondition;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.modules.system.entity.SysUser;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.vo.SysUserKeyVo;
import org.jeecg.modules.system.vo.SysUserVo;


/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {
	
	public SysUser getUserByName(String username);
	
	/**
	 * 添加用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void addUserWithRole(SysUser user,String roles);
	
	
	/**
	 * 修改用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void editUserWithRole(SysUser user,String roles);

	/**
	 * 获取用户的授权角色
	 * @param username
	 * @return
	 */
	public List<String> getRole(String username);
	
	/**
	  * 查询用户信息包括 部门信息
	 * @param username
	 * @return
	 */
	public SysUserCacheInfo getCacheUser(String username);

	/**
	 * 根据部门Id查询
	 * @param
	 * @return
	 */
	public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

	/**
	 * 根据角色Id查询
	 * @param
	 * @return
	 */
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page,String roleId, String username);

	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param username 用户名
	 * @return 角色集合
	 */
	Set<String> getUserRolesSet(String username);

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	Set<String> getUserPermissionsSet(String username);

	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param orgCode
	 */
	void updateUserDepart(String username,String orgCode);


	IPage<Map> queryPageSysList(Page<Map> page, String name, String username, String selectedRole);

    IPage<Map> getUserInfoList(Page<Map> page, String name, String username, String selectedRole,List<QueryCondition> conditions);
		/**
	 * 根据手机号获取用户名和密码
	 */
	public SysUser getUserByPhone(String phone);
	
	
	/**
	 * 根据邮箱获取用户
	 */
	public SysUser getUserByEmail(String email);

	/**
	 * 查询申报用户列表
	 * @param page
	 * @param account
	 * @param state
	 * @return
	 */
	IPage<SysUserVo> pageCustomer(Page<SysUserVo> page, String account, Integer state,String realName,Integer userType,List<QueryCondition> conditions);
	
	
	/**
	 * 添加用户和用户部门关系
	 * @param user
	 * @param selectedParts
	 */
	void addUserWithDepart(SysUser user, String selectedParts);

	/**
	 * 编辑用户和用户部门关系
	 * @param user
	 * @param departs
	 */
	void editUserWithDepart(SysUser user, String departs);

	/**
	 * 查询企业用户详情
	 * @param id
	 * @return
	 */
	Map<String,Object> getCompanyDetails(String id);

	/**
	 * 查询企业用户附件
	 * @param id
	 * @return
	 */
	List<Map<String,String>> getCompanyFiles(String id);

	/**
	 * 查询个人用户详情
	 * @param id
	 * @return
	 */
	Map<String,Object> getPersonalDetails(String id);

	/**
	 * 查询个人用户附件
	 * @param id
	 * @return
	 */
	List<Map<String,String>> getPersonalFiles(String id);



	/**
	 * 查询 部门负责人
	 */
	String getUserNameByDptId(String dptId);
	
	/**
	   * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffective(SysUser sysUser);

    IPage<SysUser> getAdminList(Page<SysUser> page ,String username);

    IPage<SysUser> getPageList(Page<SysUser> page, String username);

    void updateUserRole(String userId);

	void doExpertEmpower();

	List<SysUserKeyVo> keyList();

	List<Map> getContentInfoByParams(String applyId, String field, String byValue, String val);
}
