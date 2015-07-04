<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
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
			
			//事件监听
			$('#officd_type').change(function(){//类型改变  页面修改
				if($(this).val()=="1"){
					//重置公司独有信息
					$('#officeDiv input').each(function(){
						$(this).val('');
						$(this).attr("disabled",false);
					});
					$('#officeDiv').show();
				}else{
					$('#officeDiv input').each(function(){
						$(this).attr("disabled",true);
					});
					$('#officeDiv').hide();
				}
			});
			init();
		});
		//页面初始化
		function init(){
			if("${office.id}"){
				if(ofType=='1'){
					$('#officeDiv').show();
				}else{
					$('#officeDiv input').each(function(){
						$(this).attr("disabled",true);
					});
					$('#officeDiv').hide();
				}
			}else{
				$('#officd_type').trigger('change');
			}
		}
	</script>
</head>
<body>
<div class="whiteBoxNav">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/office/list?parent.id=${office.id}">机构列表</a></li>
		<li class="active"><a href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}">机构<shiro:hasPermission name="sys:office:edit">${not empty office.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:office:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<!-- 公共信息 -->
		<fieldset>
			<div class="form-group">
				<label class="control-label col-md-2">上级机构：</label>
				<div class="col-md-3">
					<sys:treeselect id="parent" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
						title="机构" url="/sys/office/treeData" extId="${office.id}" cssClass="form-control" allowClear="${office.currentUser.admin}"/>
				</div>
				<label class="control-label col-md-2">归属区域：</label>
				<div class="col-md-3">
					<sys:treeselect id="area" name="area.id" value="${office.area.id}" labelName="area.name" labelValue="${office.area.name}"
						title="区域" url="/sys/area/treeData" cssClass="form-control required"/>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-2">机构类型：</label>
				<div class="col-md-3">
					<form:select path="type" class="form-control" id="officd_type">
						<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<label class="control-label col-md-2">机构级别：</label>
				<div class="col-md-3">
					<form:select path="grade" class="form-control">
						<form:options items="${fns:getDictList('sys_office_grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			
			<div class="form-group">
				<label class="control-label col-md-2">机构名称：</label>
				<div class="col-md-3">
					<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
				</div>
				<label class="control-label col-md-2">流程机构：</label>
				<div class="col-md-3">
					<form:select path="actType" class="form-control">
						<form:option value="">请选择</form:option>
						<form:option value="1">市场部</form:option>
						<form:option value="2">设计部</form:option>
					</form:select>
				</div>
			</div>
		</fieldset>
		<!-- 公司独有 -->
		<fieldset id="officeDiv">
		
			<div class="form-group">
				<label class="control-label col-md-2">工商注册名：</label>
				<div class="col-md-3">
					<form:input path="alias" htmlEscape="false" maxlength="50" class="form-control required"/>
				</div>
				<label class="control-label col-md-2">税号：</label>
				<div class="col-md-3">
					<form:input path="taxNum" htmlEscape="false" maxlength="50" class="form-control required"/>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-2">机构编码：</label>
				<div class="col-md-3">
					<form:input path="code" htmlEscape="false" maxlength="50" class="form-control required"/>
				</div>
				<label class="control-label col-md-2">是否可用：</label>
				<div class="col-md-3">
					<form:select path="useable" cssClass="form-control">
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-2">法人：</label>
				<div class="col-md-3">
					<form:input path="primaryPerson" htmlEscape="false" maxlength="50" class="form-control required"/>
				</div>
				<label class="control-label col-md-2">总经理：</label>
				<div class="col-md-3">
					<sys:treeselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="deputyPerson.name" labelValue="${office.deputyPerson.name}"
						title="用户" url="/sys/office/treeData?type=3" allowClear="true" notAllowSelectParent="true"  cssClass="form-control"/>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-2">联系地址：</label>
				<div class="col-md-3">
					<form:input path="address" htmlEscape="false" maxlength="50" class="form-control"/>
				</div>
				<label class="control-label col-md-2">邮政编码：</label>
				<div class="col-md-3">
					<form:input path="zipCode" htmlEscape="false" maxlength="50" class="form-control"/>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-2">总经理电话：</label>
				<div class="col-md-3">
					<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control"/>
				</div>
				<label class="control-label col-md-2">公司电话：</label>
				<div class="col-md-3">
					<form:input path="gsphone" htmlEscape="false" maxlength="50" class="form-control"/>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-2">传真：</label>
				<div class="col-md-3">
					<form:input path="fax" htmlEscape="false" maxlength="50" class="form-control"/>
				</div>
				<label class="control-label col-md-2">邮箱：</label>
				<div class="col-md-3">
					<form:input path="email" htmlEscape="false" maxlength="50" class="form-control"/>
				</div>
			</div>
		</fieldset>
		<div class="form-group">
			<label class="control-label col-md-2">备注信息：</label>
			<div class="col-md-8">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control"/>
			</div>
		</div>
		
		<div class="form-group">
			<shiro:hasPermission name="sys:office:edit">
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