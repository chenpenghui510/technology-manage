package org.jeecg.modules.system.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 登录账号
     */
    @Excel(name = "登录账号", width = 15)
    private String username;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
    private String realname;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户类型(0:超管 1:个人用户 2.企业用户 3：专家用户)
     */
    @Excel(name = "用户类型", width = 15,dicCode="userType")
    @Dict(dicCode = "userType")
    private Integer userType;

    /**
     * 部门code
     */
    private String orgCode;

    /**
     * md5密码盐
     */
    private String salt;

    /**
     * 头像
     */
    @Excel(name = "头像", width = 15)
    private String avatar;
    /**
     * 生日
     */
    @Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    @Excel(name = "性别", width = 15,dicCode="sex")
    @Dict(dicCode = "sex")
    private Integer sex;

    /**
     * 电子邮件
     */
    @Excel(name = "电子邮件", width = 15)
    private String email;

    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    private String phone;


    /**
     * 状态(1：正常  2：冻结  3.资料不全 4.待审核）
     */
    @Excel(name = "状态", width = 15,dicCode="user_status")
    @Dict(dicCode = "user_status")
    private Integer status;

    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态", width = 15,dicCode="del_flag")
    @TableLogic
    private String delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 审核意见
     */
    private String auditComments;
    /**
     * 同步工作流引擎1同步0不同步
     */
    private String activitiSync;
    /**
     * 对应专家用户ID（专家用户独有）
     */
    private String expertId;

    /*拉黑状态*/
    private String black;
}
