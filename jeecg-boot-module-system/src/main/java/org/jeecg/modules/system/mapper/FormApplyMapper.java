package org.jeecg.modules.system.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.annotation.Param;
import org.jeecg.common.system.query.QueryCondition;
import org.jeecg.modules.system.entity.FormApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 专项申请
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
public interface FormApplyMapper extends BaseMapper<FormApply> {
    IPage<FormApply> page2(Page<FormApply> page, @Param("userId") String userId, @Param("dpetId") String dpetId,@Param("name") String name);

    /**
     *  获取 业务审核列表
     * @param page
     * @param businessName 业务名称
     * @param businessType    分类id
     * @param personName   企业名称
     * @param status       审核状态
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param userId       当前登录人id   为空时则获取所有审核
     * @return
     */
    IPage<Map> getFormApplyList(Page<Map> page, @Param("businessName")String businessName, @Param("businessType")String businessType,
                                @Param("personName") String personName, @Param("status")String status,
                                @Param("startTime") Date startTime, @Param("endTime")Date endTime, @Param("userId") String userId);
    List<Map<String,Object>> getFormApplyList1(@Param("businessName")String businessName, @Param("businessType")String businessType,
                                @Param("personName") String personName, @Param("status")String status,
                                @Param("startTime") Date startTime, @Param("endTime")Date endTime, @Param("business") String business);

    List<Map<String,Object>> getYearReportInfo(@Param("personName") String personName, @Param("status")String status,
                                               @Param("startTime") Date startTime, @Param("endTime")Date endTime, @Param("business") String business);

    IPage<Map> formApplyReviewList(Page<Map> page, @Param("businessName")String businessName, @Param("businessType")String businessType,
                                   @Param("personName") String personName, @Param("startTime")Date startTime,
                                   @Param("endTime")Date endTime, @Param("userId") String userId);

    List<FormApply>  getApplyInfoByName( @Param("name") String name, @Param("busId") Integer busId);
    List<String> getComponentByAppId(@Param("id")String id);

    List<String> getComponentIdByAppId(@Param("id")String id);

    String getValueBy(@Param("id")String id,@Param("componentId")String componentId);


    List<Map<String,Object>> getExamineApplyInfo(@Param("id") Integer id);

    List<Map<String,Object>> getExamineApplyUpload(@Param("id") Integer id);

    boolean deleteByAppId(@Param("appId") Integer appId);

    int deleteApplyContent(@Param("id") String id);

    int deleteApplyFile(@Param("id") String id);

    List<Map> getContentByApplyId(@Param("applyId") Integer applyId);

    List<Map> getAllContentLabel(@Param("busId") String busId);

    List<Map> getContentInfoByParams(@Param("applyId") Integer applyId,@Param("field")  String field,@Param("byValue") String byValue,@Param("val") String val);

    IPage<Map> getFormApplyList2(Page<Map> page, @Param("businessName")String businessName, @Param("businessType")String businessType,
                                 @Param("personName") String personName, @Param("status")String status,
                                 @Param("startTime") Date startTime, @Param("endTime")Date endTime, @Param("business") String business,@org.apache.ibatis.annotations.Param("list") List<QueryCondition> conditions);

    List<Map<String,Object>> getBusinessIdByApply();

    Integer getApplyCount(@Param("bsId")Integer bsId,@Param("year")String year,@Param("status")Integer status);

    List<String> yearList();

    Integer getApplyCountYear(@Param("level")Integer level,@Param("year")String year,@Param("status")Integer status);

    List<FormApply> getApplyInfoByForm(@Param("formId") Integer formId,@Param("userId")String userId);
}
