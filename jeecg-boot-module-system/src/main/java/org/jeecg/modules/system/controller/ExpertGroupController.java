package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.ExpertGroup;
import org.jeecg.modules.system.entity.ExpertUserGroup;
import org.jeecg.modules.system.entity.FormApply;
import org.jeecg.modules.system.entity.FormApplyExpertGroup;
import org.jeecg.modules.system.service.IExpertGroupService;
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
import org.jeecg.modules.system.util.GainetUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @Description: expert_group
* @Author: jeecg-boot
* @Date:   2019-07-08
* @Version: V1.0
*/
@Slf4j
@Api(tags="expert_group")
@RestController
@RequestMapping("/group/expertGroup")
public class ExpertGroupController {
   @Autowired
   private IExpertGroupService expertGroupService;
   @Autowired
   private IFormApplyExpertGroupService formApplyExpertGroupService;
   @Autowired
   private IFormApplyService formApplyService;
   @Autowired
   private IExpertUserGroupService expertUserGroupService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

   /**
     * 分页列表查询
    * @param expertGroup
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "expert_group-分页列表查询")
   @ApiOperation(value="expert_group-分页列表查询", notes="expert_group-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<ExpertGroup>> queryPageList(ExpertGroup expertGroup,
                                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
       Result<IPage<ExpertGroup>> result = new Result<IPage<ExpertGroup>>();
       String groupName = expertGroup.getGroupName();
       expertGroup.setGroupName(null);
       expertGroup.setDelFlag(0);
       QueryWrapper<ExpertGroup> queryWrapper = QueryGenerator.initQueryWrapper(expertGroup, req.getParameterMap());
       if(!GainetUtils.isEmpty(groupName)){
           queryWrapper.like("group_name",groupName);
       }
       Page<ExpertGroup> page = new Page<ExpertGroup>(pageNo, pageSize);
       IPage<ExpertGroup> pageList = expertGroupService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
     *   添加
    * @param expertGroup
    * @return
    */
   @AutoLog(value = "expert_group-添加")
   @ApiOperation(value="expert_group-添加", notes="expert_group-添加")
   @PostMapping(value = "/add")
   public Result<ExpertGroup> add(@RequestBody ExpertGroup expertGroup) {
       Result<ExpertGroup> result = new Result<ExpertGroup>();
       try {
           if(GainetUtils.isEmpty(expertGroup.getGroupName())){
               result.error500("参数为空");
               return result;
           }
           List<ExpertGroup> list = expertGroupService.findByName(expertGroup.getGroupName());
           if(list != null && list.size() > 0){
               result.error500("该组名已存在");
               return result;
           }
           expertGroup.setStatus(0);
           expertGroup.setDelFlag(0);
           expertGroup.setCreateTime(new Date());
           expertGroupService.save(expertGroup);
           sysBaseAPI.addLog("专家组："+expertGroup.getGroupName()+"添加成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
           result.success("添加成功！");
       } catch (Exception e) {
           log.error(e.getMessage(),e);
           result.error500("操作失败");
       }
       return result;
   }

   /**
     *  编辑
    * @param expertGroup
    * @return
    */
   @AutoLog(value = "expert_group-编辑")
   @ApiOperation(value="expert_group-编辑", notes="expert_group-编辑")
   @PutMapping(value = "/edit")
   public Result<ExpertGroup> edit(@RequestBody ExpertGroup expertGroup) {
       Result<ExpertGroup> result = new Result<ExpertGroup>();
       ExpertGroup expertGroupEntity = expertGroupService.getById(expertGroup.getId());
       if(expertGroupEntity==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = expertGroupService.updateById(expertGroup);
           //TODO 返回false说明什么？
           if(ok) {
               sysBaseAPI.addLog("专家组："+expertGroup.getGroupName()+"修改成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
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
   @AutoLog(value = "expert_group-通过id删除")
   @ApiOperation(value="expert_group-通过id删除", notes="expert_group-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<ExpertGroup> delete(@RequestParam(name="id",required=true) String id) {
       Result<ExpertGroup> result = new Result<ExpertGroup>();
       ExpertGroup expertGroup = expertGroupService.getById(id);
       if(expertGroup==null) {
           result.error500("未找到对应实体");
       }else {
           try{
               //移出业务
               QueryWrapper<FormApplyExpertGroup> queryWrapper = new QueryWrapper<FormApplyExpertGroup>();
               queryWrapper.eq("expert_group_id",expertGroup.getId());
               List<FormApplyExpertGroup> applyGroupList = formApplyExpertGroupService.list(queryWrapper);
               if(applyGroupList!=null&&applyGroupList.size()>0){
                   for(FormApplyExpertGroup f : applyGroupList){
                       FormApply apply = formApplyService.getById(f.getFormApplyId());
                       if(apply.getStatus()==8){
                           formApplyExpertGroupService.removeById(f.getId());
                       }else{
                           continue;
                       }
                   }
               }
               //移出人员
               QueryWrapper<ExpertUserGroup> userWrapper = new QueryWrapper<ExpertUserGroup>();
               userWrapper.eq("group_id",expertGroup.getId());
               expertUserGroupService.remove(userWrapper);
               //假删专家组
               expertGroup.setDelFlag(1);
                expertGroupService.updateById(expertGroup);
               sysBaseAPI.addLog("专家组："+expertGroup.getGroupName()+"删除成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
               result.success("删除成功!");
           }catch(Exception e){
               log.error(e.getMessage(),e);
               result.error500("删除专家组失败:"+e.getMessage());
           }
       }

       return result;
   }

   /**
     *  批量删除
    * @param ids
    * @return
    */
   @AutoLog(value = "expert_group-批量删除")
   @ApiOperation(value="expert_group-批量删除", notes="expert_group-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<ExpertGroup> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       Result<ExpertGroup> result = new Result<ExpertGroup>();
       if(ids==null || "".equals(ids.trim())) {
           result.error500("参数不识别！");
       }else {
           this.expertGroupService.removeByIds(Arrays.asList(ids.split(",")));
           result.success("删除成功!");
       }
       return result;
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @AutoLog(value = "expert_group-通过id查询")
   @ApiOperation(value="expert_group-通过id查询", notes="expert_group-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<ExpertGroup> queryById(@RequestParam(name="id",required=true) String id) {
       Result<ExpertGroup> result = new Result<ExpertGroup>();
       ExpertGroup expertGroup = expertGroupService.getById(id);
       if(expertGroup==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(expertGroup);
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
     QueryWrapper<ExpertGroup> queryWrapper = null;
     try {
         String paramsStr = request.getParameter("paramsStr");
         if (oConvertUtils.isNotEmpty(paramsStr)) {
             String deString = URLDecoder.decode(paramsStr, "UTF-8");
             ExpertGroup expertGroup = JSON.parseObject(deString, ExpertGroup.class);
             queryWrapper = QueryGenerator.initQueryWrapper(expertGroup, request.getParameterMap());
         }
     } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
     }

     //Step.2 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     List<ExpertGroup> pageList = expertGroupService.list(queryWrapper);
     //导出文件名称
     mv.addObject(NormalExcelConstants.FILE_NAME, "expert_group列表");
     mv.addObject(NormalExcelConstants.CLASS, ExpertGroup.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("expert_group列表数据", "导出人:Jeecg", "导出信息"));
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
             List<ExpertGroup> listExpertGroups = ExcelImportUtil.importExcel(file.getInputStream(), ExpertGroup.class, params);
             for (ExpertGroup expertGroupExcel : listExpertGroups) {
                 expertGroupService.save(expertGroupExcel);
             }
             return Result.ok("文件导入成功！数据行数:" + listExpertGroups.size());
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
