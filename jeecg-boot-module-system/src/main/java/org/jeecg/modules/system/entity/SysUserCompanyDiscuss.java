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
 * @Description: 研发中心
 * @Author: jeecg-boot
 * @Date:   2019-08-21
 * @Version: V1.0
 */
@Data
@TableName("sys_user_company_discuss")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_user_company_discuss对象", description="研发中心")
public class SysUserCompanyDiscuss {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**用户id*/
    @ApiModelProperty(value = "用户id")
	private java.lang.String userId;
	/**公司名称*/
	@Excel(name = "公司名称", width = 15)
	@ApiModelProperty(value = "公司名称")
	private java.lang.String userName;
	/**研发中心名称*/
	@Excel(name = "研发中心名称", width = 15)
    @ApiModelProperty(value = "研发中心名称")
	private java.lang.String name;
	/**类型：1工程技术研究中心，2企业技术中心，3工程研究中心，4制造创新中心，5工业设计中心，6重点实验室*/
	@Excel(name = "类型", width = 15,dicCode = "discuss_sort")
    @ApiModelProperty(value = "类型")
	@Dict(dicCode = "discuss_sort")
	private java.lang.Integer sort;
	/**等级：1国家级2省级3市级*/
	@Excel(name = "等级", width = 15,dicCode = "patent_level")
    @ApiModelProperty(value = "等级")
	@Dict(dicCode = "patent_level")
	private java.lang.Integer level;
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
