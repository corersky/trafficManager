<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>客户充值</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/business/recharge/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/business/recharge/list">客户充值列表</a></li>
		<shiro:hasPermission name="business:recharge:edit"><li><a href="${ctx}/business/recharge/form">客户充值添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="recharge" action="${ctx}/business/recharge/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>手机号：</label><form:input path="mobile" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>业务类型：</label><form:input path="type" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>充值状态：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('business_recharge_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="clearfix"></li>
			<li><label>充值时间：</label>
				<input id="beginDate"  name="beginDate"  type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" 
					value="<fmt:formatDate value="${recharge.beginDate}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
					　--　
				<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" 
					value="<fmt:formatDate value="${recharge.endDate}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			</li>
			<c:if test="${fns:getUser().admin }">
				<li><label>充值人员：</label>
					<sys:treeselect id="oaNotifyRecord" name="oaNotifyRecordIds" value="${oaNotify.oaNotifyRecordIds}" labelName="oaNotifyRecordNames" labelValue="${oaNotify.oaNotifyRecordNames}"
						title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" notAllowSelectParent="true" />
				</li>
			</c:if>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>手机号</th><th>流量</th><th class="sort-column login_name">业务类型</th><th class="sort-column name">状态</th><th>操作员</th><th>充值时间</th><shiro:hasPermission name="business:recharge:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="recharge">
			<tr>
				<td>${recharge.mobile}</td>
				<td>${recharge.flowSize}</td>
				<td>${recharge.type}</td>
				<td>${fns:getDictLabel(recharge.status, 'recharge_status', '')}</td>
				<td>${recharge.createBy.name}</td>
				<td><fmt:formatDate value="${recharge.createDate}" type="both"/></td>
				<shiro:hasPermission name="business:recharge:edit"><td>
    				<a href="${ctx}/business/recharge/partnerInfo?partnerOrderNo=${recharge.partnerOrderNo}">查看</a>
					<a href="${ctx}/business/recharge/delete?id=${recharge.id}" onclick="return confirmx('确认要删除该充值记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
</body>
</html>