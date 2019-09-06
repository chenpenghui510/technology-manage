package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.PublicUploadAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 公共附件表
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
public interface PublicUploadAnnexMapper extends BaseMapper<PublicUploadAnnex> {
    List<PublicUploadAnnex> searchByQuery(@Param("keyId") String keyId,@Param("annexType")Integer annexType,@Param("required")Integer required);
}
