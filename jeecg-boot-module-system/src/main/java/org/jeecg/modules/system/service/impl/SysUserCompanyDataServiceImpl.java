package org.jeecg.modules.system.service.impl;

import org.jeecg.modules.system.entity.SysUserCompanyData;
import org.jeecg.modules.system.mapper.SysUserCompanyDataMapper;
import org.jeecg.modules.system.service.ISysUserCompanyDataService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 企业信息
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
@Service
public class SysUserCompanyDataServiceImpl extends ServiceImpl<SysUserCompanyDataMapper, SysUserCompanyData> implements ISysUserCompanyDataService {
    @Resource
    SysUserCompanyDataMapper sysUserCompanyDataMapper;

    @Override
    public Map getCompanyManagerInfo(String id) {
        return sysUserCompanyDataMapper.getCompanyManagerInfo(id);
    }

    @Override
    public List<SysUserCompanyData> getUserList(String cardNumber, String oldCard) {
        return sysUserCompanyDataMapper.getUserList(cardNumber,oldCard);
    }
}
