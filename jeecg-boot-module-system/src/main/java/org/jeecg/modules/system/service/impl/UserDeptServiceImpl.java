package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.UserDept;
import org.jeecg.modules.system.mapper.UserDeptMapper;
import org.jeecg.modules.system.service.IUserDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description: 用户-部门-业务表
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
@Service
public class UserDeptServiceImpl extends ServiceImpl<UserDeptMapper, UserDept> implements IUserDeptService {

    @Autowired
    private UserDeptMapper userDeptMapper;

    @Override
    public List<String> getBusinessIds(String depId) {

        return userDeptMapper.getBusinessIds(depId);

    }
}
