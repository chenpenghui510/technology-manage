package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: business_announcement
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
@Data
@TableName("business_announcement")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="business_announcement对象", description="business_announcement")
public class BusinessAnnouncement {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**业务id*/
	@Excel(name = "业务id", width = 15)
    @ApiModelProperty(value = "业务id")
	private Integer businessId;
	/**通知id*/
	@Excel(name = "通知id", width = 15)
    @ApiModelProperty(value = "通知id")
	private String sysAnnouncementId;
	/**状态（0，正常，1已删除)*/
	@Excel(name = "状态（0，正常，1已删除)", width = 15)
    @ApiModelProperty(value = "状态（0，正常，1已删除)")
	private Integer status;
}
