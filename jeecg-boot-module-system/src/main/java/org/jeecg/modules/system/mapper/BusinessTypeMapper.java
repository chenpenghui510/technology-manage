package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.annotation.Param;
import org.jeecg.modules.system.entity.BusinessType;


/**
 * @Description: 业务类型
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
public interface BusinessTypeMapper extends BaseMapper<BusinessType> {

    IPage<BusinessType> getList(Page page, @Param("name") String name, @Param("byUserId") String byUserId);
}
