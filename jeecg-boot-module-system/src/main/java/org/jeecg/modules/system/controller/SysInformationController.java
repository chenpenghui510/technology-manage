package org.jeecg.modules.system.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysInformation;
import org.jeecg.modules.system.mapper.SysInformationMapper;
import org.jeecg.modules.system.service.ISysInformationService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 资讯配置
 * @Author: jeecg-boot
 * @Date:   2019-07-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="资讯配置")
@RestController
@RequestMapping("/system/sysInformation")
public class SysInformationController {
	@Autowired
	private ISysInformationService sysInformationService;
	@Autowired
	private SysInformationMapper sysInformationMapper;
	 @Value(value = "${jeecg.path.upload}")
	 private String uploadpath;
	/**
	  * 分页列表查询
	 * @param sysInformation
	 * @param pageNo
	 * @param pageSizepatentRotaryInfo
	 * @param req
	 * @return
	 */
	@AutoLog(value = "资讯配置-分页列表查询")
	@ApiOperation(value="资讯配置-分页列表查询", notes="资讯配置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysInformation>> queryPageList(SysInformation sysInformation,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysInformation>> result = new Result<IPage<SysInformation>>();
		QueryWrapper<SysInformation> queryWrapper = QueryGenerator.initQueryWrapper(sysInformation, req.getParameterMap());
		Page<SysInformation> page = new Page<SysInformation>(pageNo, pageSize);
		IPage<SysInformation> pageList = sysInformationService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysInformation
	 * @return
	 */
	@AutoLog(value = "资讯配置-添加")
	@ApiOperation(value="资讯配置-添加", notes="资讯配置-添加")
	@PostMapping(value = "/add")
	public Result<SysInformation> add(@RequestBody SysInformation sysInformation) {
		Result<SysInformation> result = new Result<SysInformation>();
		try {
			sysInformationMapper.insert(sysInformation);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysInformation
	 * @return
	 */
	@AutoLog(value = "资讯配置-编辑")
	@ApiOperation(value="资讯配置-编辑", notes="资讯配置-编辑")
	@PutMapping(value = "/edit")
	public Result<SysInformation> edit(@RequestBody SysInformation sysInformation) {
		Result<SysInformation> result = new Result<SysInformation>();
		SysInformation sysInformationEntity = sysInformationService.getById(sysInformation.getId());
		if(sysInformationEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysInformationService.updateById(sysInformation);
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
	@AutoLog(value = "资讯配置-通过id删除")
	@ApiOperation(value="资讯配置-通过id删除", notes="资讯配置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<SysInformation> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysInformation> result = new Result<SysInformation>();
		SysInformation sysInformation = sysInformationService.getById(id);
		if(sysInformation==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysInformationService.removeById(id);
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
	@AutoLog(value = "资讯配置-批量删除")
	@ApiOperation(value="资讯配置-批量删除", notes="资讯配置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysInformation> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysInformation> result = new Result<SysInformation>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysInformationService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "资讯配置-通过id查询")
	@ApiOperation(value="资讯配置-通过id查询", notes="资讯配置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysInformation> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysInformation> result = new Result<SysInformation>();
		SysInformation sysInformation = sysInformationService.getById(id);
		if(sysInformation==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysInformation);
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
      QueryWrapper<SysInformation> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SysInformation sysInformation = JSON.parseObject(deString, SysInformation.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysInformation, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<SysInformation> pageList = sysInformationService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "资讯配置列表");
      mv.addObject(NormalExcelConstants.CLASS, SysInformation.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("资讯配置列表数据", "导出人:Jeecg", "导出信息"));
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
              List<SysInformation> listSysInformations = ExcelImportUtil.importExcel(file.getInputStream(), SysInformation.class, params);
              for (SysInformation sysInformationExcel : listSysInformations) {
                  sysInformationService.save(sysInformationExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listSysInformations.size());
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

  @PostMapping(value = "/upload")
  public Result<?> upload(HttpServletRequest request, HttpServletResponse response) {
	  Result<?> result = new Result<>();
	  try {
		  String ctxPath = uploadpath+"/sysInformation";
		  String fileName = null;
		  String bizPath = "files";
		  String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
		  File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
		  if (!file.exists()) {
			  file.mkdirs();// 创建文件根目录
		  }
		  MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		  MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
		  String orgName = mf.getOriginalFilename();// 获取文件名
		  fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
		  String savePath = file.getPath() + File.separator + fileName;
		  File savefile = new File(savePath);
		  FileCopyUtils.copy(mf.getBytes(), savefile);
		  String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
		  if (dbpath.contains("\\")) {
			  dbpath = dbpath.replace("\\", "/");
		  }
		  result.setMessage(dbpath);
		  result.setSuccess(true);
	  } catch (IOException e) {
		  result.setSuccess(false);
		  result.setMessage(e.getMessage());
		  log.error(e.getMessage(), e);
	  }
	  return result;
  }
	 /**
	  * 预览图片
	  * 请求地址：http://localhost:8080/common/view/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
	  *
	  * @param request
	  * @param response
	  */
	 @GetMapping(value = "/view/**")
	 public void view(HttpServletRequest request, HttpServletResponse response) {
		 // ISO-8859-1 ==> UTF-8 进行编码转换
		 String imgPath = extractPathFromPattern(request);
		 // 其余处理略
		 InputStream inputStream = null;
		 OutputStream outputStream = null;
		 try {
			 imgPath = imgPath.replace("..", "");
			 if (imgPath.endsWith(",")) {
				 imgPath = imgPath.substring(0, imgPath.length() - 1);
			 }
			 response.setContentType("image/jpeg;charset=utf-8");
			 String localPath = uploadpath+"/sysInformation";
			 String imgurl = localPath + File.separator + imgPath;
			 inputStream = new BufferedInputStream(new FileInputStream(imgurl));
			 outputStream = response.getOutputStream();
			 byte[] buf = new byte[1024];
			 int len;
			 while ((len = inputStream.read(buf)) > 0) {
				 outputStream.write(buf, 0, len);
			 }
			 response.flushBuffer();
		 } catch (IOException e) {
			 log.error("预览图片失败" + e.getMessage());
			 // e.printStackTrace();
		 } finally {
			 if (inputStream != null) {
				 try {
					 inputStream.close();
				 } catch (IOException e) {
					 log.error(e.getMessage(), e);
				 }
			 }
			 if (outputStream != null) {
				 try {
					 outputStream.close();
				 } catch (IOException e) {
					 log.error(e.getMessage(), e);
				 }
			 }
		 }

	 }
	 /**
	  *  把指定URL后的字符串全部截断当成参数
	  *  这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
	  * @param request
	  * @return
	  */
	 private static String extractPathFromPattern(final HttpServletRequest request) {
		 String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		 String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		 return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
	 }
	 @RequestMapping(value = "/patentListByType", method = RequestMethod.GET)
	 public Result<List<SysInformation> > patentListByType(@RequestParam(name="type") String type,@RequestParam(name="content") String content){
		 Result<List<SysInformation> > result = new Result<List<SysInformation> >();
		 try{
			 List<SysInformation> companyDetails= sysInformationService.patentListByType(Integer.valueOf(type),content);
			 result.setResult(companyDetails);
			 result.success("查询列表成功!");
		 }catch(Exception e){
			 log.error(e.getMessage(),e);
			 result.error500("获取列表信息失败!");
		 }
		 return result;
	 }


     /**
      * 获取主页面模块的数据
      * @return
      */
     @RequestMapping(value="/patentRotaryInfo",method = RequestMethod.GET)
     public Result<Map<String,Object>> patentRotaryInfo(HttpServletRequest request){
         Result<Map<String,Object>> result = new Result<Map<String,Object>>();
         try{
             //获取轮播图信息
             List<Map<String,Object>> infoList1 = sysInformationService.getPatentRotaryListByType("1");
             //获取公开信息
             List<Map<String,Object>> infoList2 = sysInformationService.getPatentListByType("3");
             //获取法规信息
             List<Map<String,Object>> infoList3 = sysInformationService.getPatentListByType("4");
             //获取维权信息
             List<Map<String,Object>> infoList4 = sysInformationService.getPatentListByType("5");
             //获取明星信息
             List<Map<String,Object>> infoList5 = sysInformationService.getPatentConpanyList("6");
             //获取通知公告信息
             List<Map<String,Object>> infoList6 = sysInformationService.getPatentRotaryListByType("3");
             Map map=new HashMap();
             map.put("type1",infoList1);
             map.put("type2",infoList2);
             map.put("type3",infoList3);
             map.put("type4",infoList4);
             map.put("type5",infoList5);
             map.put("type6",infoList6);
             result.setResult(map);
             result.success("查询成功!");
         }catch(Exception e){
             log.error(e.getMessage(),e);
             result.error500("获取失败!");
         }
         return result;
     }

	 @AutoLog(value = "分页列表查询")
	 @ApiOperation(value="分页列表查询", notes="分页列表查询")
	 @GetMapping(value = "/queryPageByTypeList")
	 public Result<IPage<SysInformation>> queryPageByTypeList(@RequestParam(name="type") String type,@RequestParam(name="content") String content,
		 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo, @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
		 HttpServletRequest req) {
		 Result<IPage<SysInformation>> result = new Result<IPage<SysInformation>>();
		 Page<SysInformation> page = new Page<SysInformation>(pageNo, pageSize);
		 IPage<SysInformation> pageList = sysInformationService.queryPageByTypeList(page, type,content);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	 @AutoLog(value = "分页列表查询")
	 @ApiOperation(value="分页列表查询", notes="分页列表查询")
	 @GetMapping(value = "/initialList")
	 public Result<IPage<SysInformation>> initialList(@RequestParam(name="content") String content,
		 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo, @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
		 Result<IPage<SysInformation>> result = new Result<IPage<SysInformation>>();
		 Page<SysInformation> page = new Page<SysInformation>(pageNo, pageSize);
		IPage<SysInformation> pageList = sysInformationService.initialList(page,content);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
}
