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
 * @Description: 用户
 * @Author: jeecg-boot
 * @Date:   2019-06-28
 * @Version: V1.0
 */
@Data
@TableName("system_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="system_user对象", description="用户")
public class SysyemUser {
    
	/**用户ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "用户ID")
	private java.lang.String id;
	/**登录账号*/
	@Excel(name = "登录账号", width = 15)
    @ApiModelProperty(value = "登录账号")
	private java.lang.String username;
	/**账户密码*/
	@Excel(name = "账户密码", width = 15)
    @ApiModelProperty(value = "账户密码")
	private java.lang.String password;
	/**客户姓名*/
	@Excel(name = "客户姓名", width = 15)
    @ApiModelProperty(value = "客户姓名")
	private java.lang.String name;
	/**企业名称*/
	@Excel(name = "企业名称", width = 15)
    @ApiModelProperty(value = "企业名称")
	private java.lang.String companyName;
	/**账户类型(0.管理员 1.企业用户 2.专家用户)*/
	@Excel(name = "账户类型(0.管理员 1.企业用户 2.专家用户)", width = 15)
    @ApiModelProperty(value = "账户类型(0.管理员 1.企业用户 2.专家用户)")
	private java.lang.Integer userRoleType;
	/**管理员角色ID*/
	@Excel(name = "管理员角色ID", width = 15)
    @ApiModelProperty(value = "管理员角色ID")
	private java.lang.Integer adminRoleId;
	/**邮箱地址*/
	@Excel(name = "邮箱地址", width = 15)
    @ApiModelProperty(value = "邮箱地址")
	private java.lang.String email;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private java.lang.String phone;
	/**用户状态(0.资料不全 1.待审核 2.在用 3.暂停)*/
	@Excel(name = "用户状态(0.资料不全 1.待审核 2.在用 3.暂停)", width = 15)
    @ApiModelProperty(value = "用户状态(0.资料不全 1.待审核 2.在用 3.暂停)")
	private java.lang.Integer userState;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
}
