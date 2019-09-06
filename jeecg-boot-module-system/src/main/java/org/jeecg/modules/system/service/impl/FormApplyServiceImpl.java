package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryCondition;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.service.IFormApplyService;
import org.jeecg.modules.system.service.IFormFileService;
import org.jeecg.modules.system.util.GainetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @Description: 专项申请
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
@Service
public class FormApplyServiceImpl extends ServiceImpl<FormApplyMapper, FormApply> implements IFormApplyService {

    @Resource
    private FormApplyMapper formApplyMapper;
    @Resource
    private FormTemplateMapper formTemplateMapper;
    @Resource
    private  FormComponentMapper formComponentMapper;
    @Resource
    private FormContentMapper formContentMapper;
    @Resource
    private FormFileMapper formFileMapper;
    @Resource
    private BusinessMapper businessMapper;
    @Resource
    private FormApplyFileMapper  formApplyFileMapper;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private IFormFileService formFileService;
    @Override
    public IPage<FormApply> page2(Page<FormApply> page,String userId, String dpetId, String name){
        IPage<FormApply> list=formApplyMapper.page2(page,userId,dpetId,name);
        return list;
    }

    @Override
    public IPage<Map> getFormApplyList(Page<Map> page, String businessName, String businessType, String personName,
                                       String status, Date start_time, Date end_time, String userId) {
        return  formApplyMapper.getFormApplyList(page,businessName,businessType,personName,
                status,start_time,end_time, userId);
    }

    @Override
    public  List<Map<String,Object>> getFormApplyList1( String businessName, String businessType, String personName,
                                       String status, Date start_time, Date end_time, String business) {
        return  formApplyMapper.getFormApplyList1(businessName,businessType,personName,
                status,start_time,end_time, business);
    }

    @Override
    public List<Map<String,Object>> getYearReportInfo(String personName,String status,Date start_time,Date end_time,String business){
        return formApplyMapper.getYearReportInfo(personName,status,start_time,end_time,business);
    }

    @Override
    public IPage<Map> formApplyReviewList(Page<Map> page, String businessName, String businessType, String personName, Date start_time, Date end_time, String userId) {
        return formApplyMapper.formApplyReviewList(page,businessName,businessType,personName,
                start_time,end_time, userId);
    }

    /**
     * 修改
     * @param map
     * @return
     */
    @Override
    public Result<?> updateApplyForm(Map<String, Object> map){
        Result<String> result = new Result<>();
        //申请记录id
        Integer appId= GainetUtils.intOf(map.get("appId"),0);
        //获取当前申请记录
        FormApply formApply=formApplyMapper.selectById(appId);
        if(formApply==null){
            result.error500("未找到对应模板信息");
            return result;
        }
        //父业务id
        Integer busId=formApply.getBusinessId();
        //业务名称
        String name=String.valueOf(map.get("name"));
        //若修改名称需判断当前名称是否唯一
        if(!name.equals(formApply.getName())){
            List<FormApply> formApplyList=formApplyMapper.getApplyInfoByName(name,busId);
            if(formApplyList!=null&&formApplyList.size()>0){
                sysBaseAPI.addLog("修改业务表单：申请期间该业务名称已存在！请给与确认", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_1);
                result.error500("修改失败！申请期间该业务名称已存在！请给与确认");
                return result;
            }
        }

        Business business = businessMapper.selectById(busId);
        if(business==null){
            result.error500("未找到申请的业务信息");
            return result;
        }
        Date now = new Date();
        //判断是否符合专利申请时间
        if(now.getTime()<business.getStartTime().getTime()||now.getTime()>business.getEndTime().getTime()){
            result.error500("当前非业务申报时间，无法申报该业务！");
            return result;
        }
        FormTemplate formTemplate =formTemplateMapper.getTemplateByBusId(busId);
        if(formTemplate==null){
            result.error500("未找到对应模板信息");
            return result;
        }
        //根据专利名称和业务时间周期判断该业务是否可以进行申报
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //保存申请表
        formApply.setBusinessId(busId);
        formApply.setFormId(formTemplate.getFormId());
        formApply.setFormType(formTemplate.getFormType());
        formApply.setName(name);
        formApply.setCreatePerson(sysUser.getId());
        formApply.setPersonName(sysUser.getRealname());
        formApply.setValid(1);
        formApply.setStatus(1);
        formApply.setUpdateTime(new Date());
        formApplyMapper.updateById(formApply);
        //删除组件
        formApplyMapper.deleteByAppId(formApply.getId());
        //保存组件值和组件Id
        Map<String,Object> comMap=(Map)map.get("content");
        for(Map.Entry<String, Object> vo : comMap.entrySet()){
            String key=vo.getKey();
            Object value= vo.getValue();
            System.out.println(vo.getKey()+"  "+vo.getValue());
            if(!"name".equals(key)){
                FormContent formContent=new FormContent();
                formContent.setApplyId(formApply.getId());
                String num=key.substring(6);
                formContent.setFormComponentId(Integer.valueOf(num));
                formContent.setValue(StringUtils.strip(value.toString(), "[]"));
                formContentMapper.insert(formContent);
            }
        }
        result.setResult(formApply.getId().toString());
        result.success("修改成功！");
        return result;
    }

