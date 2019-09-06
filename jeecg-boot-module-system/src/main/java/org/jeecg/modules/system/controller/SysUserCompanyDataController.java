package org.jeecg.modules.system.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.GainetUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 企业信息
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="企业信息")
@RestController
@RequestMapping("/system/company")
public class SysUserCompanyDataController {
	@Autowired
	private ISysUserCompanyDataService sysUserCompanyDataService;

	 @Autowired
	 private ISysUserService sysUserService;

     @Autowired
     private IPublicUploadAnnexService publicUploadAnnexService;

	 @Autowired
	 private ISysRoleService sysRoleService;
	 @Autowired
	 private ISysUserRoleService sysUserRoleService;
	/**
	  * 分页列表查询
	 * @param sysUserCompanyData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "企业信息-分页列表查询")
	@ApiOperation(value="企业信息-分页列表查询", notes="企业信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysUserCompanyData>> queryPageList(SysUserCompanyData sysUserCompanyData,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysUserCompanyData>> result = new Result<IPage<SysUserCompanyData>>();
		QueryWrapper<SysUserCompanyData> queryWrapper = QueryGenerator.initQueryWrapper(sysUserCompanyData, req.getParameterMap());
		Page<SysUserCompanyData> page = new Page<SysUserCompanyData>(pageNo, pageSize);
		IPage<SysUserCompanyData> pageList = sysUserCompanyDataService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysUserCompanyData
	 * @return
	 */
	@AutoLog(value = "企业信息-添加")
	@ApiOperation(value="企业信息-添加", notes="企业信息-添加")
	@PostMapping(value = "/add")
	public Result<SysUserCompanyData> add(@RequestBody SysUserCompanyData sysUserCompanyData) {
		Result<SysUserCompanyData> result = new Result<SysUserCompanyData>();
		try {
			sysUserCompanyDataService.save(sysUserCompanyData);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysUserCompanyData
	 * @return
	 */
	@AutoLog(value = "企业信息-编辑")
	@ApiOperation(value="企业信息-编辑", notes="企业信息-编辑")
	@PutMapping(value = "/edit")
	public Result<SysUserCompanyData> edit(@RequestBody SysUserCompanyData sysUserCompanyData) {
		Result<SysUserCompanyData> result = new Result<SysUserCompanyData>();
		SysUserCompanyData sysUserCompanyDataEntity = sysUserCompanyDataService.getById(sysUserCompanyData.getId());
		if(sysUserCompanyDataEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserCompanyDataService.updateById(sysUserCompanyData);
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业信息-通过id删除")
	@ApiOperation(value="企业信息-通过id删除", notes="企业信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<SysUserCompanyData> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysUserCompanyData> result = new Result<SysUserCompanyData>();
		SysUserCompanyData sysUserCompanyData = sysUserCompanyDataService.getById(id);
		if(sysUserCompanyData==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserCompanyDataService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "企业信息-批量删除")
	@ApiOperation(value="企业信息-批量删除", notes="企业信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysUserCompanyData> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysUserCompanyData> result = new Result<SysUserCompanyData>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysUserCompanyDataService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业信息-通过id查询")
	@ApiOperation(value="企业信息-通过id查询", notes="企业信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysUserCompanyData> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysUserCompanyData> result = new Result<SysUserCompanyData>();
		SysUserCompanyData sysUserCompanyData = sysUserCompanyDataService.getById(id);
		if(sysUserCompanyData==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysUserCompanyData);
			result.setSuccess(true);
		}
		return result;
	}



	 @GetMapping(value = "/getUserInfoByComId")
	 public Result<SysUser> getUserInfoByComId(@RequestParam(name="id",required=true) String id) {
		 Result<SysUser> result = new Result<SysUser>();
		 SysUserCompanyData sysUserCompanyData = sysUserCompanyDataService.getById(id);
		 if(sysUserCompanyData==null) {
			 result.error500("未找到对应实体");
		 }else {
			SysUser sysUser= sysUserService.getById(sysUserCompanyData.getUserId());
			 result.setResult(sysUser);
			 result.setSuccess(true);
		 }
		 return result;
	 }

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<SysUserCompanyData> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
			  SysUserCompanyData sysUserCompanyData = JSON.parseObject(deString, SysUserCompanyData.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysUserCompanyData, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<SysUserCompanyData> pageList = sysUserCompanyDataService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "企业信息列表");
      mv.addObject(NormalExcelConstants.CLASS, SysUserCompanyData.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("企业信息列表数据", "导出人:Jeecg", "导出信息"));
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
              List<SysUserCompanyData> listsysUserCompanyDatas = ExcelImportUtil.importExcel(file.getInputStream(), SysUserCompanyData.class, params);
              for (SysUserCompanyData sysUserCompanyDataExcel : listsysUserCompanyDatas) {
                  sysUserCompanyDataService.save(sysUserCompanyDataExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listsysUserCompanyDatas.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

	 /**
	  * 查询企业信息
	  * @param
	  * @return
	  */
	 @AutoLog(value = "查询企业信息")
	 @ApiOperation(value="查询企业信息", notes="查询企业信息")
	 @GetMapping(value = "/getCompanyManagerInfo")
	 public Result<Map> getCompanyManagerInfo() {
		 Result<Map> result = new Result<Map>();
		 //获取当前用户信息
		 Subject subject = SecurityUtils.getSubject();
		 LoginUser sysUser = (LoginUser)subject.getPrincipal();
		 SysUser sys= sysUserService.getById(sysUser.getId());
		 Map map=new HashMap();
		 if(sys.getUserType()!=2){
			map.put("type",2);
			result.setResult(map);
			result.setSuccess(true);
			return result;
		 }
		//根据当前的用户ID查询是否存在企业信息
		 map=sysUserCompanyDataService.getCompanyManagerInfo(sysUser.getId());
		 map.put("type",1);
		// sysUserCompanyData sysUserCompanyData = sysUserCompanyDataService.getById(id);
		 result.setResult(map);
		 result.setSuccess(true);
		 return result;
	 }
	 /**
	  * 查询企业信息
	  * @param
	  * @return
	  */
	 @AutoLog(value = "查询企业信息")
	 @ApiOperation(value="查询企业信息", notes="查询企业信息")
	 @GetMapping(value = "/getCompanyManagerInfo1")
	 public Result<Map> getCompanyManagerInfo1(@RequestParam(name="id")String id) {
		 Result<Map> result = new Result<Map>();
		 //根据当前的用户ID查询是否存在企业信息
		 Map map=sysUserCompanyDataService.getCompanyManagerInfo(id);
		 // sysUserCompanyData sysUserCompanyData = sysUserCompanyDataService.getById(id);
		 result.setResult(map);
		 result.setSuccess(true);
		 return result;
	 }

	 @RequestMapping(value="/doCheckCardNumber",method=RequestMethod.GET)
	 public Result<Boolean> doCheckCardNumber(@RequestParam(name="cardNumber")String cardNumber,@RequestParam(name="oldCard")String oldCard){
		 Result<Boolean> result = new Result<Boolean>();
		 SysUserCompanyData sysUserCompanyData= new SysUserCompanyData();
		 sysUserCompanyData.setUserCard(cardNumber);
		 try{
			 List<SysUserCompanyData> newData=new ArrayList<SysUserCompanyData>();
		 	if(GainetUtils.isEmpty(oldCard)){
				 newData = sysUserCompanyDataService.list(new QueryWrapper<SysUserCompanyData>(sysUserCompanyData));
			}else{
				newData = sysUserCompanyDataService.getUserList(cardNumber,oldCard);
			}
			 if(newData!=null&&newData.size()!=0){
				 result.setSuccess(false);
				 result.setMessage("证件号已被使用");
			 }else{
				 result.success("该证件号可以使用!");
			 }
		 }catch(Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("查询企业信息异常!");
		 }
		 return result;
	 }

	 @RequestMapping(value = "/addAndEditCompanyInfo", method = RequestMethod.POST)
	 public Result<SysUserCompanyData> addAndEditCompanyInfo(@RequestBody JSONObject jsonObject) {
		 Result<SysUserCompanyData> result = new Result<SysUserCompanyData>();
		 if(jsonObject==null){
			 result.error500("传参失败!");
			 return result;
		 }
		 try {
			 String username = jsonObject.getString("username")!=null?jsonObject.getString("username"):"";
			 String user_name = jsonObject.getString("user_name")!=null?jsonObject.getString("user_name"):"";
			 String user_sex = jsonObject.getString("user_sex")!=null?jsonObject.getString("user_sex"):"";
			 String user_card = jsonObject.getString("user_card")!=null?jsonObject.getString("user_card"):"";
			 String phone = jsonObject.getString("phone")!=null?jsonObject.getString("phone"):"";
			 String user_telephone = jsonObject.getString("user_telephone")!=null?jsonObject.getString("user_telephone"):"";
			 String email = jsonObject.getString("email")!=null?jsonObject.getString("email"):"";
			 String user_post = jsonObject.getString("user_post")!=null?jsonObject.getString("user_post"):"";
			 String user_title = jsonObject.getString("user_title")!=null?jsonObject.getString("user_title"):"";
			 String id = jsonObject.getString("id");

			 //修改user表信息
			 Subject subject = SecurityUtils.getSubject();
			 LoginUser loginUser = (LoginUser)subject.getPrincipal();
			 SysUser sysUser= sysUserService.getById(loginUser.getId());
			 //保存企业信息
			 SysUserCompanyData sysUserCompanyData=new SysUserCompanyData();
			 if(!GainetUtils.isEmpty(id)){//修改
				 sysUserCompanyData= sysUserCompanyDataService.getById(id);
				 sysUser= sysUserService.getById(sysUserCompanyData.getUserId());
			 }
			 sysUser.setEmail(email);
			 sysUser.setPhone(phone);
			 sysUser.setUsername(username);
			 sysUser.setSex(Integer.valueOf(user_sex));
			 sysUserService.updateById(sysUser);

			 sysUserCompanyData.setUserCard(user_card);
			 sysUserCompanyData.setUserId(sysUser.getId());
			 sysUserCompanyData.setUserName(user_name);
			 sysUserCompanyData.setUserPost(user_post);
			 sysUserCompanyData.setUserTitle(user_title);
			 sysUserCompanyData.setUserTelephone(user_telephone);
			 sysUserCompanyData.setUserSex(Integer.valueOf(user_sex));
			 sysUserCompanyData.setCreateTime(new Date());//设置创建时间
			 if(GainetUtils.isEmpty(id)){
				 sysUserCompanyDataService.save(sysUserCompanyData);
				 //默认添加固定信息附件\
                 PublicUploadAnnex publicUploadAnnex=new PublicUploadAnnex();
                 publicUploadAnnex.setFileName("营业执照扫描件");
                 publicUploadAnnex.setKeyId(sysUserCompanyData.getId());
                 publicUploadAnnex.setAnnexType(3);
                 publicUploadAnnex.setRequired(1);
                 publicUploadAnnexService.save(publicUploadAnnex);
                 PublicUploadAnnex publicUploadAnnex1=new PublicUploadAnnex();
                 publicUploadAnnex1.setFileName("法人身份证扫描件");
                 publicUploadAnnex1.setKeyId(sysUserCompanyData.getId());
                 publicUploadAnnex1.setAnnexType(3);
                 publicUploadAnnex1.setRequired(1);
                 publicUploadAnnexService.save(publicUploadAnnex1);
			 }else{
				 sysUserCompanyDataService.updateById(sysUserCompanyData);
			 }
			 result.success("操作成功！");
			 result.setResult(sysUserCompanyData);
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 result.error500("操作失败");
		 }
		 return result;
	 }

	 /**
	  *  完善信息后修改状态和角色
	  * @param
	  * @return
	  */
	 @AutoLog(value = "完善信息后修改状态")
	 @ApiOperation(value="完善信息后修改状态", notes="完善信息后修改状态")
	 @GetMapping(value = "/updateUserState")
	 public Result<SysUserCompanyData> updateUserState(@RequestParam(name="type") Integer type,@RequestParam(name="id") String id) {
		 Result<SysUserCompanyData> result = new Result<SysUserCompanyData>();
		 SysUserCompanyData sysUserCompanyDataEntity = sysUserCompanyDataService.getById(id);
		 if(sysUserCompanyDataEntity==null) {
			 result.error500("未找到对应实体");
		 }else {
			 SysUser sysUser= sysUserService.getById(sysUserCompanyDataEntity.getUserId());
			 if(sysUser.getStatus()==3||sysUser.getStatus()==5){//只有材料未完善和退回将状态改为待审核
                 sysUser.setStatus(4);//修改用户状态为待审核
             }
			 boolean ok= sysUserService.updateById(sysUser);
			 //修改用户的角色
			 /*SysRole  sysRole=new SysRole();
			 sysRole.setRoleCode("qy");//获取角色企业信息id
			 sysRole=  sysRoleService.getOne(new QueryWrapper<SysRole>(sysRole));

			 SysUserRole sysUserRole=new SysUserRole();
			 sysUserRole.setUserId(sysUser.getId());
			 sysUserRole=sysUserRoleService.getOne(new QueryWrapper<SysUserRole>(sysUserRole));
			 sysUserRole.setRoleId(sysRole.getId());
			 boolean ok= sysUserRoleService.updateById(sysUserRole);*/
			 if(ok) {
				 result.success("修改成功!");
			 }
		 }
		 return result;
	 }



}
