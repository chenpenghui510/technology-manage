package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysBlacklist;
import org.jeecg.modules.system.mapper.SysBlacklistMapper;
import org.jeecg.modules.system.service.ISysBlacklistService;
import org.jeecg.modules.system.util.GainetUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: 黑名单
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
@Service
@Slf4j
public class SysBlacklistServiceImpl extends ServiceImpl<SysBlacklistMapper, SysBlacklist> implements ISysBlacklistService {

    @Resource
    private SysBlacklistMapper sysBlacklistMapper;

    public boolean saveBlack(String userId,Integer black,String reason,String startTime,String endTime){
        try {
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //先查有没有拉黑记录
            QueryWrapper<SysBlacklist> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("user_id",userId);
            List<SysBlacklist> sysBlacklists=list(queryWrapper);
            SysBlacklist sysBlacklist=new SysBlacklist();
            if(sysBlacklists!=null&&sysBlacklists.size()>0){//4种情况，1.在黑名单加入。2,在黑名单剔除。3.不在黑名单加入。4，不在黑名单剔除
                sysBlacklist=sysBlacklists.get(0);
                if(black==1){
                    sysBlacklist.setReason(reason);
                    sysBlacklist.setUpdateTime(new Date());
                    Date stDate=format1.parse(startTime);
                    Date etDate=format1.parse(endTime);
                    sysBlacklist.setStartTime(stDate);
                    sysBlacklist.setEndTime(etDate);
                    sysBlacklistMapper.updateById(sysBlacklist);
                }else{
                    sysBlacklistMapper.deleteById(sysBlacklist.getId());
                }
            }else{
                if(black==1) {
                    sysBlacklist.setUserId(userId);
                    sysBlacklist.setReason(reason);
                    sysBlacklist.setUpdateTime(new Date());
                    Date stDate = format1.parse(startTime);
                    Date etDate = format1.parse(endTime);
                    sysBlacklist.setStartTime(stDate);
                    sysBlacklist.setEndTime(etDate);
                    sysBlacklistMapper.insert(sysBlacklist);
                }else{
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean inBlackTime(String userId,String time){
        Integer count=sysBlacklistMapper.inBlackTime(userId,time);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }
}
