package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.UserBusiness;

import java.util.List;

/**
 * @Description: sys_user_business
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
public interface UserBusinessMapper extends BaseMapper<UserBusiness> {

    List<UserBusiness> getEntityBy(String adminId, String bId);

    List<UserBusiness> findByBidAdminid(String businessId, String userId);
}
