<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#no").focus();
			$("#inputForm").validate({
				rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
				messages: {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
					var ids = [], nodes = tree.getCheckedNodes(true);
					for(var i=0; i<nodes.length; i++) {
						ids.push(nodes[i].id);
					}
					$("#officeIds").val(ids);
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
			
			var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
				data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
					tree.checkNode(node, !node.checked, true, true);
					return false;
				}}};
			
			// 用户-机构
			var zNodes=[
					<c:forEach items="${officeList}" var="office">{id:"${office.id}", pId:"${not empty office.parent?office.parent.id:0}", name:"${office.name}"},
		            </c:forEach>];
			// 初始化树结构
			var tree = $.fn.zTree.init($("#officeTree"), setting, zNodes);
			// 不选择父节点
			tree.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
			// 默认选择节点
			var ids = "${user.officeIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", ids[i]);
				try{tree.checkNode(node, true, false);}catch(e){}
			}
			// 默认展开全部节点
			tree.expandAll(true);
			// 刷新（显示/隐藏）机构
			refreshOfficeTree();
			$("#dataScope").change(function(){
				refreshOfficeTree();
			});
		});
		function refreshOfficeTree(){
			if($("#dataScope").val()==9){
				$("#officeTree").show();
			}else{
				$("#officeTree").hide();
			}
		}
	</script>
</head>
<body>
<div class="whiteBoxNav">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/list">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${user.id}">用户<shiro:hasPermission name="sys:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		
		<div class="form-group">
			<label class="control-label col-md-2">头像：</label>
			<div class="col-md-8">
				<input id="photo" name="photo" type="file" accept="image/*" multiple="false" data-show-upload="false" data-show-caption="true" class="file-loadding">
				<script type="text/javascript">
				    var initialPreview = null;
					if("${user.photo}"){
						initialPreview = "<img src='${user.photo}' class='file-preview-image' alt='' title='头像图片'>";
					}
					//图片上传
				   $('#photo').fileinput({
					   previewFileType: "image",
					   initialPreview: [
							initialPreview
				        ],
				        overwriteInitial: false,
					    uploadUrl: "${ctx}/design/material/material/iconUpload", // server upload action
					    uploadAsync: false,
					    showUpload: false, // hide upload button
					    showRemove: false, // hide remove button
					    language: "zh",
					    allowedFileExtensions: ["jpg", "png"]
				   }).on("filebatchselected", function(event, files) {
					   $('#photo').fileinput("upload");
				   });
				</script>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">归属公司：</label>
			<div class="col-md-3">
				<sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
					title="公司" url="/sys/office/treeData?type=1" cssClass="form-control required"/>
			</div>
			<label class="control-label col-md-2">归属部门：</label>
			<div class="col-md-3">
				<sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">工号：</label>
			<div class="col-md-3">
				<form:input path="no" htmlEscape="false" maxlength="50" class="form-control required"/>
			</div>
			<label class="control-label col-md-2">性别：</label>
			<div class="col-md-3">
				<form:radiobuttons path="gender" items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false" class="" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">姓名：</label>
			<div class="col-md-3">
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
			</div>
			<label class="control-label col-md-2">登录名：</label>
			<div class="col-md-3">
				<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
				<form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control required userName"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">密码：</label>
			<div class="col-md-3">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="form-control ${empty user.id?'required':''}"/>
				<c:if test="${not empty user.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if>
			</div>
			<label class="control-label col-md-2">确认密码：</label>
			<div class="col-md-3">
				<input id="confirmNewPassword" class="form-control" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" equalTo="#newPassword"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">邮箱：</label>
			<div class="col-md-3">
				<form:input path="email" htmlEscape="false" maxlength="100" class="form-control email"/>
			</div>
			<label class="control-label col-md-2">电话：</label>
			<div class="col-md-3">
				<form:input path="phone" htmlEscape="false" maxlength="100" class="form-control"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">手机：</label>
			<div class="col-md-3">
				<form:input path="mobile" htmlEscape="false" maxlength="100" class="form-control"/>
			</div>
			<label class="control-label col-md-2">是否允许登录：</label>
			<div class="col-md-3">
				<form:select path="loginFlag" class="form-control">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">用户类型：</label>
			<div class="col-md-3">
				<form:select path="userType" class="form-control">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<label class="control-label col-md-2">数据范围：</label>
			<div class="col-md-3">
				<form:select path="dataScope" class="form-control">
					<form:options items="${fns:getDictList('sys_data_scope')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">数据授权：</label>
			<div class="col-md-8">
				<div id="officeTree" class="ztree" style="margin-left:100px;margin-top:3px;float:left;"></div>
				<form:hidden path="officeIds"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-2">备注信息：</label>
			<div class="col-md-8">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control"/>
			</div>
		</div>
		
		<c:if test="${not empty user.id}">
			<div class="form-group">
				<label class="control-label col-md-2">创建时间:</label>
				<div class="col-md-2">
					<span class="lbl"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></span>
				</div>
				<label class="control-label col-md-2">最后登陆:</label>
				<div class="col-md-4">
					<label class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></label>
				</div>
			</div>
		</c:if>
		<div class="form-group">
			<shiro:hasPermission name="sys:user:edit">
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