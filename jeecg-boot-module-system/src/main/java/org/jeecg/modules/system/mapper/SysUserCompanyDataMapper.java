package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysUserCompanyData;

/**
 * @Description: 企业信息
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
public interface SysUserCompanyDataMapper extends BaseMapper<SysUserCompanyData> {

    Map getCompanyManagerInfo(@Param("userId") String userId);

    List<SysUserCompanyData> getUserList(@Param("cardNumber")String cardNumber,@Param("oldCard") String oldCard);
}
