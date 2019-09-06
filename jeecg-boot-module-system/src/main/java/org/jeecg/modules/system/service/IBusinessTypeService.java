package org.jeecg.modules.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.BusinessType;
import org.jeecg.modules.system.model.BusinessTypeTreeModel;

import java.util.List;

/**
 * @Description: 业务类型
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
public interface IBusinessTypeService extends IService<BusinessType> {

    IPage<BusinessType> getList(Page page, String name, String byUserId);

    List<BusinessTypeTreeModel> queryTreeList();

    List<BusinessTypeTreeModel> searhBy(String keyWord);
}
