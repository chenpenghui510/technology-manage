package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.ExpertGroup;

import java.util.List;

/**
 * @Description: expert_group
 * @Author: jeecg-boot
 * @Date:   2019-07-08
 * @Version: V1.0
 */
public interface ExpertGroupMapper extends BaseMapper<ExpertGroup> {

    List<ExpertGroup> findByName(@Param("groupName") String groupName);
}
