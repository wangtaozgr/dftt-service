package ${basePackageName}.api.vo;

import com.atao.base.vo.BaseVo;
<#list table.importTypeList as importType >
import ${importType};
</#list>

/**
 * ${table.remarks}
 *
 * @author ${author}
 */
public class ${table.className}Vo extends BaseVo {
<#macro field classType name remarks collumnName>
    /**
     * ${remarks}
     */
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
    private ${tf.javaTypeName} ${tf.name};
</#list>

<#list table.fieldListWithoutKey as tf>
    <#if tf.collumnName != "DEL_FLAG">
        <@field classType="${tf.javaTypeName}" name="${tf.name}" remarks="${tf.remarks}" collumnName="${tf.collumnName}" />
    </#if>
</#list>

<#list table.fieldList as tf>
    <#if tf.collumnName != "DEL_FLAG">
    <@getSetMethod classType="${tf.javaTypeName}" name="${tf.name}"
        upperName="${tf.methodName}" remarks="${tf.remarks}" />
    </#if>
</#list>

    public static class TF {
        <#macro enumField name collumnName remarks>
        public static String ${name} = "${collumnName}";  // ${remarks}
        </#macro>
        <#list table.fieldList as field>
            <@enumField name="${field.name}" collumnName="${field.name}" remarks="${field.remarks}" />
        </#list>

    }

}
