package ${package}.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;



<#if hasBigDecimal>
    import java.math.BigDecimal;
</#if>


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ${comments}
 * @author ${author}
 */
@Data
@TableName("${tableName}")
public class ${className}Entity implements Serializable {
	private static final long serialVersionUID = 1L;

<#list columns as column>

    /**
    * ${column.comments}
    */
    <#if column.columnName == pk.columnName>
    @TableId
    </#if>
    private ${column.attrType} ${column.lowerFirstAttrName};
</#list>



}
