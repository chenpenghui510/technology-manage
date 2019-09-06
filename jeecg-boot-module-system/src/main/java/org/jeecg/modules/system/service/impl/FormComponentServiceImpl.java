package org.jeecg.modules.system.service.impl;

import org.jeecg.modules.system.entity.FormComponent;
import org.jeecg.modules.system.mapper.FormComponentMapper;
import org.jeecg.modules.system.service.IFormComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 表单组件
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
@Service
public class FormComponentServiceImpl extends ServiceImpl<FormComponentMapper, FormComponent> implements IFormComponentService {
    @Autowired
    private FormComponentMapper formComponentMapper;

    @Override
    public List<Map<String,Object>> listByForm(Integer formId){
        List<Map<String,Object>> rstList = formComponentMapper.listByForm(formId);
        if(rstList!=null&&rstList.size()>0){
            //处理单选、多选中的选项
            for(Map<String,Object> map : rstList){
                String type=map.get("type").toString();
                if("3".equals(type)||"4".equals(type)||"5".equals(type)){
                    String options = map.get("selection_option")==null?"":map.get("selection_option").toString();
                    List<String> optionList = new ArrayList<String>();
                    for(int i=0;i<options.split(",").length;i++){
                        if(!"".equals(options.split(",")[i])){
                            optionList.add(options.split(",")[i]);
                        }
                    }
                    map.put("options",optionList);
                }
            }
        }
        return rstList;
    }
}
