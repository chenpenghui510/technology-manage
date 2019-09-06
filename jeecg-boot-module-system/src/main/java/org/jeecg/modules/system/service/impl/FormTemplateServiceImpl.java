package org.jeecg.modules.system.service.impl;

import org.jeecg.modules.system.entity.FormTemplate;
import org.jeecg.modules.system.mapper.FormTemplateMapper;
import org.jeecg.modules.system.service.IFormTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 表单模板
 * @Author: jeecg-boot
 * @Date:   2019-07-12
 * @Version: V1.0
 */
@Service
public class FormTemplateServiceImpl extends ServiceImpl<FormTemplateMapper, FormTemplate> implements IFormTemplateService {
    @Resource
    private FormTemplateMapper formTemplateMapper;
    @Override
    public FormTemplate getTemplateByBusId(Integer id) {
        return formTemplateMapper.getTemplateByBusId(id);
    }

    @Override
    public List<Map<String, Object>> getFileInfoByTmpId(Integer formId) {
        return formTemplateMapper.getFileInfoByTmpId(formId);
    }

    @Override
    public Map<String, Object> getFileInfoByTmpId(Integer id,Integer appId) {
        return formTemplateMapper.getFileInfoByAppId(id,appId);
    }
}
