package org.jeecg.modules.system.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryCondition;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.model.SysUserDepartsVO;
import org.jeecg.modules.system.util.GainetUtils;
import org.jeecg.modules.system.util.GainetUtils;
import org.jeecg.modules.system.vo.*;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import sun.util.calendar.BaseCalendar;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

	@Autowired
	private ISysUserService sysUserService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private ISysDictService dictService;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysRoleService sysRoleService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;

	@Autowired
	private ISysUserDepartService sysUserDepartService;

	@Autowired
	private ISysUserRoleService userRoleService;

    @Autowired
    private IExpertUserGroupService expertUserGroupService;

    @Autowired
    private IExpertGroupService expertGroupService;
    @Autowired
    private ISysDictService iSysDictService;

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
    private ISysRoleService iSysRoleService;
	@Autowired
    private ISysUserRoleService iSysUserRoleService;
	@Autowired
    private IUserBusinessService userBusinessService;
	@Autowired
    private IBusinessService businessService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	//@RequiresPermissions("sys:user:list")
	public Result<IPage<SysUser>> queryPageList(SysUser user,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                      @RequestParam(name="cAdmin", defaultValue="1") Integer cAdmin,//选择 没有分配的管理员
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        String username = user.getUsername();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);

        if(!GainetUtils.isEmpty(cAdmin) && cAdmin == 0){
            IPage<SysUser> pageList = sysUserService.getAdminList(page, username);
            result.setSuccess(true);
            result.setResult(pageList);

        }else{
            user.setUsername(null);
            QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
            if(!GainetUtils.isEmpty(username)){
                queryWrapper.like("username", username);
            }
            IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);
            result.setSuccess(true);
            result.setResult(pageList);
        }
		return result;
	}

    /**
     * 获取管理员信息
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value = "/sysList", method = RequestMethod.GET)
    public Result<IPage<Map>> queryPageSysList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
        Result<IPage<Map>> result = new Result<IPage<Map>>();
        Page<Map> page = new Page<Map>(pageNo, pageSize);
        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String selectedRole = req.getParameter("selectedRole");
        IPage<Map> pageList = sysUserService.queryPageSysList(page, name,username,selectedRole);
        sysBaseAPI.addLog("获取管理员信息成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     *
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value = "/UserInfoList", method = RequestMethod.GET)
    public Result<IPage<Map>> getUserInfoList(@RequestParam(name="current", defaultValue="1") Integer current,
                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
        Result<IPage<Map>> result = new Result<IPage<Map>>();
        Page<Map> page = new Page<Map>(current, pageSize);
        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String selectedRole = req.getParameter("selectedRole");

        //高级组合查询
        String superQueryParams = req.getParameter("superQueryParams");
// 解码
        List<QueryCondition>  conditions=null;
        if(oConvertUtils.isNotEmpty(superQueryParams)) {
            try {
                superQueryParams = URLDecoder.decode(superQueryParams, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("--高级查询参数转码失败!", e);
                sysBaseAPI.addLog("业务统计高级查询参数转码失败!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);

            }
             conditions = JSON.parseArray(superQueryParams, QueryCondition.class);
        }
        IPage<Map> pageList = sysUserService.getUserInfoList(page, name,username,selectedRole,conditions);
        System.out.println(superQueryParams);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysUser> add(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        String selectedRoles = jsonObject.getString("selectedroles");
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            user.setCreateTime(new Date());//设置创建时间
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(1);
            user.setDelFlag("0");
            sysUserService.addUserWithRole(user, selectedRoles);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Result<SysUser> edit(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        try {
            SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
            if(sysUser==null) {
                result.error500("未找到对应实体");
            }else {
                SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setUpdateTime(new Date());
                user.setPassword(sysUser.getPassword());
                String roles = jsonObject.getString("selectedroles");
                roles=  StringUtils.strip(roles, "[]");
                sysUserService.editUserWithRole(user, roles);
                result.success("修改成功!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 删除用户
     */

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<SysUser> delete(@RequestParam(name="id",required=true) String id) {
        Result<SysUser> result = new Result<SysUser>();
        // 定义SysUserDepart实体类的数据库查询LambdaQueryWrapper
        LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<SysUserDepart>();
        SysUser sysUser = sysUserService.getById(id);
        if(sysUser==null) {
            result.error500("未找到对应实体");
        }else {
            // 当某个用户被删除时,删除其ID下对应的部门数据
            query.eq(SysUserDepart::getUserId, id);
            boolean ok = sysUserService.removeById(id);
            sysUserDepartService.remove(query);
            if(ok) {
                sysBaseAPI.addLog("删除用户账号："+sysUser.getUsername()+"成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
                result.success("删除成功!");
            }
        }

        return result;
    }

    /**
     * 批量删除用户
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<SysUser> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        // 定义SysUserDepart实体类的数据库查询对象LambdaQueryWrapper
        LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<SysUserDepart>();
        String[] idArry = ids.split(",");
        Result<SysUser> result = new Result<SysUser>();
        if(ids==null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        }else {
            this.sysUserService.removeByIds(Arrays.asList(ids.split(",")));
            // 当批量删除时,删除在SysUserDepart中对应的所有部门数据
            for(String id : idArry) {
                query.eq(SysUserDepart::getUserId, id);
                this.sysUserDepartService.remove(query);
            }
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 冻结&解冻用户
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
    public Result<SysUser> frozenBatch(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            boolean flag=true;
            for (String id : arr) {
                if(oConvertUtils.isNotEmpty(id)) {
                    if("2".equals(status)){
                        List<UserBusiness> relation = userBusinessService.getEntityBy(id, null);
                        if(relation!=null&&relation.size()>0){
                            flag=false;
                            break;
                        }
                    }
                    this.sysUserService.update(new SysUser().setStatus(Integer.parseInt(status)),
                            new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId,id));
                }
            }
            if(!flag){
                result.setSuccess(false);
                result.setMessage(" 禁用失败！该管理员名下绑定的有业务，请先做好业务交接后再进行禁用操作!");
                return result;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败"+e.getMessage());
        }
        result.success("操作成功!");
        return result;

    }

    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<SysUser> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysUser> result = new Result<SysUser>();
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysUser);
            result.setSuccess(true);
        }
        return result;
    }

    @RequestMapping(value = "/queryUserRole", method = RequestMethod.GET)
    public Result<List<String>> queryUserRole(@RequestParam(name = "userid", required = true) String userid) {
        Result<List<String>> result = new Result<>();
        List<String> list = new ArrayList<String>();
        List<SysUserRole> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userid));
        if (userRole == null || userRole.size() <= 0) {
            result.error500("未找到用户相关角色信息");
        } else {
            for (SysUserRole sysUserRole : userRole) {
                list.add(sysUserRole.getRoleId());
            }
            result.setSuccess(true);
            result.setResult(list);
        }
        return result;
    }


    /**
     * 校验用户账号是否唯一<br>
     * 可以校验其他 需要检验什么就传什么。。。
     *
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/checkOnlyUser", method = RequestMethod.GET)
    public Result<Boolean> checkUsername(SysUser sysUser) {
        Result<Boolean> result = new Result<>();
        result.setResult(true);//如果此参数为false则程序发生异常
        String id = sysUser.getId();
        log.info("--验证用户信息是否唯一---id:" + id);
        try {
            SysUser oldUser = null;
            if (oConvertUtils.isNotEmpty(id)) {
                oldUser = sysUserService.getById(id);
            } else {
                sysUser.setId(null);
            }
            //通过传入信息查询新的用户信息
            sysUser.setId(null);
            SysUser newUser = sysUserService.getOne(new QueryWrapper<SysUser>(sysUser));
            if (newUser != null) {
                //如果根据传入信息查询到用户了，那么就需要做校验了。
                if (oldUser == null) {
                    //oldUser为空=>新增模式=>只要用户信息存在则返回false
                    result.setSuccess(false);
                    result.setMessage("用户账号已存在");
                    return result;
                } else if (!id.equals(newUser.getId())) {
                    //否则=>编辑模式=>判断两者ID是否一致-
                    result.setSuccess(false);
                    result.setMessage("用户账号已存在");
                    return result;
                }
            }

        } catch (Exception e) {
            result.setSuccess(false);
            result.setResult(false);
            result.setMessage(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/changPassword", method = RequestMethod.PUT)
    public Result<SysUser> changPassword(@RequestBody SysUser sysUser) {
        Result<SysUser> result = new Result<SysUser>();
        String password = sysUser.getPassword();
        sysUser = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername()));
        if (sysUser == null) {
            result.error500("未找到对应实体");
        } else {
            String salt = oConvertUtils.randomGen(8);
            sysUser.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
            sysUser.setPassword(passwordEncode);
            this.sysUserService.updateById(sysUser);
            result.setResult(sysUser);
            result.success("密码修改完成！");
            sysBaseAPI.addLog("账户："+sysUser.getUsername()+"修改密码成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
        }
        return result;
    }

    /**
     * 查询指定用户和部门关联的数据
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userDepartList", method = RequestMethod.GET)
    public Result<List<DepartIdModel>> getUserDepartsList(@RequestParam(name = "userId", required = true) String userId) {
        Result<List<DepartIdModel>> result = new Result<>();
        try {
            List<DepartIdModel> depIdModelList = this.sysUserDepartService.queryDepartIdsOfUser(userId);
            if (depIdModelList != null && depIdModelList.size() > 0) {
                result.setSuccess(true);
                result.setMessage("查找成功");
                result.setResult(depIdModelList);
            } else {
                result.setSuccess(false);
                result.setMessage("查找失败");
            }
            return result;
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("查找过程中出现了异常: " + e.getMessage());
            return result;
        }

    }

    /**
     * 给指定用户添加对应的部门
     *
     * @param sysUserDepartsVO
     * @return
     */
    @RequestMapping(value = "/addUDepartIds", method = RequestMethod.POST)
    public Result<String> addSysUseWithrDepart(@RequestBody SysUserDepartsVO sysUserDepartsVO) {
        boolean ok = this.sysUserDepartService.addSysUseWithrDepart(sysUserDepartsVO);
        Result<String> result = new Result<String>();
        try {
            if (ok) {
                result.setMessage("添加成功!");
                result.setSuccess(true);
            } else {
                throw new Exception("添加失败!");
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(true);
            result.setMessage("添加数据的过程中出现市场了: " + e.getMessage());
            return result;
        }

    }

    /**
     * 根据用户id编辑对应的部门信息
     *
     * @param sysUserDepartsVO
     * @return
     */
    @RequestMapping(value = "/editUDepartIds", method = RequestMethod.PUT)
    public Result<String> editSysUserWithDepart(@RequestBody SysUserDepartsVO sysUserDepartsVO) {
        Result<String> result = new Result<String>();
        boolean ok = sysUserDepartService.editSysUserWithDepart(sysUserDepartsVO);
        if (ok) {
            result.setMessage("更新成功!");
            result.setSuccess(true);
            return result;
        }else{
            result.setMessage("修改失败！该管理员名下有业务存在，无法修改部门！请先交接完业务后再进行修改!");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 生成在添加用户情况下没有主键的问题,返回给前端,根据该id绑定部门数据
     *
     * @return
     */
    @RequestMapping(value = "/generateUserId", method = RequestMethod.GET)
    public Result<String> generateUserId() {
        Result<String> result = new Result<>();
        System.out.println("我执行了,生成用户ID==============================");
        String userId = UUID.randomUUID().toString().replace("-", "");
        result.setSuccess(true);
        result.setResult(userId);
        return result;
    }

    /**
     * 根据部门id查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryUserByDepId", method = RequestMethod.GET)
    public Result<List<SysUser>> queryUserByDepId(@RequestParam(name = "id", required = true) String id) {
        Result<List<SysUser>> result = new Result<>();
        List<SysUser> userList = sysUserDepartService.queryUserByDepId(id);
        try {
            result.setSuccess(true);
            result.setResult(userList);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            return result;
        }
    }

    /**
     * 查询所有用户所对应的角色信息
     *
     * @return
     */
    @RequestMapping(value = "/queryUserRoleMap", method = RequestMethod.GET)
    public Result<Map<String, String>> queryUserRole() {
        Result<Map<String, String>> result = new Result<>();
        Map<String, String> map = userRoleService.queryUserRole();
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     *
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysUser sysUser,HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysUser> pageList = sysUserService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "用户列表");
        mv.addObject(NormalExcelConstants.CLASS, SysUser.class);
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户列表数据", "导出人:"+user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }
    /**省科技龙头企业
     * 导出excel
     *
     * @param request
     *
     */
    @RequestMapping(value = "/keyExportXls")
    public ModelAndView keyExportXls(SysUser sysUser,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysUserKeyVo> pageList = sysUserService.keyList();
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "省科技龙头企业");
        mv.addObject(NormalExcelConstants.CLASS, SysUserKeyVo.class);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("省科技龙头企业", "导出人:"+user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }
    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @RequiresPermissions("user:import")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SysUser> listSysUsers = ExcelImportUtil.importExcel(file.getInputStream(), SysUser.class, params);
                for (SysUser sysUserExcel : listSysUsers) {
                    if (sysUserExcel.getPassword() == null) {
                        // 密码默认为“123456”
                        sysUserExcel.setPassword("123456");
                    }
                    sysUserService.save(sysUserExcel);
                }
                return Result.ok("文件导入成功！数据行数：" + listSysUsers.size());
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("抱歉! 您导入的数据中用户名已经存在.");
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * @功能：根据id 批量查询
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/queryByIds", method = RequestMethod.GET)
    public Result<Collection<SysUser>> queryByIds(@RequestParam String userIds) {
        Result<Collection<SysUser>> result = new Result<>();
        String[] userId = userIds.split(",");
        Collection<String> idList = Arrays.asList(userId);
        Collection<SysUser> userRole = sysUserService.listByIds(idList);
        result.setSuccess(true);
        result.setResult(userRole);
        return result;
    }

    /**
     * 首页密码修改
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public Result<SysUser> changPassword(@RequestBody JSONObject json) {
        Result<SysUser> result = new Result<SysUser>();
        String username = json.getString("username");
        String oldpassword = json.getString("oldpassword");
        SysUser user = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if(user==null) {
            result.error500("未找到用户!");
            return result;
        }
        String passwordEncode = PasswordUtil.encrypt(username, oldpassword, user.getSalt());
        if(!user.getPassword().equals(passwordEncode)) {
            result.error500("旧密码输入错误!");
            return result;
        }

        String password = json.getString("password");
        String confirmpassword = json.getString("confirmpassword");
        if(oConvertUtils.isEmpty(password)) {
            result.error500("新密码不存在!");
            return result;
        }

        if(!password.equals(confirmpassword)) {
            result.error500("两次输入密码不一致!");
            return result;
        }
        String newpassword = PasswordUtil.encrypt(username, password, user.getSalt());
        this.sysUserService.update(new SysUser().setPassword(newpassword), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
        result.success("密码修改完成！");
        sysBaseAPI.addLog("账户："+username+"修改密码成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
        return result;
    }

    @RequestMapping(value = "/userRoleList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> userRoleList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        String roleId = req.getParameter("roleId");
        String username = req.getParameter("username");
        IPage<SysUser> pageList = sysUserService.getUserByRoleId(page,roleId,username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * 给指定角色添加用户
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/addSysUserRole", method = RequestMethod.POST)
    public Result<String> addSysUserRole(@RequestBody SysUserRoleVO sysUserRoleVO) {
        Result<String> result = new Result<String>();
        try {
            String sysRoleId = sysUserRoleVO.getRoleId();
            for(String sysUserId:sysUserRoleVO.getUserIdList()) {
                SysUserRole sysUserRole = new SysUserRole(sysUserId,sysRoleId);
                QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
                queryWrapper.eq("role_id", sysRoleId).eq("user_id",sysUserId);
                SysUserRole one = sysUserRoleService.getOne(queryWrapper);
                if(one==null){
                    sysUserRoleService.save(sysUserRole);
                }

            }

            result.setMessage("添加成功!");
            result.setSuccess(true);
            return result;
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("出错了: " + e.getMessage());
            return result;
        }
    }
    /**
     *   删除指定角色的用户关系
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteUserRole", method = RequestMethod.DELETE)
    public Result<SysUserRole> deleteUserRole(@RequestParam(name="roleId") String roleId,
                                                    @RequestParam(name="userId",required=true) String userId
    ) {
        Result<SysUserRole> result = new Result<SysUserRole>();
        try {
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
            queryWrapper.eq("role_id", roleId).eq("user_id",userId);
            sysUserRoleService.remove(queryWrapper);
            result.success("删除成功!");
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 批量删除指定角色的用户关系
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteUserRoleBatch", method = RequestMethod.DELETE)
    public Result<SysUserRole> deleteUserRoleBatch(
            @RequestParam(name="roleId") String roleId,
            @RequestParam(name="userIds",required=true) String userIds) {
        Result<SysUserRole> result = new Result<SysUserRole>();
        try {
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
            queryWrapper.eq("role_id", roleId).in("user_id",Arrays.asList(userIds.split(",")));
            sysUserRoleService.remove(queryWrapper);
            result.success("删除成功!");
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 部门用户列表
     */
    @RequestMapping(value = "/departUserList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> departUserList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        String depId = req.getParameter("depId");
        String username = req.getParameter("username");
        IPage<SysUser> pageList = sysUserService.getUserByDepId(page,depId,username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 给指定部门添加对应的用户
     */
    @RequestMapping(value = "/editSysDepartWithUser", method = RequestMethod.POST)
    public Result<String> editSysDepartWithUser(@RequestBody SysDepartUsersVO sysDepartUsersVO) {
        Result<String> result = new Result<String>();
        try {
            String sysDepId = sysDepartUsersVO.getDepId();
            for(String sysUserId:sysDepartUsersVO.getUserIdList()) {
                SysUserDepart sysUserDepart = new SysUserDepart(null,sysUserId,sysDepId);
                QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
                queryWrapper.eq("dep_id", sysDepId).eq("user_id",sysUserId);
                SysUserDepart one = sysUserDepartService.getOne(queryWrapper);
                if(one==null){
                    sysUserDepartService.save(sysUserDepart);
                }
            }
            result.setMessage("添加成功!");
            result.setSuccess(true);
            return result;
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("出错了: " + e.getMessage());
            return result;
        }
    }

    /**
     *   删除指定机构的用户关系
     */
    @RequestMapping(value = "/deleteUserInDepart", method = RequestMethod.DELETE)
    public Result<SysUserDepart> deleteUserInDepart(@RequestParam(name="depId") String depId,
                                                    @RequestParam(name="userId",required=true) String userId
    ) {
        Result<SysUserDepart> result = new Result<SysUserDepart>();
        try {
            QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
            queryWrapper.eq("dep_id", depId).eq("user_id",userId);
            //查看该管理员下是否分的有业务，若有业务则先移除管理员与业务之前的关系
            List<UserBusiness> relationList = userBusinessService.getEntityBy(userId,null);
            if(relationList!=null&&relationList.size()>0){
                for(UserBusiness userBusiness:relationList){
                    if(userBusiness!=null){
                        userBusinessService.removeById(userBusiness.getId());
                        //查询中间表是否还有 此业务的管理员  没有则赋值1
                        List<UserBusiness> otherRelation = userBusinessService.getEntityBy(null,userBusiness.getBusinessId().toString());
                        if(otherRelation == null||otherRelation.size()<1){
                            businessService.removeForAdmin(businessService.getById(userBusiness.getBusinessId()));
                        }

                    }
                }
            }
            sysUserDepartService.remove(queryWrapper);
            result.success("删除成功!");
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 批量删除指定机构的用户关系
     */
    @RequestMapping(value = "/deleteUserInDepartBatch", method = RequestMethod.DELETE)
    public Result<SysUserDepart> deleteUserInDepartBatch(
            @RequestParam(name="depId") String depId,
            @RequestParam(name="userIds",required=true) String userIds) {
        Result<SysUserDepart> result = new Result<SysUserDepart>();
        try {
            for(int i=0;i<userIds.split(",").length;i++){
                if(StringUtils.isEmpty(userIds.split(",")[i])){
                    continue;
                }else{
                    String userId = userIds.split(",")[i];
                    QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
                    queryWrapper.eq("dep_id", depId).eq("user_id",userId);
                    //查看该管理员下是否分的有业务，若有业务则先移除管理员与业务之前的关系
                    List<UserBusiness> relationList = userBusinessService.getEntityBy(userId,null);
                    if(relationList!=null&&relationList.size()>0){
                        for(UserBusiness userBusiness:relationList){
                            if(userBusiness!=null){
                                userBusinessService.removeById(userBusiness.getId());
                                //查询中间表是否还有 此业务的管理员  没有则赋值1
                                List<UserBusiness> otherRelation = userBusinessService.getEntityBy(null,userBusiness.getBusinessId().toString());
                                if(otherRelation == null||otherRelation.size()<1){
                                    businessService.removeForAdmin(businessService.getById(userBusiness.getBusinessId()));
                                }
                            }
                        }
                    }
                    sysUserDepartService.remove(queryWrapper);
                }
            }
            result.success("删除成功!");
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }



    @RequestMapping(value="/listCustomer",method=RequestMethod.GET)
    public Result<IPage<SysUserVo>> queryCustomerPageList(SysUser user, @RequestParam(name="pageNo",defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name="pageSize",defaultValue = "10")Integer pageSize, HttpServletRequest req){
        Result<IPage<SysUserVo>> result = new Result<IPage<SysUserVo>>();
        String account = user.getUsername();
        Integer state = user.getStatus();
        String realName = user.getRealname();
        Integer userType = user.getUserType();
        String superQueryParams = req.getParameter("superQueryParams");
        List<QueryCondition>  conditions=null;
        if(oConvertUtils.isNotEmpty(superQueryParams)) {
            try {
                superQueryParams = URLDecoder.decode(superQueryParams, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("--高级查询参数转码失败!", e);
                sysBaseAPI.addLog("业务统计高级查询参数转码失败!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);

            }
            conditions = JSON.parseArray(superQueryParams, QueryCondition.class);
        }
        Page<SysUserVo> page = new Page<SysUserVo>(pageNo, pageSize);
        IPage<SysUserVo> pageList = sysUserService.pageCustomer(page, account,state,realName,userType,conditions);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
    public Result<SysUser> addUser(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            //设置创建时间
            user.setCreateTime(new Date());
            user.setCreateBy(sysUser.getRealname());
            String salt = oConvertUtils.randomGen(8);
            //设置初始密码
            if(user.getPassword()==null||"".equals(user.getPassword())){
                user.setPassword("123456");
            }
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(3);
            user.setDelFlag("0");
            //保存用户 以及 用户角色
            SysRole  sysRole=new SysRole();
            if(user.getUserType()==1){
                sysRole.setRoleCode("zlwws_gr");
            }else if(user.getUserType()==2){
                sysRole.setRoleCode("zlwws_qy");
                //sysUserService.addUserWithRole(user, "54f720e28c422c469673adaad098af8d");
            }
            sysRole=   sysRoleService.getOne(new QueryWrapper<SysRole>(sysRole));
            sysUserService.addUserWithRole(user, sysRole.getId());
            sysBaseAPI.addLog("添加用户："+sysUser.getUsername()+"成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
            result.success("添加成功！初始密码：123456");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @RequestMapping(value = "/editCustomer", method = RequestMethod.PUT)
    public Result<SysUser> editUser(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        try {
            SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
            if(sysUser==null) {
                result.error500("未找到对应实体");
            }else {
                SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setUpdateTime(new Date());
                //String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
                user.setPassword(sysUser.getPassword());
                if(!sysUser.getUserType().toString().equals(user.getUserType().toString())){
                    result.setSuccess(false);
                    result.setMessage("修改失败！用户类型无法更改！");
                    return result;
                }
                if(user.getUserType()==1||user.getUserType()==2){
                    sysUserService.updateById(user);
                }else{
                    String roles = jsonObject.getString("selectedroles");
                    sysUserService.editUserWithRole(user, roles);
                }
                sysBaseAPI.addLog("修改用户："+user.getUsername()+"成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
                result.success("修改成功!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }
    @RequestMapping(value="/getCompanyFiles",method = RequestMethod.GET)
    public Result<List<Map<String,String>>> getCompanyFiles(@RequestParam(name="id") String id){
        Result<List<Map<String,String>>> result = new Result<List<Map<String,String>>>();
        try{
            List<Map<String,String>> companyFiles = sysUserService.getCompanyFiles(id);
            result.setResult(companyFiles);
            result.success("查询企业用户附件成功!");
        }catch(Exception e){
            log.error(e.getMessage(),e);
            result.error500("获取企业用户附件失败!");
        }
        return result;
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/deleteCustomer", method = RequestMethod.DELETE)
    public Result<SysUser> deleteUser(@RequestParam(name="id",required=true) String id) {
        Result<SysUser> result = new Result<SysUser>();
        // 定义SysUserDepart实体类的数据库查询LambdaQueryWrapper
        LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<SysUserDepart>();
        SysUser sysUser = sysUserService.getById(id);
        if(sysUser==null) {
            result.error500("未找到对应实体");
        }else {
            boolean ok = sysUserService.removeById(id);
            System.out.println(sysUser);
            //删除用户和部门的管理表
            query.eq(SysUserDepart::getUserId, id);
            sysUserDepartService.remove(query);
            if(ok) {
                result.success("删除成功!");
                sysBaseAPI.addLog("删除用户："+sysUser.getUsername()+"成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
            }
        }
        return result;
    }

    /**
     * 审核用户
     */
    @RequestMapping(value = "/examineUser", method = RequestMethod.PUT)
    public Result<SysUser> examineCompanyUser(@RequestBody Map<String,String> params) {
        Result<SysUser> result = new Result<SysUser>();
        // 定义SysUserDepart实体类的数据库查询LambdaQueryWrapper
        SysUser sysUser = sysUserService.getById(params.get("id"));
        if(sysUser==null) {
            result.error500("未找到对应实体");
        }else {
            //若状态不等于待审核则无法审核
            if(sysUser.getStatus()!=4){
                result.error500("用户状态不是待审核状态，无法进行审核");
            }else{
                sysUser.setStatus(1);
                sysUser.setAuditComments(params.get("auditComments"));
                sysUserService.updateById(sysUser);
                sysUserService.updateUserRole(sysUser.getId());
                result.success("审核成功!");
                sysBaseAPI.addLog("用户："+sysUser.getUsername()+"审核通过!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
            }
        }
        return result;
    }

    /**
     * 驳回用户
     */
    @RequestMapping(value = "/rejectUser", method = RequestMethod.PUT)
    public Result<SysUser> rejectUser(@RequestBody Map<String,String> params) {
        Result<SysUser> result = new Result<SysUser>();
        // 定义SysUserDepart实体类的数据库查询LambdaQueryWrapper
        SysUser sysUser = sysUserService.getById(params.get("id"));
        if(sysUser==null) {
            result.error500("未找到对应实体");
        }else {
            //若状态不等于待审核则无法审核
            if(sysUser.getStatus()!=4){
                result.error500("用户状态不是待审核状态，无法进行驳回操作");
            }else{
                sysUser.setStatus(5);
                sysUser.setAuditComments(params.get("auditComments"));
                sysUserService.updateById(sysUser);
                result.success("驳回成功!");
                sysBaseAPI.addLog("用户："+sysUser.getUsername()+"驳回成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
            }
        }
        return result;
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/getPersonalDetail", method = RequestMethod.GET)
    public Result<Object> getPersonalDetail(@RequestParam(name="id") String id){
        Result<Object> result = new Result<Object>();
        try{
            Map<String,Object> personalDetails= sysUserService.getPersonalDetails(id);
            SysPersonVo sysPersonVo=new SysPersonVo();
            if(personalDetails.get("id")!=null){
                sysPersonVo.setId(GainetUtils.stringOf(personalDetails.get("id")));
            }
            sysPersonVo.setName(GainetUtils.stringOf(personalDetails.get("realname")));
            sysPersonVo.setUsername(GainetUtils.stringOf(personalDetails.get("username")));
            sysPersonVo.setUserSex(GainetUtils.stringOf(personalDetails.get("personal_sex")));
            sysPersonVo.setUserCard(GainetUtils.stringOf(personalDetails.get("user_card")));
            sysPersonVo.setPhone(GainetUtils.stringOf(personalDetails.get("phone")));
            sysPersonVo.setUserTelephone(GainetUtils.stringOf(personalDetails.get("user_telephone")));
            sysPersonVo.setEmail(GainetUtils.stringOf(personalDetails.get("email")));
            sysPersonVo.setUserPost(GainetUtils.stringOf(personalDetails.get("post")));
            sysPersonVo.setUserTitle(GainetUtils.stringOf(personalDetails.get("professional_titles")));
            Object obj=  iSysDictService.getDictInfoByObj(sysPersonVo);
            result.setResult(obj);
            result.success("查询个人用户详情成功!");
        }catch(Exception e){
            log.error(e.getMessage(),e);
            result.error500("获取个人用户详情信息失败!");
        }
        return result;
    }
    @RequestMapping(value = "/getCompanyDetail", method = RequestMethod.GET)
    public Result<Object> getCompanyDetails(@RequestParam(name="id") String id){
        Result<Object> result = new Result<Object>();
        try{
            Map<String,Object> companyDetails= sysUserService.getCompanyDetails(id);
            SysCompanyVo sysCompanyVo=new SysCompanyVo();
            if(companyDetails.get("id")!=null){
                sysCompanyVo.setId(String.valueOf(companyDetails.get("id")));
            }
            if(companyDetails.get("username")!=null){ sysCompanyVo.setUsername(String.valueOf(companyDetails.get("username")));}
            if(companyDetails.get("realname")!=null){sysCompanyVo.setRealname(String.valueOf(companyDetails.get("realname")));}
            if(companyDetails.get("password")!=null){sysCompanyVo.setPassword(String.valueOf(companyDetails.get("password")));}
            if(companyDetails.get("email")!=null){sysCompanyVo.setEmail(String.valueOf(companyDetails.get("email")));}
            if(companyDetails.get("user_type")!=null){sysCompanyVo.setUserType(String.valueOf(companyDetails.get("user_type")));}
            if(companyDetails.get("phone")!=null){sysCompanyVo.setPhone(String.valueOf(companyDetails.get("phone")));}
            if(companyDetails.get("status")!=null){sysCompanyVo.setStatus(String.valueOf(companyDetails.get("status")));}
            if(companyDetails.get("create_time")!=null){sysCompanyVo.setCreateTime((Date)companyDetails.get("create_time"));}
            if(companyDetails.get("user_sex")!=null){sysCompanyVo.setUserSex(String.valueOf(companyDetails.get("user_sex")));}
            if(companyDetails.get("user_card")!=null){sysCompanyVo.setUserCard(String.valueOf(companyDetails.get("user_card")));}
            if(companyDetails.get("user_name")!=null){ sysCompanyVo.setName(String.valueOf(companyDetails.get("user_name")));}
            if(companyDetails.get("user_post")!=null){sysCompanyVo.setUserPost(String.valueOf(companyDetails.get("user_post")));}
            if(companyDetails.get("user_title")!=null){sysCompanyVo.setUserTitle(String.valueOf(companyDetails.get("user_title")));}
            if(companyDetails.get("user_telephone")!=null){sysCompanyVo.setUserTelephone(String.valueOf(companyDetails.get("user_telephone")));}
            if(companyDetails.get("company_name")!=null){sysCompanyVo.setCompanyName(String.valueOf(companyDetails.get("company_name")));}
            if(companyDetails.get("social_credit_code")!=null){sysCompanyVo.setSocialCreditCode(String.valueOf(companyDetails.get("social_credit_code")));}
            if(companyDetails.get("competent_department")!=null){ sysCompanyVo.setCompetentDepartment(String.valueOf(companyDetails.get("competent_department")));}
            if(companyDetails.get("unit_nature")!=null){sysCompanyVo.setUnitNature(String.valueOf(companyDetails.get("unit_nature")));}
            if(companyDetails.get("incorporation_place")!=null){sysCompanyVo.setIncorporationPlace(String.valueOf(companyDetails.get("incorporation_place")));}
            if(companyDetails.get("company_address_city")!=null){sysCompanyVo.setCompanyAddressCity(String.valueOf(companyDetails.get("company_address_city")));}
            if(companyDetails.get("company_detail_address")!=null){sysCompanyVo.setCompanyDetailAddress(String.valueOf(companyDetails.get("company_detail_address")));}
            if(companyDetails.get("company_telephone")!=null){sysCompanyVo.setCompanyTelephone(String.valueOf(companyDetails.get("company_telephone")));}
            if(companyDetails.get("company_fax")!=null){ sysCompanyVo.setCompanyFax(String.valueOf(companyDetails.get("company_fax")));}
            if(companyDetails.get("company_email")!=null){sysCompanyVo.setCompanyEmail(String.valueOf(companyDetails.get("company_email")));}
            if(companyDetails.get("company_postal_code")!=null){sysCompanyVo.setCompanyPostalCode(String.valueOf(companyDetails.get("company_postal_code")));}
            if(companyDetails.get("company_bank_account")!=null){ sysCompanyVo.setCompanyBankAccount(String.valueOf(companyDetails.get("company_bank_account")));}
            if(companyDetails.get("company_bank_code")!=null){sysCompanyVo.setCompanyBankCode(String.valueOf(companyDetails.get("company_bank_code")));}
            if(companyDetails.get("bank_name")!=null){ sysCompanyVo.setBankName(String.valueOf(companyDetails.get("bank_name")));}
            if(companyDetails.get("registered_capital")!=null){sysCompanyVo.setRegisteredCapital(String.valueOf(companyDetails.get("registered_capital")));}
            if(companyDetails.get("capital_type")!=null){   sysCompanyVo.setCapitalType(String.valueOf(companyDetails.get("capital_type")));}
            if(companyDetails.get("credit_rating")!=null){  sysCompanyVo.setCreditRating(String.valueOf(companyDetails.get("credit_rating")));}
            if(companyDetails.get("company_create_time")!=null){ sysCompanyVo.setCompanyCreateTime((Date)companyDetails.get("company_create_time"));}
            if(companyDetails.get("business_scope")!=null){ sysCompanyVo.setBusinessScope(String.valueOf(companyDetails.get("business_scope")));}
            if(companyDetails.get("legal_person")!=null){sysCompanyVo.setLegalPerson(String.valueOf(companyDetails.get("legal_person")));}
            if(companyDetails.get("legal_telephone")!=null){  sysCompanyVo.setLegalTelephone(String.valueOf(companyDetails.get("legal_telephone")));}
            if(companyDetails.get("document_type")!=null){ sysCompanyVo.setDocumentType(String.valueOf(companyDetails.get("document_type")));}
            if(companyDetails.get("document_code")!=null){sysCompanyVo.setDocumentCode(String.valueOf(companyDetails.get("document_code")));}
            if(companyDetails.get("legal_phone")!=null){ sysCompanyVo.setLegalPhone(String.valueOf(companyDetails.get("legal_phone")));}
            if(companyDetails.get("legal_email")!=null){ sysCompanyVo.setLegalEmail(String.valueOf(companyDetails.get("legal_email")));}
            if(companyDetails.get("legal_education")!=null){sysCompanyVo.setLegalEducation(String.valueOf(companyDetails.get("legal_education")));}
            if(companyDetails.get("legal_post")!=null){ sysCompanyVo.setLegalPost(String.valueOf(companyDetails.get("legal_post")));}
            if(companyDetails.get("legal_academic_degree")!=null){ sysCompanyVo.setLegalAcademicDegree(String.valueOf(companyDetails.get("legal_academic_degree")));}
            if(companyDetails.get("legal_sex")!=null){ sysCompanyVo.setLegalSex(String.valueOf(companyDetails.get("legal_sex")));}
            if(companyDetails.get("legal_birth_date")!=null){sysCompanyVo.setLegalBirthDate((Date)companyDetails.get("legal_birth_date"));}
            if(companyDetails.get("legal_title")!=null){ sysCompanyVo.setLegalTitle(String.valueOf(companyDetails.get("legal_title")));}
            if(companyDetails.get("iso_time")!=null){sysCompanyVo.setIsoTime((Date)companyDetails.get("iso_time"));}
            if(companyDetails.get("violate")!=null){ sysCompanyVo.setViolate(String.valueOf(companyDetails.get("violate")));}
            if(companyDetails.get("financing_amount")!=null){ sysCompanyVo.setFinancingAmount(String.valueOf(companyDetails.get("financing_amount")));}
            if(companyDetails.get("financing_time")!=null){ sysCompanyVo.setFinancingTime((Date)companyDetails.get("financing_time"));}
            if(companyDetails.get("government_aid")!=null){ sysCompanyVo.setGovernmentAid(String.valueOf(companyDetails.get("government_aid")));}
            if(companyDetails.get("national_advantage")!=null){ sysCompanyVo.setNationalAdvantage(String.valueOf(companyDetails.get("national_advantage")));}
            if(companyDetails.get("provincial_advantage")!=null){ sysCompanyVo.setProvincialAdvantage(String.valueOf(companyDetails.get("provincial_advantage")));}
            if(companyDetails.get("city_advantage")!=null){ sysCompanyVo.setCityAdvantage(String.valueOf(companyDetails.get("city_advantage")));}
            if(companyDetails.get("zz_science")!=null){ sysCompanyVo.setZzScience(String.valueOf(companyDetails.get("zz_science")));}
            if(companyDetails.get("record_number")!=null){ sysCompanyVo.setRecordNumber(String.valueOf(companyDetails.get("record_number")));}
            if(companyDetails.get("provincial_key_time")!=null){sysCompanyVo.setProvincialKeyTime((Date)companyDetails.get("provincial_key_time"));}
            if(companyDetails.get("regulatory_enterprises")!=null){ sysCompanyVo.setRegulatoryEnterprises(String.valueOf(companyDetails.get("regulatory_enterprises")));}
            if(companyDetails.get("organization_of_scientists")!=null){ sysCompanyVo.setOrganizationOfScientists(String.valueOf(companyDetails.get("organization_of_scientists")));}
            if(companyDetails.get("company_industry")!=null){sysCompanyVo.setCompanyIndustry(String.valueOf(companyDetails.get("company_industry")));}
            Object obj=  iSysDictService.getDictInfoByObj(sysCompanyVo);
            result.setResult(obj);
            result.success("查询企业用户详情成功!");
        }catch(Exception e){
            log.error(e.getMessage(),e);
            result.error500("获取企业用户详情信息失败!");
        }
        return result;
    }



    public String getDictInfo(String dictCode,String value){
        if(GainetUtils.isEmpty(dictCode)||GainetUtils.isEmpty(value)){
            return null;
        }
        String result=null;
        List<DictModel> ls = sysDictService.queryDictItemsByCode(dictCode);
        for(int i=0;i<ls.size();i++){
            if(value.equals(ls.get(i).getValue())){
                result=  ls.get(i).getText();
            }
        }
        return result;
    }
    /*
     *  查询当前用户的所有部门/当前部门编码
     * @return
     */
    @RequestMapping(value = "/getCurrentUserDeparts", method = RequestMethod.GET)
    public Result<Map<String,Object>> getCurrentUserDeparts() {
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        try {
        	LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            List<SysDepart> list = this.sysDepartService.queryUserDeparts(sysUser.getId());
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("list", list);
            map.put("orgCode", sysUser.getOrgCode());
            result.setSuccess(true);
            result.setResult(map);
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("查询失败！");
        }
        return result;
    }

    /**
     * 获取专家里列表
     * @param user
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value="/expertList",method=RequestMethod.GET)
    public Result<IPage<SysUser>> getExpertList(SysUser user,@RequestParam(name="pageNo",defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name="pageSize",defaultValue = "10")Integer pageSize,HttpServletRequest req){
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        /*String username = user.getUsername();
        user.setUsername(null);
        user.setUserType(3);
        user.setDelFlag("0");
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
        if(!GainetUtils.isEmpty(username)){
            queryWrapper.like("username",queryWrapper);
        }
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);*/

        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        IPage<SysUser> pageList = sysUserService.getPageList(page, user.getUsername());
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加专家
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/addExpert", method = RequestMethod.POST)
    public Result<SysUser> addExpert(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser == null){
            result.error500("登陆超时");
        }
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            //设置创建时间
            user.setCreateTime(new Date());
            user.setCreateBy(sysUser.getRealname());
            String salt = oConvertUtils.randomGen(8);
            //设置初始密码
            if(user.getPassword()==null||"".equals(user.getPassword())){
                user.setPassword("123456");
            }
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(3);
            user.setDelFlag("0");
            //保存用户 以及 用户角色  专家
            user.setUserType(3);
            SysRole role = new SysRole();
            role.setRoleCode("zlwws_zj");
            SysRole roleEntity = iSysRoleService.getOne(new QueryWrapper<SysRole>(role));
            sysUserService.addUserWithRole(user, roleEntity.getId());//TODO
            result.success("添加成功！");
            sysBaseAPI.addLog("专家账号："+user.getUsername()+"创建成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 删除 专家
     * @return
     */
    @RequestMapping(value = "/deleteExpert", method = RequestMethod.DELETE)
    public Result<SysUser> deleteExpert(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        if(GainetUtils.isEmpty(jsonObject.get("id"))){
            result.error500("参数为空");
            return result;
        }
        SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
        if(sysUser==null) {
            result.error500("未找到对应实体");
        }else if(sysUser.getStatus() == 1) {
            result.error500("账号状态正常，不能删除！");
        }else{
            sysUser.setDelFlag("1");
            //业务
            if(sysUserService.updateById(sysUser)) {
                result.success("删除成功!");
                sysBaseAPI.addLog("专家账号："+sysUser.getUsername()+"删除成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
            }
        }
        return result;
    }

    /**
     * 删除 专家 批量
     * @return
     */
    @RequestMapping(value = "/deleteBatchExpert", method = RequestMethod.DELETE)
    public Result<SysUser> deleteBatchExpert(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        if(GainetUtils.isEmpty(jsonObject.get("userId"))){
            result.error500("参数为空");
            return result;
        }
        String ids = jsonObject.getString("userId");
        String error="";
        for(String id : ids.split(",")){
            if(GainetUtils.isEmpty(id)){
                continue;
            }
            SysUser sysUser = sysUserService.getById(id);
            if(sysUser.getStatus()==1){
                sysUser.setDelFlag("1");
                sysUserService.updateById(sysUser);
            }else{
                error+=sysUser.getUsername()+";";
            }
        }
        if(!"".equals(error)){
            result.success("部分账号解除授权成功!解除失败的账号为:"+error+"失败原因：账号状态异常!");
        }else{
            result.success("解除授权成功!");
        }
        sysBaseAPI.addLog("解除授权成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
        return result;
    }

    public static void main(String[] args) {
        int nums = 25;
        for (int i = 1; i < nums; i++) {
            String username = "";
            long a = System.currentTimeMillis() / 1000;
            if (i < 10) {
                username = a + "00" + i;
            } else if (i < 100) {
                username = a + "0" + i;
            }
            System.out.println(username);
        }
    }

    @RequestMapping(value = "/addBatchExperts", method = RequestMethod.POST)
    public Result<SysUser> addBatchExperts(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser == null){
            result.error500("登陆超时");
            return result;
        }
        Integer nums = jsonObject.getInteger("nums");
        if(nums > 99){
            result.error500("");
            return result;
        }
        jsonObject.remove("num");
        try {
            SysRole role = new SysRole();
            role.setRoleCode("zlwws_zj");
            SysRole roleEntity = iSysRoleService.getOne(new QueryWrapper<SysRole>(role));
            for(int i=1; i<=nums; i++){
                SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setCreateBy(sysUser.getUsername());
                String username = "";
                long a = System.currentTimeMillis()/1000;
                if(i < 10){
                    username = a + "00" + i;
                }else if(i < 100){
                    username = a + "0" + i;
                }
                user.setUsername(username);
                //设置创建时间
                user.setCreateTime(new Date());
                user.setCreateBy(sysUser.getRealname());
                String salt = oConvertUtils.randomGen(8);
                //设置初始密码
                if(GainetUtils.isEmpty(user.getPassword())){
                    user.setPassword("123456");
                }
                user.setSalt(salt);
                String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
                user.setStatus(3);
                user.setDelFlag("0");
                //保存用户 以及 用户角色  专家
                user.setUserType(3);
                sysUserService.addUserWithRole(user, roleEntity.getId());//TODO
            }
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 给专家 分组
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/setGroup", method = RequestMethod.PUT)
    public Result<SysUser> setGroup(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        try{
            String str = "";
            if(GainetUtils.isEmpty(jsonObject.get("groupId"))){
                result.error500("参数组id为空");
                return result;
            }
            if(GainetUtils.isEmpty(jsonObject.get("userId"))){
                result.error500("参数userid为空");
                return result;
            }
            String groupId = jsonObject.getString("groupId");
            String userIds = jsonObject.getString("userId");
            for(String userId : userIds.split(",")){
                if(GainetUtils.isEmpty(userId)){
                    continue;
                }
                ExpertUserGroup expertUser = expertUserGroupService.getByUserId(userId);
                if(expertUser != null){
                    SysUser user = sysUserService.getById(userId);
                    str += user.getUsername()+",";
                    continue;
                }
                ExpertUserGroup expertUserGroup = new ExpertUserGroup();
                expertUserGroup.setGroupId(Integer.valueOf(groupId));
                expertUserGroup.setUserId(userId);
                expertUserGroupService.saveOrUpdate(expertUserGroup);
                ExpertGroup expertGroup = expertGroupService.getById(groupId);
                if(expertGroup.getStatus() != 1){
                    expertGroup.setStatus(1);
                    expertGroupService.saveOrUpdate(expertGroup);
                }
            }
            if(GainetUtils.isEmpty(str)){
                result.success("操作成功");
            }else{
                result.success(str+"已有分组，已跳过");
            }
            sysBaseAPI.addLog("专家分组成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
        }catch(Exception e){
            log.error(e.getMessage(),e);
            result.error500("操作异常");
        }
        return result;
    }

    /**
     * 脱离分组
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/deleteUserGroup", method = RequestMethod.POST)
    public Result<Map<String,Object>> deleteUserGroup(@RequestBody JSONObject jsonObject){
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        try{
            if(GainetUtils.isEmpty(jsonObject.get("id"))){
                result.error500("参数为空");
                return result;
            }
            String userId = jsonObject.getString("id");
            ExpertUserGroup expertUserGroup = expertUserGroupService.getByUserId(userId);
            if(expertUserGroup != null){
                expertUserGroupService.removeById(expertUserGroup.getId());
            }
            result.success("操作成功");
        }catch(Exception e){
            log.error(e.getMessage(),e);
            result.error500("操作异常");
        }
        return result;
    }

    /**
     * 分组 解散人员
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/removeAllUserByGroup", method = RequestMethod.POST)
    public Result<Map<String,Object>> removeAllUserByGroup(@RequestBody JSONObject jsonObject){
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        try{
            if(GainetUtils.isEmpty(jsonObject.get("gid"))){
                result.error500("参数为空");
                return result;
            }
            String gid = jsonObject.getString("gid");
            boolean b = expertUserGroupService.deleteByGid(gid);
            if(!b){
                result.error500("删除失败");
                return result;
            }
            ExpertGroup expertGroup = expertGroupService.getById(gid);
            if(expertGroup.getStatus() != 0){
                expertGroup.setStatus(0);
                expertGroupService.saveOrUpdate(expertGroup);
            }
            result.success("操作成功");
        }catch(Exception e){
            log.error(e.getMessage(),e);
            e.printStackTrace();
            result.error500("操作异常");
        }
        return result;
    }


    @RequestMapping(value="/getPersonalFiles",method = RequestMethod.GET)
    public Result<List<Map<String,String>>> getPersonalFiles(@RequestParam(name="id") String id){
        Result<List<Map<String,String>>> result = new Result<List<Map<String,String>>>();
        try{
            List<Map<String,String>> companyFiles = sysUserService.getPersonalFiles(id);
            result.setResult(companyFiles);
            result.success("查询个人用户附件成功!");
        }catch(Exception e){
            log.error(e.getMessage(),e);
            result.error500("获取个人用户附件失败!");
        }
        return result;
    }




	/**
	 * 用户注册接口
	 *
	 * @param jsonObject
	 * @param user
	 * @return
	 */
	@PostMapping("/register")
	public Result<JSONObject> userRegister(@RequestBody JSONObject jsonObject, SysUser user) {
		Result<JSONObject> result = new Result<JSONObject>();
		String phone = jsonObject.getString("phone");
		String smscode = jsonObject.getString("smscode");
		Object code = redisUtil.get(phone);
		String username = jsonObject.getString("username");
		String password = jsonObject.getString("password");
		String userType = jsonObject.getString("userType");
		if(userType==null){
            result.setMessage("请选择用户类型");
            result.setSuccess(false);
            return result;
        }
		SysUser sysUser1 = sysUserService.getUserByName(username);
		if (sysUser1 != null) {
            result.setMessage("用户名已注册");
            result.setSuccess(false);
            return result;
        }
	//	SysUser sysUser2 = sysUserService.getUserByPhone(phone);
		//根据手机号和所在的用户类型查询用户信息（因为可能出现一个人即使个人用户又是企业用户）
		SysUser sysUser2 = sysUserService.getUserByPhone(phone);

		if (sysUser2 != null) {
			result.setMessage("该手机号已注册");
			result.setSuccess(false);
			return result;
		}
	/*	SysUser sysUser3 = sysUserService.getUserByEmail(email);
		if (sysUser3 != null) {
			result.setMessage("邮箱已被注册");
			result.setSuccess(false);
			return result;
		}*/

		if (!smscode.equals(code)) {
			result.setMessage("手机验证码错误");
			result.setSuccess(false);
			return result;
		}

		try {
			user.setCreateTime(new Date());// 设置创建时间
			String salt = oConvertUtils.randomGen(8);
			String passwordEncode = PasswordUtil.encrypt(username, password, salt);
			user.setSalt(salt);
			user.setUsername(username);
			user.setPassword(passwordEncode);
			user.setPhone(phone);
            user.setUserType(Integer.valueOf(userType));
            if("0".equals(userType)){//管理员为完成。暂时没有
                user.setStatus(1);
            }else{
                //专家企业为资料不全
                user.setStatus(3);
            }
			user.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
			user.setActivitiSync(CommonConstant.ACT_SYNC_1);
			//sysUserService.save(user);
			//注册时直接指定对应的角色
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setUserId(user.getId());
            SysRole  sysRole=new SysRole();
            if("1".equals(userType)){
                sysRole.setRoleCode("zlwws_gr");
            }else if("2".equals(userType)){
                sysRole.setRoleCode("zlwws_qy");
            }else if("3".equals(userType)){
                sysRole.setRoleCode("zlwws_zj");
            }
            sysRole=   sysRoleService.getOne(new QueryWrapper<SysRole>(sysRole));
            sysUserService.addUserWithRole(user, sysRole.getId());
			result.success("注册成功");
		} catch (Exception e) {
			result.error500("注册失败");
		}
		return result;
	}

	/**
	 *根据用户名或手机号查询用户信息
	 * @param
	 * @return
	 */
	@GetMapping("/querySysUser")
	public Result<Map<String, Object>> querySysUser(SysUser sysUser) {
		String phone = sysUser.getPhone();
		String username = sysUser.getUsername();
		Result<Map<String, Object>> result = new Result<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (oConvertUtils.isNotEmpty(phone)) {
			SysUser userList = sysUserService.getUserByPhone(phone);
			map.put("username",userList.getUsername());
			map.put("phone",userList.getPhone());
			result.setSuccess(true);
			result.setResult(map);
			return result;
		}
		if (oConvertUtils.isNotEmpty(username)) {
			SysUser userList = sysUserService.getUserByName(username);
			map.put("username",userList.getUsername());
			map.put("phone",userList.getPhone());
			result.setSuccess(true);
			result.setResult(map);
			return result;
		}
		result.setSuccess(false);
		result.setMessage("验证失败");
		return result;
	}

	/**
	 * 用户手机号验证
	 */
	@PostMapping("/phoneVerification")
	public Result<String> phoneVerification(@RequestBody JSONObject jsonObject) {
		Result<String> result = new Result<String>();
		String phone = jsonObject.getString("phone");
		String smscode = jsonObject.getString("smscode");
		Object code = redisUtil.get(phone);
		if (!smscode.equals(code)) {
			result.setMessage("手机验证码错误");
			result.setSuccess(false);
			return result;
		}
		redisUtil.set(phone, smscode);
		result.setResult(smscode);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 用户更改密码
	 */
	@GetMapping("/passwordChange")
	public Result<SysUser> passwordChange(@RequestParam(name="username")String username,
										  @RequestParam(name="password")String password,
			                              @RequestParam(name="smscode")String smscode,
			                              @RequestParam(name="phone") String phone) {
        Result<SysUser> result = new Result<SysUser>();
        SysUser sysUser=new SysUser();
        Object object= redisUtil.get(phone);
        if(null==object) {
        	result.setMessage("更改密码失败");
            result.setSuccess(false);
        }
        if(!smscode.equals(object)) {
        	result.setMessage("更改密码失败");
            result.setSuccess(false);
        }
        sysUser = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,username));
        if (sysUser == null) {
            result.setMessage("未找到对应实体");
            result.setSuccess(false);
            return result;
        } else {
            String salt = oConvertUtils.randomGen(8);
            sysUser.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
            sysUser.setPassword(passwordEncode);
            this.sysUserService.updateById(sysUser);
            result.setSuccess(true);
            result.setMessage("密码修改完成！");
            sysBaseAPI.addLog("用户："+sysUser.getUsername()+"密码修改成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
            return result;
        }
    }

    /**
     *根据类型获取专利页面信息
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value = "/patentList", method = RequestMethod.GET)
    public Result<IPage<Map>> patentList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
        Result<IPage<Map>> result = new Result<IPage<Map>>();
        Page<Map> page = new Page<Map>(pageNo, pageSize);
        String type = req.getParameter("type");
        //IPage<Map> pageList = sysUserService.queryPageSysList(page, type);
        result.setSuccess(true);
        //result.setResult(pageList);
        return result;
    }

    @RequestMapping(value = "/getCurrentUser",method=RequestMethod.GET)
    public Result<SysUser> getCurrentUser(){
        Result<SysUser> result = new Result<SysUser>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysUser sysUser= sysUserService.getById(loginUser.getId());
        result.setResult(sysUser);
        result.success("获取当前用户信息成功!");
        return result;
    }

    @RequestMapping(value="/empower",method=RequestMethod.GET)
    public Result<String> doExpertEmpower(@RequestParam String ids){
        Result<String> result = new Result<String>();
        String error="";
        if(!"".equals(ids)){
            for(int i=0;i<ids.split(",").length;i++){
                String id=ids.split(",")[i];
                if(!"".equals(id)){
                    SysUser sysUser=sysUserService.getById(ids.split(",")[i]);
                    if(sysUser.getStatus()==6){
                        sysUser.setStatus(4);
                        sysUserService.updateById(sysUser);
                    }else{
                        error+=sysUser.getUsername()+";";
                    }
                }
            }
        }
        if(!"".equals(error)){
            result.success("部分授权成功!授权失败账号为:"+error+"，失败原因:账号状态不满足授权状态!");
        }else{
            result.success("授权成功!");
        }
        sysBaseAPI.addLog("专家授权成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
        return result;
    }

}
