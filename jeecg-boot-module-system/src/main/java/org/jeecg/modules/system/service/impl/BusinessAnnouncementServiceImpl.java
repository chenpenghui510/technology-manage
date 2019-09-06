package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.BusinessAnnouncement;
import org.jeecg.modules.system.mapper.BusinessAnnouncementMapper;
import org.jeecg.modules.system.service.IBusinessAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: business_announcement
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
@Service
public class BusinessAnnouncementServiceImpl extends ServiceImpl<BusinessAnnouncementMapper, BusinessAnnouncement> implements IBusinessAnnouncementService {

}