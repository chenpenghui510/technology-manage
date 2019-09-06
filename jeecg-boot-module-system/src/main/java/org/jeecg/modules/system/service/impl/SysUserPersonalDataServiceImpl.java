package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserPersonalData;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.mapper.SysUserPersonalDataMapper;
import org.jeecg.modules.system.service.ISysUserPersonalDataService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.GainetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 个人信息完善
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
@Service
public class SysUserPersonalDataServiceImpl extends ServiceImpl<SysUserPersonalDataMapper, SysUserPersonalData> implements ISysUserPersonalDataService {

    @Autowired
    private SysUserPersonalDataMapper sysUserPersonalDataMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    public boolean editPersonal(String userId,String expertName,String expertSex,String cardNumber,String tel,String testEmail,String administrativePost,String professionalTitles,String img){

        try{
        QueryWrapper<SysUserPersonalData> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<SysUserPersonalData> sysUserPersonalDatas=list(queryWrapper);
        SysUserPersonalData sysUserPersonalData=new SysUserPersonalData();
        int a=0;
        if(sysUserPersonalDatas!=null&&sysUserPersonalDatas.size()>0){
            sysUserPersonalData=sysUserPersonalDatas.get(0);
            a=1;
        }
        sysUserPersonalData.setUserId(userId);
        sysUserPersonalData.setPersonalSex(GainetUtils.intOf(expertSex,0));
        sysUserPersonalData.setIdentityCardNumber(GainetUtils.filterStr(cardNumber));
        sysUserPersonalData.setTel(GainetUtils.filterStr(tel));
        sysUserPersonalData.setPost(administrativePost);
        sysUserPersonalData.setProfessionalTitles(professionalTitles);
        sysUserPersonalData.setIdentityCardImg(img);
            QueryWrapper<SysUser> sysUserQueryWrapper=new QueryWrapper<>();
            sysUserQueryWrapper.eq("id",userId);
            List<SysUser> sysUsers=sysUserService.list(sysUserQueryWrapper);
            if(sysUsers==null||sysUsers.size()==0){
                return false;
            }
            SysUser sysUser=sysUsers.get(0);
            if(sysUser==null){
                return false;
            }
            if(sysUser.getStatus()==3||sysUser.getStatus()==5){//初次完善和被驳回，提交会变成待审核
                sysUser.setStatus(4);
            }
            sysUser.setEmail(testEmail);
            sysUser.setRealname(expertName);
            int c=sysUserMapper.updateById(sysUser);
        if(a==1){//修改
            int b=sysUserPersonalDataMapper.updateById(sysUserPersonalData);
        }else{//新增
            int b=sysUserPersonalDataMapper.insert(sysUserPersonalData);
        }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }
    public Map<String,Object> personalData(String userId){
        return sysUserPersonalDataMapper.personalData(userId);
    }
}
