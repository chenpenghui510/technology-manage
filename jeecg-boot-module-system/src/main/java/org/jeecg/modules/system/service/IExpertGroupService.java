package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.ExpertGroup;

import java.util.List;

/**
 * @Description: expert_group
 * @Author: jeecg-boot
 * @Date:   2019-07-08
 * @Version: V1.0
 */
public interface IExpertGroupService extends IService<ExpertGroup> {

    List<ExpertGroup> findByName(String groupName);
}
