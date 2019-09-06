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
 * @Description: 资讯配置
 * @Author: jeecg-boot
 * @Date:   2019-07-11
 * @Version: V1.0
 */
@Data
@TableName("sys_information")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_information对象", description="资讯配置")
public class SysInformation {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**名称（标题）*/
	@Excel(name = "名称（标题）", width = 15)
    @ApiModelProperty(value = "名称（标题）")
	private java.lang.String name;
	/**链接地址*/
	@Excel(name = "链接地址", width = 15)
    @ApiModelProperty(value = "链接地址")
	private java.lang.String url;
	/**图片*/
	@Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
	private java.lang.String img;
	/**类型：1.首页焦点图2.专利奖展示3.信息公开4.法规政策5.维权援助6.明星企业7.联系我们8.国家级优势企业9.省级优势企业10.市级优势企业*/
	@Excel(name = "类型：1.首页焦点图2.专利奖展示3.信息公开4.法规政策5.维权援助6.明星企业7.联系我们8.国家级优势企业9.省级优势企业10.市级优势企业", width = 15,dicCode = "informationType")
	@Dict(dicCode = "informationType")
	private Integer type;
	/**是否有效 1有效0无效*/
	@Excel(name = "是否有效 1有效0无效", width = 15,dicCode = "valid_status")
    @ApiModelProperty(value = "是否有效 1有效0无效")
	@Dict(dicCode = "valid_status")
	private Integer status;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
	private java.lang.String content;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remark;
	/**来源*/
	@Excel(name = "来源", width = 15)
    @ApiModelProperty(value = "来源")
	private java.lang.String source;
}
