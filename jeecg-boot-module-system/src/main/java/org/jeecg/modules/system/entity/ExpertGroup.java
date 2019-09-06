package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: expert_group
 * @Author: jeecg-boot
 * @Date:   2019-07-08
 * @Version: V1.0
 */
@Data
@TableName("expert_group")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="expert_group对象", description="expert_group")
public class ExpertGroup {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**组名称*/
	@Excel(name = "组名称", width = 15)
    @ApiModelProperty(value = "组名称")
	private String groupName;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private String groupDescription;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	private Integer delFlag;

	@Excel(name = "结束状态(0.待分配  1.已分配  2.解散 )", width = 15)
	@ApiModelProperty(value = "结束状态(0.待分配  1.已分配  2.解散 )")
	@Dict(dicCode = "group_status")
	private Integer status;

	@Excel(name = "创建时间", width = 15)
	@ApiModelProperty(value = "创建时间")
	private Date createTime;



}
