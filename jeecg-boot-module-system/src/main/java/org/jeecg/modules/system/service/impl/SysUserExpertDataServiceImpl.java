package org.jeecg.modules.system.service.impl;

import org.jeecg.modules.system.entity.PublicUploadAnnex;
import org.jeecg.modules.system.entity.SysUserExpertData;
import org.jeecg.modules.system.mapper.SysUserExpertDataMapper;
import org.jeecg.modules.system.service.IPublicUploadAnnexService;
import org.jeecg.modules.system.service.ISysUserExpertDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 专家用户
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
@Service
public class SysUserExpertDataServiceImpl extends ServiceImpl<SysUserExpertDataMapper, SysUserExpertData> implements ISysUserExpertDataService {
    @Autowired
    private SysUserExpertDataMapper sysUserExpertDataMapper;
    @Autowired
    private IPublicUploadAnnexService iPublicUploadAnnexService;
    @Override
    public List<Map<String,String>> getExpertFiles(String id){
        return sysUserExpertDataMapper.getExpertFiles(id);
    }

    @Override
    public Boolean doCheckRegion(String id){
        Map<String,Object> rstMap = sysUserExpertDataMapper.doCheckRegion(Integer.valueOf(id));
        if(rstMap!=null&&"3".equals(rstMap.get("level").toString())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void doSaveFiles(String id){
        for(int i=1;i<=3;i++){
            PublicUploadAnnex file = new PublicUploadAnnex();
            file.setAnnexType(2);
            file.setKeyId(id);
            file.setRequired(1);
            if(i==1){
                file.setFileName("身份证件扫描件");
            }else if(i==2){
                file.setFileName("最高学历扫描件");
            }else if(i==3){
                file.setFileName("职称证书扫描件");
            }
            try{
                iPublicUploadAnnexService.save(file);
            }catch(Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }

    @Override
    public String getRegionById(Integer regionId){
        return sysUserExpertDataMapper.getRegionById(regionId);
    }
}
