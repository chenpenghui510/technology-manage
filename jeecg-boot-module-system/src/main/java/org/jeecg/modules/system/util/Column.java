package org.jeecg.modules.system.util;

public class Column {
	private String field;
	private String fieldName;
	private boolean fontRed;

	public Column(String field, String fieldName, boolean fontRed) {
		super();
		this.field = field;
		this.fieldName = fieldName;
		this.fontRed = fontRed;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isFontRed() {
		return fontRed;
	}

	public void setFontRed(boolean fontRed) {
		this.fontRed = fontRed;
	}

	/**
	 * 
	 * [（适用于导出数据）]<br>
	 *
	 * @2018年1月2日下午5:26:13<br>
	 * @param column
	 * @param columnName
	 * @return
	 */
	public static Column getInstance(String field, String fieldName) {
		return new Column(field, fieldName, false);
	}

	/**
	 * 
	 * [（适用于导出某列需要表红的数据）]<br>
	 *
	 * @2018年1月10日上午11:47:48<br>
	 * @param field
	 * @param fieldName
	 * @param fontRed
	 * @return
	 */
	public static Column getInstance(String field, String fieldName, boolean fontRed) {
		return new Column(field, fieldName, fontRed);
	}

	/**
	 * 
	 * [（适用于导出模板）]<br>
	 *
	 * @2018年1月10日上午11:46:32<br>
	 * @param fieldName
	 *            字段名
	 * @param fontRed
	 *            是否标红
	 * @return
	 */
	public static Column getInstance(String fieldName, boolean fontRed) {
		return new Column("", fieldName, fontRed);
	}

}
