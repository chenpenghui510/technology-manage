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
 * @Description: 个人信息完善
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
@Data
@TableName("sys_user_personal_data")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_user_personal_data对象", description="个人信息完善")
public class SysUserPersonalData {

	/**ID*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**关联用户注册表ID*/
	@Excel(name = "关联用户注册表ID", width = 15)
    @ApiModelProperty(value = "关联用户注册表ID")
	private java.lang.String userId;
	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称")
	private java.lang.String companyName;
	/**性别(1.男 2.女)*/
	@Excel(name = "性别(1.男 2.女)", width = 15)
    @ApiModelProperty(value = "性别(1.男 2.女)")
	private java.lang.Integer personalSex;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "身份证号")
	private java.lang.String identityCardNumber;
	/**身份证图片地址*/
	@Excel(name = "身份证图片地址", width = 15)
    @ApiModelProperty(value = "身份证图片地址")
	private java.lang.String identityCardImg;
	/**固定电话*/
	@Excel(name = "固定电话", width = 15)
    @ApiModelProperty(value = "固定电话")
	private java.lang.String tel;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
	private java.lang.String post;
	/**职称*/
	@Excel(name = "职称", width = 15)
    @ApiModelProperty(value = "职称")
	private java.lang.String professionalTitles;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**上次修改时间*/
    @ApiModelProperty(value = "上次修改时间")
	private java.util.Date updateTime;
}
