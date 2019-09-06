package org.jeecg.modules.system.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.jeecg.modules.system.entity.SysUserCompanySpecial;
import org.jeecg.modules.system.service.ISysUserCompanySpecialService;
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
 * @Description: 重大专项
 * @Author: jeecg-boot
 * @Date:   2019-08-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="重大专项")
@RestController
@RequestMapping("/system/sysUserCompanySpecial")
public class SysUserCompanySpecialController {
	@Autowired
	private ISysUserCompanySpecialService sysUserCompanySpecialService;
	 @Autowired
	 private ISysUserService sysUserService;
	/**
	  * 分页列表查询
	 * @param sysUserCompanySpecial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "重大专项-分页列表查询")
	@ApiOperation(value="重大专项-分页列表查询", notes="重大专项-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysUserCompanySpecial>> queryPageList(SysUserCompanySpecial sysUserCompanySpecial,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysUserCompanySpecial>> result = new Result<IPage<SysUserCompanySpecial>>();
		Subject subject = SecurityUtils.getSubject();
		LoginUser sysUser = (LoginUser)subject.getPrincipal();
		if(sysUser==null){
			result.error500("未成功登陆");
			return result;
		}
		SysUser user=sysUserService.getById(sysUser.getId());
		if(user==null){
			result.error500("未成功登陆");
			return result;
		}
		String name= req.getParameter("name");
		String level= req.getParameter("level");
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime= req.getParameter("startTime");
		String endTime= req.getParameter("endTime");
		String userId="";
		if(user.getUserType()!=0){//如果不是管理员查当前登录人的数据
			userId=sysUser.getId();
		}
		Page<SysUserCompanySpecial> page = new Page<SysUserCompanySpecial>(pageNo, pageSize);
		IPage<SysUserCompanySpecial> pageList = sysUserCompanySpecialService.pageListNew(page,name,level,startTime,endTime,userId);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysUserCompanySpecial
	 * @return
	 */
	@AutoLog(value = "重大专项-添加")
	@ApiOperation(value="重大专项-添加", notes="重大专项-添加")
	@PostMapping(value = "/add")
	public Result<SysUserCompanySpecial> add(@RequestBody SysUserCompanySpecial sysUserCompanySpecial) {
		Result<SysUserCompanySpecial> result = new Result<SysUserCompanySpecial>();
		try {
			Subject subject = SecurityUtils.getSubject();
			LoginUser sysUser = (LoginUser)subject.getPrincipal();
			if(sysUser==null){
				result.error500("未成功登陆");
				return result;
			}
			sysUserCompanySpecial.setUserId(sysUser.getId());
			sysUserCompanySpecial.setUserName(sysUser.getRealname());
			sysUserCompanySpecial.setCreateTime(new Date());
			sysUserCompanySpecial.setUpdateTime(new Date());
			sysUserCompanySpecialService.save(sysUserCompanySpecial);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysUserCompanySpecial
	 * @return
	 */
	@AutoLog(value = "重大专项-编辑")
	@ApiOperation(value="重大专项-编辑", notes="重大专项-编辑")
	@PutMapping(value = "/edit")
	public Result<SysUserCompanySpecial> edit(@RequestBody SysUserCompanySpecial sysUserCompanySpecial) {
		Result<SysUserCompanySpecial> result = new Result<SysUserCompanySpecial>();
		Subject subject = SecurityUtils.getSubject();
		LoginUser sysUser = (LoginUser)subject.getPrincipal();
		if(sysUser==null){
			result.error500("未成功登陆");
			return result;
		}
		SysUserCompanySpecial sysUserCompanySpecialEntity = sysUserCompanySpecialService.getById(sysUserCompanySpecial.getId());
		if(sysUserCompanySpecialEntity==null) {
			result.error500("未找到对应实体");
		}else {
			sysUserCompanySpecialEntity.setUpdateTime(new Date());
			boolean ok = sysUserCompanySpecialService.updateById(sysUserCompanySpecial);
			//TODO 返回false说明什么？
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
	@AutoLog(value = "重大专项-通过id删除")
	@ApiOperation(value="重大专项-通过id删除", notes="重大专项-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<SysUserCompanySpecial> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysUserCompanySpecial> result = new Result<SysUserCompanySpecial>();
		SysUserCompanySpecial sysUserCompanySpecial = sysUserCompanySpecialService.getById(id);
		if(sysUserCompanySpecial==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserCompanySpecialService.removeById(id);
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
	@AutoLog(value = "重大专项-批量删除")
	@ApiOperation(value="重大专项-批量删除", notes="重大专项-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysUserCompanySpecial> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysUserCompanySpecial> result = new Result<SysUserCompanySpecial>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysUserCompanySpecialService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "重大专项-通过id查询")
	@ApiOperation(value="重大专项-通过id查询", notes="重大专项-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysUserCompanySpecial> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysUserCompanySpecial> result = new Result<SysUserCompanySpecial>();
		SysUserCompanySpecial sysUserCompanySpecial = sysUserCompanySpecialService.getById(id);
		if(sysUserCompanySpecial==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysUserCompanySpecial);
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
      QueryWrapper<SysUserCompanySpecial> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SysUserCompanySpecial sysUserCompanySpecial = JSON.parseObject(deString, SysUserCompanySpecial.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysUserCompanySpecial, request.getParameterMap());
          }else{
			  SysUserCompanySpecial sysUserCompanySpecial = new SysUserCompanySpecial();
			  queryWrapper = QueryGenerator.initQueryWrapper(sysUserCompanySpecial, request.getParameterMap());
		  }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	  Subject subject = SecurityUtils.getSubject();
	  LoginUser sysUser = (LoginUser)subject.getPrincipal();
	  if(sysUser==null){
		  return mv;
	  }
	  SysUser user=sysUserService.getById(sysUser.getId());
	  if(user==null){
		  return mv;
	  }
	  if(user.getUserType()!=0){//如果不是管理员查当前登录人的数据
		  queryWrapper.eq("user_id",sysUser.getId());
	  }
      List<SysUserCompanySpecial> pageList = sysUserCompanySpecialService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "重大专项");
      mv.addObject(NormalExcelConstants.CLASS, SysUserCompanySpecial.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("重大专项数据", "", "导出信息"));
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
              List<SysUserCompanySpecial> listSysUserCompanySpecials = ExcelImportUtil.importExcel(file.getInputStream(), SysUserCompanySpecial.class, params);
              for (SysUserCompanySpecial sysUserCompanySpecialExcel : listSysUserCompanySpecials) {
                  sysUserCompanySpecialService.save(sysUserCompanySpecialExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listSysUserCompanySpecials.size());
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
