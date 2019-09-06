package org.jeecg.modules.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserCompanyPatent;
import org.jeecg.modules.system.entity.SysUserCompanySubsidy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 省市研发后补助
 * @Author: jeecg-boot
 * @Date:   2019-08-19
 * @Version: V1.0
 */
public interface SysUserCompanySubsidyMapper extends BaseMapper<SysUserCompanySubsidy> {

    IPage<SysUserCompanySubsidy> pageListNew(Page<SysUserCompanySubsidy> page, @Param(value = "name") String name, @Param(value = "sort") String sort, @Param(value = "level") String level, @Param(value = "startTime") String startTime, @Param(value = "endTime") String endTime, @Param(value = "userId")String userId);

}
