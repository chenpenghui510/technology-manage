package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysUserExpertData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 专家用户
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
public interface ISysUserExpertDataService extends IService<SysUserExpertData> {
    /**
     * 查询个人用户附件
     * @param id
     * @return
     */
    List<Map<String,String>> getExpertFiles(String id);

    /**
     * 校验所选地区是否为区/县级
     * @param id
     * @return
     */
    Boolean doCheckRegion(String id);

    /**
     * 添加默认附件信息
     * @param id
     */
    void doSaveFiles(String id);

    /**
     * 获取户籍所在地
     * @param regionId
     * @return
     */
    String getRegionById(Integer regionId);
}
