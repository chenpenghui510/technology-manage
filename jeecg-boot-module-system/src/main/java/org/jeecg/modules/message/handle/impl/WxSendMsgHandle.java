package org.jeecg.modules.message.handle.impl;

import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.ISendMsgHandle;

public class WxSendMsgHandle implements ISendMsgHandle {

	@Override
	public void SendMsg(String es_receiver, String es_title, String es_content , SysMessage sysMessage) {
		// TODO Auto-generated method stub
		System.out.println("发微信消息模板");
	}

}
