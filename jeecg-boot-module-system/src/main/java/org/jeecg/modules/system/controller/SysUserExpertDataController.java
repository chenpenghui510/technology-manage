package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserExpertData;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserExpertDataService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.service.ISysUserService;
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
 * @Description: 专家用户
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="专家用户")
@RestController
@RequestMapping("/sys/expert")
public class SysUserExpertDataController {
	@Autowired
	private ISysUserExpertDataService sysUserExpertDataService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
    private ISysDictService iSysDictService;

	/**
	  * 分页列表查询
	 * @param sysUserExpertData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "专家用户-分页列表查询")
	@ApiOperation(value="专家用户-分页列表查询", notes="专家用户-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysUserExpertData>> queryPageList(SysUserExpertData sysUserExpertData,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysUserExpertData>> result = new Result<IPage<SysUserExpertData>>();
		sysUserExpertData.setIsDelete(0);
		QueryWrapper<SysUserExpertData> queryWrapper = QueryGenerator.initQueryWrapper(sysUserExpertData, req.getParameterMap());
		Page<SysUserExpertData> page = new Page<SysUserExpertData>(pageNo, pageSize);
		IPage<SysUserExpertData> pageList = sysUserExpertDataService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysUserExpertData
	 * @return
	 */
	@AutoLog(value = "专家用户-添加")
	@ApiOperation(value="专家用户-添加", notes="专家用户-添加")
	@PostMapping(value = "/add")
	public Result<SysUserExpertData> add(@RequestBody SysUserExpertData sysUserExpertData) {
		Result<SysUserExpertData> result = new Result<SysUserExpertData>();
		try {
			sysUserExpertData.setIsDelete(0);
			sysUserExpertDataService.save(sysUserExpertData);
            sysUserExpertDataService.doSaveFiles(sysUserExpertData.getExpertId());
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			String id= sysUser.getId();
			SysUser user = sysUserService.getById(id);
			if(user.getUserType()==3){
				user.setExpertId(sysUserExpertData.getExpertId());
				sysUserService.updateById(user);
			}
			result.setResult(sysUserExpertData);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysUserExpertData
	 * @return
	 */
	@AutoLog(value = "专家用户-编辑")
	@ApiOperation(value="专家用户-编辑", notes="专家用户-编辑")
	@PostMapping(value = "/edit")
	public Result<SysUserExpertData> edit(@RequestBody SysUserExpertData sysUserExpertData) {
		Result<SysUserExpertData> result = new Result<SysUserExpertData>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String userId= sysUser.getId();
		try{
			SysUser user = sysUserService.getById(userId);
			boolean ok = sysUserExpertDataService.updateById(sysUserExpertData);
			if(user.getUserType()==3){
				if(user.getExpertId()==null||"".equals(user.getExpertId())){
					user.setExpertId(sysUserExpertData.getExpertId());
					sysUserService.updateById(user);
				}
			}
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result.error500("修改专家信息异常!");
		}
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专家用户-通过id删除")
	@ApiOperation(value="专家用户-通过id删除", notes="专家用户-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<SysUserExpertData> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysUserExpertData> result = new Result<SysUserExpertData>();
		SysUserExpertData sysUserExpertData = sysUserExpertDataService.getById(id);
		if(sysUserExpertData==null) {
			result.error500("未找到对应实体");
		}else {
			sysUserExpertData.setIsDelete(1);
			sysUserExpertDataService.updateById(sysUserExpertData);
			result.success("删除成功!");
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "专家用户-批量删除")
	@ApiOperation(value="专家用户-批量删除", notes="专家用户-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysUserExpertData> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysUserExpertData> result = new Result<SysUserExpertData>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysUserExpertDataService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "专家用户-通过id查询")
	 @ApiOperation(value="专家用户-通过id查询", notes="专家用户-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<Object> queryById(@RequestParam(name="id",required=true) String id) {
		 Result<Object> result = new Result<Object>();
		 SysUserExpertData sysUserExpertData = sysUserExpertDataService.getById(id);
		 if(sysUserExpertData==null||sysUserExpertData.getIsDelete()==1) {
			 result.error500("未获取到相关专家信息！");
		 }else {
		 	Object obj = iSysDictService.getDictInfoByObj(sysUserExpertData);
		 	result.setResult(obj);
		 	result.setSuccess(true);
		 }
		 return result;
	 }

	 /**
	  * 根据当前登录用户信息获取专家完善信息
	  * @return
	  */
	 @AutoLog(value = "专家用户-根据当前登录用户信息获取专家完善信息")
	 @ApiOperation(value="专家用户-根据当前登录用户信息获取专家完善信息", notes="专家用户-根据当前登录用户信息获取专家完善信息")
	 @GetMapping(value = "/queryByLogin")
	 public Result<SysUserExpertData> queryByLogin(@RequestParam(required = false,value="id")String id) {
		 Result<SysUserExpertData> result = new Result<SysUserExpertData>();
		 if(id==null){
			 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			 String userId= sysUser.getId();
			 SysUser user = sysUserService.getById(userId);
			 if(user.getExpertId()!=null){
				 SysUserExpertData sysUserExpertData = sysUserExpertDataService.getById(user.getExpertId());
				 if(sysUserExpertData==null||sysUserExpertData.getIsDelete()==1) {
					 result.error500("未获取到相关专家信息！");
				 }else {
					 result.setResult(sysUserExpertData);
					 result.setSuccess(true);
				 }
			 }else{
				 result.error500("未获取到相关专家信息！");
			 }
		 }else{
			 SysUserExpertData sysUserExpertData = sysUserExpertDataService.getById(id);
			 if(sysUserExpertData==null||sysUserExpertData.getIsDelete()==1) {
				 result.error500("未获取到相关专家信息！");
			 }else {
				 result.setResult(sysUserExpertData);
				 result.setSuccess(true);
			 }
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
      QueryWrapper<SysUserExpertData> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SysUserExpertData sysUserExpertData = JSON.parseObject(deString, SysUserExpertData.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysUserExpertData, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<SysUserExpertData> pageList = sysUserExpertDataService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "专家用户列表");
      mv.addObject(NormalExcelConstants.CLASS, SysUserExpertData.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("专家用户列表数据", "导出人:Jeecg", "导出信息"));
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
              List<SysUserExpertData> listSysUserExpertDatas = ExcelImportUtil.importExcel(file.getInputStream(), SysUserExpertData.class, params);
              for (SysUserExpertData sysUserExpertDataExcel : listSysUserExpertDatas) {
                  sysUserExpertDataService.save(sysUserExpertDataExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listSysUserExpertDatas.size());
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

	 @RequestMapping(value="/getExpertFiles",method = RequestMethod.GET)
	 public Result<List<Map<String,String>>> getExpertFiles(@RequestParam(name="id") String id){
		 Result<List<Map<String,String>>> result = new Result<List<Map<String,String>>>();
		 try{
			 List<Map<String,String>> expertFiles = sysUserExpertDataService.getExpertFiles(id);
			 result.setResult(expertFiles);
			 result.success("查询专家用户附件成功!");
		 }catch(Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("获取专家用户附件失败!");
		 }
		 return result;
	 }

	 @RequestMapping(value="/checkRegion",method=RequestMethod.GET)
	 public Result<Boolean> doCheckRegion(@RequestParam(name="id")String id){
		 Result<Boolean> result = new Result<Boolean>();
		 try{
			Boolean flag = sysUserExpertDataService.doCheckRegion(id);
			 result.setResult(flag);
			 result.success("查询专家用户附件成功!");
		 }catch(Exception e){
		 	log.error(e.getMessage(),e);
		 	result.error500("校验地区信息异常!");
		 }
		 return result;
	 }

	 @RequestMapping(value="/searchPhoneByCardNumber",method=RequestMethod.GET)
	 public Result<SysUserExpertData> searchPhoneByCardNumber(@RequestParam(required = false,name="number")Object number){
  		Result<SysUserExpertData> result = new Result<SysUserExpertData>();
  		SysUserExpertData sysUserExpertData= new SysUserExpertData();
  		sysUserExpertData.setCardNumber(number+"");
  		sysUserExpertData.setIsDelete(0);
  		try{
			SysUserExpertData newData = sysUserExpertDataService.getOne(new QueryWrapper<SysUserExpertData>(sysUserExpertData));
			if(newData!=null){
				SysUser sysUser = new SysUser();
				sysUser.setExpertId(newData.getExpertId());
				sysUser.setDelFlag("0");
				SysUser newUser = sysUserService.getOne(new QueryWrapper<SysUser>(sysUser));
				if(newUser!=null){
					result.setSuccess(false);
					result.setMessage("该证件号已绑定其他账号，您没有查询权限");
				}else{
					result.setResult(newData);
					result.success("查询专家信息成功!");
				}
			}else{
				result.setSuccess(false);
				result.setMessage("根据该证件号未查询到已注册的专家信息,请点击“去填写”完善专家信息! ");
			}
		}catch(Exception e){
  			log.error(e.getMessage(),e);
  			result.error500("查询专家信息异常!");
		}
  		return result;
	 }
	 @RequestMapping(value="/doCheckCardNumber",method=RequestMethod.GET)
	 public Result<Boolean> doCheckCardNumber(@RequestParam(name="cardNumber")String cardNumber){
  		Result<Boolean> result = new Result<Boolean>();
		 SysUserExpertData sysUserExpertData= new SysUserExpertData();
		 sysUserExpertData.setCardNumber(cardNumber+"");
		 sysUserExpertData.setIsDelete(0);
		 try{
			 SysUserExpertData newData = sysUserExpertDataService.getOne(new QueryWrapper<SysUserExpertData>(sysUserExpertData));
			 if(newData!=null){
			 	result.setSuccess(false);
			 	result.setMessage("证件号已被使用");
			 }else{
				 result.success("该证件号可以使用!");
			 }
		 }catch(Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("查询专家信息异常!");
		 }
		 return result;
	 }
	 @RequestMapping(value="/doCheckCode",method=RequestMethod.GET)
	 public Result<Boolean> doCheckCode(@RequestParam(name="captcha")String captcha,@RequestParam(name="phoneNumber")String phoneNumber){
		Result<Boolean> result = new Result<Boolean>();
		try{
			Object smscode = redisUtil.get(phoneNumber);
			if(smscode==null){
				result.setSuccess(false);
				result.setMessage("获取手机验证码异常，请稍后再试!");
			}else{
				if (!captcha.equals(smscode)) {
					result.setMessage("手机验证码错误");
					result.setSuccess(false);
				}else{
					result.setResult(true);
					result.setMessage("查询成功");
				}
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result.error500("查询专家信息异常!");
		}
		 return result;
	 }

	 /**
	  * 提交审核
	  * @param expertId
	  * @return
	  */
	 @RequestMapping(value="/finish",method=RequestMethod.GET)
	 public Result<Boolean> doFinishWrite(@RequestParam(name="expertId")String expertId){
  		Result<Boolean> result = new Result<Boolean>();
  		try{
			SysUser sysUser = new SysUser();
			sysUser.setExpertId(expertId);
			sysUser.setUserType(3);
			sysUser.setDelFlag("0");
			SysUser entityUser = sysUserService.getOne(new QueryWrapper<SysUser>(sysUser));
			if(entityUser==null){
				result.setSuccess(false);
				result.setMessage("未找到专家账号信息!");
			}else{
				if(entityUser.getStatus()!=3){
					result.setSuccess(false);
					result.setMessage("当前非资料未完善状态无法提交审核!");
				}else{
					entityUser.setStatus(6);
					sysUserService.updateById(entityUser);
					result.success("提交审核成功!");
				}
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result.error500("提交审核操作异常!");
		}
  		return result;
	 }

	 /**
	  * 重新提交
	  * @param expertId
	  * @return
	  */
	 @RequestMapping(value="/reWrite",method=RequestMethod.GET)
	 public Result<Boolean> reWrite(@RequestParam(name="expertId")String expertId){
		 Result<Boolean> result = new Result<Boolean>();
		 try{
			 SysUser sysUser = new SysUser();
			 sysUser.setExpertId(expertId);
			 sysUser.setUserType(3);
			 sysUser.setDelFlag("0");
			 SysUser entityUser = sysUserService.getOne(new QueryWrapper<SysUser>(sysUser));
			 if(entityUser==null){
				 result.setSuccess(false);
				 result.setMessage("未找到专家账号信息!");
			 }else{
				 if(entityUser.getStatus()!=6){
					 result.setSuccess(false);
					 result.setMessage("当前非待授权状态无法重新提交!");
				 }else{
					 entityUser.setStatus(3);
					 sysUserService.updateById(entityUser);
					 result.success("成功!");
				 }
			 }
		 }catch(Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("重新提交操作异常!");
		 }
		 return result;
	 }

	 /**
	  * 获取户籍所在地
	  * @return
	  */
	 @RequestMapping(value="/getRegionById",method=RequestMethod.GET)
	 public Result<String> getRegionById(@RequestParam(required = false,value="regionId")Integer regionId){
	 	Result<String> result = new Result<>();
	 	try{
	 		if(regionId==null){
	 			result.setResult("");
				result.success("获取成功!");
			}else{
				String region = sysUserExpertDataService.getRegionById(regionId);
				result.setResult(region);
				result.success("获取成功!");
			}
		}catch(Exception e){
	 		log.error(e.getMessage(),e);
	 		result.error500("获取异常!");
		}
	 	return result;
	 }

}