    @Override
    public Result<String> addApplyForm(Map<String, Object> map) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Result<String> result = new Result<>();
        //业务名称
        String name=String.valueOf(map.get("name"));

        //父业务id
        Integer busId=Integer.valueOf(map.get("busId").toString());
        Business business = businessMapper.selectById(busId);
        if(business==null){
            sysBaseAPI.addLog("添加业务表单：未找到申请的业务信息", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_1);
            result.error500("未找到申请的业务信息");
            return result;
        }
        Date now = new Date();
        //判断是否符合专利申请时间
        if(now.getTime()<business.getStartTime().getTime()||now.getTime()>business.getEndTime().getTime()){
            sysBaseAPI.addLog("添加业务表单：当前非业务申报时间，无法申报该业务！", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_1);
            result.error500("当前非业务申报时间，无法申报该业务！");
            return result;
        }
        FormTemplate formTemplate =formTemplateMapper.getTemplateByBusId(busId);
        if(formTemplate==null){
            sysBaseAPI.addLog("添加业务表单：未找到对应模板信息", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_1);
            result.error500("未找到对应模板信息");
            return result;
        }
        //统计型申请一个模板只能申请一次
        if(business.getBusinessKind()==1){
            List<FormApply> applyList= formApplyMapper.getApplyInfoByForm(formTemplate.getFormId(),sysUser.getId());
            if(applyList!=null&&applyList.size()>0){
                sysBaseAPI.addLog("添加业务表单：统计型业务只能申请一次，该业务您已提交申请，请核实！", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_1);
                result.error500("申请失败！统计型业务只能申请一次，该业务您已提交申请，请核实！");
                return result;
            }
        }
        //根据专利名称和业务时间周期判断该业务是否可以进行申报
        List<FormApply> formApplyList=formApplyMapper.getApplyInfoByName(name,busId);
       if(formApplyList.size()!=0){
           sysBaseAPI.addLog("添加业务表单：申请期间该业务名称已存在！请给与确认", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_1);
           result.error500("申请期间该业务名称已存在！请给与确认");
           return result;
       }
        //保存申请表
        FormApply formApply=new FormApply();
        formApply.setBusinessId(busId);
        formApply.setFormId(formTemplate.getFormId());
        formApply.setFormType(formTemplate.getFormType());
        formApply.setName(name);
        formApply.setCreatePerson(sysUser.getId());
        formApply.setPersonName(sysUser.getRealname());
        formApply.setValid(1);
        if(business.getBusinessLevel()!=4){//4为区/县级
            formApply.setStatus(11);//已完成(用于国家级省级的摘要填写状态)
        }else if(business.getBusinessKind()==1){//1为成绩统计数据
            formApply.setStatus(11);//成绩统计数据不用审核直接标记为已完成
        }else{
            //判断下一步是否有附件
            FormFile formFile=new FormFile();
            formFile.setFormId(formTemplate.getFormId());
            formFile.setUploadType(2);
            List<FormFile> lsit=  formFileService.list(new QueryWrapper<FormFile>(formFile));
            if(lsit.size()==0){//为0标识没有附件信息
                formApply.setStatus(2);//待提交
            }else{
                formApply.setStatus(1);//待上传
            }
        }
        formApply.setCreateTime(new Date());
        formApply.setUpdateTime(new Date());
        formApplyMapper.insert(formApply);
        //保存组件值和组件Id
        Map<String,Object> comMap=(Map)map.get("content");
        for(Map.Entry<String, Object> vo : comMap.entrySet()){
            String key=vo.getKey();
            Object value= vo.getValue();
            System.out.println(vo.getKey()+"  "+vo.getValue());
            if(!"name".equals(key)){
                FormContent formContent=new FormContent();
                formContent.setApplyId(formApply.getId());
                String num=key.substring(6);
                formContent.setFormComponentId(Integer.valueOf(num));
                formContent.setValue(StringUtils.strip(value.toString(), "[]"));
                formContentMapper.insert(formContent);
            }
        }

