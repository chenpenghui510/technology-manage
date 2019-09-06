package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserExpertData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 专家用户
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
public interface SysUserExpertDataMapper extends BaseMapper<SysUserExpertData> {

    /**
     * 查询个人用户附件
     * @param id
     * @return
     */
    List<Map<String,String>> getExpertFiles(@Param("id") String id);

    Map<String,Object> doCheckRegion(@Param("id") Integer id);

    String getRegionById(@Param("regionId")Integer regionId);
}
