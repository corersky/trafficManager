<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账户充值</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sys/userRecharge/list");
			$("#searchForm").submit();
	    	return false;
	    }
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/userRecharge/list">账户充值纪录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="userRecharge" action="${ctx}/sys/userRecharge/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>充值时间：</label>
				<input id="beginDate"  name="beginDate"  type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" 
					value="<fmt:formatDate value="${userRecharge.beginDate}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
					　--　
				<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" 
					value="<fmt:formatDate value="${userRecharge.endDate}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th width="55px">序号</th><th>充值金额</th><th class="sort-column login_name">操作员</th><th>备注</th><th class="sort-column name">充值时间</th><shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="recharge" varStatus="status">
			<tr>
				<td>${status.index }</td>
				<td>${recharge.balance}</td>
				<td>${recharge.createBy.name}</td>
				<td>${recharge.remarks}</td>
				<td><fmt:formatDate value="${recharge.createDate}" type="both"/></td>
				<td>
					详情
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>