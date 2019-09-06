package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysUserCompanySpecial;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUserCompanySubsidy;

/**
 * @Description: 重大专项
 * @Author: jeecg-boot
 * @Date:   2019-08-19
 * @Version: V1.0
 */
public interface ISysUserCompanySpecialService extends IService<SysUserCompanySpecial> {
    IPage<SysUserCompanySpecial> pageListNew(Page<SysUserCompanySpecial> page, String name, String level, String startTime, String endTime, String userId);


}
