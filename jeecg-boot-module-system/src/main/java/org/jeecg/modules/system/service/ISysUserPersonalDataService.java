package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysUserPersonalData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 个人信息完善
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
public interface ISysUserPersonalDataService extends IService<SysUserPersonalData> {

    boolean editPersonal(String userId, String expertName,String expertSex,String cardNumber,String tel,String testEmail,String administrativePost,String professionalTitles,String img);

    Map<String,Object> personalData(String userId);
}
