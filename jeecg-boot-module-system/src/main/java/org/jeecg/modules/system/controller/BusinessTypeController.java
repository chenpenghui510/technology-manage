package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
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
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.BusinessType;
import org.jeecg.modules.system.model.BusinessTypeTreeModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.IBusinessTypeService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
* @Description: 业务类型
* @Author: jeecg-boot
* @Date:   2019-07-04
* @Version: V1.0
*/
@Slf4j
@Api(tags="业务类型")
@RestController
@RequestMapping("/sys/businessType")
public class BusinessTypeController {
   @Autowired
   private IBusinessTypeService businessTypeService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

   /**
     * 分页列表查询
    * @param businessType
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "业务类型-分页列表查询")
   @ApiOperation(value="业务类型-分页列表查询", notes="业务类型-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<BusinessType>> queryPageList(BusinessType businessType,
                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                    HttpServletRequest req) {
      /* Result<IPage<BusinessType>> result = new Result<IPage<BusinessType>>();
       QueryWrapper<BusinessType> queryWrapper = QueryGenerator.initQueryWrapper(businessType, req.getParameterMap());
       Page<BusinessType> page = new Page<BusinessType>(pageNo, pageSize);
       IPage<BusinessType> pageList = businessTypeService.page(page, queryWrapper);*/
       Result<IPage<BusinessType>> result = new Result<IPage<BusinessType>>();

       Page<BusinessType> page = new Page<BusinessType>(pageNo, pageSize);
       IPage<BusinessType> pageList = businessTypeService.getList(page, businessType.getName(),businessType.getByUserId());

       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
     *   添加
    * @param businessType
    * @return
    */
   @AutoLog(value = "业务类型-添加")
   @ApiOperation(value="业务类型-添加", notes="业务类型-添加")
   @PostMapping(value = "/add")
   public Result<BusinessType> add(@RequestBody BusinessType businessType) {
       Result<BusinessType> result = new Result<BusinessType>();
       try {
           //获取当前登陆人
           LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
           businessType.setByUserId(sysUser.getId());
           businessTypeService.save(businessType);
           sysBaseAPI.addLog("业务类型："+businessType.getName()+"添加成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
           result.success("添加成功！");
       } catch (Exception e) {
           log.error(e.getMessage(),e);
           result.error500("操作失败");
       }
       return result;
   }

   /**
     *  编辑
    * @param businessType
    * @return
    */
   @AutoLog(value = "业务类型-编辑")
   @ApiOperation(value="业务类型-编辑", notes="业务类型-编辑")
   @PutMapping(value = "/edit")
   public Result<BusinessType> edit(@RequestBody BusinessType businessType) {
       Result<BusinessType> result = new Result<BusinessType>();
       BusinessType businessTypeEntity = businessTypeService.getById(businessType.getId());
       if(businessTypeEntity==null) {
           result.error500("未找到对应实体");
       }else {
           businessType.setByUserId(businessTypeEntity.getByUserId());
           businessType.setCreateTime(businessTypeEntity.getCreateTime());
           boolean ok = businessTypeService.updateById(businessType);
           //TODO 返回false说明什么？
           if(ok) {
               sysBaseAPI.addLog("业务类型："+businessType.getName()+"修改成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
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
   @AutoLog(value = "业务类型-通过id删除")
   @ApiOperation(value="业务类型-通过id删除", notes="业务类型-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<BusinessType> delete(@RequestParam(name="id",required=true) String id) {
       Result<BusinessType> result = new Result<BusinessType>();
       BusinessType businessType = businessTypeService.getById(id);
       if(businessType==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = businessTypeService.removeById(id);
           if(ok) {
               sysBaseAPI.addLog("业务类型："+businessType.getName()+"删除成功！", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
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
   @AutoLog(value = "业务类型-批量删除")
   @ApiOperation(value="业务类型-批量删除", notes="业务类型-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<BusinessType> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       Result<BusinessType> result = new Result<BusinessType>();
       if(ids==null || "".equals(ids.trim())) {
           result.error500("参数不识别！");
       }else {
           this.businessTypeService.removeByIds(Arrays.asList(ids.split(",")));
           result.success("删除成功!");
       }
       return result;
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @AutoLog(value = "业务类型-通过id查询")
   @ApiOperation(value="业务类型-通过id查询", notes="业务类型-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<BusinessType> queryById(@RequestParam(name="id",required=true) String id) {
       Result<BusinessType> result = new Result<BusinessType>();
       BusinessType businessType = businessTypeService.getById(id);
       if(businessType==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(businessType);
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
     QueryWrapper<BusinessType> queryWrapper = null;
     try {
         String paramsStr = request.getParameter("paramsStr");
         if (oConvertUtils.isNotEmpty(paramsStr)) {
             String deString = URLDecoder.decode(paramsStr, "UTF-8");
             BusinessType businessType = JSON.parseObject(deString, BusinessType.class);
             queryWrapper = QueryGenerator.initQueryWrapper(businessType, request.getParameterMap());
         }
     } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
     }

     //Step.2 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     List<BusinessType> pageList = businessTypeService.list(queryWrapper);
     //导出文件名称
     mv.addObject(NormalExcelConstants.FILE_NAME, "业务类型列表");
     mv.addObject(NormalExcelConstants.CLASS, BusinessType.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("业务类型列表数据", "导出人:Jeecg", "导出信息"));
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
             List<BusinessType> listBusinessTypes = ExcelImportUtil.importExcel(file.getInputStream(), BusinessType.class, params);
             for (BusinessType businessTypeExcel : listBusinessTypes) {
                 businessTypeService.save(businessTypeExcel);
             }
             return Result.ok("文件导入成功！数据行数:" + listBusinessTypes.size());
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

    @RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public Result<List<BusinessType>> queryall() {
        Result<List<BusinessType>> result = new Result<>();
        QueryWrapper<BusinessType> queryWrapper = new QueryWrapper<BusinessType>();
        queryWrapper.eq("status","1");
        List<BusinessType> list = businessTypeService.list(queryWrapper);
        if(list==null||list.size()<=0) {
            result.error500("未找到角色信息");
        }else {
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }

    //业务 大分类、小分类  树
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<List<BusinessTypeTreeModel>> queryTreeList() {
        Result<List<BusinessTypeTreeModel>> result = new Result<>();
        try {
            List<BusinessTypeTreeModel> list = businessTypeService.queryTreeList();
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }
    @RequestMapping(value = "/searchBy", method = RequestMethod.GET)
    public Result<List<BusinessTypeTreeModel>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord) {
        Result<List<BusinessTypeTreeModel>> result = new Result<List<BusinessTypeTreeModel>>();
        try {
            List<BusinessTypeTreeModel> treeList = this.businessTypeService.searhBy(keyWord);
            if (treeList.size() == 0 || treeList == null) {
                throw new Exception();
            }
            result.setSuccess(true);
            result.setResult(treeList);
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("查询失败或没有您想要的任何数据!");
            return result;
        }
    }
}
