package com.winhxd.b2c.common.domain.promotion.util;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * 基类重写toString,hashCode,equals
 * 
 * @author sunlulu
 *
 */
public class BaseDomain implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("操作人")
	private Long operator;// 操作人
	@ApiModelProperty("操作人姓名")
	private String operatorName;// 操作人姓名
	@ApiModelProperty("操作授权")
	private String operatorAuthorization; //操作授权
	
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorAuthorization() {
		return operatorAuthorization;
	}

	public void setOperatorAuthorization(String operatorAuthorization) {
		this.operatorAuthorization = operatorAuthorization;
	}
}
