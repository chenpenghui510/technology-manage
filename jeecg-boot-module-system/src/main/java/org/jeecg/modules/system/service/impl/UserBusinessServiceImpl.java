package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.UserBusiness;
import org.jeecg.modules.system.mapper.UserBusinessMapper;
import org.jeecg.modules.system.service.IUserBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: sys_user_business
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
@Service
public class UserBusinessServiceImpl extends ServiceImpl<UserBusinessMapper, UserBusiness> implements IUserBusinessService {

    @Autowired
    UserBusinessMapper userBusinessMapper;

    @Override
    public List<UserBusiness> getEntityBy(String adminId, String bId) {
        return userBusinessMapper.getEntityBy(adminId, bId);
    }

    @Override
    public List<UserBusiness> findByBidAdminid(String businessId, String adminId) {
        return userBusinessMapper.findByBidAdminid(businessId, adminId);
    }

}
