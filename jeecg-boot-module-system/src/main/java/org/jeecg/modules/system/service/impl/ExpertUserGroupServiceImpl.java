package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.ExpertUserGroup;
import org.jeecg.modules.system.mapper.ExpertUserGroupMapper;
import org.jeecg.modules.system.service.IExpertUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: expert_user_group
 * @Author: jeecg-boot
 * @Date:   2019-07-09
 * @Version: V1.0
 */
@Service
public class ExpertUserGroupServiceImpl extends ServiceImpl<ExpertUserGroupMapper, ExpertUserGroup> implements IExpertUserGroupService {

    @Autowired
    private ExpertUserGroupMapper expertUserGroupMapper;

    @Override
    public ExpertUserGroup getByUserId(String userId) {
        return  expertUserGroupMapper.getByUserId(userId);
    }

    @Override
    public boolean deleteByGid(String groupId) {
        return expertUserGroupMapper.deleteByGid(groupId);
    }

    @Override
    public List<Map> getApplyByGroupId(Integer groupId){
        return expertUserGroupMapper.getApplyByGroupId(groupId);
    }

    @Override
    public List<Map> getUserByGroupId(Integer groupId){
        return expertUserGroupMapper.getUserByGroupId(groupId);
    }
}
