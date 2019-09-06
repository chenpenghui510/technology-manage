package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.query.QueryCondition;
import org.jeecg.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.vo.SysUserKeyVo;
import org.jeecg.modules.system.vo.SysUserVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	 * 通过用户账号查询用户信息
	 *
	 * @param username
	 * @return
	 */
	public SysUser getUserByName(@Param("username") String username);

	/**
	 * 根据部门Id查询用户信息
	 *
	 * @param page
	 * @param departId
	 * @return
	 */
	IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

	/**
	 * 根据角色Id查询用户信息
	 *
	 * @param page
	 * @param
	 * @return
	 */
	IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);

	/**
	 * 根据用户名设置部门ID
	 *
	 * @param username
	 */
	void updateUserDepart(@Param("username") String username,@Param("orgCode") String orgCode);

	IPage<Map> queryPageSysList(Page<Map> page, @Param("name") String name, @Param("username") String username, @Param("selectedRole") String selectedRole);

	IPage<Map> getUserInfoList(Page<Map> page, @Param("name") String name, @Param("username") String username, @Param("selectedRole") String selectedRole,@Param("list") List<QueryCondition> conditions);

	List<SysUser> getAllUserInfo();

	List<String> getUserNameByDptId(String dptId);

	/**
	 * 查询申报用户列表
	 *
	 * @param page
	 * @param account
	 * @param state
	 * @return
	 */
	IPage<SysUserVo> pageCustomer(Page<SysUserVo> page, @Param("account") String account, @Param("state") Integer state,@Param("realName")String realName,@Param("userType")Integer userType,@Param("list") List<QueryCondition> conditions);

	/**
	 * 获取企业用户详情
	 *
	 * @param id
	 * @return
	 */
	Map<String, Object> getCompanyDetails(@Param("id") String id);

	/**
	 * 获取企业用户详情
	 *
	 * @param id
	 * @return
	 */
	List<Map<String, String>> getCompanyFiles(@Param("id") String id);

	/**
	 * 根据邮箱查询用户信息
	 *
	 * @param email
	 * @return
	 */
	public SysUser getUserByEmail(@Param("email") String email);

	/**
	 * 根据手机号和所在的用户类型查询用户信息（因为可能出现一个人即使个人用户又是企业用户）
	 *
	 * @param phone
	 * @return
	 */
	public SysUser getUserByPhone(@Param("phone") String phone);


	/**
	 * 查询个人用户详情
	 *
	 * @param id
	 * @return
	 */
	Map<String, Object> getPersonalDetails(@Param("id") String id);

	/**
	 * 查询个人用户附件
	 *
	 * @param id
	 * @return
	 */
	List<Map<String, String>> getPersonalFiles(@Param("id") String id);


	/**
	 * 获取专家列表
	 *
	 * @return
	 */
	IPage<SysUser> getPageList(Page<SysUser> page, String username);

	IPage<SysUser> getAdminList(Page<SysUser> page, @Param("username") String username);

	void doExpertEmpower();

	List<SysUserKeyVo> keyList();

	List<Map> getContentInfoByParams(@Param("applyId") String applyId,@Param("field")  String field,@Param("byValue") String byValue,@Param("val") String val);
}