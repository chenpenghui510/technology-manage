package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.PublicUploadAnnex;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 公共附件表
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
public interface IPublicUploadAnnexService extends IService<PublicUploadAnnex> {
    /**
     * 通过条件查询附件列表
     * @param keyId
     * @param annexType
     * @param required
     * @return
     */
    List<PublicUploadAnnex> searchByQuery(String keyId,Integer annexType,Integer required);
}
