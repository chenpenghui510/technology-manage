package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysBlacklist;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 黑名单
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
public interface SysBlacklistMapper extends BaseMapper<SysBlacklist> {

    Integer inBlackTime(@Param(value = "userId") String userId,@Param(value = "time")String time);
}
