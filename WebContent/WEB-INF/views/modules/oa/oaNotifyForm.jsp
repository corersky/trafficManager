<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
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
		<li><a href="${ctx}/oa/oaNotify/">通知列表</a></li>
		<li class="active"><a href="${ctx}/oa/oaNotify/form?id=${oaNotify.id}">通知<shiro:hasPermission name="oa:oaNotify:edit">${oaNotify.status eq '1' ? '查看' : not empty oaNotify.id ? '修改' : '添加'}</shiro:hasPermission><shiro:lacksPermission name="oa:oaNotify:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaNotify" enctype="multipart/form-data" action="${ctx}/oa/oaNotify/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="form-group">
			<label class="control-label col-md-2">类型：</label>
			<div class="col-md-3">
				<form:select path="type" class="form-control required">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>	
		<div class="form-group">
			<label class="control-label col-md-2">标题：</label>
			<div class="col-md-8">
				<form:input path="title" htmlEscape="false" maxlength="200" class="form-control required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">内容：</label>
			<div class="col-md-8">
				<form:textarea path="content" htmlEscape="false" rows="6" maxlength="2000" class="form-control required"/>
			</div>
		</div>
		<c:if test="${oaNotify.status ne '1'}">
			<div class="form-group">
				<label class="control-label col-md-2">附件：</label>
				<div class="col-md-8">
					<input id="files" name="file" type="file" multiple="true" data-show-upload="false" data-show-caption="true" class="file-loadding">
					<script type="text/javascript">
						//图片上传
					   $('#files').fileinput({
						    showPreview: false,
			                overwriteInitial: true,
						    showUpload: false, // hide upload button
						    showRemove: false, // hide remove button
						    language: "zh",
						    allowedFileExtensions: ["zip", "rar", "gz", "tgz"]
					   });
					</script>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-2">状态：</label>
				<div class="col-md-3">
					<form:radiobuttons path="status" items="${fns:getDictList('oa_notify_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
					<span class="help-inline"><font color="red">*</font> 发布后不能进行操作。</span>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-2">接受人：</label>
				<div class="col-md-8">
	                <sys:treeselect id="oaNotifyRecord" name="oaNotifyRecordIds" value="${oaNotify.oaNotifyRecordIds}" labelName="oaNotifyRecordNames" labelValue="${oaNotify.oaNotifyRecordNames}"
						title="用户" url="/sys/office/treeData?type=3" cssClass="form-control required" notAllowSelectParent="true" checked="true"/>
				</div>
			</div>
		</c:if>
		<c:if test="${oaNotify.status eq '1'}">
			<div class="form-group">
				<label class="control-label col-md-2">附件：</label>
				<div class="col-md-8">
					<input id="files" name="file" type="file" multiple="false" data-show-upload="false" data-show-caption="true" class="file-loadding">
					<script type="text/javascript">
						var initialPreview = null;
						if("${oaNotify.files}"){
							var file = "${oaNotify.files}";
							file = file.substring(file.lastIndexOf("/")+1, file.length);
							initialPreview="<div class='file-preview-text'><h2><i class='glyphicon glyphicon-file'></i></h2> <a href='${oaNotify.files}' target='_blank'>" +
							file + "</div>";
						}
						//图片上传
					   $('#files').fileinput({
						    initialPreview: [
								initialPreview
			                ],
			                overwriteInitial: true,
						    showUpload: false, // hide upload button
						    showRemove: false, // hide remove button
						    language: "zh",
						    allowedFileExtensions: ["zip", "rar", "gz", "tgz"]
					   });
					</script>
				</div>
			</div>
			
			<div class="form-group">
				<label class="control-label col-md-2">接受人：</label>
				<div class="col-md-8">
					<table id="contentTable" class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th>接受人</th>
								<th>接受部门</th>
								<th>阅读状态</th>
								<th>阅读时间</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${oaNotify.oaNotifyRecordList}" var="oaNotifyRecord">
							<tr>
								<td>
									${oaNotifyRecord.user.name}
								</td>
								<td>
									${oaNotifyRecord.user.office.name}
								</td>
								<td>
									${fns:getDictLabel(oaNotifyRecord.readFlag, 'oa_notify_read', '')}
								</td>
								<td>
									<fmt:formatDate value="${oaNotifyRecord.readDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					已查阅：${oaNotify.readNum} &nbsp; 未查阅：${oaNotify.unReadNum} &nbsp; 总共：${oaNotify.readNum + oaNotify.unReadNum}
				</div>
			</div>
		</c:if>
		<div class="form-actions">
			<c:if test="${oaNotify.status ne '1'}">
				<shiro:hasPermission name="oa:oaNotify:edit">
                        <label class="control-label col-md-4"></label>
						<div class="col-md-6">
							<button class="btn btn-success" type="submit" id="ok">保存</button>
							<button class="btn btn-info" type="reset" id="no">重 置</button>
							<input id="btnCancel" class="btn btn-warning" type="button" value="返 回" onclick="history.go(-1)" />
						</div>
                </shiro:hasPermission>
			</c:if>
		</div>
	</form:form>
</div>
</body>
</html>