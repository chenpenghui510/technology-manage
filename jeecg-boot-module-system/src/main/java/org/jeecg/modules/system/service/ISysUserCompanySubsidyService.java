package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysUserCompanySubsidy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 省市研发后补助
 * @Author: jeecg-boot
 * @Date:   2019-08-19
 * @Version: V1.0
 */
public interface ISysUserCompanySubsidyService extends IService<SysUserCompanySubsidy> {

    IPage<SysUserCompanySubsidy> pageListNew(Page<SysUserCompanySubsidy> page, String name, String sort, String level,String startTime, String endTime, String userId);
}
