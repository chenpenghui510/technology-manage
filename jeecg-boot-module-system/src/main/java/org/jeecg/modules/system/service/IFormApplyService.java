package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryCondition;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.modules.system.entity.FormApply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 专项申请
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
public interface IFormApplyService extends IService<FormApply> {

    IPage<FormApply> page2(Page<FormApply> page,String userId,String dpetId,String name);

    Result<?> addApplyForm(Map<String, Object> map);

    Result<?> updateApplyForm(Map<String, Object> map);

    Result<?> addApplyFileInfo(Map<String, Object> map,String applyId);
    //IPage<Map> getFormApplyList(Page<Map> page, Map map);
    IPage<Map> getFormApplyList(Page<Map> page, String businessName, String businessType, String personName, String status,
                                Date start_time, Date end_time, String userId);
    List<Map<String,Object>> getFormApplyList1(String businessName, String businessType, String personName, String status,
                                Date start_time, Date end_time, String business);

    List<Map<String,Object>> getYearReportInfo(String personName,String status,Date start_time,Date end_time,String business);

    Result<Map> getComponentByAppId(String id);

    Result<Map> getColumn(String id);

    IPage<Map> formApplyReviewList(Page<Map> page, String businessName, String businessType, String personName, Date start_time, Date end_time, String userId);

    List<Map<String,Object>> getExamineApplyInfo(Integer id);

    List<Map<String,Object>> getExamineApplyUpload(Integer id);

    boolean deleteAllInfo(String id);

    boolean deleteFileByAppId(Integer AppId);

    List<Map> getContentByApplyId(String id);

    List<Map> getAllContentLabel(String busId);

    List<Map> getContentInfoByParams(String applyId, String field, String byValue, String val);

    IPage<Map> getFormApplyList2(Page<Map> page, String businessName, String businessType, String personName, String status, Date start_time, Date end_time, String business,List<QueryCondition> conditions);

    List<Map<String,Object>> getBusinessIdByApply();

    Integer getApplyCount(Integer bsId,String year,Integer status);

    List<String> yearList();

    Integer getApplyCountYear(Integer level,String year,Integer status);
}
