package org.jeecg.modules.system.controller;

import java.io.*;
import java.util.*;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.FormApply;
import org.jeecg.modules.system.entity.FormComponent;
import org.jeecg.modules.system.entity.FormTemplate;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.IFormApplyService;
import org.jeecg.modules.system.service.IFormComponentService;
import org.jeecg.modules.system.service.IFormTemplateService;
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
 * @Description: 表单模板
 * @Author: jeecg-boot
 * @Date:   2019-07-12
 * @Version: V1.0
 */
@Slf4j
@Api(tags="表单模板")
@RestController
@RequestMapping("/form/template")
public class FormTemplateController {
	@Autowired
	private IFormTemplateService formTemplateService;

	@Autowired
	private IFormComponentService iFormComponentService;

	@Autowired
    private IFormApplyService formApplyService;
	 @Autowired
	 private ISysBaseAPI sysBaseAPI;

	/**
	  * 分页列表查询
	 * @param formTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "表单模板-分页列表查询")
	@ApiOperation(value="表单模板-分页列表查询", notes="表单模板-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FormTemplate>> queryPageList(FormTemplate formTemplate,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<FormTemplate>> result = new Result<IPage<FormTemplate>>();
		formTemplate.setDelFlag(1);
		QueryWrapper<FormTemplate> queryWrapper = QueryGenerator.initQueryWrapper(formTemplate, req.getParameterMap());
		Page<FormTemplate> page = new Page<FormTemplate>(pageNo, pageSize);
		IPage<FormTemplate> pageList = formTemplateService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param formTemplate
	 * @return
	 */
	@AutoLog(value = "表单模板-添加")
	@ApiOperation(value="表单模板-添加", notes="表单模板-添加")
	@PutMapping(value = "/add")
	public Result<FormTemplate> add(@RequestBody FormTemplate formTemplate) {
		Result<FormTemplate> result = new Result<FormTemplate>();
		try {
			if(formTemplate.getFormState()==1){
				//判断有木有在用的表单模板
				FormTemplate useTemplate = new FormTemplate();
				useTemplate.setDelFlag(1);
				useTemplate.setFormType(1);
				useTemplate.setRelationBusinessId(formTemplate.getRelationBusinessId());
				useTemplate.setFormState(1);
				FormTemplate oldTemplate =  formTemplateService.getOne(new QueryWrapper<FormTemplate>(useTemplate));
				if(oldTemplate!=null){
					oldTemplate.setFormState(2);
					formTemplateService.updateById(oldTemplate);
				}
			}

			formTemplate.setDelFlag(1);
			formTemplate.setCreateTime(new Date());
			formTemplateService.save(formTemplate);
			sysBaseAPI.addLog("申报模板："+formTemplate.getFormName()+"创建成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param formTemplate
	 * @return
	 */
	@AutoLog(value = "表单模板-编辑")
	@ApiOperation(value="表单模板-编辑", notes="表单模板-编辑")
	@PutMapping(value = "/edit")
	public Result<FormTemplate> edit(@RequestBody FormTemplate formTemplate) {
		Result<FormTemplate> result = new Result<FormTemplate>();
		FormTemplate formTemplateEntity = formTemplateService.getById(formTemplate.getFormId());
		if(formTemplateEntity==null) {
			result.error500("未找到对应实体");
		}else {
			if(formTemplate.getFormState()==1){
				//判断有木有在用的表单模板
				FormTemplate useTemplate = new FormTemplate();
				useTemplate.setDelFlag(1);
				useTemplate.setFormType(1);
				useTemplate.setRelationBusinessId(formTemplate.getRelationBusinessId());
				useTemplate.setFormState(1);
				FormTemplate oldTemplate =  formTemplateService.getOne(new QueryWrapper<FormTemplate>(useTemplate));
				if(oldTemplate!=null){
					if(!oldTemplate.getFormId().toString().equals(formTemplate.getFormId().toString())){
						oldTemplate.setFormState(2);
						formTemplateService.updateById(oldTemplate);
					}
				}
			}
			boolean ok = formTemplateService.updateById(formTemplate);
			//TODO 返回false说明什么？
			if(ok) {
				sysBaseAPI.addLog("申报模板："+formTemplate.getFormName()+"修改成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
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
	@AutoLog(value = "表单模板-通过id删除")
	@ApiOperation(value="表单模板-通过id删除", notes="表单模板-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<FormTemplate> delete(@RequestParam(name="id",required=true) String id) {
		Result<FormTemplate> result = new Result<FormTemplate>();
		FormTemplate formTemplate = formTemplateService.getById(id);
		if(formTemplate==null) {
			result.error500("未找到对应实体");
		}else {
			formTemplate.setDelFlag(2);
			formTemplateService.updateById(formTemplate);
			sysBaseAPI.addLog("申报模板："+formTemplate.getFormName()+"删除成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
			result.success("删除成功!");
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "表单模板-批量删除")
	@ApiOperation(value="表单模板-批量删除", notes="表单模板-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<FormTemplate> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<FormTemplate> result = new Result<FormTemplate>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.formTemplateService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "表单模板-通过id查询")
	@ApiOperation(value="表单模板-通过id查询", notes="表单模板-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FormTemplate> queryById(@RequestParam(name="id",required=true) String id) {
		Result<FormTemplate> result = new Result<FormTemplate>();
		FormTemplate formTemplate = formTemplateService.getById(id);
		if(formTemplate==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(formTemplate);
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
      QueryWrapper<FormTemplate> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              FormTemplate formTemplate = JSON.parseObject(deString, FormTemplate.class);
              queryWrapper = QueryGenerator.initQueryWrapper(formTemplate, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<FormTemplate> pageList = formTemplateService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "表单模板列表");
      mv.addObject(NormalExcelConstants.CLASS, FormTemplate.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("表单模板列表数据", "导出人:Jeecg", "导出信息"));
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
              List<FormTemplate> listFormTemplates = ExcelImportUtil.importExcel(file.getInputStream(), FormTemplate.class, params);
              for (FormTemplate formTemplateExcel : listFormTemplates) {
                  formTemplateService.save(formTemplateExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listFormTemplates.size());
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
	  * 校验某一业务下表单名称是否唯一<br>
	  * @param formTemplate
	  * @return
	  */
	 @RequestMapping(value = "/checkFormName", method = RequestMethod.GET)
	 public Result<Boolean> checkFormName(FormTemplate formTemplate) {
		 Result<Boolean> result = new Result<>();
		 result.setResult(true);//如果此参数为false则程序发生异常
		 Integer id = formTemplate.getFormId();
		 log.info("--验证用户信息是否唯一---id:" + id);
		 try {
			 FormTemplate oldTemplate = null;
			 if (oConvertUtils.isNotEmpty(id)) {
				 oldTemplate = formTemplateService.getById(id);
			 } else {
				 formTemplate.setFormId(null);
			 }
			 //通过传入信息查询新的用户信息
			 formTemplate.setFormId(null);
			 FormTemplate newTemplate = formTemplateService.getOne(new QueryWrapper<FormTemplate>(formTemplate));
			 if (newTemplate != null) {
				 if (oldTemplate == null) {
					 result.setSuccess(false);
					 result.setMessage("当前业务下该表单名称已存在");
					 return result;
				 } else if (!id.equals(newTemplate.getFormId())) {
					 //否则=>编辑模式=>判断两者ID是否一致-
					 result.setSuccess(false);
					 result.setMessage("当前业务下该表单名称已存在");
					 return result;
				 }
			 }

		 } catch (Exception e) {
		 	e.printStackTrace();
			 result.setSuccess(false);
			 result.setResult(false);
			 result.setMessage(e.getMessage());
			 return result;
		 }
		 result.setSuccess(true);
		 return result;
	 }

	 /**
	  * 通过id查询
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "表单模板信息和附件信息-通过业务id查询")
	 @ApiOperation(value="表单模板信息和附件信息-通过业务id查询", notes="表单模板信息和附件信息-通过业务id查询")
	 @GetMapping(value = "/applyRequire")
	 public Result<Map> applyRequire(@RequestParam(name="id",required=true) String id) {
		 Result<Map> result = new Result<Map>();
		// FormTemplate formTemplate = formTemplateService.getById(id);
		 //获取要求内容
		 FormTemplate formTemplate = formTemplateService.getTemplateByBusId(Integer.valueOf(id));
		 if(formTemplate==null){
			 result.error500("未找到对应实体");
		 }
		 //获取申请附件内容
		 List<Map<String,Object>> list=formTemplateService.getFileInfoByTmpId(formTemplate.getFormId());
		 Map<String,Object> allMap=new HashMap<>();
		 allMap.put("tmp",formTemplate);
		 allMap.put("file",list);
		 result.setResult(allMap);
		 result.setSuccess(true);
		 return result;
	 }
	 /**
	  * 通过id查询 回显已上传的文档修改
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "表单模板信息和附件信息-回显已上传的文档修改")
	 @ApiOperation(value="表单模板信息和附件信息-回显已上传的文档修改", notes="表单模板信息和附件信息-回显已上传的文档修改")
	 @GetMapping(value = "/applyRequireUpdate")
	 public Result<Map> applyRequireUpdate(@RequestParam(name="id",required=true) String id,Integer appId) {
		 Result<Map> result = new Result<Map>();
		 // FormTemplate formTemplate = formTemplateService.getById(id);
		 //获取要求内容
         String businessId = id;
         if(businessId==null||"".equals(businessId)){
             if(appId!=null){
                 FormApply apply = formApplyService.getById(appId);
                 businessId=apply.getBusinessId().toString();
             }
         }
		 FormTemplate formTemplate = formTemplateService.getTemplateByBusId(Integer.valueOf(businessId));
		 if(formTemplate==null){
			 result.error500("未找到对应实体");
		 }
		 //获取申请附件内容
		 List<Map<String,Object>> rsList=new ArrayList<>();
		 List<Map<String,Object>> list=formTemplateService.getFileInfoByTmpId(formTemplate.getFormId());
		 if(list!=null&&list.size()>0) {
			 for (int i=0;i<list.size();i++){
				 Map<String, Object> ftMap = list.get(i);
			 	if(ftMap==null){
			 		continue;
				}
				Integer ftId= GainetUtils.intOf(ftMap.get("id"),0);
			 	Map<String,Object> fileAppMap=formTemplateService.getFileInfoByTmpId(ftId,appId);
			 	if(fileAppMap!=null){
			 		String url=GainetUtils.stringOf(fileAppMap.get("url"));
					ftMap.put("url",url);
				}
				 rsList.add(ftMap);
			 }
		 }
		 Map<String,Object> allMap=new HashMap<>();
		 allMap.put("tmp",formTemplate);
		 allMap.put("file",rsList);
		 result.setResult(allMap);
		 result.setSuccess(true);
		 return result;
	 }

	 /**
	  * 根据业务查询模板列表
     * @param businessId
     * @return
			 */
	 @RequestMapping(value = "/listByBusiness", method = RequestMethod.GET)
	 public Result<List<FormTemplate>> listByBusiness(@RequestParam(name="businessId")Integer businessId) {
		 Result<List<FormTemplate>> result = new Result<>();
		 QueryWrapper<FormTemplate> queryWrapper = new QueryWrapper<FormTemplate>();
		 queryWrapper.eq("relation_business_id",businessId).eq("del_flag",1);
		 List<FormTemplate> list = formTemplateService.list(queryWrapper);
		 if(list==null||list.size()<=0) {
			 result.setSuccess(false);
			 result.setMessage("未找到对应的模板信息!");
		 }else {
			 result.setResult(list);
			 result.setSuccess(true);
		 }
		 return result;
	 }

	 /**
	  * 复制表单模板
	  * @param params
	  * @return
	  */
	 @RequestMapping(value="/doCopyTemplate",method=RequestMethod.PUT)
	 public Result<String> doCopyTemplate(@RequestBody Map<String,String> params){
	 	Result<String> result = new Result<>();
	 	String nowForm = params.get("nowForm");
	 	String newForm = params.get("businessTemplate");
	 	if(nowForm.equals(newForm)){
	 		result.success("复制模板成功!");
		}else{
			QueryWrapper<FormComponent> queryWrapper = new QueryWrapper<FormComponent>();
			queryWrapper.eq("form_id",Integer.valueOf(newForm)).eq("del_flag",1);
			List<FormComponent> nowComponents=iFormComponentService.list(queryWrapper);
			if(nowComponents!=null&&nowComponents.size()>0){
				for(FormComponent component:nowComponents){
					component.setId(null);
					component.setFormId(Integer.valueOf(nowForm));
					iFormComponentService.save(component);
				}
			}
			result.success("复制模板成功");
		}
	 	return result;
	 }

}
