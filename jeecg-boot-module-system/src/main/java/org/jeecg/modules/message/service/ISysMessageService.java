package org.jeecg.modules.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.message.entity.SysMessage;

import java.util.Map;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageService extends JeecgService<SysMessage> {
   void  updateMsgStatus(String id);
   SysMessage getSysMessageInfo(String id);

    IPage<Map> findSendMessageByBid(Page page, String id);
}
