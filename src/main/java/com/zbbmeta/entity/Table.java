package com.zbbmeta.entity;

import lombok.Data;

import java.util.List;

/**
 * @author springboot葵花宝典
 * @description: TODO
 */
@Data
public class Table {
    //表的名称
    private String tableName;
    //表的备注
    private String comments;
    //表的主键
    private Column pk;
    //表的列名(不包含主键)
    private List<Column> columns;

    //类名(第一个字母大写)，如：sys_user => SysUser
    private String upperFirstClassName;
    //类名(第一个字母小写)，如：sys_user => sysUser
    private String lowerFirstClassname;
}
