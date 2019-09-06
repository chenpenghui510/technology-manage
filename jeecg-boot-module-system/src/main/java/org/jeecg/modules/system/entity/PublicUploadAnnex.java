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
 * @Description: 公共附件表
 * @Author: jeecg-boot
 * @Date:   2019-08-06
 * @Version: V1.0
 */
@Data
@TableName("public_upload_annex")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PublicUploadAnnex {
    
	/**
	 * ID
	 * */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 附件类型(1.个人附件2.专家附件 3.企业附件 )
	 * */
	@Excel(name = "附件类型(1.个人附件2.专家附件 3.企业附件 )", width = 15)
	private Integer annexType;
	/**
	 * 对应外键ID
	 * */
	@Excel(name = "对应外键ID", width = 15)
	private String keyId;
	/**
	 * 附件名称
	 * */
	@Excel(name = "附件名称", width = 15)
	private String fileName;
	/**
	 * 附件路径
	 * */
	@Excel(name = "附件路径", width = 15)
	private String filePath;
	/**
	 * 对应字典涵义
	 * */
	@Excel(name = "对应字典涵义", width = 15)
	private Integer dictionary;
	/**
	 * 上传时间
	 * */
	@Excel(name = "上传时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date uploadTime;
	/**
	 * 是否必需(1.是 2.否)
	 * */
	@Excel(name = "是否必需(1.是 2.否)", width = 15)
	private Integer required;
}
