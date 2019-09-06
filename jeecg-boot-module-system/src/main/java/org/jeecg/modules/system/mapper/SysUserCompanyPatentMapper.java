package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserCompanyPatent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 企业用户专利著作
 * @Author: jeecg-boot
 * @Date:   2019-08-16
 * @Version: V1.0
 */
public interface SysUserCompanyPatentMapper extends BaseMapper<SysUserCompanyPatent> {

    Integer countByUserIdAndSort(@Param(value = "userId") String userId,@Param(value = "sort") Integer sort);

    //年度
    List<Integer> yearList();

    List<Map<String,Object>> userList();

    //数量
    Integer censusCount(@Param(value = "year") Integer year,@Param(value = "sort") String sort,@Param(value = "level") String level,@Param(value = "prize") String prize,@Param(value = "user_id") String user_id);

    IPage<SysUserCompanyPatent> pageListNew(Page<SysUserCompanyPatent> page, @Param(value = "name") String name,@Param(value = "sort") String sort,@Param(value = "level") String level,@Param(value = "prize") String prize,@Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime,@Param(value = "userId")String userId);
}
