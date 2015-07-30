<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加策略</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function validate(){
			var result = $("#inputForm").valid(); 
			return result;
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="tactics" action="${ctx}/sys/tactics/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">用户:</label>
			<div class="controls">
				<sys:treeselect id="tactics" name="user.id" value="${tactics.user.id}" labelName="user.name" labelValue="${tactics.user.name}"
				title="用户" url="/sys/office/treeData?type=3" cssClass="input-small required" notAllowSelectParent="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">运营商:</label>
			<div class="controls">
				<form:select path="feeType" class="input-medium required">
					<form:option value="">全部</form:option>
					<form:options items="${fns:getDictList('recharge_operators')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最小消费:</label>
			<div class="controls">
				<form:input path="minConsume" htmlEscape="false" maxlength="50" cssClass="input-small required"/>
				<span></span>
				最大消费：
				<form:input path="maxConsume" htmlEscape="false" maxlength="50" cssClass="input-small"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商务费率:</label>
			<div class="controls">
				<form:input path="feeRate" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
	</form:form>
</body>
</html>