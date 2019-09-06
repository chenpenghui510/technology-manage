package org.jeecg.modules.message.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.message.entity.SysMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
public interface SysMessageMapper extends BaseMapper<SysMessage> {

    SysMessage getSysMessageInfo(@Param("id") String id);
    Integer falseMessageCount(String code);

    Integer sixFalseMessageCount(String code);

    IPage<Map> findSendMessageByBid(Page page, @Param("businessId") String id);
}
