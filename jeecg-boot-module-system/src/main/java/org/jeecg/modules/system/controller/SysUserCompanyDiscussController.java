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
import org.jeecg.modules.system.entity.SysUserCompanyDiscuss;
import org.jeecg.modules.system.service.ISysUserCompanyDiscussService;
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
 * @Description: 研发中心
 * @Author: jeecg-boot
 * @Date:   2019-08-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="研发中心")
@RestController
@RequestMapping("/system/sysUserCompanyDiscuss")
public class SysUserCompanyDiscussController {
	@Autowired
	private ISysUserCompanyDiscussService sysUserCompanyDiscussService;
	@Autowired
	private ISysUserService sysUserService;
	/**
	  * 分页列表查询
	 * @param sysUserCompanyDiscuss
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "研发中心-分页列表查询")
	@ApiOperation(value="研发中心-分页列表查询", notes="研发中心-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysUserCompanyDiscuss>> queryPageList(SysUserCompanyDiscuss sysUserCompanyDiscuss,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysUserCompanyDiscuss>> result = new Result<IPage<SysUserCompanyDiscuss>>();
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
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime= req.getParameter("startTime");
		String endTime= req.getParameter("endTime");
		String userId="";
		if(user.getUserType()!=0){//如果不是管理员查当前登录人的数据
			userId=sysUser.getId();
		}

		Page<SysUserCompanyDiscuss> page = new Page<SysUserCompanyDiscuss>(pageNo, pageSize);
		IPage<SysUserCompanyDiscuss> pageList = sysUserCompanyDiscussService.pageListNew(page,name,sort,level,startTime,endTime,userId);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysUserCompanyDiscuss
	 * @return
	 */
	@AutoLog(value = "研发中心-添加")
	@ApiOperation(value="研发中心-添加", notes="研发中心-添加")
	@PostMapping(value = "/add")
	public Result<SysUserCompanyDiscuss> add(@RequestBody SysUserCompanyDiscuss sysUserCompanyDiscuss) {
		Result<SysUserCompanyDiscuss> result = new Result<SysUserCompanyDiscuss>();
		try {
			Subject subject = SecurityUtils.getSubject();
			LoginUser sysUser = (LoginUser)subject.getPrincipal();
			if(sysUser==null){
				result.error500("未成功登陆");
				return result;
			}
			sysUserCompanyDiscuss.setUserId(sysUser.getId());
			sysUserCompanyDiscuss.setUserName(sysUser.getRealname());
			sysUserCompanyDiscuss.setCreateTime(new Date());
			sysUserCompanyDiscuss.setUpdateTime(new Date());
			sysUserCompanyDiscussService.save(sysUserCompanyDiscuss);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysUserCompanyDiscuss
	 * @return
	 */
	@AutoLog(value = "研发中心-编辑")
	@ApiOperation(value="研发中心-编辑", notes="研发中心-编辑")
	@PutMapping(value = "/edit")
	public Result<SysUserCompanyDiscuss> edit(@RequestBody SysUserCompanyDiscuss sysUserCompanyDiscuss) {
		Result<SysUserCompanyDiscuss> result = new Result<SysUserCompanyDiscuss>();
		Subject subject = SecurityUtils.getSubject();
		LoginUser sysUser = (LoginUser)subject.getPrincipal();
		if(sysUser==null){
			result.error500("未成功登陆");
			return result;
		}
		SysUserCompanyDiscuss sysUserCompanyDiscussEntity = sysUserCompanyDiscussService.getById(sysUserCompanyDiscuss.getId());
		if(sysUserCompanyDiscussEntity==null) {
			result.error500("未找到对应实体");
		}else {
			sysUserCompanyDiscussEntity.setUpdateTime(new Date());
			boolean ok = sysUserCompanyDiscussService.updateById(sysUserCompanyDiscuss);
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
	@AutoLog(value = "研发中心-通过id删除")
	@ApiOperation(value="研发中心-通过id删除", notes="研发中心-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<SysUserCompanyDiscuss> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysUserCompanyDiscuss> result = new Result<SysUserCompanyDiscuss>();
		SysUserCompanyDiscuss sysUserCompanyDiscuss = sysUserCompanyDiscussService.getById(id);
		if(sysUserCompanyDiscuss==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserCompanyDiscussService.removeById(id);
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
	@AutoLog(value = "研发中心-批量删除")
	@ApiOperation(value="研发中心-批量删除", notes="研发中心-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysUserCompanyDiscuss> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysUserCompanyDiscuss> result = new Result<SysUserCompanyDiscuss>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysUserCompanyDiscussService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "研发中心-通过id查询")
	@ApiOperation(value="研发中心-通过id查询", notes="研发中心-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysUserCompanyDiscuss> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysUserCompanyDiscuss> result = new Result<SysUserCompanyDiscuss>();
		SysUserCompanyDiscuss sysUserCompanyDiscuss = sysUserCompanyDiscussService.getById(id);
		if(sysUserCompanyDiscuss==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysUserCompanyDiscuss);
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
      QueryWrapper<SysUserCompanyDiscuss> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SysUserCompanyDiscuss sysUserCompanyDiscuss = JSON.parseObject(deString, SysUserCompanyDiscuss.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysUserCompanyDiscuss, request.getParameterMap());
          }else{
			  SysUserCompanyDiscuss sysUserCompanyDiscuss=new SysUserCompanyDiscuss();
			  queryWrapper = QueryGenerator.initQueryWrapper(sysUserCompanyDiscuss, request.getParameterMap());
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
      List<SysUserCompanyDiscuss> pageList = sysUserCompanyDiscussService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "研发中心列表");
      mv.addObject(NormalExcelConstants.CLASS, SysUserCompanyDiscuss.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("研发中心列表数据", "", "导出信息"));
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
              List<SysUserCompanyDiscuss> listSysUserCompanyDiscusss = ExcelImportUtil.importExcel(file.getInputStream(), SysUserCompanyDiscuss.class, params);
              for (SysUserCompanyDiscuss sysUserCompanyDiscussExcel : listSysUserCompanyDiscusss) {
                  sysUserCompanyDiscussService.save(sysUserCompanyDiscussExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listSysUserCompanyDiscusss.size());
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
