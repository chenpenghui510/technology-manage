package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.annotation.Param;
import org.jeecg.modules.system.entity.Business;

import java.util.List;
import java.util.Map;

/**
 * @Description: 业务表
 * @Author: jeecg-boot
 * @Date:   2019-06-28
 * @Version: V1.0
 */
public interface BusinessMapper extends BaseMapper<Business> {

    IPage<Map> getPageList(Page page, @Param("businessName") String businessName,
                           @Param("status")String status, @Param("businessLevel") String businessLevel,@Param("department")String department,@Param("businessType")String businessType, @Param("delFlag")String delFlag);


    IPage<Business> getPageList2(Page page, @Param("businessName") String businessName, @Param("dpetId") String dpetId,
                                 @Param("byUserId") String byUserId, @Param("status")String status);

    List<Business> businessListByType(@Param("typeId")  String typeId);

    Map<String,Object> searchBusinessDetailsById(@Param("id")Integer id);
}
