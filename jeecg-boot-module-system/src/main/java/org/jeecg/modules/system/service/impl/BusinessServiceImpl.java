package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.Business;
import org.jeecg.modules.system.mapper.BusinessMapper;
import org.jeecg.modules.system.service.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 业务表
 * @Author: jeecg-boot
 * @Date:   2019-06-28
 * @Version: V1.0
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business> implements IBusinessService {

    @Autowired
    private BusinessMapper businessMapper;

    @Override
    public boolean removeForDpt(Business business) {
        business.setGiveStatus(0);
        business.setDpetId("");
        business.setUpdateTime(new Date());
        return saveOrUpdate(business);
    }


    @Override
    public boolean removeForAdmin(Business business) {
        business.setGiveStatus(1);
        business.setUpdateTime(new Date());
        return saveOrUpdate(business);
    }

    @Override
    public IPage<Map> getPageList(Page<Map> page,  String businessName,String status, String businessLevel,String department,String businessType,String delFlag) {
        IPage<Map> list = businessMapper.getPageList(page, businessName, status, businessLevel,department,businessType,delFlag);
        return list;
    }

    @Override
    public IPage<Business> getPageList2(Page<Business> page, String name, String dpetId,String adminId, String giveStatus) {
        IPage<Business> list = businessMapper.getPageList2(page, name, dpetId, adminId, giveStatus);
        return list;
    }

    @Override
    public List<Business> businessListByType(String typeId) {
        return businessMapper.businessListByType(typeId);
    }

    @Override
    public Map<String,Object> searchBusinessDetailsById(Integer id){
        return businessMapper.searchBusinessDetailsById(id);
    }


}
