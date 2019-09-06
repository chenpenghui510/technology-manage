package org.jeecg.modules.system.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.Api;
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
 * @Description: 专项申请
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
@Data
@TableName("form_apply")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="form_apply对象", description="专项申请")
public class  FormApply {
    
	/**ID*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**填写表单ID*/
	@Excel(name = "填写表单ID", width = 15)
    @ApiModelProperty(value = "填写表单ID")
	private java.lang.Integer formId;
	/**填写业务ID*/
	@Excel(name = "填写业务ID", width = 15)
	@ApiModelProperty(value = "填写业务ID")
	private java.lang.Integer businessId;
	/**表单类型(1.业务型表单)*/
	@Excel(name = "表单类型(1.业务型表单)", width = 15)
    @ApiModelProperty(value = "表单类型(1.业务型表单)")
	private java.lang.Integer formType;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**填写人ID*/
	@Excel(name = "填写人ID", width = 15)
    @ApiModelProperty(value = "填写人ID")
	private java.lang.String createPerson;
	/**填写人名称*/
	@Excel(name = "填写人名称", width = 15)
	@ApiModelProperty(value = "填写人名称")
	private java.lang.String personName;
	/**审核状态：1.待上传 （直接跳转上传页面） （可编辑）
				 2.待提交 （可编辑）
				 3.待初审 （有个操作是撤回）（可编辑）
				 4.初审中
				 5.初审通过（待授权状态）
				 6.初审退回  （可编辑） 相当于待提交状态
				 7.初审拒绝 （可查看不可编辑）
				 8.待复审
				 9.复审通过
				 10.复审拒绝 （可查看不可编辑）*/
	@Excel(name = "审核状态：1.未开始2.审核中3.成功4.失败", width = 15)
    @ApiModelProperty(value = "审核状态：1.未开始2.审核中3.成功4.失败")
	@Dict(dicCode = "applyStatus")
	private java.lang.Integer status;
	/**有效状态：1有效0无效*/
	@Excel(name = "有效状态：1有效0无效", width = 15)
	@ApiModelProperty(value = "有效状态：1有效0无效")
	@Dict(dicCode = "valid_status")
	private java.lang.Integer valid;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
	/**
	 * 审核意见
	 */
	@ApiModelProperty(value="审核意见")
	private String opinion;

	@ApiModelProperty(value="初审人ID")
	private String firstExaminePerson;

	@ApiModelProperty(value="初审时间")
	private Date firstExamineTime;

	@ApiModelProperty(value="复审人ID")
	private String reExaminePerson;

	@ApiModelProperty(value="复审时间")
	private Date reExamineTime;
}
