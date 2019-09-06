package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.FormTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 表单模板
 * @Author: jeecg-boot
 * @Date:   2019-07-12
 * @Version: V1.0
 */
public interface FormTemplateMapper extends BaseMapper<FormTemplate> {

    FormTemplate getTemplateByBusId(@Param("id") Integer id);

    List<Map<String, Object>> getFileInfoByTmpId(@Param("formId")  Integer formId);

    Map<String,Object> getFileInfoByAppId(@Param("id")  Integer id,@Param("appId")  Integer appId);
}
