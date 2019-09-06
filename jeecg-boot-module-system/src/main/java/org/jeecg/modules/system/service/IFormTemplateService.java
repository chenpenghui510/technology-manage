package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.FormTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 表单模板
 * @Author: jeecg-boot
 * @Date:   2019-07-12
 * @Version: V1.0
 */
public interface IFormTemplateService extends IService<FormTemplate> {

    FormTemplate getTemplateByBusId(Integer id);

    List<Map<String, Object>> getFileInfoByTmpId(Integer formId);

    Map<String, Object> getFileInfoByTmpId(Integer id,Integer appId);
}
