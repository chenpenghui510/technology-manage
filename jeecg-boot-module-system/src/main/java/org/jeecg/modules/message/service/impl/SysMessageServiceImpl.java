package org.jeecg.modules.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.system.base.service.impl.JeecgServiceImpl;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.mapper.SysMessageMapper;
import org.jeecg.modules.message.service.ISysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageServiceImpl extends JeecgServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {
    @Autowired
   private  SysMessageMapper sysMessageMapper;
    @Override
    public void updateMsgStatus(String id) {
        LambdaQueryWrapper<SysMessage> queryWrapper = new LambdaQueryWrapper<SysMessage>();
        queryWrapper.eq(SysMessage::getAnnouncementId, id);
        List<SysMessage> sysMessageList=sysMessageMapper.selectList(queryWrapper);
        for(SysMessage sys:sysMessageList){
            sysMessageMapper.deleteById(sys.getId());
        }
    }

    @Override
    public SysMessage getSysMessageInfo(String id) {
        return sysMessageMapper.getSysMessageInfo(id);
    }

    @Override
    public IPage<Map> findSendMessageByBid(Page page, String id) {


        return sysMessageMapper.findSendMessageByBid(page, id);
    }
}