        result.setResult(formApply.getId().toString());
        result.success("添加成功！");
        sysBaseAPI.addLog("添加业务表单成功", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_1);
        return result;
    }

    /**
     *添加附件
     * @param map
     * @return
     */
    @Override
    public Result<?> addApplyFileInfo(Map<String, Object> map,String applyId) {
        Result<?> result = new Result<>();
        for(Map.Entry<String, Object> vo : map.entrySet()){
            FormApplyFile  formApplyFile=new FormApplyFile();
            String key=vo.getKey();
            String num=key.substring(6);//获取组件id
            //新建applyFile
            FormFile formFile=formFileMapper.selectById(Integer.valueOf(num));//组件实体
            Object value= vo.getValue();//附件内容
            Map fileMap=(Map)value;
            Map fileMap1=(Map)fileMap.get("file");
            Map fileMap2=(Map)fileMap1.get("response");
            String url=(String)fileMap2.get("message");//上传路径
            System.out.println(num+"  "+url);
            //保存实体
            formApplyFile.setFileName(formFile.getFileName());
            formApplyFile.setType(formFile.getUploadType());
            formApplyFile.setUrl(url);
            formApplyFile.setCreateTime(new Date());
            formApplyFile.setFileId(Integer.valueOf(num));
            formApplyFile.setApplyId(Integer.valueOf(applyId));
            formApplyFileMapper.insert(formApplyFile);
        }
        //修改申请状态为待提交
        FormApply formApply=formApplyMapper.selectById(Integer.valueOf(applyId));
        Business business = businessMapper.selectById(formApply.getBusinessId());
        if(business.getBusinessLevel()!=4||business.getBusinessKind()==1){
            formApply.setStatus(11);
        }else{
            formApply.setStatus(2);//待提交
        }
        formApplyMapper.updateById(formApply);
        result.success("操作成功！");
        sysBaseAPI.addLog("添加附件成功", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_2);
        return result;
    }
    @Override
    public Result<Map> getComponentByAppId(String id){
        Result<Map> result = new Result<Map>();
        List<String> strings=formApplyMapper.getComponentByAppId(id);
        Map<String,Object> allMap=new HashMap<>();
        allMap.put("column",strings);
        result.setResult(allMap);
        result.setSuccess(true);
        return result;
    }
    @Override
    public Result<Map> getColumn(String id){
        Result<Map> result = new Result<Map>();
        List<String> strings=formApplyMapper.getComponentByAppId(id);
        List<String> idStrings=formApplyMapper.getComponentIdByAppId(id);
        Map<String,Object> rsMap=new HashMap<>();
        //循环开始拼装model对象
        if(strings!=null&&strings.size()>0){
            for (int i=0;i<strings.size();i++){
                //根据form_component_id和apply_id确定唯一表单项的值
                String componentId=idStrings.get(i);
                String value=formApplyMapper.getValueBy(id,componentId);
                rsMap.put(strings.get(i),value);
            }
        }
        result.setResult(rsMap);
        result.setSuccess(true);
        return result;
    }
    @Override
    public Integer getApplyCount(Integer bsId,String year,Integer status){
        return formApplyMapper.getApplyCount(bsId,year,status);
    }
    @Override
    public List<String> yearList(){
        return formApplyMapper.yearList();
    }
    @Override
    public Integer getApplyCountYear(Integer level,String year,Integer status){
        return formApplyMapper.getApplyCountYear(level,year,status);
    }
    /**
     * 删除三级业务信息和关联的信息
     * @param id
     * @return
     */
    @Override
    public boolean deleteAllInfo(String id) {
        //删除申请业务的关联信息
        int a=formApplyMapper.deleteApplyContent(id);
        int b=formApplyMapper.deleteApplyFile(id);
        //删除业务信息
        int i=formApplyMapper.deleteById(id);
        if(a!=-1&&b!=-1&&i!=-1){
            return true;
        }
        return false;
    }
    @Override
    public boolean deleteFileByAppId(Integer AppId){
        int i=formApplyMapper.deleteApplyFile(GainetUtils.stringOf(AppId));
        if(i!=-1){
            return true;
        }
        return false;
    }

    @Override
    public List<Map> getContentByApplyId(String id) {
        return formApplyMapper.getContentByApplyId(Integer.valueOf(id));
    }

    @Override
    public List<Map> getAllContentLabel(String busId) {
        return formApplyMapper.getAllContentLabel(busId);
    }

    @Override
    public List<Map> getContentInfoByParams(String applyId, String field, String byValue, String val) {
        return formApplyMapper.getContentInfoByParams(Integer.valueOf(applyId),field,byValue,val);
    }

    @Override
    public IPage<Map> getFormApplyList2(Page<Map> page, String businessName, String businessType, String personName, String status, Date start_time, Date end_time, String business,List<QueryCondition> conditions) {
        return formApplyMapper.getFormApplyList2(page,businessName,businessType,personName,
                status,start_time,end_time, business ,conditions);
    }

    @Override
    public List<Map<String,Object>> getExamineApplyInfo(Integer id){
        return formApplyMapper.getExamineApplyInfo(id);
    }

    @Override
    public List<Map<String,Object>> getExamineApplyUpload(Integer id){
        return formApplyMapper.getExamineApplyUpload(id);
    }
    @Override
    public List<Map<String,Object>> getBusinessIdByApply(){
        return formApplyMapper.getBusinessIdByApply();
    }
}
