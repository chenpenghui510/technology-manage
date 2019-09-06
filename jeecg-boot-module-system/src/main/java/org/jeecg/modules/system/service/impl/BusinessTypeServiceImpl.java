package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.Business;
import org.jeecg.modules.system.entity.BusinessType;
import org.jeecg.modules.system.mapper.BusinessTypeMapper;
import org.jeecg.modules.system.model.BusinessTypeTreeModel;
import org.jeecg.modules.system.service.IBusinessService;
import org.jeecg.modules.system.service.IBusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description: 业务类型
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
@Service
public class BusinessTypeServiceImpl extends ServiceImpl<BusinessTypeMapper, BusinessType> implements IBusinessTypeService {

    @Autowired
    private BusinessTypeMapper businessTypeMapper;

    @Autowired
    private IBusinessService businessService;

    @Override
    public IPage<BusinessType> getList(Page page, String name, String byUserId) {
       return businessTypeMapper.getList(page, name,byUserId);
    }

    @Override
    public List<BusinessTypeTreeModel> queryTreeList() {
        LambdaQueryWrapper<BusinessType> query = new LambdaQueryWrapper<BusinessType>();
        query.eq(BusinessType::getStatus, 1);
        query.orderByAsc(BusinessType::getCreateTime);
        List<BusinessType> list = this.list(query);
        // 二级 树状数据
        List<BusinessTypeTreeModel> listResult = new ArrayList<>();
        for(BusinessType businessType : list){
            if(businessType == null){
                continue;
            }
            BusinessTypeTreeModel businessTypeTreeModel = new BusinessTypeTreeModel(businessType);
            LambdaQueryWrapper<Business> query2 = new LambdaQueryWrapper<Business>();
            query2.eq(Business::getBusinessType, businessType.getId());
            query2.eq(Business::getDelFlag, 0);
            query2.eq(Business::getGiveStatus, 2);//分配状态(0.未分配 1.已分配给部门 2.已分配给管理员)'
            query2.orderByAsc(Business::getCreateTime);
            List<Business> list2 = businessService.list(query2);
            if(list2 != null && list2.size() > 0){
                List<BusinessTypeTreeModel> listResult2 = new ArrayList<>();
                for(Business business : list2){
                    BusinessTypeTreeModel businessTypeTreeModel2 = new BusinessTypeTreeModel(business);
                    listResult2.add(businessTypeTreeModel2);
                }
                businessTypeTreeModel.setChildren(listResult2);
            }
            listResult.add(businessTypeTreeModel);
        }
        return listResult;
    }

    /**
     * 根据关键字搜索业务名称
     * @param keyWord
     * @return
     */
    public List<BusinessTypeTreeModel> searhBy(String keyWord){
        LambdaQueryWrapper<Business> query=new LambdaQueryWrapper<Business>();//模糊查询
        query.like(Business::getBusinessName, keyWord);
        BusinessTypeTreeModel businessTypeTreeModel=new BusinessTypeTreeModel();
        List<Business> businessList=businessService.list(query);
        List<BusinessTypeTreeModel> businessTypeTreeModels=new ArrayList<>();
        if(businessList!=null&&businessList.size()>0){
            for (Business business:businessList){
                businessTypeTreeModel=new BusinessTypeTreeModel(business);
                businessTypeTreeModel.setChildren(null);
                businessTypeTreeModels.add(businessTypeTreeModel);
            }
            return businessTypeTreeModels;
        }
        return null;
    }
}
