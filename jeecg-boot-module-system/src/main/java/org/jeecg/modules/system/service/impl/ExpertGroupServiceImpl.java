package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.ExpertGroup;
import org.jeecg.modules.system.mapper.ExpertGroupMapper;
import org.jeecg.modules.system.service.IExpertGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: expert_group
 * @Author: jeecg-boot
 * @Date:   2019-07-08
 * @Version: V1.0
 */
@Service
public class ExpertGroupServiceImpl extends ServiceImpl<ExpertGroupMapper, ExpertGroup> implements IExpertGroupService {

    @Autowired
    private ExpertGroupMapper expertGroupMapper;

    @Override
    public List<ExpertGroup> findByName(String groupName) {
        List<ExpertGroup> list = expertGroupMapper.findByName(groupName);
        return list;
    }
}
