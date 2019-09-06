package org.jeecg.modules.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserCompanySpecial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysUserCompanySubsidy;

/**
 * @Description: 重大专项
 * @Author: jeecg-boot
 * @Date:   2019-08-19
 * @Version: V1.0
 */
public interface SysUserCompanySpecialMapper extends BaseMapper<SysUserCompanySpecial> {
    IPage<SysUserCompanySpecial> pageListNew(Page<SysUserCompanySpecial> page, @Param(value = "name") String name,@Param(value = "level") String level, @Param(value = "startTime") String startTime, @Param(value = "endTime") String endTime, @Param(value = "userId")String userId);

}
