package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 业务
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
@Data
@TableName("business")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="business对象", description="业务")
public class Business {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**业务名称*/
	@Excel(name = "业务名称", width = 15)
    @ApiModelProperty(value = "业务名称")
	private String businessName;
	/**业务类型*/
	@Excel(name = "业务类型", width = 32)
    @ApiModelProperty(value = "业务类型")
	private String businessType;
	/**业务级别(1.国家级 2.省级 3.市级 4.区/县级 )*/
	@Excel(name = "业务级别(1.国家级 2.省级 3.市级 4.区/县级 )", width = 15, dicCode="business_level")
    @ApiModelProperty(value = "业务级别(1.国家级 2.省级 3.市级 4.区/县级 )")
	@Dict(dicCode = "business_level")
	private Integer businessLevel;
	/**所属部门id*/
	@Excel(name = "所属部门id", width = 15)
    @ApiModelProperty(value = "所属部门id")
	private String dpetId;
	/**所属执行人员id*/
	/*@Excel(name = "所属执行人员id", width = 15)
    @ApiModelProperty(value = "所属执行人员id")
	private String adminId;*/

	/**分配状态(0.未分配 1.已分配给部门 2.已分配给管理员)*/
	@Excel(name = "分配状态(0.未分配 1.已分配给部门 2.已分配给管理员)", width = 15)
	@ApiModelProperty(value = "分配状态(0.未分配 1.已分配给部门 2.已分配给管理员)")
    @Dict(dicCode = "give_status")
	private Integer giveStatus;

	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
	private Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
	private Date endTime;
	/**受理状态(0.未受理 1.受理中 2.暂停受理)*/
	@Excel(name = "受理状态(0.未受理 1.受理中 2.暂停受理 )", width = 15)
    @ApiModelProperty(value = "受理状态(0.未受理 1.受理中 2.暂停受理 )")
    @Dict(dicCode = "business_status")
	private Integer status;
	/**添加时间*/
	@Excel(name = "添加时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间")
	private Date createTime;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
    @ApiModelProperty(value = "创建人id")
	private String byUser;

	@Excel(name = "删除状态", width = 15,dicCode="del_flag")
	@ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	private Integer delFlag;

	@Excel(name="业务种类", width = 15)
	@ApiModelProperty(value="业务种类(1.成绩统计2.业务申报)")
	private Integer businessKind;
}
