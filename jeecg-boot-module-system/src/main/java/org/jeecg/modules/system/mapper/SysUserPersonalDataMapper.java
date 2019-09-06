package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserPersonalData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 个人信息完善
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
public interface SysUserPersonalDataMapper extends BaseMapper<SysUserPersonalData> {

    Map<String,Object> personalData(@Param("id") String userId);

}
