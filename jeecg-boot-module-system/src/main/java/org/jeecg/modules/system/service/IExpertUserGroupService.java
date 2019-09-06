package org.jeecg.modules.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.ExpertUserGroup;

import java.util.List;
import java.util.Map;

/**
 * @Description: expert_user_group
 * @Author: jeecg-boot
 * @Date:   2019-07-09
 * @Version: V1.0
 */
public interface IExpertUserGroupService extends IService<ExpertUserGroup> {

    ExpertUserGroup getByUserId(String userId);

    boolean deleteByGid(String gid);

    List<Map> getApplyByGroupId(Integer groupId);

    List<Map> getUserByGroupId(Integer groupId);

}
