package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 资讯配置
 * @Author: jeecg-boot
 * @Date:   2019-07-11
 * @Version: V1.0
 */
public interface SysInformationMapper extends BaseMapper<SysInformation> {

    List<SysInformation> patentListByType(@Param("type") Integer type,@Param("text") String text);
    List<Map<String,Object>> getPatentListByType(@Param("type") Integer type);

    List<Map<String,Object>> getPatentConpanyList(@Param("type")  Integer type);

    List<Map<String,Object>> getPatentRotaryListByType( @Param("type")  Integer type);

    IPage<SysInformation> queryPageByTypeList(Page<SysInformation> page,@Param("type") String type,@Param("text") String text);

    IPage<SysInformation> initialList(Page<SysInformation> page,@Param("text") String text);
}
