package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysUserCompanySubsidy;
import org.jeecg.modules.system.mapper.SysUserCompanySubsidyMapper;
import org.jeecg.modules.system.service.ISysUserCompanySubsidyService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 省市研发后补助
 * @Author: jeecg-boot
 * @Date:   2019-08-19
 * @Version: V1.0
 */
@Service
public class SysUserCompanySubsidyServiceImpl extends ServiceImpl<SysUserCompanySubsidyMapper, SysUserCompanySubsidy> implements ISysUserCompanySubsidyService {

    @Resource
    private SysUserCompanySubsidyMapper sysUserCompanySubsidyMapper;

    public IPage<SysUserCompanySubsidy> pageListNew(Page<SysUserCompanySubsidy> page, String name, String sort, String level, String startTime, String endTime, String userId){
        return sysUserCompanySubsidyMapper.pageListNew(page,name,sort,level,startTime,endTime,userId);
    }
}
