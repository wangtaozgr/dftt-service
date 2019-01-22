<#assign entityLable = table.remarks?replace("表", "") />
<#assign basePath = "${r'${ctx}'}/${moduleName}/${table.className}/" />
<#assign basePerm = "${moduleName}:${table.className}" />
<#assign entityIdRef = "${r'${'}entity.${table.primaryField.name}}" />
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${entityLable}管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascipt:void()">${entityLable}列表</a></li>
		<shiro:hasPermission name="${basePerm}:edit"><li><a href="${basePath}form" onclick="return lasySubmit(reForm,this.href);">${entityLable}添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="entry" action="${basePath}" method="post" class="searchForm breadcrumb form-search form-horizontal">
        <input id="pageNo" name="pageNo" type="hidden" value="${r'${page.pageNo}'}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${r'${page.pageSize}'}"/>
	${r'${paramCover.unCoveredInputs}'}
        <div id="searchDiv">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
        </div>
	</form:form>
	<tags:message />
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<#list table.fieldList as field>
			<th>${field.remarks}</th>
		</#list>
			<shiro:hasPermission name="${basePerm}:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${r'${page.list}'}" var="entity">
			<tr>
				<#assign isFirst = 1 />
		<#list table.fieldList as field>
			<#if isFirst == 1 >
                <td><a href="${basePath}form?isView=1&id=${entityIdRef}">${r'${'}entity.${field.name}}</a></td>
			<#assign isFirst = 0 />
			<#elseif field.javaTypeName == 'Date'>
			<td><fmt:formatDate value="${r'${'}entity.${field.name}}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<#else>
			<td>${r'${'}entity.${field.name}}</td>
			</#if>
		</#list>
            <shiro:hasPermission name="${basePerm}:edit"><td>
                <a href="${basePath}form?id=${entityIdRef}" onclick="return lasySubmit(reForm,this.href);">修改</a>
                <a href="${basePath}delete?id=${entityIdRef}"
                   onclick="return lasyConfirm('确认要删除该${entityLable}吗？', reForm,this.href);">删除</a>
            </td></shiro:hasPermission>
        </tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${r'${page}'}</div>
    <form name="reForm" method="post">
		${r'${paramCover.coveredInputs}'}
    </form>
</body>
</html>