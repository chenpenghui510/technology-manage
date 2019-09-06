package org.jeecg.modules.system.mapper;

import org.jeecg.modules.system.entity.TechnologyUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


public interface TechnologyUserMapper extends BaseMapper<TechnologyUser> {
    /**
     * 管理员用户信息
     * @param page
     * @param
     * @return
     */
    IPage<TechnologyUser> sysUserList(Page<TechnologyUser> page, @Param("username") String username , @Param("name") String name, @Param("userState") String userState);
}
