package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.system.entity.FormComponent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 表单组件
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
public interface IFormComponentService extends IService<FormComponent> {
    List<Map<String,Object>> listByForm(Integer formId);

}
