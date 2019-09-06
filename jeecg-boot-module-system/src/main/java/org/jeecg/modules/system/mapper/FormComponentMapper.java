package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.FormComponent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 表单组件
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
public interface FormComponentMapper extends BaseMapper<FormComponent> {
    List<Map<String,Object>> listByForm(@Param("formId") Integer formId);
}
