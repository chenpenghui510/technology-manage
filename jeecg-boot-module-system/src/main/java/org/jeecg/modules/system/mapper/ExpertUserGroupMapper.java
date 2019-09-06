package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.ExpertUserGroup;

import java.util.List;
import java.util.Map;

/**
 * @Description: expert_user_group
 * @Author: jeecg-boot
 * @Date:   2019-07-09
 * @Version: V1.0
 */
public interface ExpertUserGroupMapper extends BaseMapper<ExpertUserGroup> {

    ExpertUserGroup getByUserId(@Param("userId") String userId);

    boolean deleteByGid(@Param("groupId") String groupId);

    List<Map> getApplyByGroupId(@Param("groupId") Integer groupId);

    List<Map> getUserByGroupId(@Param("groupId") Integer groupId);
}
