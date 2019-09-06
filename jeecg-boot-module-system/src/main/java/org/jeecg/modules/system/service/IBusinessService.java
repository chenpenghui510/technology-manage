package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.Business;

import java.util.List;
import java.util.Map;

/**
 * @Description: 业务表
 * @Author: jeecg-boot
 * @Date:   2019-06-28
 * @Version: V1.0
 */
public interface IBusinessService extends IService<Business> {

    boolean removeForDpt(Business business);

    boolean removeForAdmin(Business business);


    IPage<Map> getPageList(Page<Map> page,String businessName,String status, String businessLevel,String department,String businessType,String delFlag);

    IPage<Business> getPageList2(Page<Business> page,String name, String dpetId, String adminId, String giveStatus);


    List<Business> businessListByType(String valueOf);

    Map<String,Object> searchBusinessDetailsById(Integer id);
}
