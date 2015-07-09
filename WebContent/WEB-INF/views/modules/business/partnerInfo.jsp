<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>运营商订单信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/business/recharge/list">充值管理列表</a></li>
		<li class="active"><a href="${ctx}/business/recharge/partnerInfo?partnerOrderNo=${partner.partnerOrderNo}">订单信息</a></li>
	</ul>
	<form:form class="form-horizontal">
		<sys:message content="${message}"/>
		<fieldset>
			<legend>订单详情</legend>
			<table class="table-form">
				<tr>
					<td class="tit">订单号</td><td>${partner.orderNo}</td>
					<td class="tit">合作方ID</td><td>${partner.partnerId}</td>
					<td class="tit">合作方单号</td><td>${partner.partnerOrderNo}</td>
				</tr>
				<tr>
					<td class="tit">手机号</td>
					<td class="tit">${partner.partnerOrderNo}</td>
					<td class="tit">运营商</td>
					<td>${partner.operators}</td>
					<td class="tit">商品ID</td>
					<td>${partner.productId}</td>
				</tr>
				<tr>
					<td class="tit">商品名称</td>
					<td>${partner.productName}</td>
					<td class="tit">流量(MB)</td>
					<td>${partner.flowSize}</td>
					<td class="tit">单价</td>
					<td>${partner.fee}</td>
				</tr>
				<tr>
					<td class="tit">流量类型</td>
					<td>${partner.flowType}</td>
					<td class="tit">合计金额</td>
					<td>${partner.totalFee}</td>
					<td class="tit">订单状态</td>
					<td>${partner.status}</td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
