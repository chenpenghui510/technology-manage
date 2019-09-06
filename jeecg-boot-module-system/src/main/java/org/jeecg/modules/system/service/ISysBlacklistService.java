package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysBlacklist;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 黑名单
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
public interface ISysBlacklistService extends IService<SysBlacklist> {

    boolean saveBlack(String userId,Integer black,String reason,String startTime,String endTime);

    boolean inBlackTime(String userId,String time);

}
