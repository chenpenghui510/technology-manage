package org.jeecg.modules.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserCompanyDiscuss;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysUserCompanySubsidy;

/**
 * @Description: 研发中心
 * @Author: jeecg-boot
 * @Date:   2019-08-21
 * @Version: V1.0
 */
public interface SysUserCompanyDiscussMapper extends BaseMapper<SysUserCompanyDiscuss> {
    IPage<SysUserCompanyDiscuss> pageListNew(Page<SysUserCompanyDiscuss> page, @Param(value = "name") String name, @Param(value = "sort") String sort, @Param(value = "level") String level, @Param(value = "startTime") String startTime, @Param(value = "endTime") String endTime, @Param(value = "userId")String userId);

}
