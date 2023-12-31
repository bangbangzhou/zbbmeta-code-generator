package com.zbbmeta.entity;

import lombok.Data;

/**
 * 列对象
 */
@Data
public class Column {

	//列名
	private String columnName;
	//列名类型
	private String dataType;
	//列名备注
	private String comments;

	//属性名称(第一个字母大写)，如：user_name => UserName
	private String upperFirstAttrName;
	//属性名称(第一个字母小写)，如：user_name => userName
	private String lowerFirstAttrName;
	//属性类型
	private String attrType;
	//auto_increment
	private String extra;
}
