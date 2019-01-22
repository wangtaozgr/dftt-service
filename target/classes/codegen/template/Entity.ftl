package ${basePackageName}.model;

import com.atao.base.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Id;
<#list table.importTypeList as importType >
import ${importType};
</#list>

/**
 * ${table.remarks}
 *
 * @author ${author}
 */
public class ${table.className} extends BaseEntity {
<#macro field classType name remarks collumnName>
    /**
     * ${remarks}
     */
    @Column(name = "${collumnName}")
    private ${classType} ${name};
</#macro>
<#macro getSetMethod classType name upperName remarks>
    public ${classType} get${upperName}() {
        return ${name};
    }

    public void set${upperName}(${classType} ${name}) {
        this.${name} = ${name};
    }
</#macro>

<#list table.primaryKeyList as tf>
    /**
    * ${tf.remarks}
    */
    @Id
    @Column(name = "${tf.collumnName}")
    private ${tf.javaTypeName} ${tf.name};
</#list>

<#list table.fieldListWithoutKey as tf>
        <@field classType="${tf.javaTypeName}" name="${tf.name}" remarks="${tf.remarks}" collumnName="${tf.collumnName}" />
</#list>

<#list table.fieldList as tf>
    <@getSetMethod classType="${tf.javaTypeName}" name="${tf.name}"
        upperName="${tf.methodName}" remarks="${tf.remarks}" />
</#list>

    public static class TF {

        public static String TABLE_NAME = "${table.name}";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("${table.schemaPropName}");   // 库名

<#macro enumField name collumnName remarks>
        public static String ${name} = "${collumnName}";  // ${remarks}
</#macro>
<#list table.fieldList as field>
    <@enumField name="${field.name}" collumnName="${field.collumnName}" remarks="${field.remarks}" />
</#list>

    }
}
