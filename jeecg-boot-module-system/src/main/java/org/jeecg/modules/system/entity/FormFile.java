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
 * @Description: 表单上传材料
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
@Data
@TableName("form_file")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FormFile {
    
	/**
	 * 主键ID
	 * */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 表单模板ID
	 * */
	@Excel(name = "表单模板ID", width = 15)
	private Integer formId;

	/**
	 * 附件名称
	 * */
	@Excel(name = "附件名称", width = 15)
	private String fileName;

	/**
	 * 附件模板地址
	 * */
	@Excel(name = "附件模板地址", width = 15)
	private String url;

	/**
	 * 上传类型(1.管理员上传 2.用户上传)
	 * */
	@Excel(name = "上传类型(1.管理员上传 2.用户上传)", width = 15)
	private Integer uploadType;

	/**
	 * 创建时间
	 * */
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 删除状态(1.正常 2.已删除)
	 * */
	@Excel(name = "删除状态", width = 15)
	private Integer delFlag;
}
