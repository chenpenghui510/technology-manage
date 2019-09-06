package org.jeecg.modules.system.controller;

import java.text.SimpleDateFormat;
import java.util.*;
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
import org.jeecg.modules.system.entity.SysUserCompanyPatent;
import org.jeecg.modules.system.service.ISysUserCompanyPatentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.GainetUtils;
import org.jeecg.modules.system.vo.SysUserPatentCountVo;
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
 * @Description: 企业用户专利著作
 * @Author: jeecg-boot
 * @Date:   2019-08-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags="企业用户专利著作")
@RestController
@RequestMapping("/system/sysUserCompanyPatent")
public class SysUserCompanyPatentController {
	@Autowired
	private ISysUserCompanyPatentService sysUserCompanyPatentService;
	 @Autowired
	 private ISysUserService sysUserService;
	/**
	  * 分页列表查询
	 * @param sysUserCompanyPatent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "企业用户专利著作-分页列表查询")
	@ApiOperation(value="企业用户专利著作-分页列表查询", notes="企业用户专利著作-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysUserCompanyPatent>> queryPageList(SysUserCompanyPatent sysUserCompanyPatent,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysUserCompanyPatent>> result = new Result<IPage<SysUserCompanyPatent>>();
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
		String sort= req.getParameter("sort");
		String level= req.getParameter("level");
		String prize= req.getParameter("prize");
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime= req.getParameter("startTime");
		String endTime= req.getParameter("endTime");
		String userId="";

		if(user.getUserType()!=0){//如果不是管理员查当前登录人的数据
			userId=sysUser.getId();
			//queryWrapper.eq("user_id",sysUser.getId());
		}
		Page<SysUserCompanyPatent> page = new Page<SysUserCompanyPatent>(pageNo, pageSize);
		IPage<SysUserCompanyPatent> pageList = sysUserCompanyPatentService.pageListNew(page,name,sort,level,prize,startTime,endTime,userId);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysUserCompanyPatent
	 * @return
	 */
	@AutoLog(value = "企业用户专利著作-添加")
	@ApiOperation(value="企业用户专利著作-添加", notes="企业用户专利著作-添加")
	@PostMapping(value = "/add")
	public Result<SysUserCompanyPatent> add(@RequestBody SysUserCompanyPatent sysUserCompanyPatent) {
		Result<SysUserCompanyPatent> result = new Result<SysUserCompanyPatent>();
		try {
			Subject subject = SecurityUtils.getSubject();
			LoginUser sysUser = (LoginUser)subject.getPrincipal();
			if(sysUser==null){
				result.error500("未成功登陆");
				return result;
			}
			sysUserCompanyPatent.setUserId(sysUser.getId());
			sysUserCompanyPatent.setUserName(sysUser.getRealname());
			sysUserCompanyPatent.setCreateTime(new Date());
			sysUserCompanyPatent.setUpdateTime(new Date());
			sysUserCompanyPatentService.save(sysUserCompanyPatent);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysUserCompanyPatent
	 * @return
	 */
	@AutoLog(value = "企业用户专利著作-编辑")
	@ApiOperation(value="企业用户专利著作-编辑", notes="企业用户专利著作-编辑")
	@PutMapping(value = "/edit")
	public Result<SysUserCompanyPatent> edit(@RequestBody SysUserCompanyPatent sysUserCompanyPatent) {
		Result<SysUserCompanyPatent> result = new Result<SysUserCompanyPatent>();
		Subject subject = SecurityUtils.getSubject();
		LoginUser sysUser = (LoginUser)subject.getPrincipal();
		if(sysUser==null){
			result.error500("未成功登陆");
			return result;
		}
		SysUserCompanyPatent sysUserCompanyPatentEntity = sysUserCompanyPatentService.getById(sysUserCompanyPatent.getId());
		if(sysUserCompanyPatentEntity==null) {
			result.error500("未找到对应实体");
		}else {
			sysUserCompanyPatent.setUpdateTime(new Date());
			boolean ok = sysUserCompanyPatentService.updateById(sysUserCompanyPatent);
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
	@AutoLog(value = "企业用户专利著作-通过id删除")
	@ApiOperation(value="企业用户专利著作-通过id删除", notes="企业用户专利著作-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<SysUserCompanyPatent> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysUserCompanyPatent> result = new Result<SysUserCompanyPatent>();
		SysUserCompanyPatent sysUserCompanyPatent = sysUserCompanyPatentService.getById(id);
		if(sysUserCompanyPatent==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserCompanyPatentService.removeById(id);
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
	@AutoLog(value = "企业用户专利著作-批量删除")
	@ApiOperation(value="企业用户专利著作-批量删除", notes="企业用户专利著作-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysUserCompanyPatent> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysUserCompanyPatent> result = new Result<SysUserCompanyPatent>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysUserCompanyPatentService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业用户专利著作-通过id查询")
	@ApiOperation(value="企业用户专利著作-通过id查询", notes="企业用户专利著作-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysUserCompanyPatent> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysUserCompanyPatent> result = new Result<SysUserCompanyPatent>();
		SysUserCompanyPatent sysUserCompanyPatent = sysUserCompanyPatentService.getById(id);
		if(sysUserCompanyPatent==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysUserCompanyPatent);
			result.setSuccess(true);
		}
		return result;
	}
	 /**
	  * 通过userId查询
	  * @param userId
	  * @return
	  */
	 @AutoLog(value = "企业用户专利著作-通过id查询")
	 @ApiOperation(value="企业用户专利著作-通过id查询", notes="企业用户专利著作-通过id查询")
	 @PostMapping(value = "/countByUserId")
	 public Result<Map<String,Object>> countByUserId(@RequestParam(name="userId",required=true) String userId) {
		 Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		 Map<String,Object>rsMap=sysUserCompanyPatentService.countByUserId(userId);//all,valid
		 result.setResult(rsMap);
		 result.setSuccess(true);
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
      QueryWrapper<SysUserCompanyPatent> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SysUserCompanyPatent sysUserCompanyPatent = JSON.parseObject(deString, SysUserCompanyPatent.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysUserCompanyPatent, request.getParameterMap());
          }else{
			  SysUserCompanyPatent sysUserCompanyPatent=new SysUserCompanyPatent();
			  queryWrapper = QueryGenerator.initQueryWrapper(sysUserCompanyPatent, request.getParameterMap());
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
      List<SysUserPatentCountVo> pageList = sysUserCompanyPatentService.sysUserPatentCountVo(null);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "历年知识产权获批情况统计表");
      mv.addObject(NormalExcelConstants.CLASS, SysUserPatentCountVo.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("历年知识产权获批情况统计表", "", "导出信息"));
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
              List<SysUserCompanyPatent> listSysUserCompanyPatents = ExcelImportUtil.importExcel(file.getInputStream(), SysUserCompanyPatent.class, params);
              for (SysUserCompanyPatent sysUserCompanyPatentExcel : listSysUserCompanyPatents) {
                  sysUserCompanyPatentService.save(sysUserCompanyPatentExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listSysUserCompanyPatents.size());
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
