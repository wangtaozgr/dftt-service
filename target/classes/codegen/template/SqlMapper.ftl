<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${basePackageName}.mapper.${table.className}Mapper">
<#macro result4Map javaName jdbcName jdbcType >
    <result property="${javaName}" column="${jdbcName}" jdbcType="${jdbcType}" />
</#macro>
    <resultMap id="BaseResultMap" type="${table.className}">
<#list table.primaryKeyList as tf>
    <id property="${tf.name}" column="${tf.collumnName}" jdbcType="${tf.jdbcTypeName}" />
</#list>

<#list table.fieldListWithoutKey as tf>
    <@result4Map javaName="${tf.name}" jdbcName="${tf.collumnName}" jdbcType="${tf.jdbcTypeName}" />
</#list>
</resultMap>

    <sql id="BaseColumnList">
    <#assign i = 0 />
        <#list table.fieldList as tf><#if i != 0>,</#if><#assign i = i + 1/><#if i == 6><#assign i = 1/>
        </#if>${tf.collumnName}</#list>
    </sql>


    <sql id="TableClause"> ${"$\{"}${table.schemaPropName}${"}"}.${table.name} </sql>


    <select id="queryByCriteria" resultMap="BaseResultMap" parameterType="CriteriaQuery">
        <include refid="public.Select4Query" />
    </select>

    <select id="countByCriteria" resultType="int" parameterType="CriteriaQuery">
        <include refid="public.Count4Query" />
    </select>

    <delete id="deleteByCriteria" parameterType="CriteriaQuery">
        <include refid="public.Delete4Query" />
    </delete>
</mapper>
