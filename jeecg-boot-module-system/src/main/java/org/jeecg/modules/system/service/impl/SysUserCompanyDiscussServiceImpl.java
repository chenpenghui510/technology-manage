package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysUserCompanyDiscuss;
import org.jeecg.modules.system.entity.SysUserCompanySubsidy;
import org.jeecg.modules.system.mapper.SysUserCompanyDiscussMapper;
import org.jeecg.modules.system.service.ISysUserCompanyDiscussService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 研发中心
 * @Author: jeecg-boot
 * @Date:   2019-08-21
 * @Version: V1.0
 */
@Service
public class SysUserCompanyDiscussServiceImpl extends ServiceImpl<SysUserCompanyDiscussMapper, SysUserCompanyDiscuss> implements ISysUserCompanyDiscussService {

    @Resource
    private SysUserCompanyDiscussMapper sysUserCompanyDiscussMapper;

    public IPage<SysUserCompanyDiscuss> pageListNew(Page<SysUserCompanyDiscuss> page, String name, String sort, String level, String startTime, String endTime, String userId){
        return sysUserCompanyDiscussMapper.pageListNew(page,name,sort,level,startTime,endTime,userId);
    }
}
