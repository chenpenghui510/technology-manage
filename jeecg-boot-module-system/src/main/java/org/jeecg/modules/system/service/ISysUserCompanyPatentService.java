package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysUserCompanyPatent;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.vo.SysUserPatentCountVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 企业用户专利著作
 * @Author: jeecg-boot
 * @Date:   2019-08-16
 * @Version: V1.0
 */
public interface ISysUserCompanyPatentService extends IService<SysUserCompanyPatent> {


    Map<String,Object> countByUserId(String userId);


    List<SysUserPatentCountVo> sysUserPatentCountVo(Integer year);

    IPage<SysUserCompanyPatent> pageListNew(Page<SysUserCompanyPatent> page,String name,String sort,String level,String prize,String startTime,String endTime,String userId);
}
