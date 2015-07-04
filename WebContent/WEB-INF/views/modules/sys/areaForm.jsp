<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
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
		});
	</script>
</head>
<body>
<div class="whiteBoxNav">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/area/">区域列表</a></li>
		<li class="active"><a href="form?id=${area.id}&parent.id=${area.parent.id}">区域<shiro:hasPermission name="sys:area:edit">${not empty area.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:area:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="area" action="${ctx}/sys/area/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		
		<div class="form-group">
			<label class="control-label col-md-2">区域编码：</label>
			<div class="col-md-3">
				<form:input path="code" htmlEscape="false" maxlength="50" class="form-control required"/>
			</div>
			<label class="control-label col-md-2">区域名称：</label>
			<div class="col-md-3">
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">省市编码：</label>
			<div class="col-md-3">
				<form:input path="province" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
			<label class="control-label col-md-2">县区编码：</label>
			<div class="col-md-3">
				<form:input path="city" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">区域类型：</label>
			<div class="col-md-3">
				<form:select path="type" class="input-medium">
					<form:options items="${fns:getDictList('sys_area_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<label class="control-label col-md-2">区号：</label>
			<div class="col-md-3">
				<form:input path="areaCode" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">备注信息：</label>
			<div class="col-md-8">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<shiro:hasPermission name="sys:area:edit">
			    <label class="control-label col-md-2"></label>
				<div class="col-md-3">
					<button class="btn btn-success" type="submit" id="ok">保 存</button>
					<button class="btn btn-info" type="reset" id="no">重 置</button>
					<input id="btnCancel" class="btn btn-warning" type="button" value="返 回" onclick="history.go(-1)"/>
				</div>
			</shiro:hasPermission>
		</div>
	</form:form>
</div>
</body>
</html>