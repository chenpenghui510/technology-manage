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
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.system.entity.SysBlacklist;
import org.jeecg.modules.system.service.ISysBlacklistService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 黑名单
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="黑名单")
@RestController
@RequestMapping("/system/sysBlacklist")
public class SysBlacklistController {
	@Autowired
	private ISysBlacklistService sysBlacklistService;
	
	/**
	  * 分页列表查询
	 * @param sysBlacklist
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "黑名单-分页列表查询")
	@ApiOperation(value="黑名单-分页列表查询", notes="黑名单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysBlacklist>> queryPageList(SysBlacklist sysBlacklist,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysBlacklist>> result = new Result<IPage<SysBlacklist>>();
		QueryWrapper<SysBlacklist> queryWrapper = QueryGenerator.initQueryWrapper(sysBlacklist, req.getParameterMap());
		Page<SysBlacklist> page = new Page<SysBlacklist>(pageNo, pageSize);
		IPage<SysBlacklist> pageList = sysBlacklistService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param param
	 * @return
	 */
	@AutoLog(value = "黑名单-添加")
	@ApiOperation(value="黑名单-添加", notes="黑名单-添加")
	@PostMapping(value = "/add")
	public Result<SysBlacklist> add(@RequestBody Map<String,Object> param) {
		Result<SysBlacklist> result = new Result<SysBlacklist>();
		try {
			Integer black=GainetUtils.intOf(param.get("black"),0);
			String reason=GainetUtils.stringOf(param.get("reason"));
			String startTime=GainetUtils.stringOf(param.get("startTime"));
			String endTime=GainetUtils.stringOf(param.get("endTime"));
			String userId=GainetUtils.stringOf(param.get("userId"));
			if(black==1){
				if(GainetUtils.isEmpty(reason)){
					result.error500("请填写拉黑原因");
					return result;
				}
				if(GainetUtils.isEmpty(startTime)){
					result.error500("请选择拉黑开始时间");
					return result;
				}
				if(GainetUtils.isEmpty(endTime)){
					result.error500("请选择拉黑结束时间");
					return result;
				}
			}
			boolean save=sysBlacklistService.saveBlack(userId,black,reason,startTime,endTime);
			if(save){
				result.success("操作成功！");
			}else{
				result.error500("操作失败！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysBlacklist
	 * @return
	 */
	@AutoLog(value = "黑名单-编辑")
	@ApiOperation(value="黑名单-编辑", notes="黑名单-编辑")
	@PutMapping(value = "/edit")
	public Result<SysBlacklist> edit(@RequestBody SysBlacklist sysBlacklist) {
		Result<SysBlacklist> result = new Result<SysBlacklist>();
		SysBlacklist sysBlacklistEntity = sysBlacklistService.getById(sysBlacklist.getId());
		if(sysBlacklistEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysBlacklistService.updateById(sysBlacklist);
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
	@AutoLog(value = "黑名单-通过id删除")
	@ApiOperation(value="黑名单-通过id删除", notes="黑名单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<SysBlacklist> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysBlacklist> result = new Result<SysBlacklist>();
		SysBlacklist sysBlacklist = sysBlacklistService.getById(id);
		if(sysBlacklist==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysBlacklistService.removeById(id);
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
	@AutoLog(value = "黑名单-批量删除")
	@ApiOperation(value="黑名单-批量删除", notes="黑名单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysBlacklist> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysBlacklist> result = new Result<SysBlacklist>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysBlacklistService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	 /**
	  * 通过userId查询
	  * @param param
	  * @return
	  */
	 @AutoLog(value = "黑名单-通过UserId查询")
	 @ApiOperation(value="黑名单-通过UserId查询", notes="黑名单-通过UserId查询")
	 @PostMapping(value = "/queryByUserId")
	 public Result<SysBlacklist> queryByUserId(@RequestBody Map<String,Object>param) {
	 	String userId= GainetUtils.stringOf(param.get("id"));
		 Result<SysBlacklist> result = new Result<SysBlacklist>();
		 QueryWrapper<SysBlacklist> queryWrapper=new QueryWrapper<>();
		 queryWrapper.eq("user_id",userId);
		 List<SysBlacklist> sysBlacklists=sysBlacklistService.list(queryWrapper);
		 if(sysBlacklists==null||sysBlacklists.size()==0){
			 result.error500("未找到对应实体");
			 return result;
		 }
		 SysBlacklist sysBlacklist=sysBlacklists.get(0);
		 if(sysBlacklist==null) {
			 result.error500("未找到对应实体");
		 }else {
			 result.setResult(sysBlacklist);
			 result.setSuccess(true);
		 }
		 return result;
	 }
	 /**
	  * 查询当前登录用户是否被拉黑
	  * @param param
	  * @return
	  */
	 @AutoLog(value = "黑名单-通过UserId查询")
	 @ApiOperation(value="黑名单-通过UserId查询", notes="黑名单-通过UserId查询")
	 @PostMapping(value = "/black")
	 public Result<SysBlacklist> black(@RequestBody Map<String,Object>param) {
		 Subject subject = SecurityUtils.getSubject();
		 LoginUser sysUser = (LoginUser)subject.getPrincipal();
		 String userId= sysUser.getId();
		 Result<SysBlacklist> result = new Result<SysBlacklist>();
		 QueryWrapper<SysBlacklist> queryWrapper=new QueryWrapper<>();
		 queryWrapper.eq("user_id",userId);
		 List<SysBlacklist> sysBlacklists=sysBlacklistService.list(queryWrapper);
		 if(sysBlacklists==null||sysBlacklists.size()==0){
			 result.error500("未找到对应实体");
			 return result;
		 }
		 SysBlacklist sysBlacklist=sysBlacklists.get(0);
		 if(sysBlacklist==null) {
			 result.error500("未找到对应实体");
		 }else {
		 	Date date=new Date();
			 SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String nowTime=simpleDateFormat.format(date);
		 	//当前时间是否在黑名单时间内
			 if(sysBlacklistService.inBlackTime(userId,nowTime)){
				 result.setResult(sysBlacklist);
				 result.setSuccess(true);
			 }else{
				 result.error500("未在拉黑时间内");
				 return result;
			 }
		 }
		 return result;
	 }
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "黑名单-通过id查询")
	@ApiOperation(value="黑名单-通过id查询", notes="黑名单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysBlacklist> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysBlacklist> result = new Result<SysBlacklist>();
		SysBlacklist sysBlacklist = sysBlacklistService.getById(id);
		if(sysBlacklist==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysBlacklist);
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
      QueryWrapper<SysBlacklist> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SysBlacklist sysBlacklist = JSON.parseObject(deString, SysBlacklist.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysBlacklist, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<SysBlacklist> pageList = sysBlacklistService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "黑名单列表");
      mv.addObject(NormalExcelConstants.CLASS, SysBlacklist.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("黑名单列表数据", "导出人:Jeecg", "导出信息"));
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
              List<SysBlacklist> listSysBlacklists = ExcelImportUtil.importExcel(file.getInputStream(), SysBlacklist.class, params);
              for (SysBlacklist sysBlacklistExcel : listSysBlacklists) {
                  sysBlacklistService.save(sysBlacklistExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listSysBlacklists.size());
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
