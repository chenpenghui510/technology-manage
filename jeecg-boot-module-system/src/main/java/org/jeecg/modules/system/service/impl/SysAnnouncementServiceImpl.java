package org.jeecg.modules.system.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.mapper.SysMessageMapper;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.util.GainetUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
@Service("ISysAnnouncementService")
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements ISysAnnouncementService {

	@Resource
	private SysAnnouncementMapper sysAnnouncementMapper;
	
	@Resource
	private SysAnnouncementSendMapper sysAnnouncementSendMapper;

	@Resource
	private SysMessageMapper sysMessageMapper;
	@Resource
	private SysUserMapper sysUserMapper;
	@Transactional
	@Override
	public void saveAnnouncement(SysAnnouncement sysAnnouncement,String json) {
		if(sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
			sysAnnouncementMapper.insert(sysAnnouncement);
			if(sysAnnouncement.getIsExamine()==1){
				sysMsgAndSendOfAll(sysAnnouncement,json);
			}
		}else {//指定用户
			// 1.插入通告表记录
			sysAnnouncementMapper.insert(sysAnnouncement);
			// 2.插入用户通告阅读标记表记录
			String userId = sysAnnouncement.getUserIds();
			String[] userIds = userId.substring(0, (userId.length()-1)).split(",");
			String anntId = sysAnnouncement.getId();
			Date refDate = new Date();
			for(int i=0;i<userIds.length;i++) {
				if(sysAnnouncement.getIsExamine()==1&&("1".equals(sysAnnouncement.getMsgCategory())||"2".equals(sysAnnouncement.getMsgCategory()))){
					SysAnnouncementSend announcementSend = new SysAnnouncementSend();
					announcementSend.setAnntId(anntId);
					announcementSend.setUserId(userIds[i]);
					announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
					announcementSend.setReadTime(refDate);
					sysAnnouncementSendMapper.insert(announcementSend);
				}
				SysUser user=sysUserMapper.selectById(userIds[i]);
				//同步短信和邮件
				if(sysAnnouncement.getIsExamine()==1&&("3".equals(sysAnnouncement.getMsgCategory())||"4".equals(sysAnnouncement.getMsgCategory()))){
					sysMsgOfPhoneAndEmail(sysAnnouncement,user,json);
				}
			}
		}
		//因为站内信和消息只有一条所以放在循环外
		if(sysAnnouncement.getIsExamine()==1&&("1".equals(sysAnnouncement.getMsgCategory())||"2".equals(sysAnnouncement.getMsgCategory()))){
			sysMsgOfMail(sysAnnouncement);
		}
	}

	/**
	 * 同步所有用户
	 * @param sysAnnouncement
	 */
	public void sysMsgAndSendOfAll(SysAnnouncement sysAnnouncement,String json){
		//获所有用户
		List<SysUser> userList =sysUserMapper.getAllUserInfo();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		Date refDate = new Date();
		for(SysUser user:userList){
			SysMessage sysMessage=new SysMessage();

			sysMessage.setEsTitle(sysAnnouncement.getTitile());
			sysMessage.setEsContent(sysAnnouncement.getMsgContent());
			sysMessage.setEsSendTime(sysAnnouncement.getSendTime());
			sysMessage.setEsSendStatus("0");
			sysMessage.setEsSendNum(0);
			if(sysUser!=null){
				sysMessage.setCreateBy(sysUser.getUsername());
			}else{
				sysMessage.setCreateBy(sysAnnouncement.getCreateBy());
			}
			sysMessage.setCreateTime(new Date());
			sysMessage.setAnnouncementId(sysAnnouncement.getId());
			if("3".equals(sysAnnouncement.getMsgCategory())){//短信
				sysMessage.setEsType("1");
				if(user.getPhone()!=null&&user.getPhone()!=""){
					sysMessage.setEsReceiver(user.getPhone());
				}
				if(!GainetUtils.isEmpty(json)){
					JSONObject jsonObject = JSONObject.parseObject(json);
					if(!GainetUtils.isEmpty(user.getRealname())){
						jsonObject.put("userName",user.getRealname());
					}
					sysMessage.setEsParam(jsonObject.toJSONString());
				}
				sysMessageMapper.insert(sysMessage);
			}else if("4".equals(sysAnnouncement.getMsgCategory())){//邮箱
				sysMessage.setEsType("2");
				if(user.getEmail()!=null&&user.getEmail()!=""){
					sysMessage.setEsReceiver(user.getEmail());
				}
				sysMessageMapper.insert(sysMessage);
			}
			SysAnnouncementSend announcementSend = new SysAnnouncementSend();
			if("1".equals(sysAnnouncement.getMsgCategory())||"2".equals(sysAnnouncement.getMsgCategory())){//站内信//消息信
				announcementSend.setAnntId(sysAnnouncement.getId());
				announcementSend.setUserId(user.getId());
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				announcementSend.setReadTime(refDate);
				sysAnnouncementSendMapper.insert(announcementSend);
			}
		}
	}
	//同步通知和站内信
	public void  sysMsgOfMail(SysAnnouncement sysAnnouncement){
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		SysMessage sysMessage=new SysMessage();
		sysMessage.setEsTitle(sysAnnouncement.getTitile());
		sysMessage.setEsContent(sysAnnouncement.getMsgContent());
		sysMessage.setEsSendTime(sysAnnouncement.getSendTime());
		sysMessage.setEsSendStatus("0");
		sysMessage.setEsSendNum(0);
		if("ALL".equals(sysAnnouncement.getMsgType())){
			sysMessage.setEsReceiver("全体用户");
		}else{
			sysMessage.setEsReceiver("指定用户");
		}
		if(sysUser!=null){
			sysMessage.setCreateBy(sysUser.getUsername());
		}else{
			sysMessage.setCreateBy(sysAnnouncement.getCreateBy());
		}
		sysMessage.setCreateTime(new Date());
		sysMessage.setAnnouncementId(sysAnnouncement.getId());
		if("2".equals(sysAnnouncement.getMsgCategory())){//系统信息
			sysMessage.setEsType("5");
			sysMessageMapper.insert(sysMessage);
		}else if("1".equals(sysAnnouncement.getMsgCategory())){//通知公告
			sysMessage.setEsType("4");
			sysMessageMapper.insert(sysMessage);
		}
	}
	//同步邮件和短信到msg
	public void sysMsgOfPhoneAndEmail(SysAnnouncement sysAnnouncement,SysUser user,String  json){
		//审核通过后同步到消息表
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		SysMessage sysMessage=new SysMessage();
		sysMessage.setEsTitle(sysAnnouncement.getTitile());
		sysMessage.setEsContent(sysAnnouncement.getMsgContent());
		sysMessage.setEsSendTime(sysAnnouncement.getSendTime());
		sysMessage.setEsSendStatus(CommonConstant.NO_SEND);
		sysMessage.setEsSendNum(0);
		if(sysUser!=null){
			sysMessage.setCreateBy(sysUser.getUsername());
		}else{
			sysMessage.setCreateBy(sysAnnouncement.getCreateBy());
		}
		sysMessage.setCreateTime(new Date());
		sysMessage.setAnnouncementId(sysAnnouncement.getId());
		if("3".equals(sysAnnouncement.getMsgCategory())){//短信
			sysMessage.setEsType("1");
			if(user.getPhone()!=null&&user.getPhone()!=""){
				sysMessage.setEsReceiver(user.getPhone());
			}
			if(!GainetUtils.isEmpty(json)){
				JSONObject jsonObject = JSONObject.parseObject(json);
				if(!GainetUtils.isEmpty(user.getRealname())){
					jsonObject.put("userName",user.getRealname());
				}
				sysMessage.setEsParam(jsonObject.toJSONString());
			}
			sysMessageMapper.insert(sysMessage);
		}else if("4".equals(sysAnnouncement.getMsgCategory())){//邮件审核的
			sysMessage.setEsType("2");
			if(user.getEmail()!=null&&user.getEmail()!=""){
				sysMessage.setEsReceiver(user.getEmail());
			}
			sysMessageMapper.insert(sysMessage);
		}
	}
	/**
	 * @功能：编辑消息信息
	 */
	@Transactional
	@Override
	public boolean upDateAnnouncement(SysAnnouncement sysAnnouncement,String json) {
		// 1.更新系统信息表数据
		sysAnnouncementMapper.updateById(sysAnnouncement);
		if(sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
			//所有用户时同步到msg
			if(sysAnnouncement.getIsExamine()==2){
				sysMsgAndSendOfAll(sysAnnouncement,json);
			}
		}
		if(sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_UESR)) {
			// 2.补充新的通知用户数据
			String userId = sysAnnouncement.getUserIds();
			String[] userIds = userId.substring(0, (userId.length()-1)).split(",");
			String anntId = sysAnnouncement.getId();
			Date refDate = new Date();
			for(int i=0;i<userIds.length;i++) {
				LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
				queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
				queryWrapper.eq(SysAnnouncementSend::getUserId, userIds[i]);
				List<SysAnnouncementSend> announcementSends=sysAnnouncementSendMapper.selectList(queryWrapper);
				if(announcementSends.size()<=0) {
					if(sysAnnouncement.getIsExamine()==2&&("1".equals(sysAnnouncement.getMsgCategory())||"2".equals(sysAnnouncement.getMsgCategory()))){
						SysAnnouncementSend announcementSend = new SysAnnouncementSend();
						announcementSend.setAnntId(anntId);
						announcementSend.setUserId(userIds[i]);
						announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
						announcementSend.setReadTime(refDate);
						sysAnnouncementSendMapper.insert(announcementSend);
					}
				}
				SysUser user=sysUserMapper.selectById(userIds[i]);
				if(sysAnnouncement.getIsExamine()==2&&("3".equals(sysAnnouncement.getMsgCategory())||"4".equals(sysAnnouncement.getMsgCategory()))){
					sysMsgOfPhoneAndEmail(sysAnnouncement,user,json);
				}
			}
			// 3. 删除多余通知用户数据
			Collection<String> delUserIds = Arrays.asList(userIds);
			LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
			queryWrapper.notIn(SysAnnouncementSend::getUserId, delUserIds);
			queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
			sysAnnouncementSendMapper.delete(queryWrapper);
		}
		//同步通知和站内信
		if(sysAnnouncement.getIsExamine()==2&&("1".equals(sysAnnouncement.getMsgCategory())||"2".equals(sysAnnouncement.getMsgCategory()))){
			sysMsgOfMail(sysAnnouncement);
		}
		return true;
	}

	// @功能：流程执行完成保存消息通知
	@Override
	public void saveSysAnnouncement(String title, String msgContent) {
		SysAnnouncement announcement = new SysAnnouncement();
		announcement.setTitile(title);
		announcement.setMsgContent(msgContent);
		announcement.setSender("共享平台");
		announcement.setPriority(CommonConstant.PRIORITY_L);
		announcement.setMsgType(CommonConstant.MSG_TYPE_ALL);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(new Date());
		announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
		sysAnnouncementMapper.insert(announcement);
	}

	@Override
	public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, String userId,String msgCategory) {
		 return page.setRecords(sysAnnouncementMapper.querySysCementListByUserId(page, userId, msgCategory));
	}

}
