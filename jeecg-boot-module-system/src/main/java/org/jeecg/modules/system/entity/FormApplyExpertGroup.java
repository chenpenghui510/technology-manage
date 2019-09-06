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
 * @Description: form_apply_expert_group
 * @Author: jeecg-boot
 * @Date:   2019-07-26
 * @Version: V1.0
 */
@Data
@TableName("form_apply_expert_group")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="form_apply_expert_group对象", description="form_apply_expert_group")
public class FormApplyExpertGroup {
    
	/**专家组和审核 中间表*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "专家组和审核 中间表")
	private Integer id;
	/**审核表id*/
	@Excel(name = "审核表id", width = 15)
    @ApiModelProperty(value = "审核表id")
	private Integer formApplyId;
	/**专家组id*/
	@Excel(name = "专家组id", width = 15)
    @ApiModelProperty(value = "专家组id")
	private Integer expertGroupId;
	/**状态(0.正常  1删除 )*/
	@Excel(name = "状态(0.正常  1删除 )", width = 15)
    @ApiModelProperty(value = "状态(0.正常  1删除 )")
	private Integer status;
}
