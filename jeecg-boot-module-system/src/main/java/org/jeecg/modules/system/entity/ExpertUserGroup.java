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
 * @Description: expert_user_group
 * @Author: jeecg-boot
 * @Date:   2019-07-09
 * @Version: V1.0
 */
@Data
@TableName("expert_user_group")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="expert_user_group对象", description="expert_user_group")
public class ExpertUserGroup {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**组id*/
	@Excel(name = "组id", width = 15)
    @ApiModelProperty(value = "组id")
	private Integer groupId;
	/**专家id*/
	@Excel(name = "专家id", width = 15)
    @ApiModelProperty(value = "专家id")
	private String userId;
}
