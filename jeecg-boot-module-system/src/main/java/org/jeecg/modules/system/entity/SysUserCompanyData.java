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
 * @Description: 企业信息
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
@Data
@TableName("sys_user_company_data")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_user_company_data对象", description="企业信息")
public class SysUserCompanyData {

    /**
     * 企业附属表ID
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "企业附属表ID")
    private java.lang.String id;
    /**
     * 对应的用户ID
     */
    @Excel(name = "对应的用户ID", width = 15)
    @ApiModelProperty(value = "对应的用户ID")
    private java.lang.String userId;
    /**
     * userName
     */
    @Excel(name = "userName", width = 15)
    @ApiModelProperty(value = "userName")
    private java.lang.String userName;
    /**
     * 用户性别(1:男 2:女)
     */
    @Excel(name = "用户性别(1:男 2:女)", width = 15)
    @ApiModelProperty(value = "用户性别(1:男 2:女)")
    @Dict(dicCode = "sex")
    private java.lang.Integer userSex;
    /**
     * 身份证号
     */
    @Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "身份证号")
    private java.lang.String userCard;
    /**
     * 职务
     */
    @Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
    private java.lang.String userPost;
    /**
     * 职称
     */
    @Excel(name = "职称", width = 15)
    @ApiModelProperty(value = "职称")
    private java.lang.String userTitle;
    /**
     * 固定电话
     */
    @Excel(name = "固定电话", width = 15)
    @ApiModelProperty(value = "固定电话")
    private java.lang.String userTelephone;
    /**
     * 单位名称
     */
    @Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称")
    private java.lang.String companyName;
    /**
     * 社会信用代码
     */
    @Excel(name = "社会信用代码", width = 15)
    @ApiModelProperty(value = "社会信用代码")
    private java.lang.String socialCreditCode;
    /**
     * 主管部门
     */
    @Excel(name = "主管部门", width = 15)
    @ApiModelProperty(value = "主管部门")
    @Dict(dicCode = "competent_department")
    private java.lang.String competentDepartment;
    /**
     * 单位性质
     */
    @Excel(name = "单位性质", width = 15)
    @ApiModelProperty(value = "单位性质")
    @Dict(dicCode = "company_properties")
    private java.lang.Integer unitNature;
    /**
     * 工商注册地
     */
    @Excel(name = "工商注册地", width = 15)
    @ApiModelProperty(value = "工商注册地")
    private java.lang.String incorporationPlace;
    /**
     * 公司地址-省
     */
    @Excel(name = "公司地址-省", width = 15)
    @ApiModelProperty(value = "公司地址-省")
    private java.lang.String companyAddressProvince;
    /**
     * 公司地址-市
     */
    @Excel(name = "公司地址-市", width = 15)
    @ApiModelProperty(value = "公司地址-市")
    private java.lang.String companyAddressCity;
    /**
     * 公司地址-详细地址
     */
    @Excel(name = "公司地址-详细地址", width = 15)
    @ApiModelProperty(value = "公司地址-详细地址")
    private java.lang.String companyDetailAddress;
    /**
     * 公司电话
     */
    @Excel(name = "公司电话", width = 15)
    @ApiModelProperty(value = "公司电话")
    private java.lang.String companyTelephone;
    /**
     * 公司传真
     */
    @Excel(name = "公司传真", width = 15)
    @ApiModelProperty(value = "公司传真")
    private java.lang.String companyFax;
    /**
     * 公司邮箱
     */
    @Excel(name = "公司邮箱", width = 15)
    @ApiModelProperty(value = "公司邮箱")
    private java.lang.String companyEmail;
    /**
     * 公司邮政编码
     */
    @Excel(name = "公司邮政编码", width = 15)
    @ApiModelProperty(value = "公司邮政编码")
    private java.lang.String companyPostalCode;
    /**
     * 公司银行账户
     */
    @Excel(name = "公司银行账户", width = 15)
    @ApiModelProperty(value = "公司银行账户")
    private java.lang.String companyBankAccount;
    /**
     * 公司银行账号
     */
    @Excel(name = "公司银行账号", width = 15)
    @ApiModelProperty(value = "公司银行账号")
    private java.lang.String companyBankCode;
    /**
     * 银行名称
     */
    @Excel(name = "银行名称", width = 15)
    @ApiModelProperty(value = "银行名称")
    private java.lang.String bankName;
    /**
     * 注册资本(万元)
     */
    @Excel(name = "注册资本(万元)", width = 15)
    @ApiModelProperty(value = "注册资本(万元)")
    private java.lang.String registeredCapital;
    /**
     * 资本类型（1.人民币 2.美元）
     */
    @Excel(name = "资本类型（1.人民币 2.美元）", width = 15)
    @ApiModelProperty(value = "资本类型（1.人民币 2.美元）")
    @Dict(dicCode = "capital_type")
    private java.lang.Integer capitalType;
    /**
     * 信用等级（1.AAA 2.AA 3.A）
     */
    @Excel(name = "信用等级（1.AAA 2.AA 3.A）", width = 15)
    @ApiModelProperty(value = "信用等级（1.AAA 2.AA 3.A）")
    @Dict(dicCode = "credit_rating")
    private java.lang.Integer creditRating;
    /**
     * 公司成立时间
     */
    @Excel(name = "公司成立时间", width = 20, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date companyCreateTime;
    /**
     * 经营范围
     */
    @Excel(name = "经营范围", width = 15)
    @ApiModelProperty(value = "经营范围")
    private java.lang.String businessScope;
    /**
     * 法人
     */
    @Excel(name = "法人", width = 15)
    @ApiModelProperty(value = "法人")
    private java.lang.String legalPerson;
    /**
     * 证件类型（1.身份证 2.护照 3.港澳台证）
     */
    @Excel(name = "证件类型（1.身份证 2.护照 3.港澳台证）", width = 15)
    @ApiModelProperty(value = "证件类型（1.身份证 2.护照 3.港澳台证）")
    @Dict(dicCode = "cardType")
    private java.lang.Integer documentType;
    /**
     * 证件号码
     */
    @Excel(name = "证件号码", width = 15)
    @ApiModelProperty(value = "证件号码")
    private java.lang.String documentCode;
    /**
     * 法人手机号
     */
    @Excel(name = "法人手机号", width = 15)
    @ApiModelProperty(value = "法人手机号")
    private java.lang.String legalPhone;
    /**
     * 法人固定电话
     */
    @Excel(name = "法人固定电话", width = 15)
    @ApiModelProperty(value = "法人固定电话")
    private java.lang.String legalTelephone;
    /**
     * 法人邮箱
     */
    @Excel(name = "法人邮箱", width = 15)
    @ApiModelProperty(value = "法人邮箱")
    private java.lang.String legalEmail;
    /**
     * 学历(字典)
     */
    @Excel(name = "学历(字典)", width = 15)
    @ApiModelProperty(value = "学历(字典)")
    @Dict(dicCode = "legal_education")
    private java.lang.Integer legalEducation;
    /**
     * 学位
     */
    @Excel(name = "学位", width = 15)
    @ApiModelProperty(value = "学位")
    @Dict(dicCode = "degree")
    private java.lang.Integer legalAcademicDegree;
    /**
     * 法人职务
     */
    @Excel(name = "法人职务", width = 15)
    @ApiModelProperty(value = "法人职务")
    private java.lang.String legalPost;
    /**
     * 法人性别(字典)
     */
    @Excel(name = "法人性别(字典)", width = 15)
    @ApiModelProperty(value = "法人性别(字典)")
    @Dict(dicCode = "sex")
    private java.lang.Integer legalSex;
    /**
     * 法人出生日期
     */
    @Excel(name = "公司成立时间", width = 20, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date legalBirthDate;
    /**
     * 法人职称(字典)
     */
    @Excel(name = "法人职称(字典)", width = 15)
    @ApiModelProperty(value = "法人职称(字典)")
    @Dict(dicCode = "professional_titles")
    private java.lang.Integer legalTitle;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 贯标时间
     */
    @Excel(name = "贯标时间", width = 20, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "贯标时间")
    private java.util.Date isoTime;
    /**
     * 知识产权被侵权是否：0否，1是
     */
    @Excel(name = "知识产权被侵权是否：0否，1是", width = 15)
    @ApiModelProperty(value = "知识产权被侵权是否：0否，1是")
    private java.lang.Integer violate;
    /**
     * 融资金额
     */
    @Excel(name = "融资金额", width = 15)
    @ApiModelProperty(value = "融资金额")
    private java.lang.Double financingAmount;
    /**
     * 融资年度
     */
    @Excel(name = "融资年度", width = 20, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "融资年度")
    private java.util.Date financingTime;
    /**
     * 政府资助贴息金额
     */
    @Excel(name = "政府资助贴息金额", width = 15)
    @ApiModelProperty(value = "政府资助贴息金额")
    private java.lang.Double governmentAid;
    /**
     * 是否国家级知识产权优势企业:0否，1国家强企，2国家示范企业
     */
    @Excel(name = "是否国家级知识产权优势企业:0否，1国家强企，2国家示范企业", width = 15)
    @ApiModelProperty(value = "是否国家级知识产权优势企业:0否，1国家强企，2国家示范企业")
    private java.lang.Integer nationalAdvantage;
    /**
     * 是否省级知识产权优势企业：0否，1省级强企，2省级优势企业
     */
    @Excel(name = "是否省级知识产权优势企业：0否，1省级强企，2省级优势企业", width = 15)
    @ApiModelProperty(value = "是否省级知识产权优势企业：0否，1省级强企，2省级优势企业")
    private java.lang.Integer provincialAdvantage;
    /**
     * 是否市级知识产权优势企业:0.否，1.市级强企，2.市级示范企业
     */
    @Excel(name = "是否市级知识产权优势企业:0.否，1.市级强企，2.市级示范企业", width = 15)
    @ApiModelProperty(value = "是否市级知识产权优势企业:0.否，1.市级强企，2.市级示范企业")
    private java.lang.Integer cityAdvantage;
    /**
     * 是否郑州市科技型企业:0否，1是
     */
    @Excel(name = "是否郑州市科技型企业:0否，1是", width = 15)
    @ApiModelProperty(value = "是否郑州市科技型企业:0否，1是")
    private java.lang.Integer zzScience;
    /**
     * 备案编号
     */
    @Excel(name = "备案编号", width = 15)
    @ApiModelProperty(value = "备案编号")
    private java.lang.String recordNumber;
    /**
     * 省科技龙头企业年度
     */
    @Excel(name = "省科技龙头企业年度", width = 20, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "省科技龙头企业年度")
    private java.util.Date provincialKeyTime;
    /**
     * 是否规上企业(0：否 1：是)
     */
    @Excel(name = "是否规上企业(0：否 1：是)", width = 15)
    @ApiModelProperty(value = "是否规上企业(0：否 1：是)")
    private java.lang.Integer regulatoryEnterprises;
    /**
     * 是否科协(0：否 1：是)
     */
    @Excel(name = "是否科协(0：否 1：是)", width = 15)
    @ApiModelProperty(value = "是否科协(0：否 1：是)")
    private java.lang.Integer organizationOfScientists;

    @Excel(name = "公司行业类型")
    @ApiModelProperty(value="公司行业类型")
    @Dict(dicCode = "industry")
    private java.lang.Integer companyIndustry;

}