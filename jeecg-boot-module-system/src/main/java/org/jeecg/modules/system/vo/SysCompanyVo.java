package org.jeecg.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class SysCompanyVo implements Serializable {
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
     * 性别（1：男 2：女）
     */
    @Dict(dicCode = "sex")
    private String sex;

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
    private java.lang.String name;
    /**用户性别(1:男 2:女)*/
    @Dict(dicCode = "sex")
    private java.lang.String userSex;
    /**身份证号*/
    private java.lang.String userCard;
    /**职务*/
    private java.lang.String userPost;
    /**职称*/
    private java.lang.String userTitle;
    /**固定电话*/
    private java.lang.String userTelephone;
    /**单位名称*/
    private java.lang.String companyName;
    /**社会信用代码*/
    private java.lang.String socialCreditCode;
    /**主管部门*/
    @Dict(dicCode = "competent_department")
    private java.lang.String competentDepartment;
    /**单位性质*/
    @Dict(dicCode = "company_properties")
    private java.lang.String unitNature;
    /**工商注册地*/
    private java.lang.String incorporationPlace;
    /**公司地址-省*/
    private java.lang.String companyAddressProvince;
    /**公司地址-市*/
    private java.lang.String companyAddressCity;
    /**公司地址-详细地址*/
    private java.lang.String companyDetailAddress;
    /**公司电话*/
    private java.lang.String companyTelephone;
    /**公司传真*/
    private java.lang.String companyFax;
    /**公司邮箱*/
    private java.lang.String companyEmail;
    /**公司邮政编码*/
    private java.lang.String companyPostalCode;
    /**公司银行账户*/
    private java.lang.String companyBankAccount;
    /**公司银行账号*/
    private java.lang.String companyBankCode;
    /**银行名称*/
    private java.lang.String bankName;
    /**注册资本(万元)*/
    private java.lang.String registeredCapital;
    /**资本类型（1.人民币 2.美元）*/
    @Dict(dicCode = "capital_type")
    private java.lang.String capitalType;
    /**信用等级（1.AAA 2.AA 3.A）*/
    @Dict(dicCode = "credit_rating")
    private java.lang.String creditRating;
    /**公司成立时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date companyCreateTime;
    /**经营范围*/
    @ApiModelProperty(value = "经营范围")
    private java.lang.String businessScope;
    /**法人*/
    private java.lang.String legalPerson;
    /**证件类型（1.身份证 2.护照 3.港澳台证）*/
    @Dict(dicCode = "cardType")
    private java.lang.String documentType;
    /**证件号码*/
    private java.lang.String documentCode;
    /**法人手机号*/
    private java.lang.String legalPhone;
    /**法人固定电话*/
    private java.lang.String legalTelephone;
    /**法人邮箱*/
    private java.lang.String legalEmail;
    /**学历(字典)*/
    @Dict(dicCode = "legal_education")
    private java.lang.String legalEducation;
    /**学位*/
    @Dict(dicCode = "degree")
    private java.lang.String legalAcademicDegree;
    /**法人职务*/
    private java.lang.String legalPost;
    /**法人性别(字典)*/
    @Dict(dicCode = "sex")
    private java.lang.String legalSex;
    /**法人出生日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date legalBirthDate;
    /**法人职称(字典)*/
    @Dict(dicCode = "professional_titles")
    private java.lang.String legalTitle;
    /**更新时间*/
    private java.util.Date updateTime;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 贯标时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "贯标时间")
    private java.util.Date isoTime;
    /**
     * 知识产权被侵权是否：0否，1是
     */
    @ApiModelProperty(value = "知识产权被侵权是否：0否，1是")
    private java.lang.String violate;
    /**
     * 融资金额
     */
    @ApiModelProperty(value = "融资金额")
    private java.lang.String financingAmount;
    /**
     * 融资年度
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "融资年度")
    private java.util.Date financingTime;
    /**
     * 政府资助贴息金额
     */
    @ApiModelProperty(value = "政府资助贴息金额")
    private java.lang.String governmentAid;
    /**
     * 是否国家级知识产权优势企业:0否，1国家强企，2国家示范企业
     */
    @ApiModelProperty(value = "是否国家级知识产权优势企业:0否，1国家强企，2国家示范企业")
    private java.lang.String nationalAdvantage;
    /**
     * 是否省级知识产权优势企业：0否，1省级强企，2省级优势企业
     */
    @ApiModelProperty(value = "是否省级知识产权优势企业：0否，1省级强企，2省级优势企业")
    private java.lang.String provincialAdvantage;
    /**
     * 是否市级知识产权优势企业:0.否，1.市级强企，2.市级示范企业
     */
    @ApiModelProperty(value = "是否市级知识产权优势企业:0.否，1.市级强企，2.市级示范企业")
    private java.lang.String cityAdvantage;
    /**
     * 是否郑州市科技型企业:0否，1是
     */
    @ApiModelProperty(value = "是否郑州市科技型企业:0否，1是")
    private java.lang.String zzScience;
    /**
     * 备案编号
     */
    @ApiModelProperty(value = "备案编号")
    private java.lang.String recordNumber;
    /**
     * 省科技龙头企业年度
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "省科技龙头企业年度")
    private java.util.Date provincialKeyTime;
    /**
     * 是否规上企业(0：否 1：是)
     */
    @ApiModelProperty(value = "是否规上企业(0：否 1：是)")
    private java.lang.String regulatoryEnterprises;
    /**
     * 是否科协(0：否 1：是)
     */
    @ApiModelProperty(value = "是否科协(0：否 1：是)")
    private java.lang.String organizationOfScientists;

    @ApiModelProperty(value="公司行业类型")
    @Dict(dicCode = "industry")
    private java.lang.String companyIndustry;

}
