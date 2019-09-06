package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.ExpertUserGroup;
import org.jeecg.modules.system.entity.FormApply;
import org.jeecg.modules.system.entity.FormApplyExpertGroup;
import org.jeecg.modules.system.service.IExpertUserGroupService;
import org.jeecg.modules.system.service.IFormApplyExpertGroupService;
import org.jeecg.modules.system.service.IFormApplyService;
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
 * @Description: expert_user_group
 * @Author: jeecg-boot
 * @Date:   2019-07-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags="expert_user_group")
@RestController
@RequestMapping("/group_user/expertUserGroup")
public class ExpertUserGroupController {
	@Autowired
	private IExpertUserGroupService expertUserGroupService;
	@Autowired
	private IFormApplyService formApplyService;
	@Autowired
	private IFormApplyExpertGroupService formApplyExpertGroupService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	
	/**
	  * 分页列表查询
	 * @param expertUserGroup
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "expert_user_group-分页列表查询")
	@ApiOperation(value="expert_user_group-分页列表查询", notes="expert_user_group-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ExpertUserGroup>> queryPageList(ExpertUserGroup expertUserGroup,
														@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														HttpServletRequest req) {
		Result<IPage<ExpertUserGroup>> result = new Result<IPage<ExpertUserGroup>>();
		QueryWrapper<ExpertUserGroup> queryWrapper = QueryGenerator.initQueryWrapper(expertUserGroup, req.getParameterMap());
		Page<ExpertUserGroup> page = new Page<ExpertUserGroup>(pageNo, pageSize);
		IPage<ExpertUserGroup> pageList = expertUserGroupService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param expertUserGroup
	 * @return
	 */
	@AutoLog(value = "expert_user_group-添加")
	@ApiOperation(value="expert_user_group-添加", notes="expert_user_group-添加")
	@PostMapping(value = "/add")
	public Result<ExpertUserGroup> add(@RequestBody ExpertUserGroup expertUserGroup) {
		Result<ExpertUserGroup> result = new Result<ExpertUserGroup>();
		try {
			expertUserGroupService.save(expertUserGroup);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param expertUserGroup
	 * @return
	 */
	@AutoLog(value = "expert_user_group-编辑")
	@ApiOperation(value="expert_user_group-编辑", notes="expert_user_group-编辑")
	@PutMapping(value = "/edit")
	public Result<ExpertUserGroup> edit(@RequestBody ExpertUserGroup expertUserGroup) {
		Result<ExpertUserGroup> result = new Result<ExpertUserGroup>();
		ExpertUserGroup expertUserGroupEntity = expertUserGroupService.getById(expertUserGroup.getId());
		if(expertUserGroupEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = expertUserGroupService.updateById(expertUserGroup);
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
	@AutoLog(value = "expert_user_group-通过id删除")
	@ApiOperation(value="expert_user_group-通过id删除", notes="expert_user_group-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<ExpertUserGroup> delete(@RequestParam(name="id",required=true) String id) {
		Result<ExpertUserGroup> result = new Result<ExpertUserGroup>();
		ExpertUserGroup expertUserGroup = expertUserGroupService.getById(id);
		if(expertUserGroup==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = expertUserGroupService.removeById(id);
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
	@AutoLog(value = "expert_user_group-批量删除")
	@ApiOperation(value="expert_user_group-批量删除", notes="expert_user_group-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<ExpertUserGroup> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<ExpertUserGroup> result = new Result<ExpertUserGroup>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.expertUserGroupService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "expert_user_group-通过id查询")
	@ApiOperation(value="expert_user_group-通过id查询", notes="expert_user_group-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ExpertUserGroup> queryById(@RequestParam(name="id",required=true) String id) {
		Result<ExpertUserGroup> result = new Result<ExpertUserGroup>();
		ExpertUserGroup expertUserGroup = expertUserGroupService.getById(id);
		if(expertUserGroup==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(expertUserGroup);
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
      QueryWrapper<ExpertUserGroup> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              ExpertUserGroup expertUserGroup = JSON.parseObject(deString, ExpertUserGroup.class);
              queryWrapper = QueryGenerator.initQueryWrapper(expertUserGroup, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<ExpertUserGroup> pageList = expertUserGroupService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "expert_user_group列表");
      mv.addObject(NormalExcelConstants.CLASS, ExpertUserGroup.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("expert_user_group列表数据", "导出人:Jeecg", "导出信息"));
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
              List<ExpertUserGroup> listExpertUserGroups = ExcelImportUtil.importExcel(file.getInputStream(), ExpertUserGroup.class, params);
              for (ExpertUserGroup expertUserGroupExcel : listExpertUserGroups) {
                  expertUserGroupService.save(expertUserGroupExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listExpertUserGroups.size());
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
	  * 查询已分配至某专家组下的业务申请
	  * @param groupId
	  * @return
	  */
	  @RequestMapping(value = "/getApplyByGroupId", method = RequestMethod.GET)
	  public Result<List<Map>> getApplyByGroupId(@RequestParam(name="groupId")Integer groupId){
		Result<List<Map>> result = new Result<List<Map>>();
		try{
			List<Map> list = expertUserGroupService.getApplyByGroupId(groupId);
			result.setSuccess(true);
			result.setResult(list);
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result.error500("查询列表失败!");
		}
		return result;
	  }

	 /**
	  * 查询已分配至某专家组下的专家账号
	  * @param groupId
	  * @return
	  */
	 @RequestMapping(value = "/getUserByGroupId", method = RequestMethod.GET)
	 public Result<List<Map>> getUserByGroupId(@RequestParam(name="groupId")Integer groupId){
		 Result<List<Map>> result = new Result<List<Map>>();
		 try{
			 List<Map> pageList = expertUserGroupService.getUserByGroupId(groupId);
			 result.setSuccess(true);
			 result.setResult(pageList);
		 }catch(Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("查询列表失败!");
		 }
		 return result;
	 }

	 @RequestMapping(value="/removeFromGroup",method=RequestMethod.GET)
	 public Result<String> removeFromGroup(@RequestParam(name="applyId")Integer applyId,@RequestParam(name="groupId")Integer groupId){
	 	Result<String> result = new Result<String>();
	 	try{
			FormApply formApply = formApplyService.getById(applyId);
			if(formApply.getStatus()!=8){
				result.setSuccess(false);
				result.setMessage("业务状态非待复审状态，无法移出专家组!");
			}else{
				QueryWrapper<FormApplyExpertGroup> queryWrapper = new QueryWrapper<FormApplyExpertGroup>();
				queryWrapper.eq("form_apply_id", applyId).eq("expert_group_id",groupId);
				formApplyExpertGroupService.remove(queryWrapper);
				sysBaseAPI.addLog("申报名称为："+formApply.getName()+"移出专家组成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
				result.success("操作成功!");
			}

		}catch(Exception e){
			log.error(e.getMessage(),e);
			result.error500("操作异常，请稍后再试!");
		}
	 	return result;
	 }

}
