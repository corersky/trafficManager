<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
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
		});
	</script>
</head>
<body>
<div class="whiteBoxNav">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/user/info">个人信息</a></li>
		<li><a href="${ctx}/sys/user/modifyPwd">修改密码</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/info" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<sys:message content="${message}"/>
		<fieldset>
		<div class="form-group">
			<label class="control-label col-md-2">头&nbsp;&nbsp;&nbsp;&nbsp;像:</label>
			<div class="col-md-3">
				<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">归属公司:</label>
			<div class="col-md-3">
				<span class="form-control">${user.company.name}</span>
			</div>
			<label class="control-label col-md-2">归属部门:</label>
			<div class="col-md-3">
				<span class="form-control">${user.office.name}</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">姓&nbsp;&nbsp;&nbsp;&nbsp;名:</label>
			<div class="col-md-3">
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required" readonly="true"/>
			</div>
			<label class="control-label col-md-2">邮&nbsp;&nbsp;&nbsp;&nbsp;箱:</label>
			<div class="col-md-3">
				<form:input path="email" htmlEscape="false" maxlength="50" class="form-control email"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">电&nbsp;&nbsp;&nbsp;&nbsp;话:</label>
			<div class="col-md-3">
				<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
			<label class="control-label col-md-2">手&nbsp;&nbsp;&nbsp;&nbsp;机:</label>
			<div class="col-md-3 ">
				<form:input path="mobile" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">备&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
			<div class="col-md-8">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">用户类型:</label>
			<div class="col-md-3">
				<span class="form-control">${fns:getDictLabel(user.userType, 'sys_user_type', '无')}</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">用户角色:</label>
			<div class="col-md-8 ">
				<span class="form-control">${user.roleNames}</span>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">上次登录:</label>
			<div class="col-md-8">
				<label class="lbl">IP: ${user.oldLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.oldLoginDate}" type="both" dateStyle="full"/></label>
			</div>
		</div>
		<div class="form-group">
			     <label class="control-label col-md-4"></label>
				 <div class="col-md-6">
					<button class="btn btn-success" type="submit" id="ok">保 存</button>
					<button class="btn btn-info" type="reset" id ="no">重 置</button>
		            <input id="btnCancel" class="btn btn-warning" type="button" value="返 回" onclick="history.go(-1)"/>
				 </div>
		</div>
	   </fieldset>
	</form:form>
</div>
</body>
</html>