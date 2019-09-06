package org.jeecg.modules.system.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserPersonalData;
import org.jeecg.modules.system.service.ISysUserPersonalDataService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.GainetUtils;
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
 * @Description: 个人信息完善
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="个人信息完善")
@RestController
@RequestMapping("/system/sysUserPersonalData")
public class SysUserPersonalDataController {
	@Resource
	private ISysUserPersonalDataService sysUserPersonalDataService;

	@Resource
	private ISysUserService sysUserService;
	
	/**
	  * 分页列表查询
	 * @param sysUserPersonalData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "个人信息完善-分页列表查询")
	@ApiOperation(value="个人信息完善-分页列表查询", notes="个人信息完善-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysUserPersonalData>> queryPageList(SysUserPersonalData sysUserPersonalData,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysUserPersonalData>> result = new Result<IPage<SysUserPersonalData>>();
		QueryWrapper<SysUserPersonalData> queryWrapper = QueryGenerator.initQueryWrapper(sysUserPersonalData, req.getParameterMap());
		Page<SysUserPersonalData> page = new Page<SysUserPersonalData>(pageNo, pageSize);
		IPage<SysUserPersonalData> pageList = sysUserPersonalDataService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysUserPersonalData
	 * @return
	 */
	@AutoLog(value = "个人信息完善-添加")
	@ApiOperation(value="个人信息完善-添加", notes="个人信息完善-添加")
	@PostMapping(value = "/add")
	public Result<SysUserPersonalData> add(@RequestBody SysUserPersonalData sysUserPersonalData) {
		Result<SysUserPersonalData> result = new Result<SysUserPersonalData>();
		try {
			sysUserPersonalDataService.save(sysUserPersonalData);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param
	 * @return
	 */
	@AutoLog(value = "个人信息完善-编辑")
	@ApiOperation(value="个人信息完善-编辑", notes="个人信息完善-编辑")
	@PostMapping (value = "/edit")
	public Result<Map<String,Object>> edit(@RequestBody Map<String,Object>params) {
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		Subject subject = SecurityUtils.getSubject();
		LoginUser sysUser = (LoginUser)subject.getPrincipal();
		if(sysUser==null){
			result.setCode(1);
			result.setMessage("未获取到用户信息");
			return result;
		}
		SysUser user=sysUserService.getById(sysUser.getId());
		String userId= GainetUtils.stringOf(params.get("userId"));
		if(user.getUserType()!=0){
			if(!userId.equals(sysUser.getId())){
				result.setCode(1);
				result.setMessage("获取到用户信息异常");
				return result;
			}
		}
		String expertName= GainetUtils.stringOf(params.get("expertName"));
		String expertSex= GainetUtils.stringOf(params.get("expertSex"));
		String cardNumber= GainetUtils.stringOf(params.get("cardNumber"));
		String tel= GainetUtils.stringOf(params.get("tel"));
		String testEmail= GainetUtils.stringOf(params.get("testEmail"));
		String administrativePost= GainetUtils.stringOf(params.get("administrativePost"));
		String professionalTitles= GainetUtils.stringOf(params.get("professionalTitles"));
		String img= GainetUtils.stringOf(params.get("img"));

		boolean b=sysUserPersonalDataService.editPersonal(userId,expertName,expertSex,cardNumber,tel,testEmail,administrativePost,professionalTitles,img);
		if(b){
			result.setCode(0);
			result.setMessage("保存成功");
		}else {
			result.setCode(1);
			result.setMessage("保存失败");
		}
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "个人信息完善-通过id删除")
	@ApiOperation(value="个人信息完善-通过id删除", notes="个人信息完善-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<SysUserPersonalData> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysUserPersonalData> result = new Result<SysUserPersonalData>();
		SysUserPersonalData sysUserPersonalData = sysUserPersonalDataService.getById(id);
		if(sysUserPersonalData==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserPersonalDataService.removeById(id);
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
	@AutoLog(value = "个人信息完善-批量删除")
	@ApiOperation(value="个人信息完善-批量删除", notes="个人信息完善-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysUserPersonalData> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysUserPersonalData> result = new Result<SysUserPersonalData>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysUserPersonalDataService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "个人信息完善-通过id查询")
	@ApiOperation(value="个人信息完善-通过id查询", notes="个人信息完善-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Map<String,Object>> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		Subject subject = SecurityUtils.getSubject();
		LoginUser sysUser = (LoginUser)subject.getPrincipal();
		if(sysUser==null){
			result.setCode(1);
			result.setMessage("未获取到用户信息");
			return result;
		}
		Map<String,Object> rsMap=new HashMap<>();
		if(!GainetUtils.isEmpty(id)){
			SysUser user=sysUserService.getById(sysUser.getId());
			if(user.getUserType()==0){
				rsMap=sysUserPersonalDataService.personalData(id);
			}
		}else{
			rsMap=sysUserPersonalDataService.personalData(sysUser.getId());
		}

		if(rsMap==null){
			result.setCode(1);
			result.setMessage("未获取到用户信息");
			return result;
		}else{
		result.setResult(rsMap);
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
      QueryWrapper<SysUserPersonalData> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SysUserPersonalData sysUserPersonalData = JSON.parseObject(deString, SysUserPersonalData.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysUserPersonalData, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<SysUserPersonalData> pageList = sysUserPersonalDataService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "个人信息完善列表");
      mv.addObject(NormalExcelConstants.CLASS, SysUserPersonalData.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("个人信息完善列表数据", "导出人:Jeecg", "导出信息"));
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
              List<SysUserPersonalData> listSysUserPersonalDatas = ExcelImportUtil.importExcel(file.getInputStream(), SysUserPersonalData.class, params);
              for (SysUserPersonalData sysUserPersonalDataExcel : listSysUserPersonalDatas) {
                  sysUserPersonalDataService.save(sysUserPersonalDataExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listSysUserPersonalDatas.size());
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

}
