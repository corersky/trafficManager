<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
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
		<li><a href="${ctx}/sys/dict/">字典列表</a></li>
		<li class="active"><a href="${ctx}/sys/dict/form?id=${dict.id}">字典<shiro:hasPermission name="sys:dict:edit">${not empty dict.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:dict:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		
		<div class="form-group">
			<label class="control-label col-md-2">键&nbsp;&nbsp;&nbsp;&nbsp;值：</label>
			<div class="col-md-3">
				<form:input path="value" htmlEscape="false" maxlength="50" type="number" class="form-control required" placeholder="从1开始的不重复的数字"/>
			</div>
			<label class="control-label col-md-2">标&nbsp;&nbsp;&nbsp;&nbsp;签：</label>
			<div class="col-md-3">
				<form:input path="label" htmlEscape="false" maxlength="50" class="form-control required" placeholder="中文名称"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">类&nbsp;&nbsp;&nbsp;&nbsp;型：</label>
			<div class="col-md-3">
				<form:input path="type" htmlEscape="false" maxlength="50" class="form-control required" placeholder="字典类型(英文字符)"/>
			</div>
			<label class="control-label col-md-2">排&nbsp;&nbsp;&nbsp;&nbsp;序：</label>
			<div class="col-md-3">
				<form:input path="sort" htmlEscape="false" maxlength="11" class="form-control required" placeholder="从10开始数字" type="number"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">备注信息：</label>
			<div class="col-md-8">
				<form:textarea path="description" htmlEscape="false" rows="4" maxlength="255" class="form-control" placeholder="备注说明"/>
			</div>
		</div>
		<div class="form-group">
			<shiro:hasPermission name="sys:dict:edit">
			     <label class="control-label col-md-4"></label>
				 <div class="col-md-6">
					<button class="btn btn-success" type="submit" id="ok">保 存</button>
					<button class="btn btn-info" type="reset" id ="no">重 置</button>
		            <input id="btnCancel" class="btn btn-warning" type="button" value="返 回" onclick="history.go(-1)"/>
				 </div>
            </shiro:hasPermission>
		</div>
	</form:form>
</div>
</body>
</html>