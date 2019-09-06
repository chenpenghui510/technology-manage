package org.jeecg.modules.message.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.ISendMsgHandle;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.handle.enums.SendMsgTypeEnum;
import org.jeecg.modules.message.mapper.SysMessageMapper;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.util.GainetUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 发送消息任务
 */

@Slf4j
public class SendMsgJob implements Job {

	@Autowired
	private ISysMessageService sysMessageService;

	@Autowired
	private ISysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysMessageMapper sysMessageMapper;
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		log.info(String.format(" Jeecg-Boot 发送消息任务 SendMsgJob !  时间:" + DateUtils.getTimestamp()));

		// 1.读取消息中心数据，只查询未发送的和发送失败不超过次数的
		QueryWrapper<SysMessage> queryWrapper = new QueryWrapper<SysMessage>();
		queryWrapper.eq("es_send_status", SendMsgStatusEnum.WAIT.getCode())
				.or(i -> i.eq("es_send_status", SendMsgStatusEnum.FAIL.getCode()).lt("es_send_num", 6));
		List<SysMessage> sysMessages = sysMessageService.list(queryWrapper);
		System.out.println(sysMessages);

        List<String> announcementList=new ArrayList<>();
        String aCode="";
		// 2.根据不同的类型走不通的发送实现类
		for (SysMessage sysMessage : sysMessages) {
			ISendMsgHandle sendMsgHandle = null;
			if(sysMessage.getEsType()==null){
				log.info("定时发送消息记录状态异常，编号："+sysMessage.getId());
				continue;
			}else if (sysMessage.getEsType().equals("4") || sysMessage.getEsType().equals("5")) {
				QueryWrapper<SysAnnouncement> sysAnnouncementQueryWrapper = new QueryWrapper<SysAnnouncement>();
				sysAnnouncementQueryWrapper.eq("id", sysMessage.getAnnouncementId());
				List<SysAnnouncement> sysAnnouncements = sysAnnouncementService.list(sysAnnouncementQueryWrapper);
				if(sysAnnouncements!=null&&sysAnnouncements.size()>0){
					SysAnnouncement sysAnnouncement=sysAnnouncements.get(0);
					sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);//已发布
					sysAnnouncement.setSendTime(new Date());
					boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
					Integer sendNum = sysMessage.getEsSendNum();
					if (sendNum == null) {
						sendNum = 0;
					}
					sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
					sysMessage.setEsSendNum(++sendNum);
					// 发送结果回写到数据库
					sysMessageService.updateById(sysMessage);
				}else{
					log.info("发送站内信方法异常，原因：根据实体sysMessage获取父表SysAnnouncement为空");
					return;
				}
			} else {
				try {
					if (sysMessage.getEsType().equals(SendMsgTypeEnum.EMAIL.getType())) {
						String className = SendMsgTypeEnum.EMAIL.getImplClass();
						sendMsgHandle = (ISendMsgHandle) Class.forName(className).newInstance();
					} else if (sysMessage.getEsType().equals(SendMsgTypeEnum.SMS.getType())) {
						sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.SMS.getImplClass()).newInstance();
					} else if (sysMessage.getEsType().equals(SendMsgTypeEnum.WX.getType())) {
						sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.WX.getImplClass()).newInstance();
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				Integer sendNum = sysMessage.getEsSendNum();
				if (sendNum == null) {
					sendNum = 0;
				}
				try {
					sendMsgHandle.SendMsg(sysMessage.getEsReceiver(), sysMessage.getEsTitle(),
							sysMessage.getEsContent().toString(), sysMessage);
					// 发送消息成功
					sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
				} catch (Exception e) {
					e.printStackTrace();
					// 发送消息出现异常
					sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
				}
				sysMessage.setEsSendNum(++sendNum);
				// 发送结果回写到数据库
				sysMessageService.updateById(sysMessage);
                /*QueryWrapper<SysAnnouncement> sysAnnouncementQueryWrapper = new QueryWrapper<SysAnnouncement>();
                sysAnnouncementQueryWrapper.eq("id", sysMessage.getAnnouncementId());
                List<SysAnnouncement> sysAnnouncements = sysAnnouncementService.list(sysAnnouncementQueryWrapper);
                if(sysAnnouncements!=null&&sysAnnouncements.size()>0) {
                    SysAnnouncement sysAnnouncement = sysAnnouncements.get(0);
                    sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);//已发布
                    sysAnnouncement.setSendTime(new Date());
                    boolean ok = sysAnnouncementService.updateById(sysAnnouncement);*/
                }
                //取出父表的标识,存入集合中
            if(aCode!=sysMessage.getAnnouncementId()){
                announcementList.add(sysMessage.getAnnouncementId());
                aCode=sysMessage.getAnnouncementId();
            }
		}
		//如果有失败的发送记录 就把父表状态标记为发送中
		if(announcementList!=null&&announcementList.size()>0){
		    for (int i=0;i<announcementList.size();i++){
		        String aId=announcementList.get(i);
		        SysAnnouncement sysAnnouncement=sysAnnouncementService.getById(aId);
                Integer aCount=sysMessageMapper.falseMessageCount(aId);
		        if(GainetUtils.intOf(aCount,0)>0){
                    sysAnnouncement.setSendStatus("5");
                }else{
                    sysAnnouncement.setSendStatus("1");
                }
                Integer bCount=sysMessageMapper.sixFalseMessageCount(aId);
		        if(GainetUtils.intOf(bCount,0)>0){
                    sysAnnouncement.setSendStatus("6");
                }
                sysAnnouncement.setSendTime(new Date());
                sysAnnouncement.setSender(sysAnnouncement.getCreateBy());
                boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
            }
        }
	}

}
