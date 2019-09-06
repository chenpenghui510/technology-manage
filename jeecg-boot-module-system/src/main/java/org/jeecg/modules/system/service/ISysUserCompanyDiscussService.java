package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysUserCompanyDiscuss;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUserCompanySubsidy;

/**
 * @Description: 研发中心
 * @Author: jeecg-boot
 * @Date:   2019-08-21
 * @Version: V1.0
 */
public interface ISysUserCompanyDiscussService extends IService<SysUserCompanyDiscuss> {
    IPage<SysUserCompanyDiscuss> pageListNew(Page<SysUserCompanyDiscuss> page, String name, String sort, String level, String startTime, String endTime, String userId);

}
