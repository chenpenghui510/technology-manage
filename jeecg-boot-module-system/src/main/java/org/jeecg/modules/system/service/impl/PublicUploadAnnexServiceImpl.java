package org.jeecg.modules.system.service.impl;

import org.jeecg.modules.system.entity.PublicUploadAnnex;
import org.jeecg.modules.system.mapper.PublicUploadAnnexMapper;
import org.jeecg.modules.system.service.IPublicUploadAnnexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 公共附件表
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
@Service
public class PublicUploadAnnexServiceImpl extends ServiceImpl<PublicUploadAnnexMapper, PublicUploadAnnex> implements IPublicUploadAnnexService {
    @Resource
    private PublicUploadAnnexMapper publicUploadAnnexMapper;
    @Override
    public List<PublicUploadAnnex> searchByQuery(String keyId,Integer annexType,Integer required){
        return publicUploadAnnexMapper.searchByQuery(keyId,annexType,required);
    }
}
