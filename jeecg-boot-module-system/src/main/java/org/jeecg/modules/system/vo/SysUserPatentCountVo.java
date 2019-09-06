package org.jeecg.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
public class SysUserPatentCountVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**userName*/
    @Excel(name = "年度", width = 15)
    @ApiModelProperty(value = "年度")
    private Integer year;
    /**对应的用户ID*/
    @ApiModelProperty(value = "对应的用户ID")
    private java.lang.String userId;
    /**userName*/
    @Excel(name = "企业名称", width = 15)
    @ApiModelProperty(value = "企业名称")
    private java.lang.String userName;
    /**userName*/
    @Excel(name = "专利授权总数", width = 15)
    @ApiModelProperty(value = "专利授权总数")
    private Integer all;
    /**userName*/
    @Excel(name = "有效发明专利", width = 15)
    @ApiModelProperty(value = "有效发明专利")
    private Integer invent;
    /**userName*/
    @Excel(name = "有效实用新型专利", width = 15)
    @ApiModelProperty(value = "有效实用新型专利")
    private Integer practical;
    /**userName*/
    @Excel(name = "有效外观专利", width = 15)
    @ApiModelProperty(value = "有效外观专利")
    private Integer exterior;
    /**userName*/
    @Excel(name = "有效国际专利", width = 15)
    @ApiModelProperty(value = "有效国际专利")
    private Integer international;
    /**userName*/
    @Excel(name = "国家金奖", width = 15)
    @ApiModelProperty(value = "国家金奖")
    private Integer countrySpecial;
    /**userName*/
    @Excel(name = "国家一等奖", width = 15)
    @ApiModelProperty(value = "国家一等奖")
    private Integer countryOne;
    /**userName*/
    @Excel(name = "国家二等奖", width = 15)
    @ApiModelProperty(value = "国家二等奖")
    private Integer countryTwo;
    /**userName*/
    @Excel(name = "省特等奖", width = 15)
    @ApiModelProperty(value = "省特等奖")
    private Integer provinceSpecial;
    /**userName*/
    @Excel(name = "省一等奖", width = 15)
    @ApiModelProperty(value = "省一等奖")
    private Integer provinceOne;
    /**userName*/
    @Excel(name = "省二等奖", width = 15)
    @ApiModelProperty(value = "省二等奖")
    private Integer provinceTwo;
    /**数量结束开始企业信息*/
    /**userName*/
    @Excel(name = "是否国家级知识产权优势企业", width = 15,dicCode = "nationalAdvantage")
    @ApiModelProperty(value = "是否国家级知识产权优势企业")
    private Integer nationalAdvantage;
    /**userName*/
    @Excel(name = "是否省级知识产权优势企业", width = 15,dicCode = "provincialAdvantage")
    @ApiModelProperty(value = "是否省级知识产权优势企业")
    private Integer provincialAdvantage;
    /**userName*/
    @Excel(name = "是否市级知识产权优势企业", width = 15,dicCode = "cityAdvantage")
    @ApiModelProperty(value = "是否市级知识产权优势企业")
    private Integer cityAdvantage;
    /**贯标时间*/
    @Excel(name = "贯标时间", width = 20, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "贯标时间")
    private java.util.Date isoTime;
    /**知识产权被侵权是否：0否，1是*/
    @Excel(name = "知识产权被是否侵权", width = 15,dicCode = "yesOrNo")
    @ApiModelProperty(value = "知识产权被是否侵权")
    private java.lang.Integer violate;
    /**融资金额*/
    @Excel(name = "融资金额", width = 15)
    @ApiModelProperty(value = "融资金额")
    private java.lang.Double financingAmount;
    /**融资年度*/
    @Excel(name = "融资年度", width = 20, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "融资年度")
    private java.util.Date financingTime;
    /**政府资助贴息金额*/
    @Excel(name = "政府资助贴息金额", width = 15)
    @ApiModelProperty(value = "政府资助贴息金额")
    private java.lang.Double governmentAid;
}
