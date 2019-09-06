package org.jeecg.modules.system.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 专项申请
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
@Data
@TableName("form_content")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="form_content对象", description="专项申请")
public class FormContent {
    
	/**ID*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
	private java.lang.Integer id;
	/**申请表单ID*/
	@Excel(name = "申请表单ID", width = 15)
    @ApiModelProperty(value = "申请表单ID")
	private java.lang.Integer applyId;
	/**对应组件ID*/
	@Excel(name = "对应组件ID", width = 15)
    @ApiModelProperty(value = "对应组件ID")
	private java.lang.Integer formComponentId;
	/**表单值*/
	@Excel(name = "表单值", width = 15)
    @ApiModelProperty(value = "表单值")
	private java.lang.Object value;
}
