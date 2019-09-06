package org.jeecg.modules.message.handle;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.message.entity.SysMessage;

public interface ISendMsgHandle{

	void SendMsg(String es_receiver, String es_title, String es_content, SysMessage sysMessage);
}
