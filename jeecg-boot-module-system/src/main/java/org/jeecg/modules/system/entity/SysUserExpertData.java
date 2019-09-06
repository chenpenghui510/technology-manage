package org.jeecg.modules.system.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 专家用户
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
@Data
@TableName("sys_user_expert_data")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserExpertData {
    
	/**
	 * 专家主键ID
	 * */
    @TableId(type = IdType.UUID)
	@Excel(name = "专家主键ID", width = 15)
	private String expertId;

	/**
	 *专家名称
	 * */
	@Excel(name = "专家名称", width = 15)
	private String expertName;
	/**
	 * 专家类别(1.技术专家 2.财务专家 3.管理专家 4.风投专家)
	 * */
	@Excel(name = "专家类别", width = 15,dicCode="userType")
	@Dict(dicCode = "expert_type")
	private Integer expertType;
	/**
	 * 证件类型(1.身份证 2.护照 3.港澳台证)
	 * */
	@Excel(name = "证件类型", width = 15,dicCode="cardType")
	@Dict(dicCode = "cardType")
	private Integer cardType;

	/**
	 * 证件号
	 * */
	@Excel(name = "证件号", width = 15)
	private String cardNumber;
	/**
	 * 性别(1.男 2.女)
	 * */
	@Excel(name = "性别(1.男 2.女)", width = 15,dicCode="sex")
	@Dict(dicCode = "sex")
	private Integer expertSex;

	/**
	 * 民族
	 * */
	@Excel(name = "民族", width = 15,dicCode="nation")
	@Dict(dicCode = "nation")
	private Integer nation;

	/**
	 * 出生日期
	 * */
	@Excel(name = "出生日期", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;

	/**
	 * 政治面貌
	 * */
	@Excel(name = "政治面貌", width = 15,dicCode="politic_countenance")
	@Dict(dicCode = "politic_countenance")
	private Integer politicCountenance;
	/**
	 * 工作单位
	 * */
	@Excel(name = "工作单位", width = 15)
	private String companyName;

	/**
	 * 单位性质ID
	 * */
	@Excel(name = "单位性质ID", width = 15,dicCode="company_properties")
	@Dict(dicCode = "company_properties")
	private Integer companyProperties;

	/**
	 * 通信地址
	 * */
	@Excel(name = "通信地址", width = 15)
	private String mailAddress;

	/**
	 * 邮编
	 * */
	@Excel(name = "邮编", width = 15)
	private String zipCode;

	/**
	 * 户籍所在地(关联region表主键)
	 * */
	@Excel(name = "户籍所在地", width = 15)
	private Integer registeredPemanent;

	/**
	 * 详细地址
	 * */
	@Excel(name = "详细地址", width = 15)
	private String address;

	/**
	 * 行政职务
	 * */
	@Excel(name = "行政职务", width = 15,dicCode="administrative_post")
	@Dict(dicCode = "administrative_post")
	private Integer administrativePost;

	/**
	 * 职称(1.正高级 2.副高级 3.中级 4.助理级 5.技术员级 6.其他)
	 * */
	@Excel(name = "职称", width = 15,dicCode="professional_titles")
	@Dict(dicCode = "professional_titles")
	private Integer professionalTitles;

	/**
	 * 固定电话
	 * */
	@Excel(name = "固定电话", width = 15)
	private String tel;

	/**
	 * 手机号
	 * */
	@Excel(name = "手机号", width = 15)
	private String phone;

	/**
	 * 传真
	 * */
	@Excel(name = "传真", width = 15)
	private String fax;

	/**
	 * 邮箱
	 * */
	@Excel(name = "邮箱", width = 15)
	private String email;

	/**
	 * 开户银行
	 * */
	@Excel(name = "开户银行", width = 15)
	private String depositBank;

	/**
	 * 银行账号
	 * */
	@Excel(name = "银行账号", width = 15)
	private String accountNumber;

	/**
	 * 现从事专业
	 * */
	@Excel(name = "现从事专业", width = 15)
	private String presentOccupation;

	/**
	 * 所学专业
	 * */
	@Excel(name = "所学专业", width = 15)
	private String studyMajor;

	/**
	 * 毕业院校(第一学历)
	 * */
	@Excel(name = "毕业院校(第一学历)", width = 15)
	private String firstGraduatedSchool;
	/**
	 * 毕业院校(最高学历)
	 * */
	@Excel(name = "毕业院校(最高学历)", width = 15)
	private String highestGraduatedSchool;

	/**
	 * 学位(1.博士 2.硕士 3.学士 4.其他)
	 * */
	@Excel(name = "学位(1.博士 2.硕士 3.学士 4.其他)", width = 15,dicCode="degree")
	@Dict(dicCode = "degree")
	private Integer degree;

	/**
	 * 第一外语
	 * */
	@Excel(name = "第一外语", width = 15)
	private String fisrtForeignLanguage;

	/**
	 * 第二外语
	 * */
	@Excel(name = "第二外语", width = 15)
	private String secondForeignLanguage;

	/**
	 * 第三外语
	 * */
	@Excel(name = "第三外语", width = 15)
	private String thirdForeignLanguage;

	/**
	 * 是否工程院院士(1.是 2.不是)
	 * */
	@Excel(name = "是否工程院院士", width = 15)
	private Integer isNae;

	/**
	 * 是否中科院院士(Academician of the Chinese Academy of Sciences缩写)（1.是 2.不是）
	 * */
	@Excel(name = "是否中科院院士", width = 15)
	private Integer isAcas;

	/**
	 * 是否享受政府津贴(1.是 2.不是)
	 * */
	@Excel(name = "是否享受政府津贴", width = 15)
	private Integer enjoyGovernmentAllowances;

	/**
	 * 国家/省级突出贡献专家(1.是 2.不是)
	 * */
	@Excel(name = "国家/省级突出贡献专家", width = 15)
	private Integer outstandingContributionExperts;

	/**
	 * 研究方向
	 * */
	@Excel(name = "研究方向", width = 15)
	private String researchDirections;

	/**
	 * 获奖情况
	 * */
	@Excel(name = "获奖情况", width = 15)
	private String awardDetails;

	/**
	 * 主要学术成就
	 * */
	@Excel(name = "主要学术成就", width = 15)
	private String mainAcademicAchievement;

	/**
	 * 创建时间
	 * */
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createTime;

	/**
	 * 修改时间
	 * */
	private Date updateTime;

	/**
	 * 是否删除(0:正常 1:删除)
	 */
	private Integer isDelete;
}
