package org.jeecg.modules.test.controller;

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
import org.jeecg.modules.test.entity.Aaaa;
import org.jeecg.modules.test.service.IAaaaService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: aa
 * @Author: jeecg-boot
 * @Date:   2019-06-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="aa")
@RestController
@RequestMapping("/test/aaaa")
public class AaaaController {
	@Autowired
	private IAaaaService aaaaService;
	
	/**
	  * 分页列表查询
	 * @param aaaa
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "aa-分页列表查询")
	@ApiOperation(value="aa-分页列表查询", notes="aa-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Aaaa>> queryPageList(Aaaa aaaa,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Aaaa>> result = new Result<IPage<Aaaa>>();
		QueryWrapper<Aaaa> queryWrapper = QueryGenerator.initQueryWrapper(aaaa, req.getParameterMap());
		Page<Aaaa> page = new Page<Aaaa>(pageNo, pageSize);
		IPage<Aaaa> pageList = aaaaService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param aaaa
	 * @return
	 */
	@AutoLog(value = "aa-添加")
	@ApiOperation(value="aa-添加", notes="aa-添加")
	@PostMapping(value = "/add")
	public Result<Aaaa> add(@RequestBody Aaaa aaaa) {
		Result<Aaaa> result = new Result<Aaaa>();
		try {
			aaaaService.save(aaaa);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param aaaa
	 * @return
	 */
	@AutoLog(value = "aa-编辑")
	@ApiOperation(value="aa-编辑", notes="aa-编辑")
	@PutMapping(value = "/edit")
	public Result<Aaaa> edit(@RequestBody Aaaa aaaa) {
		Result<Aaaa> result = new Result<Aaaa>();
		Aaaa aaaaEntity = aaaaService.getById(aaaa.getId());
		if(aaaaEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = aaaaService.updateById(aaaa);
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
	@AutoLog(value = "aa-通过id删除")
	@ApiOperation(value="aa-通过id删除", notes="aa-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<Aaaa> delete(@RequestParam(name="id",required=true) String id) {
		Result<Aaaa> result = new Result<Aaaa>();
		Aaaa aaaa = aaaaService.getById(id);
		if(aaaa==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = aaaaService.removeById(id);
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
	@AutoLog(value = "aa-批量删除")
	@ApiOperation(value="aa-批量删除", notes="aa-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Aaaa> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Aaaa> result = new Result<Aaaa>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.aaaaService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "aa-通过id查询")
	@ApiOperation(value="aa-通过id查询", notes="aa-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Aaaa> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Aaaa> result = new Result<Aaaa>();
		Aaaa aaaa = aaaaService.getById(id);
		if(aaaa==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(aaaa);
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
      QueryWrapper<Aaaa> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Aaaa aaaa = JSON.parseObject(deString, Aaaa.class);
              queryWrapper = QueryGenerator.initQueryWrapper(aaaa, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Aaaa> pageList = aaaaService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "aa列表");
      mv.addObject(NormalExcelConstants.CLASS, Aaaa.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("aa列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Aaaa> listAaaas = ExcelImportUtil.importExcel(file.getInputStream(), Aaaa.class, params);
              for (Aaaa aaaaExcel : listAaaas) {
                  aaaaService.save(aaaaExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listAaaas.size());
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
