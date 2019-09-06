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
import org.jeecg.modules.system.entity.FormContent;
import org.jeecg.modules.system.service.IFormContentService;
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
 * @Description: 专项申请
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="专项申请")
@RestController
@RequestMapping("/system/formContent")
public class FormContentController {
	@Autowired
	private IFormContentService formContentService;
	
	/**
	  * 分页列表查询
	 * @param formContent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "专项申请-分页列表查询")
	@ApiOperation(value="专项申请-分页列表查询", notes="专项申请-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FormContent>> queryPageList(FormContent formContent,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<FormContent>> result = new Result<IPage<FormContent>>();
		QueryWrapper<FormContent> queryWrapper = QueryGenerator.initQueryWrapper(formContent, req.getParameterMap());
		queryWrapper.like("del_flag",0);
		Page<FormContent> page = new Page<FormContent>(pageNo, pageSize);
		IPage<FormContent> pageList = formContentService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param formContent
	 * @return
	 */
	@AutoLog(value = "专项申请-添加")
	@ApiOperation(value="专项申请-添加", notes="专项申请-添加")
	@PostMapping(value = "/add")
	public Result<FormContent> add(@RequestBody FormContent formContent) {
		Result<FormContent> result = new Result<FormContent>();
		try {
			formContentService.save(formContent);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	 /**
	  * 假删
	  * @param map
	  * @return
	  */
	 @PostMapping(value = "/deleteFlag")
	 public Result<FormContent> deleteFlag(@RequestBody Map<String,String> map) {
		 Result<FormContent> result = new Result<FormContent>();
		 try {
		 	if(GainetUtils.isEmpty(map.get("id"))){
				result.error500("参数为空");
				return result;
			}
			FormContent formContent = formContentService.getById(Integer.valueOf(map.get("id")));
		 	Boolean b = formContentService.saveOrUpdate(formContent);
		 	if(b){
				result.success("添加成功！");
			}else{
				result.error500("操作失败");
				return result;
			}
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作异常");
		 }
		 return result;
	 }
	
	/**
	  *  编辑
	 * @param formContent
	 * @return
	 */
	@AutoLog(value = "专项申请-编辑")
	@ApiOperation(value="专项申请-编辑", notes="专项申请-编辑")
	@PutMapping(value = "/edit")
	public Result<FormContent> edit(@RequestBody FormContent formContent) {
		Result<FormContent> result = new Result<FormContent>();
		FormContent formContentEntity = formContentService.getById(formContent.getId());
		if(formContentEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = formContentService.updateById(formContent);
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
	@AutoLog(value = "专项申请-通过id删除")
	@ApiOperation(value="专项申请-通过id删除", notes="专项申请-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<FormContent> delete(@RequestParam(name="id",required=true) String id) {
		Result<FormContent> result = new Result<FormContent>();
		FormContent formContent = formContentService.getById(id);
		if(formContent==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = formContentService.removeById(id);
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
	@AutoLog(value = "专项申请-批量删除")
	@ApiOperation(value="专项申请-批量删除", notes="专项申请-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<FormContent> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<FormContent> result = new Result<FormContent>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.formContentService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专项申请-通过id查询")
	@ApiOperation(value="专项申请-通过id查询", notes="专项申请-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FormContent> queryById(@RequestParam(name="id",required=true) String id) {
		Result<FormContent> result = new Result<FormContent>();
		FormContent formContent = formContentService.getById(id);
		if(formContent==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(formContent);
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
      QueryWrapper<FormContent> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              FormContent formContent = JSON.parseObject(deString, FormContent.class);
              queryWrapper = QueryGenerator.initQueryWrapper(formContent, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<FormContent> pageList = formContentService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "专项申请列表");
      mv.addObject(NormalExcelConstants.CLASS, FormContent.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("专项申请列表数据", "导出人:Jeecg", "导出信息"));
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
              List<FormContent> listFormContents = ExcelImportUtil.importExcel(file.getInputStream(), FormContent.class, params);
              for (FormContent formContentExcel : listFormContents) {
                  formContentService.save(formContentExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listFormContents.size());
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
