package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.FormApply;
import org.jeecg.modules.system.entity.FormComponent;
import org.jeecg.modules.system.entity.FormTemplate;
import org.jeecg.modules.system.service.IFormApplyService;
import org.jeecg.modules.system.service.IFormComponentService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.service.IFormTemplateService;
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
 * @Description: 表单组件
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags="表单组件")
@RestController
@RequestMapping("/form/component")
public class FormComponentController {
	@Autowired
	private IFormComponentService formComponentService;
	 @Autowired
	 private IFormTemplateService formTemplateService;
	 @Autowired
	 private IFormApplyService formApplyService;
	
	/**
	  * 分页列表查询
	 * @param formComponent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "表单组件-分页列表查询")
	@ApiOperation(value="表单组件-分页列表查询", notes="表单组件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FormComponent>> queryPageList(FormComponent formComponent,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<FormComponent>> result = new Result<IPage<FormComponent>>();
		formComponent.setDelFlag(1);
		QueryWrapper<FormComponent> queryWrapper = QueryGenerator.initQueryWrapper(formComponent, req.getParameterMap());
		Page<FormComponent> page = new Page<FormComponent>(pageNo, pageSize);
		IPage<FormComponent> pageList = formComponentService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	 /**
	  * 查询某一表单下的所有组件
	  * @param map
	  * @return
	  */
	 @RequestMapping(value="/listByForm",method = RequestMethod.GET)
	 public Result<List<Map<String,Object>>> listByForm(@RequestParam Map<String,String> map) {
		 Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
		 List<Map<String,Object>> componentList = formComponentService.listByForm(Integer.valueOf(map.get("formId")));
		 result.setSuccess(true);
		 result.setResult(componentList);
		 return result;
	 }

	 /**
	  * 根据所选的业务进行查询模板的表单
	  * @param map
	  * @return
	  */
	 @RequestMapping(value="/getComponentListByBusId",method = RequestMethod.GET)
	 public Result<List<Map<String,Object>>> getComponentListByBusId(@RequestParam Map<String,String> map) {
		 Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
		 //根据业务Id获取模板id
		 String busId = map.get("busId");
		 if("".equals(busId)){
		 	String appId = map.get("appId");
		 	FormApply apply =formApplyService.getById(appId);
		 	busId = apply.getBusinessId().toString();
		 }
		 FormTemplate formTemplate =formTemplateService.getTemplateByBusId(Integer.valueOf(busId));
		 if(formTemplate==null){
			 result.setMessage("未找到对应模板信息");
			 return result;
		 }
		 List<Map<String,Object>> componentList = formComponentService.listByForm(formTemplate.getFormId());
		 result.setSuccess(true);
		 result.setResult(componentList);
		 return result;
	 }
	
	/**
	  *   添加
	 * @param formComponent
	 * @return
	 */
	@AutoLog(value = "表单组件-添加")
	@ApiOperation(value="表单组件-添加", notes="表单组件-添加")
	@PutMapping(value = "/add")
	public Result<FormComponent> add(@RequestBody FormComponent formComponent) {
		Result<FormComponent> result = new Result<FormComponent>();
		try {
			formComponent.setCreateTime(new Date());
			formComponent.setDelFlag(1);
			formComponentService.save(formComponent);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param formComponent
	 * @return
	 */
	@AutoLog(value = "表单组件-编辑")
	@ApiOperation(value="表单组件-编辑", notes="表单组件-编辑")
	@PutMapping(value = "/edit")
	public Result<FormComponent> edit(@RequestBody FormComponent formComponent) {
		Result<FormComponent> result = new Result<FormComponent>();
		FormComponent formComponentEntity = formComponentService.getById(formComponent.getId());
		if(formComponentEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = formComponentService.updateById(formComponent);
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
	@AutoLog(value = "表单组件-通过id删除")
	@ApiOperation(value="表单组件-通过id删除", notes="表单组件-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<FormComponent> delete(@RequestParam(name="id",required=true) String id) {
		Result<FormComponent> result = new Result<FormComponent>();
		FormComponent formComponent = formComponentService.getById(id);
		if(formComponent==null) {
			result.error500("未找到对应实体");
		}else {
			formComponent.setDelFlag(2);
			formComponentService.updateById(formComponent);
			result.success("删除成功!");
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "表单组件-批量删除")
	@ApiOperation(value="表单组件-批量删除", notes="表单组件-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<FormComponent> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<FormComponent> result = new Result<FormComponent>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.formComponentService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "表单组件-通过id查询")
	@ApiOperation(value="表单组件-通过id查询", notes="表单组件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FormComponent> queryById(@RequestParam(name="id",required=true) String id) {
		Result<FormComponent> result = new Result<FormComponent>();
		FormComponent formComponent = formComponentService.getById(id);
		if(formComponent==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(formComponent);
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
      QueryWrapper<FormComponent> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              FormComponent formComponent = JSON.parseObject(deString, FormComponent.class);
              queryWrapper = QueryGenerator.initQueryWrapper(formComponent, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<FormComponent> pageList = formComponentService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "表单组件列表");
      mv.addObject(NormalExcelConstants.CLASS, FormComponent.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("表单组件列表数据", "导出人:Jeecg", "导出信息"));
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
              List<FormComponent> listFormComponents = ExcelImportUtil.importExcel(file.getInputStream(), FormComponent.class, params);
              for (FormComponent formComponentExcel : listFormComponents) {
                  formComponentService.save(formComponentExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listFormComponents.size());
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
