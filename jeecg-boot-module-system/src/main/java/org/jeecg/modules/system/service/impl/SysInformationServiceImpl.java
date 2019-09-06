package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysInformation;
import org.jeecg.modules.system.mapper.SysInformationMapper;
import org.jeecg.modules.system.mapper.SysLogMapper;
import org.jeecg.modules.system.service.ISysInformationService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 资讯配置
 * @Author: jeecg-boot
 * @Date:   2019-07-11
 * @Version: V1.0
 */
@Service
public class SysInformationServiceImpl extends ServiceImpl<SysInformationMapper, SysInformation> implements ISysInformationService {
    @Resource
    private SysInformationMapper sysInformationMapper;
    @Override
    public List<SysInformation> patentListByType(Integer type,String content) {

        return sysInformationMapper.patentListByType(type,content);
    }

    @Override
    public List<Map<String,Object>> getPatentListByType(String type) {
        return sysInformationMapper.getPatentListByType(Integer.valueOf(type));
    }

    @Override
    public List<Map<String,Object>> getPatentConpanyList(String type) {
        return sysInformationMapper.getPatentConpanyList(Integer.valueOf(type));
    }
    @Override
    public List<Map<String,Object>> getPatentRotaryListByType(String type) {
        return sysInformationMapper.getPatentRotaryListByType(Integer.valueOf(type));
    }

    @Override
    public IPage<SysInformation> queryPageByTypeList(Page<SysInformation> page, String type, String content) {
        return sysInformationMapper.queryPageByTypeList(page,  type,  content);
    }

    @Override
    public IPage<SysInformation> initialList(Page<SysInformation> page, String content) {
        return sysInformationMapper.initialList(page,  content);
    }
}
