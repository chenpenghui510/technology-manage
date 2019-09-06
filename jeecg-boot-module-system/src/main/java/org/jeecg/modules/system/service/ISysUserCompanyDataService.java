package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUserCompanyData;

import java.util.List;
import java.util.Map;

/**
 * @Description: 企业信息
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
public interface ISysUserCompanyDataService extends IService<SysUserCompanyData> {

    Map getCompanyManagerInfo(String id);

    List<SysUserCompanyData> getUserList(String cardNumber, String oldCard);
}
