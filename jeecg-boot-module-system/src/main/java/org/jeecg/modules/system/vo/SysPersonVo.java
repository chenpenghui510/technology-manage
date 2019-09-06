package org.jeecg.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class SysPersonVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户类型(0:超管 1:个人用户 2.企业用户 3：专家用户)
     */
    @Dict(dicCode = "userType")
    private String userType;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;
    /**
     * 状态(1：正常  2：冻结  3.资料不全 4.待审核）
     */
    @Dict(dicCode = "user_status")
    private String status;

    /**
     * 审核意见
     */
    private String auditComments;

    /**userName*/
    private String name;
    /**用户性别(1:男 2:女)*/
    @Dict(dicCode = "sex")
    private String userSex;
    /**身份证号*/
    private String userCard;
    /**
     * 行政职务
     * */
    @Dict(dicCode = "administrative_post")
    private String userPost;

    /**
     * 职称(1.正高级 2.副高级 3.中级 4.助理级 5.技术员级 6.其他)
     * */
    @Dict(dicCode = "professional_titles")
    private String userTitle;
    /**固定电话*/
    private String userTelephone;

    /**更新时间*/
    private java.util.Date updateTime;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

}
