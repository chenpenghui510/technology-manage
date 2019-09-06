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
 * @Description: 企业用户专利著作
 * @Author: jeecg-boot
 * @Date:   2019-08-16
 * @Version: V1.0
 */
@Data
@TableName("sys_user_company_patent")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_user_company_patent对象", description="企业用户专利著作")
public class SysUserCompanyPatent {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private java.lang.String userId;
	/**公司名称*/
	@Excel(name = "公司名称", width = 15)
	@ApiModelProperty(value = "公司名称")
	private java.lang.String userName;
	/**专利著作名称*/
	@Excel(name = "专利著作名称", width = 15)
    @ApiModelProperty(value = "专利著作名称")
	private java.lang.String name;
	/**专利著作编号*/
	@Excel(name = "专利著作编号", width = 15)
    @ApiModelProperty(value = "专利著作编号")
	private java.lang.String number;
	/**分类：1有效发明专利，2有效实用新型专利，3有效外观专利，4有效国际专利*/
	@Excel(name = "分类", width = 15,dicCode = "patent_sort")
	@ApiModelProperty(value = "分类")
	@Dict(dicCode = "patent_sort")
	private java.lang.Integer sort;
	/**等级：1国家级2省级3市级*/
	@Excel(name = "等级", width = 15,dicCode = "patent_level")
    @ApiModelProperty(value = "等级")
	@Dict(dicCode = "patent_level")
	private java.lang.Integer level;
	/**奖级：0特等奖1一等奖2二等奖*/
	@Excel(name = "奖级", width = 15,dicCode = "patent_prize")
    @ApiModelProperty(value = "奖级")
	@Dict(dicCode = "patent_prize")
	private java.lang.Integer prize;
	/**专利年度*/
	@Excel(name = "年度", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "年度")
	private java.util.Date patentTime;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**updateTime*/
    @ApiModelProperty(value = "updateTime")
	private java.util.Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remark;
}
