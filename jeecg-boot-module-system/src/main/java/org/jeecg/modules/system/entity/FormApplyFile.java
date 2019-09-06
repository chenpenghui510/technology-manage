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
 * @Description: 申请附件
 * @Author: jeecg-boot
 * @Date:   2019-07-24
 * @Version: V1.0
 */
@Data
@TableName("form_apply_file")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="form_apply_file对象", description="申请附件")
public class FormApplyFile {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**申请表ID*/
	@Excel(name = "申请表ID", width = 15)
    @ApiModelProperty(value = "申请表ID")
	private java.lang.Integer applyId;
	/**文件名称*/
	@Excel(name = "文件名称", width = 15)
    @ApiModelProperty(value = "文件名称")
	private java.lang.String fileName;
	/**路径*/
	@Excel(name = "路径", width = 15)
    @ApiModelProperty(value = "路径")
	private java.lang.String url;
	/**类型[1用户2 专家]*/
	@Excel(name = "类型[1用户2 专家]", width = 15)
    @ApiModelProperty(value = "类型[1用户2 专家]")
	private java.lang.Integer type;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**附件模板Id*/
	@Excel(name = "附件模板Id", width = 15)
    @ApiModelProperty(value = "附件模板")
	private java.lang.Integer fileId;
}
