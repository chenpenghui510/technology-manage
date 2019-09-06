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
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 表单模板
 * @Author: jeecg-boot
 * @Date:   2019-07-12
 * @Version: V1.0
 */
@Data
@TableName("form_template")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FormTemplate {
    
	/**
	 * 表单ID
	 * */
	@TableId(type = IdType.AUTO)
	@Excel(name = "表单ID", width = 15)
	private Integer formId;

	/**
	 * 表单名称
	 * */
	@Excel(name = "表单名称", width = 15)
	private String formName;

	/**
	 * 表单状态(1.正常2.禁用)
	 * */
	@Excel(name = "表单状态(1.正常2.禁用)", width = 15)
	private Integer formState;

	@Excel(name = "模板要求", width = 15)
	private java.lang.String templateRequest;
	/**
	 * 表单类型(1.业务型表单)
	 * */
	@Excel(name = "表单类型(1.业务型表单)", width = 15,dicCode="formType")
	@Dict(dicCode = "formType")
	private Integer formType;

	/**
	 * 类型下关联的业务ID
	 * */
	@Excel(name = "类型下关联的业务ID", width = 15)
	private Integer relationBusinessId;

	/**
	 * 创建时间
	 * */
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;

	/**
	 * 删除状态(1.正常 2.已删除)
	 */
	private Integer delFlag;
}
