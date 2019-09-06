package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysInformation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 资讯配置
 * @Author: jeecg-boot
 * @Date:   2019-07-11
 * @Version: V1.0
 */
public interface ISysInformationService extends IService<SysInformation> {

    List<SysInformation> patentListByType(Integer valueOf,String content);
    List<Map<String,Object>> getPatentListByType(String type);

    List<Map<String,Object>> getPatentConpanyList(String type);

    List<Map<String,Object>> getPatentRotaryListByType(String type);

    IPage<SysInformation> queryPageByTypeList(Page<SysInformation> page, String type, String content);

    IPage<SysInformation> initialList(Page<SysInformation> page, String content);
}
