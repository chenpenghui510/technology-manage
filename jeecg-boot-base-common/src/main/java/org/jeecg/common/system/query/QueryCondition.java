package org.jeecg.common.system.query;

import java.io.Serializable;

public class QueryCondition implements Serializable {

	private static final long serialVersionUID = 4740166316629191651L;
	
	private String field;
	private String type;
	private String rule;
	private String val;
	private String byValue;
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = QueryRuleEnum.getByValue(rule).getValue();
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getByValue() {
		return byValue;
	}

	public void setByValue(String val) {
		this.byValue = QueryRuleEnum.getByValue(val).getValue();
	}
	@Override
	public String toString(){
		StringBuffer sb =new StringBuffer();
		if(field == null || "".equals(field)){
			return "";
		}
		sb.append(this.field).append(" ").append(this.rule).append(" ").append(this.type).append(" ").append(this.val);
		return sb.toString();
	}
}
