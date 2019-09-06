package org.jeecg.modules.message.handle.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.ISendMsgHandle;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
@Component
public class ZnxSendMsgHandle  implements ISendMsgHandle{
	@Resource
	private ISysAnnouncementService sysAnnouncementService;

	@Resource
	private ISysMessageService sysMessageService;
	@Resource
	private SysAnnouncementMapper sysAnnouncementMapper;
	@Override
	public void SendMsg(String es_receiver, String es_title, String es_content , SysMessage sysMessage) {
		// TODO Auto-generated method stub
		System.out.println("发站内信");
		//SysAnnouncement
		if(sysMessage==null){
			log.info("发送站内信方法异常，原因：传入实体sysMessage为空");
			return;
		}
		/*if(sysAnnouncementService==null){
			sysAnnouncementService=new ISysAnnouncementService
		}*/
		QueryWrapper<SysAnnouncement> queryWrapper = new QueryWrapper<SysAnnouncement>();
		queryWrapper.eq("id", sysMessage.getAnnouncementId());
		List<SysAnnouncement> sysAnnouncements = sysAnnouncementService.list(queryWrapper);
		if(sysAnnouncements!=null&&sysAnnouncements.size()>0){
			SysAnnouncement sysAnnouncement=sysAnnouncements.get(0);
			sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);//发布中
			sysAnnouncement.setSendTime(new Date());
			boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
		}else{
			log.info("发送站内信方法异常，原因：根据实体sysMessage获取父表SysAnnouncement为空");
			return;
		}
	}

}
