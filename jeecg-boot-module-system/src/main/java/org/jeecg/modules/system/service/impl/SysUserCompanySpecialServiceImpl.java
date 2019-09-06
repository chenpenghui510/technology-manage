package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysUserCompanySpecial;
import org.jeecg.modules.system.entity.SysUserCompanySubsidy;
import org.jeecg.modules.system.mapper.SysUserCompanySpecialMapper;
import org.jeecg.modules.system.service.ISysUserCompanySpecialService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 重大专项
 * @Author: jeecg-boot
 * @Date:   2019-08-19
 * @Version: V1.0
 */
@Service
public class SysUserCompanySpecialServiceImpl extends ServiceImpl<SysUserCompanySpecialMapper, SysUserCompanySpecial> implements ISysUserCompanySpecialService {

    @Resource
    private SysUserCompanySpecialMapper sysUserCompanySpecialMapper;

    public IPage<SysUserCompanySpecial> pageListNew(Page<SysUserCompanySpecial> page, String name, String level, String startTime, String endTime, String userId){
        return sysUserCompanySpecialMapper.pageListNew(page,name,level,startTime,endTime,userId);
    }

}
