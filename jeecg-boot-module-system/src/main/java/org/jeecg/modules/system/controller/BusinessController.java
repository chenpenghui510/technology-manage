package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.message.entity.SysMessageTemplate;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.message.service.ISysMessageTemplateService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.service.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @Description: 业务列表
* @Author: jeecg-boot
* @Date:   2019-07-02
* @Version: V1.0
*/
@Slf4j
@Api(tags="业务列表")
@RestController
@RequestMapping("/system/business")
public class BusinessController {

    @Autowired
    private IUserBusinessService userBusinessService;
   @Autowired
   private IBusinessService businessService;
    @Autowired
    private IUserDeptService iUserDeptService;
    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;
    @Autowired
    private ISysAnnouncementService sysAnnouncementService;
    @Autowired
    private IBusinessAnnouncementService businessAnnouncementService;
    @Autowired
    private ISysMessageService sysMessageService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;



   /**
     * 分页列表查询
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "业务列表-分页列表查询")
   @ApiOperation(value="业务列表-分页列表查询", notes="业务列表-分页列表查询")
   @GetMapping(value = "/listAll")
   public Result<IPage<Map>> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {

       Result<IPage<Map>> result = new Result<IPage<Map>>();

       String businessName = req.getParameter("businessName")==null ? "":req.getParameter("businessName");
       String status = req.getParameter("status")==null ? "":req.getParameter("status");
       String businessLevel = req.getParameter("businessLevel")==null ? "":req.getParameter("businessLevel");
       String department = req.getParameter("departName")==null?"":req.getParameter("departName");
       String businessType = req.getParameter("businessType")==null?"":req.getParameter("businessType");
       String delFlag = req.getParameter("delFlag")==null?"":req.getParameter("delFlag");

       Page<Map> page = new Page<Map>(pageNo, pageSize);
       IPage<Map> pageList = businessService.getPageList(page, businessName, status, businessLevel,department,businessType,delFlag);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;

   }


    @AutoLog(value = "业务列表-业务分配 选择")
    @ApiOperation(value="业务列表-分页列表查询", notes="业务列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Business>> queryPageList2(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                 HttpServletRequest req) {

        Result<IPage<Business>> result = new Result<IPage<Business>>();

        String dpetId = req.getParameter("dpetId")==null ? "":req.getParameter("dpetId");
        String adminId = req.getParameter("adminId")==null ? "":req.getParameter("adminId");
        String giveStatus = req.getParameter("giveStatus")==null ? "":req.getParameter("giveStatus");

        if(GainetUtils.isEmpty(dpetId) && GainetUtils.isEmpty(adminId) && GainetUtils.isEmpty(giveStatus)){
            result.setSuccess(true);
            result.setResult(null);
            return result;
        }

        String name = req.getParameter("businessName")==null?"":req.getParameter("businessName");
       /*QueryWrapper<Business> queryWrapper = QueryGenerator.initQueryWrapper(business, req.getParameterMap());
       if(name != null && !"".equals(name)){
           queryWrapper.like("business_name",name);
       }*/
        Page<Business> page = new Page<Business>(pageNo, pageSize);
        IPage<Business> pageList = businessService.getPageList2(page, name, dpetId, adminId, giveStatus);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;

    }

   /**
     *   添加
    * @param business
    * @return
    */
   @AutoLog(value = "业务列表-添加")
   @ApiOperation(value="业务列表-添加", notes="业务列表-添加")
   @PostMapping(value = "/add")
   public Result<Business> add(@RequestBody Business business) {
       Result<Business> result = new Result<Business>();
       try {
           //登录人
           LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
           business.setGiveStatus(0);
           business.setDelFlag(0);
           business.setCreateTime(new Date());
           business.setUpdateTime(new Date());
           business.setByUser(sysUser.getId());

           businessService.save(business);
           result.success("添加成功！");
       } catch (Exception e) {
           log.error(e.getMessage(),e);
           result.error500("操作失败");
       }
       if(result.isSuccess()){
           sysBaseAPI.addLog("添加业务:"+business.getBusinessName()+"成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
       }else{
           sysBaseAPI.addLog("添加业务:"+business.getBusinessName()+"失败!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
       }
       return result;
   }

   /**
     *  编辑
    * @param business
    * @return
    */
   @AutoLog(value = "业务列表-编辑")
   @ApiOperation(value="业务列表-编辑", notes="业务列表-编辑")
   @PutMapping(value = "/edit")
   public Result<Business> edit(@RequestBody Business business) {
       Result<Business> result = new Result<Business>();
       Business businessEntity = businessService.getById(business.getId());
       if(businessEntity==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = businessService.updateById(business);
           //TODO 返回false说明什么？
           if(ok) {
               result.success("修改成功!");
           }
       }
       if(result.isSuccess()){
           sysBaseAPI.addLog("修改业务:"+business.getBusinessName()+"成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
       }else{
           sysBaseAPI.addLog("修改业务:"+business.getBusinessName()+"失败!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
       }
       return result;
   }

   /**
     *   通过id删除
    * @param id
    * @return
    */
   @AutoLog(value = "业务列表-通过id删除")
   @ApiOperation(value="业务列表-通过id删除", notes="业务列表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<Business> delete(@RequestParam(name="id",required=true) String id) {
       Result<Business> result = new Result<Business>();
       Business business = businessService.getById(id);
       if(business==null) {
           result.error500("未找到对应实体");
       }else {
           business.setDelFlag(1);
           boolean ok = businessService.updateById(business);
           if(ok) {
               result.success("删除成功!");
           }
       }
       if(result.isSuccess()){
           sysBaseAPI.addLog("删除业务:"+business.getBusinessName()+"成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
       }else{
           sysBaseAPI.addLog("删除业务:"+business.getBusinessName()+"失败!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
       }
       return result;
   }

    /**
     *  通过ID复原删除的业务
     */
    @AutoLog(value="业务列表-通过ID复原")
    @ApiOperation(value="业务列表-通过ID复原",notes="业务列表-通过ID复原")
    @GetMapping(value="/reback")
    public  Result<String> reback(@RequestParam(name="id",required = true)String id){
        Result<String> result = new Result<String>();
        Business business = businessService.getById(id);
        if(business==null) {
            result.error500("未找到对应实体");
        }else {
            business.setDelFlag(0);
            boolean ok = businessService.updateById(business);
            if(ok) {
                result.success("复原成功!");
            }
        }
        if(result.isSuccess()){
            sysBaseAPI.addLog("复原业务:"+business.getBusinessName()+"成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
        }else{
            sysBaseAPI.addLog("复原业务:"+business.getBusinessName()+"失败!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
        }
        return result;
    }

   /**
     *  批量删除
    * @param ids
    * @return
    */
   @AutoLog(value = "业务列表-批量删除")
   @ApiOperation(value="业务列表-批量删除", notes="业务列表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<Business> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       Result<Business> result = new Result<Business>();
       if(ids==null || "".equals(ids.trim())) {
           result.error500("参数不识别！");
       }else {
           this.businessService.removeByIds(Arrays.asList(ids.split(",")));
           result.success("删除成功!");
       }
       return result;
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @AutoLog(value = "业务列表-通过id查询")
   @ApiOperation(value="业务列表-通过id查询", notes="业务列表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<Business> queryById(@RequestParam(name="id",required=true) String id) {
       Result<Business> result = new Result<Business>();
       Business business = businessService.getById(id);
       if(business==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(business);
           result.setSuccess(true);
       }
       return result;
   }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @AutoLog(value = "业务列表-通过id查询")
    @ApiOperation(value="业务列表-通过id查询", notes="业务列表-通过id查询")
    @GetMapping(value = "/businessListByType")
    public Result<List<Business> > businessListByType(@RequestParam(name="id") String id) {
        Result<List<Business> > result = new Result<List<Business> >();
       List<Business> list= businessService.businessListByType(id);
       result.setResult(list);
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
     QueryWrapper<Business> queryWrapper = null;
     try {
         String paramsStr = request.getParameter("paramsStr");
         if (oConvertUtils.isNotEmpty(paramsStr)) {
             String deString = URLDecoder.decode(paramsStr, "UTF-8");
             Business business = JSON.parseObject(deString, Business.class);
             queryWrapper = QueryGenerator.initQueryWrapper(business, request.getParameterMap());
         }
     } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
     }

     //Step.2 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     List<Business> pageList = businessService.list(queryWrapper);
     //导出文件名称
     mv.addObject(NormalExcelConstants.FILE_NAME, "业务列表列表");
     mv.addObject(NormalExcelConstants.CLASS, Business.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("业务列表列表数据", "导出人:Jeecg", "导出信息"));
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
             List<Business> listBusinesss = ExcelImportUtil.importExcel(file.getInputStream(), Business.class, params);
             for (Business businessExcel : listBusinesss) {
                 businessService.save(businessExcel);
             }
             return Result.ok("文件导入成功！数据行数:" + listBusinesss.size());
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
     *   添加
     * @return
     */
    @AutoLog(value = "分配业务-管理员")
    @ApiOperation(value="分配业务-管理员", notes="分配业务-管理员")
    @PostMapping(value = "/addBusinessToAdmin")
    public Result<?> addBusinessToAdmin(@RequestBody Map<String,String> map) {
        Result<?> result = new Result<>();
        String str = "";
        try {
            //buIds: "2,3,1"    depId: "a7d7e77e06c84325a40932163adcdaa6
            String buIds = map.get("buIds");
            String depId = map.get("depId");
            String type = map.get("type");

            if(GainetUtils.isEmpty(buIds)){
                return Result.error("业务id为空");
            }
            if(GainetUtils.isEmpty(depId)){
                return Result.error("部门id为空");
            }
            if(GainetUtils.isEmpty(type)){
                return Result.error("参数不足");
            }

            if("1".equals(type)){
                for(String businessId : buIds.split(",")){
                    if(GainetUtils.isEmpty(businessId)){
                        continue;
                    }
                    Business business = businessService.getById(Integer.valueOf(businessId));
                    if(business.getGiveStatus() != 0){
                        return Result.error("该业务已分配" );
                    }
                    business.setDpetId(depId);
                    business.setGiveStatus(1);
                    businessService.saveOrUpdate(business);
                }
            }else {
                String adminId = map.get("adminId");
                if(GainetUtils.isEmpty(adminId)){
                    return Result.error("管理员id为空");
                }

                for(String businessId : buIds.split(",")){
                    if(GainetUtils.isEmpty(businessId)){
                        continue;
                    }
                    //查询 中间表 业务是否已分配给该管理员
                    List<UserBusiness> list = userBusinessService.findByBidAdminid(businessId, adminId);
                    Business business = businessService.getById(Integer.valueOf(businessId));
                    if(list != null && list.size() > 0){
                        str += business.getBusinessName()+",";
                        continue;
                    }
                    if(!depId.equals(business.getDpetId())){
                        return Result.error("部门信息不匹配" );
                    }
                    UserBusiness userBusiness = new UserBusiness();
                    userBusiness.setBusinessId(Integer.valueOf(businessId));
                    userBusiness.setUserId(adminId);
                    if(userBusinessService.save(userBusiness)){
                        business.setGiveStatus(2);
                        businessService.saveOrUpdate(business);
                    }

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Result.error("添加异常");
        }
        if(GainetUtils.isEmpty(str)){
            sysBaseAPI.addLog("分配业务成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
            return Result.ok("分配业务成功" );
        }else{
            sysBaseAPI.addLog("分配业务:"+str+"成功!", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
            return Result.ok("业务："+str+" 已分配给该管理员！" );
        }

    }


    /**
     * 业务解绑
     * @param map
     * @return
     */
    @AutoLog(value = "业务解绑")
    @ApiOperation(value="分配业务-管理员", notes="分配业务-管理员")
    @PostMapping(value = "/removeDelete")
    public Result<?> removeDelete(@RequestBody Map<String,String> map) {

            //buIds: "2,3,1"    depId: "a7d7e77e06c84325a40932163adcdaa6
            String buIds = map.get("businessId");
            String depId = map.get("depId");
            String type = map.get("type");
            if(GainetUtils.isEmpty(buIds)){
                return Result.error("业务id为空");
            }
            if(GainetUtils.isEmpty(depId)){
                return Result.error("部门id为空");
            }
            Business business = businessService.getById(Integer.valueOf(buIds));
        try {
            if(!depId.equals(business.getDpetId())){
                return Result.error("部门信息不匹配" );
            }
            //部门-业务  解绑
            if("1".equals(type)){
                if(business.getGiveStatus() == 2 ){
                    List<UserBusiness> relationList = userBusinessService.getEntityBy(null, buIds);
                    if(relationList!=null&&relationList.size()>0){
                        for(UserBusiness userBusiness:relationList){
                            userBusinessService.removeById(userBusiness.getId());
                        }
                    }
                    businessService.removeForDpt(business);
                    return Result.ok("操作成功" );
                }
                if(business.getGiveStatus() == 1){
                    if(!businessService.removeForDpt(business)){
                        return Result.ok("操作失败" );
                    }
                }
            }else{//管理员-业务  解绑
                if(business.getGiveStatus() == 2){
                    if(!businessService.removeForAdmin(business)){
                        return Result.ok("操作失败" );
                    }
                }
            }


        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Result.error("添加异常");
        }
        sysBaseAPI.addLog("业务："+business.getBusinessName()+"解绑成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
        return Result.ok("操作成功" );
    }

    /**
     * 移除 关系   批量
     * @param map
     * @return
     */
    @AutoLog(value = "移除关系")
    @ApiOperation(value="分配业务-管理员", notes="分配业务-管理员")
    @PostMapping(value = "/removeDeleteBatch")
    public Result<?> removeDeleteBatch(@RequestBody Map<String,String> map) {

            String buIds = map.get("businessId");
            String adminId = map.get("adminId");
            String type = map.get("type");
            if(GainetUtils.isEmpty(buIds)){
                return Result.error("业务id为空");
            }
        try {
            //部门-业务  解绑
            if("1".equals(type)){
                for(String bId : buIds.split(",")){
                    if(GainetUtils.isEmpty(bId)){
                        continue;
                    }
                    Business business = businessService.getById(Integer.valueOf(bId));
                    //若业务已经分配给管理员，则先将业务与管理员关系解除后解除业务与部门之间的关联
                    if(business.getGiveStatus() == 2 ){
                        List<UserBusiness> relationList = userBusinessService.getEntityBy(null, bId);
                        if(relationList!=null&&relationList.size()>0){
                            for(UserBusiness userBusiness:relationList){
                                userBusinessService.removeById(userBusiness.getId());
                            }
                        }
                        businessService.removeForDpt(business);
                        continue;
                    }
                    if(business.getGiveStatus() == 1){
                        businessService.removeForDpt(business);
                        continue;
                    }
                }
            }else {
                //管理员-业务  解绑
                for(String bId : buIds.split(",")){
                    if(GainetUtils.isEmpty(bId)){
                        continue;
                    }
                    Business business = businessService.getById(Integer.valueOf(bId));
                    //获取 中间表  删除
                    List<UserBusiness> list1 = userBusinessService.getEntityBy(adminId, bId);
                    if(list1 != null && list1.size() > 0){
                        UserBusiness userBusiness = list1.get(0);
                        userBusinessService.removeById(userBusiness.getId());
                    }
                    //查询中间表是否还有 此业务的管理员  没有则赋值1
                    List<UserBusiness> list2 = userBusinessService.getEntityBy(null, bId);
                    if(list2 == null||list2.size()<1){
                        businessService.removeForAdmin(business);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Result.error("功能异常！");
        }
        return Result.ok("移除关系操作成功!");
    }


    @PostMapping(value = "/businessAddAnnouncement")
    public Result<?> businessAddAnnouncement(@RequestBody Map<String,String> map){
        try {
            String bid = map.get("bid");
            if(GainetUtils.isEmpty(bid)){
                return Result.error("参数为空");
            }
            //找到 业务
            Business business = businessService.getById(bid);
            if(business == null){
                return Result.error("业务不存在");
            }
            //找到 模板
            List<SysMessageTemplate> list = sysMessageTemplateService.selectByCode("BUSINESS_accept");
            if(list == null || list.size() < 1){
                return Result.error("业务短信模板不存在");
            }
            SysMessageTemplate sysMessageTemplate = list.get(0);
            //返回内容
            String content = sysMessageTemplate.getTemplateContent();
            if(GainetUtils.isEmpty(content)){
                return Result.error("短信模板内容为空");
            }
            StringBuilder contentBuilder = new StringBuilder(content);
            StringBuilder uname = new StringBuilder("${userName}");
            StringBuilder bname = new StringBuilder("${bName}");
            StringBuilder stime = new StringBuilder("${startTime}");
            StringBuilder etime = new StringBuilder("${endTime}");

            //尊敬的${userName}：您好，关于受理的业务：${bName}，于${startTime}开始，${endTime}结束，请及时处理，特此通知！
            //{"userName":"username","bName":"bname","startTime":"stime","endTime":"endtime"}
            //替换${}
            contentBuilder = GainetUtils.StringBuilderReplace(contentBuilder, uname.toString(), "用户");
            contentBuilder = GainetUtils.StringBuilderReplace(contentBuilder, bname.toString(), business.getBusinessName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            contentBuilder = GainetUtils.StringBuilderReplace(contentBuilder, stime.toString(), format.format(business.getStartTime()));
            contentBuilder = GainetUtils.StringBuilderReplace(contentBuilder, etime.toString(), format.format(business.getEndTime()));

            //content.replaceAll(bname+"", business.getBusinessName());
            //content.replaceAll(stime+"", GainetUtils.getYyyyMMddHHmmssSDF().format(business.getStartTime()));
            //content.replaceAll(etime+"", GainetUtils.getYyyyMMddHHmmssSDF().format(business.getEndTime()));

            return Result.ok(contentBuilder);

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage(),e);
            return Result.error("操作异常");
        }
    }

    /**
     * 业务 受理通知
     * @return
     */
    @RequestMapping(value = "/doBusinessAddAnnouncement", method = RequestMethod.POST)
    public Result<SysAnnouncement> doBusinessAddAnnouncement(@RequestBody Map map) {
        Result<SysAnnouncement> result = new Result<SysAnnouncement>();

        try {
            //获取当前登陆人
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if(sysUser == null || GainetUtils.isEmpty(sysUser.getUsername())){
                result.error500("登陆异常");
                return result;
            }
            JSONObject json = new JSONObject(map);
            if(json == null){
                result.error500("参数为空");
                return result;
            }
            String bid = json.getString("bid");
            json.remove("bid");
            SysAnnouncement sysAnnouncement = json.toJavaObject(SysAnnouncement.class);
            //找到 模板
            List<SysMessageTemplate> list = sysMessageTemplateService.selectByCode("BUSINESS_accept");
            if(list == null || list.size() < 1){
                result.error500("业务短信模板不存在");
                return result;
            }
            Business business = businessService.getById(bid);
            if(business == null){
                result.error500("业务不存在");
                return result;
            }
            SysMessageTemplate sysMessageTemplate = list.get(0);
            String jsonStr = sysMessageTemplate.getTemplateTestJson();
            if(GainetUtils.isEmpty(jsonStr)){
                result.error500("业务短信jsonStr不存在");
                return result;
            }
            //模板 json {"userName":"username","bName":"bname","startTime":"stime","endTime":"endtime"}
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);

            jsonObject.put("userName","您");
            jsonObject.put("bName", business.getBusinessName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jsonObject.put("startTime", format.format(business.getStartTime()));
            jsonObject.put("endTime", format.format(business.getEndTime()));

            sysAnnouncement.setIsExamine(1);
            sysAnnouncement.setCreateTime(new Date());
            sysAnnouncement.setCreateBy(sysUser.getUsername());
            sysAnnouncement.setSender(sysUser.getUsername());
            sysAnnouncement.setPriority("H");
            sysAnnouncement.setSendStatus(CommonSendStatus.UNPUBLISHED_STATUS_0);
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            //sysAnnouncementService.saveAnnouncement(sysAnnouncement);//TODO
            sysAnnouncementService.saveAnnouncement(sysAnnouncement,jsonObject.toJSONString());//TODO
            //生成中间表
            BusinessAnnouncement businessAnnouncement = new BusinessAnnouncement();
            businessAnnouncement.setBusinessId(business.getId());
            businessAnnouncement.setSysAnnouncementId(sysAnnouncement.getId());
            businessAnnouncement.setStatus(0);
            businessAnnouncementService.save(businessAnnouncement);

            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }


    @RequestMapping(value = "/getAnnouncement", method = RequestMethod.POST)
    public Result<IPage<Map>> getAnnouncement(@RequestBody Map map,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

        Result<IPage<Map>> result = new Result<IPage<Map>>();

        if(GainetUtils.isEmpty(map.get("id"))){
            result.error500("参数为空");
            return result;
        }
        String id = map.get("id").toString();
        Page<Map> page = new Page<Map>(pageNo, pageSize);
        IPage<Map> pageList = sysMessageService.findSendMessageByBid(page, id);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;

    }

    /**
     * 根据类型查询业务列表
     * @param businessType
     * @return
     */
    @RequestMapping(value = "/listByType", method = RequestMethod.GET)
    public Result<List<Business>> listByType(@RequestParam(name="businessType")String businessType) {
        Result<List<Business>> result = new Result<>();
        QueryWrapper<Business> queryWrapper = new QueryWrapper<Business>();
        queryWrapper.eq("business_type",businessType).eq("del_flag",0);
        List<Business> list = businessService.list(queryWrapper);
        if(list==null||list.size()<=0) {
            result.setSuccess(false);
            result.setMessage("未找到对应的业务信息!");
        }else {
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }

    @RequestMapping(value="businessDetails",method=RequestMethod.GET)
    public Result<Map> searchBusinessDetailsById(@RequestParam(name="id")Integer id){
        Result<Map> result = new Result<>();
        try{
            Map<String,Object> map = businessService.searchBusinessDetailsById(id);
            result.setResult(map);
            result.success("查询成功!");
        }catch(Exception e){
            log.error(e.getMessage());
            result.error500("查询异常");
        }
        return result;
    }


}
