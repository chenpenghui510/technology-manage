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
 * @Description: 表单组件
 * @Author: jeecg-boot
 * @Date:   2019-07-16
 * @Version: V1.0
 */
@Data
@TableName("form_component")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FormComponent {
    
	/**
	 * ID
	 * */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 所属表单ID
	 * */
	@Excel(name = "所属表单ID", width = 15)
	private Integer formId;
	/**
	 * 组件名称
	 * */
	@Excel(name = "组件名称", width = 15)
	private String label;
	/**
	 * 组件类型
	 * */
	@Excel(name = "组件类型", width = 15)
	private Integer type;
	/**
	 * 选项内容(JSON字符)
	 * */
	@Excel(name = "选项内容(JSON字符)", width = 15)
	private String selectionOption;
	/**
	 * 必填(1.是 2.不是)
	 * */
	@Excel(name = "必填(1.是 2.不是)", width = 15)
	private Integer isRequired;
	/**
	 * 正则
	 * */
	@Excel(name = "正则", width = 15)
	private Integer regular;
	/**
	 * 排序
	 * */
	@Excel(name = "排序", width = 15)
	private Integer indexNumber;
	/**
	 * 最大长度
	 * */
	@Excel(name = "最大长度", width = 15)
	private Integer maxLength;
	/**
	 * 创建时间
	 * */
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;

	/**
	 * 删除状态（1.正常 2.已删除）
	 */
	private Integer delFlag;

	/**
	 * 提示信息
	 */
	@Excel(name="提示信息",width=15)
	private String placeholder;
}
