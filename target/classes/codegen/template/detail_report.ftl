<?xml version="1.0" encoding="utf-8"?>
<trans name="detail" desc="${table.remarks}-详情">
    <snd>
        <field name="${table.primaryField.name}" desc="id"/>
    </snd>
    <rcv>
    <#macro enumField collumnName remarks>
        <field name="${collumnName}" desc="${remarks}"/>
    </#macro>
    <#list table.fieldList as field>
        <@enumField collumnName="${field.name}" remarks="${field.remarks}"/>
    </#list>
    </rcv>
</trans>