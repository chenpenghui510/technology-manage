package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.UserDept;

import java.util.List;

/**
 * @Description: 用户-部门-业务表
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
public interface UserDeptMapper extends BaseMapper<UserDept> {

    List<String> getBusinessIds(String depId);
}
