<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>充值管理添加</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			$("#searchForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function page(n,s){
			$("#searchForm").attr("action","${ctx}/business/recharge/rechargeList");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/business/recharge/list">充值管理列表</a></li>
		<li class="active"><a href="${ctx}/business/recharge/form?id=${recharge.id}">充值管理<shiro:hasPermission name="business:recharge:edit">${not empty area.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="business:recharge:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="searchForm" modelAttribute="recharge" action="${ctx}/business/recharge/rechargeList" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">客户手机号:</label>
			<div class="controls">
				<form:input path="mobile" htmlEscape="false" maxlength="50" class="required"/>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			</div>
		</div>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
			<th>商品名称</th>
			<th>运营商</th>
			<th class="sort-column login_name">城市类型</th>
			<th class="sort-column name">省市名称</th>
			<th>单价</th>
			<th>流量大小</th>
			<th>状态</th>
			<th>支持的网络类型</th>
			<th>可用库存</th>
			<shiro:hasPermission name="business:recharge:edit">
				<th>操作</th>
			</shiro:hasPermission>
		</tr></thead>
		<tbody>
		<c:forEach items="${list}" var="product">
			<tr>
				<td>${product.name}</td>
				<td>${product.operators}</td>
				<td>${product.cityType}</td>
				<td>${product.cityName}</td>
				<td>${product.fee}</td>
				<td>${product.flowSize }MB</td>
				<td>${product.status }</td>
				<td>${product.tagNet }</td>
				<td>${product.salesCount }</td>
				<shiro:hasPermission name="business:recharge:edit"><td>
    				<a href="${ctx}/business/recharge/form?id=${user.id}">查看</a>
					<a href="${ctx}/business/recharge/form?id=${user.id}">购买</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="form-actions">
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
</body>
</html>