package org.jeecg.modules.system.controller;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.system.query.QueryCondition;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.superSearch.ObjectParseUtil;
import org.jeecg.common.util.superSearch.QueryRuleVo;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.Column;
import com.baomidou.mybatisplus.extension.api.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.util.ExcelWriter;
import org.jeecg.modules.system.util.GainetUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
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

import static org.jeecg.common.system.query.QueryGenerator.addEasyQuery;

/**
 * @Description: 专项申请
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="专项申请")
@RestController
@RequestMapping("/system/formApply")
public class FormApplyController {
	@Autowired
	private IFormApplyService formApplyService;
	 @Autowired
	 private IFormTemplateService formTemplateService;
     @Autowired
	 private IFormApplyExpertGroupService formApplyExpertGroupService;

     @Autowired
     private ISysBaseAPI sysBaseAPI;
	 @Autowired
	 private IBusinessService businessService;
	@Autowired
	private ISysUserCompanyPatentService sysUserCompanyPatentService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysUserPersonalDataService personalDataService;
	@Autowired
	private ISysUserCompanyDataService companyDataService;
	@Autowired
	private ISysDictService iSysDictService;
     /**
	  * 分页列表查询
	 * @param formApply
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "专项申请-分页列表查询")
	@ApiOperation(value="专项申请-分页列表查询", notes="专项申请-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FormApply>> queryPageList(FormApply formApply,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<FormApply>> result = new Result<IPage<FormApply>>();
		Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser)subject.getPrincipal();
        if(sysUser==null){
			result.setSuccess(true);
			result.setResult(null);
			return result;
        }
        String userId =sysUser.getId();
        String dpetId = req.getParameter("dpetId")==null ? "":req.getParameter("dpetId");
        String name = req.getParameter("businessName")==null?"":req.getParameter("businessName");
		//QueryWrapper<FormApply> queryWrapper = QueryGenerator.initQueryWrapper(formApply, req.getParameterMap());
		Page<FormApply> page = new Page<FormApply>(pageNo, pageSize);
		IPage<FormApply> pageList = formApplyService.page2(page,userId, dpetId,name);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	 /**
	  * 获取审核列表
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/formApplyList")
	 public Result<IPage<Map>> getFormApplyList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												HttpServletRequest req) throws ParseException {
		 Result<IPage<Map>> result = new Result<IPage<Map>>();
		 Subject subject = SecurityUtils.getSubject();
		 LoginUser sysUser = (LoginUser)subject.getPrincipal();
		 if(sysUser==null){
			 result.setSuccess(true);
			 result.setResult(null);
			 return result;
		 }
		 String userId =sysUser.getId();

		 String applyType = req.getParameter("applyType");
		 if(!GainetUtils.isEmpty(applyType) && "2".equals(applyType)){
			 userId = null;
		 }
		 SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//"2019-07-24 09:53:07"
		 String startTime = req.getParameter("startTime");
		 String endTime = req.getParameter("endTime");

		 Date start_time = null;
		 if(!GainetUtils.isEmpty(startTime)){
			start_time = dd.parse(startTime);
		 }
		 Date end_time = null;
		 if(!GainetUtils.isEmpty(endTime)){
			 end_time = dd.parse(endTime);
		 }
		 String businessName = req.getParameter("businessName");
		 String businessType = req.getParameter("businessType");
		 String personName = req.getParameter("personName");
		 String status = req.getParameter("status");
		 if("5".equals(status)){
			 userId=null;
		 }
		 Page<Map> page = new Page<Map>(pageNo, pageSize);
		 IPage<Map> pageList = formApplyService.getFormApplyList(page,businessName,businessType,personName,
				 status,start_time,end_time,userId);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }


     /**
      * 获取待复审的  审核列表
      * @param pageNo
      * @param pageSize
      * @param req
      * @return
      */
     @GetMapping(value = "/formApplyReviewList")
     public Result<IPage<Map>> formApplyReviewList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) throws ParseException {
         Result<IPage<Map>> result = new Result<IPage<Map>>();
         Subject subject = SecurityUtils.getSubject();
         LoginUser sysUser = (LoginUser)subject.getPrincipal();
         if(sysUser==null){
             result.setSuccess(true);
             result.setResult(null);
             return result;
         }
         String userId =sysUser.getId();

         SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//"2019-07-24 09:53:07"
         String startTime = req.getParameter("startTime");
         String endTime = req.getParameter("endTime");

         Date start_time = null;
         if(!GainetUtils.isEmpty(startTime)){
             start_time = dd.parse(startTime);
         }
         Date end_time = null;
         if(!GainetUtils.isEmpty(endTime)){
             end_time = dd.parse(endTime);
         }
         String businessName = req.getParameter("businessName");
         String businessType = req.getParameter("businessType");
         String personName = req.getParameter("personName");

         Page<Map> page = new Page<Map>(pageNo, pageSize);
         IPage<Map> pageList = formApplyService.formApplyReviewList(page,businessName,businessType,personName,
                 start_time,end_time,userId);
         result.setSuccess(true);
         result.setResult(pageList);
         return result;
     }

	
	/**
	  *   添加
	 * @param formApply
	 * @return
	 */
	@AutoLog(value = "专项申请-添加")
	@ApiOperation(value="专项申请-添加", notes="专项申请-添加")
	@PostMapping(value = "/add")
	public Result<FormApply> add(@RequestBody FormApply formApply) {
		Result<FormApply> result = new Result<FormApply>();
		try {
			formApplyService.save(formApply);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	@AutoLog(value = "根据Appid获取组件集合")
	@ApiOperation(value="专项申请-编辑", notes="专项申请-编辑")
	@PostMapping(value = "/getComponentByAppId")
	public Result<Map> getComponentByAppId(@RequestBody String id){
		Result<Map> result=new Result<>();
		result=formApplyService.getComponentByAppId(id);
		return result;
	}
	 @AutoLog(value = "根据Appid获取组件集合")
	 @ApiOperation(value="专项申请-编辑", notes="专项申请-编辑")
	 @PostMapping(value = "/getColumn")
	 public Result<Map> getColumn(@RequestBody String id){
		 Result<Map> result=new Result<>();
		 result=formApplyService.getColumn(id);
		 return result;
	 }
	/**
	  *  编辑
	 * @param formApply
	 * @return
	 */
	@AutoLog(value = "专项申请-编辑")
	@ApiOperation(value="专项申请-编辑", notes="专项申请-编辑")
	@PutMapping(value = "/edit")
	public Result<FormApply> edit(@RequestBody FormApply formApply) {
		Result<FormApply> result = new Result<FormApply>();
		FormApply formApplyEntity = formApplyService.getById(formApply.getId());
		if(formApplyEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = formApplyService.updateById(formApply);
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
	public Result<FormApply> delete(@RequestParam(name="id",required=true) String id) {
		Result<FormApply> result = new Result<FormApply>();
		FormApply formApply = formApplyService.getById(id);
		if(formApply==null) {
			result.error500("未找到对应实体");
		}else {
			//boolean ok = formApplyService.removeById(id);
			boolean ok =formApplyService.deleteAllInfo(id);
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
	public Result<FormApply> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<FormApply> result = new Result<FormApply>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.formApplyService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<FormApply> queryById(@RequestParam(name="id",required=true) String id) {
		Result<FormApply> result = new Result<FormApply>();
		FormApply formApply = formApplyService.getById(id);
		if(formApply==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(formApply);
			result.setSuccess(true);
		}
		return result;
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
              List<FormApply> listFormApplys = ExcelImportUtil.importExcel(file.getInputStream(), FormApply.class, params);
              for (FormApply formApplyExcel : listFormApplys) {
                  formApplyService.save(formApplyExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listFormApplys.size());
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
	  *   //业务动态表单保存
	  * @param
	  * @return
	  */
	 @AutoLog(value = "业务动态表单申请-添加")
	 @ApiOperation(value="业务动态表单申请-添加", notes="业务动态表单申请-添加")
	 @PostMapping(value = "/addApplyForm")
	 public Result<?> addApplyForm(@RequestBody Map<String,Object> map) {
		 Result<?> result = new Result<>();
		 try {
		 	if(map==null||map.size()<1){
				log.error("业务动态表单申请-添加传参失败!");
				sysBaseAPI.addLog("业务动态表单申请-添加传参失败!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
				result.setMessage("业务动态表单申请-添加传参失败!");
				return result;
			}
			return	formApplyService.addApplyForm(map);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 sysBaseAPI.addLog("业务动态表单申请-添加发生异常!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
			 result.setMessage("操作失败");
		 }
		 return result;
	 }
	 //updateApplyForm
	 /**
	  *   //业务文字表单修改
	  * @param
	  * @return
	  */
	 @AutoLog(value = "业务文字表单-修改")
	 @ApiOperation(value="业务文字表单-修改", notes="业务文字表单-修改")
	 @PostMapping(value = "/updateApplyForm")
	 public Result<?> updateApplyForm(@RequestBody Map<String,Object> map) {
		 Result<?> result = new Result<>();
		 try {
			 if(map==null||map.size()<1){
				 log.error("业务动态表单申请-添加传参失败!");
				 result.setMessage("业务动态表单申请-添加传参失败!");
				 return result;
			 }
			 return	formApplyService.updateApplyForm(map);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setMessage("操作失败");
		 }
		 return result;
	 }

	 /**
	  *   //业务动态表单附件保存
	  * @param
	  * @return
	  */
	 @AutoLog(value = "业务动态表单附件申请-修改")
	 @ApiOperation(value="业务动态表单附件申请-修改", notes="业务动态表单附件申请-修改")
	 @PostMapping(value = "/editApplyFileInfo")
	 public Result<?> editApplyFileInfo(@RequestBody Map<String,Object> fileMap) {
		 Result<?> result = new Result<>();
		 try {
			 if(fileMap==null||fileMap.size()<1){
				 log.error("业务动态表单附件申请-添加传参失败!");
				 result.error500("业务动态表单附件申请-添加传参失败!");
				 return result;
			 }
			 Map<String, Object> map=(Map)fileMap.get("fileInfo");
			 if(fileMap.get("fileInfo")==null){
				 result.error500("业务动态表单附件申请-添加传参失败!");
				 return result;
			 }
			 if(fileMap.get("appId")==null){
				 result.error500("业务动态表单附件申请-添加传参失败!");
				 return result;
			 }
			 String applyId= String.valueOf(fileMap.get("appId"));
			 if (GainetUtils.isEmpty(applyId)) {
				 log.error("业务动态表单附件申请-添加申请ID传参失败!");
				 result.error500("业务动态表单附件申请-添加申请ID传参失败!");
				 return result;
			 }
			 //删除所有appid的重新添加
			 formApplyService.deleteFileByAppId(GainetUtils.intOf(applyId,0));

			 //判断内容格式是否正确
			 for(Map.Entry<String, Object> vo : map.entrySet()){
				 String key=vo.getKey();//key
				 Object value= vo.getValue();//value
				 System.out.println(vo.getKey()+"  "+vo.getValue());
				 if(value==null){
					 result.error500("传值信息失败");
					 return result;
				 }
				 Map filemap=(Map)value;
				 List fileList=(List)filemap.get("fileList");
				 if(fileList.size()>1){
					 result.error500("只能上传一个文件");
					 return result;
				 }
				 if(fileList.size()<1){
					 result.error500("上传文件不能为空");
					 return result;
				 }
			 }
			 return	formApplyService.addApplyFileInfo(map,applyId);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }
	 /**
	  *   //业务动态表单附件保存
	  * @param
	  * @return
	  */
	 @AutoLog(value = "业务动态表单附件申请-添加")
	 @ApiOperation(value="业务动态表单附件申请-添加", notes="业务动态表单附件申请-添加")
	 @PostMapping(value = "/addApplyFileInfo")
	 public Result<?> addApplyFileInfo(@RequestBody Map<String,Object> fileMap) {
		 Result<?> result = new Result<>();
		 try {
			 if(fileMap==null||fileMap.size()<1){
				 log.error("业务动态表单附件申请-添加传参失败!");
				 result.error500("业务动态表单附件申请-添加传参失败!");
				 return result;
			 }
			 Map<String, Object> map=new HashMap<>();
			 if(fileMap.get("fileInfo")!=null){
				 map=(Map)fileMap.get("fileInfo");
			 }
			 if(fileMap.get("applyId")==null){
				 result.error500("业务动态表单附件申请-添加传参失败!");
				 return result;
			 }
			 String applyId= String.valueOf(fileMap.get("applyId"));
			 if (GainetUtils.isEmpty(applyId)) {
				 log.error("业务动态表单附件申请-添加申请ID传参失败!");
				 result.error500("业务动态表单附件申请-添加申请ID传参失败!");
				 return result;
			 }
			 //判断内容格式是否正确
			 for(Map.Entry<String, Object> vo : map.entrySet()){
				 String key=vo.getKey();//key
				 Object value= vo.getValue();//value
				 System.out.println(vo.getKey()+"  "+vo.getValue());
				 if(value==null){
					 result.error500("传值信息失败");
					 return result;
				 }
				 Map filemap=(Map)value;
				 List fileList=(List)filemap.get("fileList");
				 if(fileList.size()>1){
					 result.error500("只能上传一个文件");
					 return result;
				 }
				 if(fileList.size()<1){
					 result.error500("上传文件不能为空");
					 return result;
				 }
			 }
			 return	formApplyService.addApplyFileInfo(map,applyId);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }

	 /**
	  *  提交业务
	  * @param
	  * @return
	  */
	 @AutoLog(value = "专项申请-提交业务")
	 @ApiOperation(value="专项申请-提交业务", notes="专项申请-提交业务")
	 @GetMapping(value = "/subApply")
	 public Result<FormApply> subApply(@RequestParam(name="id") String id) {
		 Result<FormApply> result = new Result<FormApply>();
		 FormApply formApplyEntity = formApplyService.getById(Integer.valueOf(id));
		 if(formApplyEntity==null) {
			 result.error500("未找到对应实体");
		 }else {
			 formApplyEntity.setStatus(3);//待初审
			 boolean ok = formApplyService.updateById(formApplyEntity);
			 if(ok) {
                 sysBaseAPI.addLog("专项申请-提交业务状态修改为待初审成功", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_2);
				 result.success("提交成功!");
			 }
		 }
		 return result;
	 }


	 /**
	  * 查询申请审核详情
	  * @param
	  * @return
	  */
	 @AutoLog(value = "查询申请审核详情")
	 @GetMapping(value = "/getExamineApplyInfo")
	 public Result<List<Map<String,Object>>> getExamineApplyInfo(@RequestParam(name="id") String id) {
		 Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
		 try{
			List<Map<String,Object>> rstList = formApplyService.getExamineApplyInfo(Integer.valueOf(id));
			result.setResult(rstList);
			result.setSuccess(true);
		 }catch(Exception e){
		 	log.error(e.getMessage(),e);
		 	result.error500("查询详情异常!");
		 }
		 return result;
	 }

     /**
      * 查询申请审核附件
      * @param
      * @return
      */
     @AutoLog(value = "查询申请审核详情")
     @GetMapping(value = "/getExamineUpload")
     public Result<List<Map<String,Object>>> getExamineUpload(@RequestParam(name="id") String id) {
         Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
         try{
             List<Map<String,Object>> rstList = formApplyService.getExamineApplyUpload(Integer.valueOf(id));
             result.setResult(rstList);
             result.setSuccess(true);
         }catch(Exception e){
             log.error(e.getMessage(),e);
             result.error500("查询详情异常!");
         }
         return result;
     }

	 /**
	  * 审核申请
	  */
	 @AutoLog(value = "审核申请")
	 @RequestMapping(value = "/examineApply", method = RequestMethod.PUT)
	 public Result<String> examineApply(@RequestBody Map<String,String> params) {
		 Result<String> result = new Result<String>();

		 // 定义FormApply实体类的数据库查询LambdaQueryWrapper
		 FormApply formApply = formApplyService.getById(params.get("id"));
		 if(formApply==null) {
			 result.error500("未找到对应实体");
		 }else {
			 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 	String status = params.get("status");
		 	if("5".equals(status)||"6".equals(status)||"7".equals(status)){
		 		if(formApply.getStatus()!=4){
		 			result.error500("当前业务状态不满足初审要求，无法进行审核！");
				}else{
		 			formApply.setFirstExaminePerson(sysUser.getId());
		 			formApply.setFirstExamineTime(new Date());
					formApply.setOpinion("初审意见:"+params.get("opinion")+";");
				}
			}
		 	if("9".equals(status)||"10".equals(status)){
		 		if(formApply.getStatus()!=8){
		 			result.error500("当前业务状态不满足复审要求，无法进行审核!");
				}else{
		 			formApply.setReExaminePerson(sysUser.getId());
		 			formApply.setReExamineTime(new Date());
		 			formApply.setOpinion(formApply.getOpinion()==null?"":formApply.getOpinion()+"复审意见:"+params.get("opinion")+";");
				}
			}
		 	formApply.setStatus(Integer.valueOf(status));
		 	formApplyService.updateById(formApply);
		 	if("5".equals(status)||"9".equals(status)){
				sysBaseAPI.addLog("申报名称为："+formApply.getName()+"的业务审核通过!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
				result.success("审核成功!");
			}else if("6".equals(status)){
				sysBaseAPI.addLog("申报名称为："+formApply.getName()+"的业务驳回成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
		 		result.success("驳回成功");
			}else if("7".equals(status)||"10".equals(status)){
				sysBaseAPI.addLog("申报名称为："+formApply.getName()+"的业务被拒绝!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
		 		result.success("拒绝成功!");
			}else{
		 		result.success("审核成功");
			}
		 }

		 return result;
	 }
     /**
      * 图表按年度统计 (1.国家级 2.省级 3.市级 4.区/县级 )
      * @param params
      * @return
      */
     @AutoLog(value = "按年度统计")
     @RequestMapping(value = "/censusByYear",method = RequestMethod.POST)
     public Result<Map<String,Object>> censusByYear(@RequestBody Map<String,String> params){
         Result<Map<String,Object>> result=new Result<>();
         Map<String,Object> rsMap=new HashMap<>();
         List<Map<String,Object>> rsListMap=new ArrayList<>();
         Integer status=GainetUtils.intOf(params.get("status"),0);
         //先查有哪些年
         List<String> yearList=formApplyService.yearList();
         if(yearList==null||yearList.size()==0){
             result.setCode(1);
             result.setMessage("操作失败，未获取到年份");
             return result;
         }
         rsMap.put("yearList",yearList);//放X轴年份
         Map<String,Object> yearCountMap1=new HashMap<>();
         yearCountMap1.put("type","国家级");
         Map<String,Object> yearCountMap2=new HashMap<>();
         yearCountMap2.put("type","省级");
         Map<String,Object> yearCountMap3=new HashMap<>();
         yearCountMap3.put("type","市级");
         Map<String,Object> yearCountMap4=new HashMap<>();
         yearCountMap4.put("type","区/县级");
         for (int i=0;i<yearList.size();i++){
             Integer yearCount1=formApplyService.getApplyCountYear(1,GainetUtils.stringOf(yearList.get(i)),status);//国家级
             yearCountMap1.put(GainetUtils.stringOf(yearList.get(i)),yearCount1);
             Integer yearCount2=formApplyService.getApplyCountYear(2,GainetUtils.stringOf(yearList.get(i)),status);//省级
             yearCountMap2.put(GainetUtils.stringOf(yearList.get(i)),yearCount2);
             Integer yearCount3=formApplyService.getApplyCountYear(3,GainetUtils.stringOf(yearList.get(i)),status);//市级
             yearCountMap3.put(GainetUtils.stringOf(yearList.get(i)),yearCount3);
             Integer yearCount4=formApplyService.getApplyCountYear(4,GainetUtils.stringOf(yearList.get(i)),status);//区/县级
             yearCountMap4.put(GainetUtils.stringOf(yearList.get(i)),yearCount4);
         }
         rsListMap.add(yearCountMap1);
         rsListMap.add(yearCountMap2);
         rsListMap.add(yearCountMap3);
         rsListMap.add(yearCountMap4);
         rsMap.put("rsListMap",rsListMap);
         result.setCode(0);
         result.setResult(rsMap);
         return result;
     }

	 /**
	  * 图表按业务统计
	  * @param params
	  * @return
	  */
	 @AutoLog(value = "按业务统计")
	 @RequestMapping(value = "/censusByBusiness",method = RequestMethod.POST)
	 public Result<Map<String,Object>> censusByBusiness(@RequestBody Map<String,String> params){
		 Result<Map<String,Object>> result=new Result<>();
		 List<String> yearList=formApplyService.yearList();
		 Map<String,Object> rsMap=new HashMap<>();
		 rsMap.put("type","申报项目个数");
		 List<String> bsNameList=new ArrayList<>();
		 String year=GainetUtils.stringOf(params.get("year"));
		 Integer status=GainetUtils.intOf(params.get("status"),0);
		 //查出来所有有效的业务id和名称
		 List<Map<String,Object>> businessList=formApplyService.getBusinessIdByApply();
		 if(businessList==null||businessList.size()==0){
			 result.setCode(1);
			 result.setMessage("操作失败，未获取到业务");
			 return result;
		 }
		 for (int i=0;i<businessList.size();i++){
		 	Map<String,Object>businessMap=businessList.get(i);
		 	if(businessMap==null){
		 		continue;
			}
			Integer bsId=GainetUtils.intOf(businessMap.get("id"),0);
		 	if(bsId==0){
				continue;
			}
		 	String bsName=GainetUtils.stringOf(businessMap.get("name"));
		 	//根据条件查询数量
			 Integer bsCount=formApplyService.getApplyCount(bsId,year,status);
			 rsMap.put(bsName,bsCount);
			 bsNameList.add(bsName);
		 }
		 List<Map<String,Object>> rsListMap=new ArrayList<>();
		 rsListMap.add(rsMap);
		 Map<String,Object>rsAllMap=new HashMap<>();
		 rsAllMap.put("bsNameList",bsNameList);
		 rsAllMap.put("rsListMap",rsListMap);
		 rsAllMap.put("yearList",yearList);
		 result.setCode(0);
		 result.setResult(rsAllMap);
		 return result;
	 }
	 /**
	  * 修改初审状态为审核中
	  */
	 @AutoLog(value = "修改初审状态为审核中")
	 @RequestMapping(value = "/doExamineApply", method = RequestMethod.PUT)
	 public Result<String> doExamineApply(@RequestBody Map<String,String> params) {
		 Result<String> result = new Result<String>();
		 // 定义FormApply实体类的数据库查询LambdaQueryWrapper
		 FormApply formApply = formApplyService.getById(params.get("id"));
		 if(formApply==null) {
			 result.error500("未找到对应实体");
		 }else {
			 String status = params.get("status");
			 if("3".equals(formApply.getStatus().toString())){
				 formApply.setStatus(4);
				 formApplyService.updateById(formApply);
			 }
			 result.success("修改状态成功!");
		 }
		 return result;
	 }


	 /**
	  * 给专家分配 申请的业务
	  * @param map
	  * @return
	  */
	 @AutoLog(value = "给专家分配 申请的业务")
	 @ApiOperation(value="给专家分配 申请的业务", notes="给专家分配 申请的业务")
	 @PutMapping(value = "/toReview")
	 public Result<?> toReview(@RequestBody Map map) {
		 Result<?> result = new Result<>();

		 if(GainetUtils.isEmpty(map.get("applyIds"))){
			 result.error500("参数为空");
			 return result;
		 }

         if(GainetUtils.isEmpty(map.get("expertGroupId"))){
             result.error500("参数为空");
             return result;
         }

		 String applyIds = map.get("applyIds").toString();
		 String expertGroupId = map.get("expertGroupId").toString();

		 for(String applyId : applyIds.split(",")){
		     FormApply formApply = formApplyService.getById(Integer.valueOf(applyId));
             formApply.setStatus(8);
             formApplyService.saveOrUpdate(formApply);

             FormApplyExpertGroup formApplyExpertGroup = new FormApplyExpertGroup();
             formApplyExpertGroup.setFormApplyId(Integer.valueOf(applyId));
             formApplyExpertGroup.setExpertGroupId(Integer.valueOf(expertGroupId));
             formApplyExpertGroup.setStatus(0);
             formApplyExpertGroupService.save(formApplyExpertGroup);
         }
		 return result;
	 }


	/**
	 * 业务统计页面
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 * @throws ParseException
	 */
	 @GetMapping(value = "/formApplyAllList")
	 public Result<IPage<Map>> formApplyAllList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												HttpServletRequest req) throws ParseException {
		 Result<IPage<Map>> result = new Result<IPage<Map>>();
		 //先更跟大的筛选条件进行查
		 SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//"2019-07-24 09:53:07"
		 String startTime = req.getParameter("startTime");
		 String endTime = req.getParameter("endTime");

		 Date start_time = null;
		 if(!GainetUtils.isEmpty(startTime)){
			 start_time = dd.parse(startTime);
		 }
		 Date end_time = null;
		 if(!GainetUtils.isEmpty(endTime)){
			 end_time = dd.parse(endTime);
		 }
		 String businessName = req.getParameter("businessName");//业务名称
		 if(!GainetUtils.isEmpty(businessName)){
			 businessName=businessName.trim();
		 }
		 String businessType = req.getParameter("businessType");//大类别
		 String business = req.getParameter("business");//二级类别
		 String personName = req.getParameter("personName");//申请人
		 if(!GainetUtils.isEmpty(personName)){
			 personName=personName.trim();
		 }
		 String status = req.getParameter("status");//状态
		 Page<Map> page = new Page<Map>(pageNo, pageSize);
         List<QueryCondition> conditions=null;
		 try {
			//在大的条件下再进行各个组件的条件查询
			 QueryWrapper<Map> queryWrapper = new QueryWrapper<Map>();
		 	//===========================================================================
		  	//高级组合查询
			 String superQueryParams = req.getParameter("superQueryParams");
			 // 解码
			 if(oConvertUtils.isNotEmpty(superQueryParams)) {
				 try {
					 superQueryParams = URLDecoder.decode(superQueryParams, "UTF-8");
				 } catch (UnsupportedEncodingException e) {
					 log.error("--高级查询参数转码失败!", e);
					 sysBaseAPI.addLog("业务统计高级查询参数转码失败!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);

				 }
				 conditions = JSON.parseArray(superQueryParams, QueryCondition.class);

			 }
	 		} catch(Exception e) {
	 			e.printStackTrace();
	 			log.info("业务统计查询发生异常!异常为:"+e.getMessage());
	 			sysBaseAPI.addLog("业务统计查询发生异常!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
	 		}
         IPage<Map> pageList = formApplyService.getFormApplyList2(page,businessName,businessType,personName,
                 status,start_time,end_time,business,conditions);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }



	 /**
	  * 导出业务信息excel
	  *
	  * @param
	  * @param
	  */
	@GetMapping(value = "/handleExportXlsOfBusiness")
	 public  void  handleExportXlsOfBusiness(HttpServletRequest req,HttpServletResponse response) throws ParseException {
		 // Step.1 组装查询条件
		 Result<IPage<Map>> result = new Result<IPage<Map>>();
		 //先更跟大的筛选条件进行查
		 SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//"2019-07-24 09:53:07"
		 String startTime = req.getParameter("startTime");
		 String endTime = req.getParameter("endTime");

		 Date start_time = null;
		 if (!GainetUtils.isEmpty(startTime)) {
			 start_time = dd.parse(startTime);
		 }
		 Date end_time = null;
		 if (!GainetUtils.isEmpty(endTime)) {
			 end_time = dd.parse(endTime);
		 }
		 String businessName = req.getParameter("businessName");//业务申请名称
		if(!GainetUtils.isEmpty(businessName)){
			businessName=businessName.trim();
		}
		 String business = req.getParameter("business");//二级业务名称
		 String businessType = req.getParameter("businessType");//大类别
		 String personName = req.getParameter("personName");//申请人
		if(!GainetUtils.isEmpty(personName)){
			personName=personName.trim();
		}
		 String status = req.getParameter("status");//状态
		 try {
			 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		 	Business yw = businessService.getById(Integer.valueOf(business));
		 	if(yw!=null&&yw.getId()==35){//年报
				list = formApplyService.getYearReportInfo(personName,status,start_time,end_time,business);
				if(list!=null&&list.size()>0){
					for(Map<String,Object> map:list){
						if(map.get("userId")!=null){
							Map<String,Object> countMap =sysUserCompanyPatentService.countByUserId(map.get("userId")+"");
							map.putAll(countMap);
						}
					}
				}
		 	}else{
				list = formApplyService.getFormApplyList1(businessName, businessType, personName,
						status, start_time, end_time, business);
			}

			 // List<Map> list=	 pageList.getRecords();//大条件查出的集合结果

			 //在大的条件下再进行各个组件的条件查询
			 QueryWrapper<Map> queryWrapper = new QueryWrapper<Map>();
			 //===========================================================================
			 //高级组合查询
			 String superQueryParams = req.getParameter("superQueryParams");
			 // 解码
			 List<Map<String, Object>> allList = new ArrayList<>();
			 if (oConvertUtils.isNotEmpty(superQueryParams)) {
				 try {
					 superQueryParams = URLDecoder.decode(superQueryParams, "UTF-8");
				 } catch (UnsupportedEncodingException e) {
					 log.error("--高级查询参数转码失败!", e);
					 sysBaseAPI.addLog("业务统计导出高级查询参数转码失败!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
				 }
				 List<QueryCondition> conditions = JSON.parseArray(superQueryParams, QueryCondition.class);
				 log.info("---高级查询参数-->" + conditions.toString());

				 for (int i = 0; i < list.size(); i++) {
					 String applyId = String.valueOf(list.get(i).get("id"));
					 boolean statu = true;
					 for (QueryCondition rule : conditions) {
						 if (oConvertUtils.isNotEmpty(rule.getField()) && oConvertUtils.isNotEmpty(rule.getRule()) && oConvertUtils.isNotEmpty(rule.getVal())) {
							 List<Map> contentList = formApplyService.getContentInfoByParams(applyId, rule.getField(), QueryRuleEnum.getByValue(rule.getRule()).getValue(), rule.getVal());
							 if (contentList.size() == 0) {
								 statu = false;
								 break;
							 }
						 }
					 }
					 if (statu == true) {
						 allList.add(list.get(i));
					 }
				 }
				 System.out.println(allList.toString());
			 }else{
				 allList=list;
			 }
			 //获取去每个申请业务的表单数据
			 for (int i = 0; i < allList.size(); i++) {//循环业务集合
				 String id = String.valueOf(allList.get(i).get("id"));//申请Id
				 List<Map> conList = formApplyService.getContentByApplyId(id);//获取该申请业务的所有表单数据
				 for (int j = 0; j < conList.size(); j++) {//循环表单数据
					 Map<String, String> conMap = (Map) conList.get(j);//获取表单的一个元素
					 String lable = conMap.get("label");
					 String value = conMap.get("value");
					 allList.get(i).put(lable, value);//将表单的元素放入到业务数据中
				 }
			 }
			 //所有的数据
			 System.out.println("导出所有数据:" + allList.toString());
			 //获取所有的表头
			 List<Column> column = new ArrayList<Column>();
			 //获取基础数据的表头
			 if(yw!=null&&yw.getId()==35){
			 	column.add(Column.getInstance("company_name", "企业名称"));
				 column.add(Column.getInstance("social_credit_code", "组织机构代码"));
				 column.add(Column.getInstance("incorporation_place", "注册地"));
				 column.add(Column.getInstance("company_detail_address", "通信地址"));
				 column.add(Column.getInstance("legal_person", "法定代表人"));
				 column.add(Column.getInstance("legal_telephone", "办公电话 "));
				 column.add(Column.getInstance("legal_phone", "移动电话"));
				 column.add(Column.getInstance("user_name", "联系人"));
				 column.add(Column.getInstance("user_telephone", "办公电话 "));
				 column.add(Column.getInstance("phone", "移动电话"));
				 column.add(Column.getInstance("company_email", "企业邮箱 "));
				 column.add(Column.getInstance("scienceCompany", "是否郑州市科技型企业"));
				 column.add(Column.getInstance("record_number", "备案编号"));
				 //获取详情的数据的表头
				 List<Map> labelList = formApplyService.getAllContentLabel(business);
				 for (int i = 0; i < labelList.size(); i++) {
					 String lab = labelList.get(i).get("label") == null ? "" : (String) labelList.get(i).get("label");
					 column.add(Column.getInstance(lab, lab));
				 }
				 column.add(Column.getInstance("all", "专利授权总数"));
				 column.add(Column.getInstance("valid", "发明专利数量"));
			 }else{
				 column.add(Column.getInstance("businessType", "业务分类"));
				 column.add(Column.getInstance("businessName", "业务二，级类别"));
				 column.add(Column.getInstance("name", "申请业务名称"));
				 column.add(Column.getInstance("personName", "申请人"));
				 column.add(Column.getInstance("businessLevel", "业务级别"));
				 column.add(Column.getInstance("createTime", "申请时间"));
				 column.add(Column.getInstance("applyStatus", "业务状态"));
				 column.add(Column.getInstance("opinion", "审批建议"));
				 //获取详情的数据的表头
				 List<Map> labelList = formApplyService.getAllContentLabel(business);
				 for (int i = 0; i < labelList.size(); i++) {
					 String lab = labelList.get(i).get("label") == null ? "" : (String) labelList.get(i).get("label");
					 column.add(Column.getInstance(lab, lab));
				 }
			 }

			 //文件的名称
			 String fileName = "业务信息统计表";
			 //表格的名称
			 String tableName = yw.getBusinessName() + "业务信息统计表";
			 ExcelWriter.exportExcel(fileName, tableName, column, allList, req, response);
		 }catch (Exception e){
		 	e.printStackTrace();
		 	log.info("业务统计导出报表发生异常!异常为:"+e.getMessage());
			 sysBaseAPI.addLog("业务统计导出报表发生异常!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
		 }
	 }



	 @AutoLog(value = "业务下ladel列表-通过budId查询")
	 @ApiOperation(value="业务ladel列表-通过budId查询", notes="业务ladel列表-通过budId查询")
	 @GetMapping(value = "/getContentLabelList")
	 public Result<List<Map> > getContentLabelList(@RequestParam(name="busId") String busId) {
		 Result<List<Map> > result = new Result<List<Map> >();
		 List<Map> list= formApplyService.getAllContentLabel(busId);
		 result.setResult(list);
		 result.setSuccess(true);
		 return result;
	 }

	 @AutoLog(value="根据申请查询申报要求")
	 @ApiOperation(value="根据申请查询申报要求",notes="根据申请查询申报要求")
	 @GetMapping(value="/getUploadApplyAsk")
	 public Result<Map> getUploadApplyAsk(@RequestParam(name="id") String id){
		Result<Map> result = new Result<Map>();
		FormApply formApply = formApplyService.getById(id);
		if(formApply!=null){
			FormTemplate formTemplate = formTemplateService.getById(formApply.getFormId());
			List<Map<String,Object>> list=formTemplateService.getFileInfoByTmpId(formTemplate.getFormId());
			Map<String,Object> resultMap=new HashMap<>();
			resultMap.put("tmp",formTemplate);
			resultMap.put("file",list);
			result.setResult(resultMap);
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
			result.setMessage("查询申报要求失败，未找到相关的业务申请信息");
		}
		return result;
	 }

	 @AutoLog(value="加载申请用户信息")
	 @ApiOperation(value="加载申报用户信息",notes="加载申报用户信息")
	 @GetMapping(value="/getApplyUserInfo")
	 public Result<Map> getApplyUserInfo(@RequestParam(name="id")String id){
		 Result<Map> result = new Result<Map>();
		 Map<String,Object> resultMap = new HashMap<>();
		 FormApply formApply = formApplyService.getById(id);
		 SysUser sysUser = sysUserService.getById(formApply.getCreatePerson());
		 resultMap.put("user",sysUser);
		 //个人用户
		 if(sysUser.getUserType()==1){
			QueryWrapper<SysUserPersonalData> personWrapper = new QueryWrapper<SysUserPersonalData>();
			personWrapper.eq("user_id",sysUser.getId());
			SysUserPersonalData personalData = personalDataService.getOne(personWrapper);
			resultMap.put("person", iSysDictService.getDictInfoByObj(personalData));
		 //企业用户
		 }else if(sysUser.getUserType()==2){
		 	SysUserCompanyData companyData = companyDataService.getOne(new QueryWrapper<SysUserCompanyData>().eq("user_id",sysUser.getId()));
		 	resultMap.put("company",iSysDictService.getDictInfoByObj(companyData));
		 }
		 result.setResult(resultMap);
		 result.success("成功!");
		 return result;
	 }

 }
