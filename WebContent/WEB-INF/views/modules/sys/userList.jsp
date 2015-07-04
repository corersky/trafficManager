<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#advancedSearchForm").attr("action","${ctx}/sys/user/export");
						$("#advancedSearchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
			init();
		});
		
		function init() {
			page_colmodel = [  {
				display : '序号',
				name : 'id',
				width : '45',
				isNo : true
			}, {
				display : '归属公司',
				width : '125',
				name : 'company.name',
				isReplaceText:true
			}, {
				display : '归属部门',
				name : 'office.name',
				isConfigurable :true,
				align : 'left'
			}, {
				display : '登录名',
				name : 'loginName',
				sortable : false,
				align : 'left'
			}, {
				display : '姓名',
				name : 'name',
				align : 'right'
			}, {
				display : '手机',
				name : 'mobile',
				isConfigurable :true,
				align : 'left'
			}, {
				display : '权限范围',
				name : 'dataScope',
				isConfigurable :true,
				align : 'left',
				replaceHtmlFunction : 'replaceDataScopeHtml'
			}, {
				display : '操作',
				name : '',
				width : '100',
				sortable : false,
				align : 'center',
				isButton : true
			} ];
			
			var page_colmode_detail = [{
				display : '登录名称',
				name : 'loginName',
				isHeader:true	
			}, {
				display : '名称',
				name : 'name',
				isHeader:true,
				isGeneral:true,
				align : 'right'
			},{
				display : '备注',
				name : 'remarks',
				isConfigurable :true,
				align : 'left'
			}];
			
			var page_buttons = [ {
				name : '详细',
				bclass : 'operate details',
				onpress : 'detailUser'
			},{
				name : '作废',
				bclass : 'operate del',
				onpress : 'writebackRecord'
			} ];
			
			var page = {
				id : "Sys_user",		
				url : "${ctx}/sys/user/findPage",
				dataType : 'json',
				colModel : page_colmodel,
				colModel_Detail : page_colmode_detail,
				buttons : page_buttons,
				sortname : 'BusiDate',
				sortorder : 'desc',
				usepager : true,
				useRp : true,
				rp : 10,
				showTableToggleBtn : false,
				width : 'auto',
				height : 'auto',
				setPageParams : 'getPageParams()'
			};
			myPage = page;
			$("#resultList").flexigrid(page);
			// DoMore();
		}
		
		//打开新标签
		function detailUser(i){
			var data=getData(i);
			var id = data.id;
			window.open("${ctx}/sys/user/form?id="+id, '_self'); 
		}
		
		function replaceDataScopeHtml(i){
			var data = getData(i);
			if(data!=null){
				var dataScope = data.dataScope;
				switch(dataScope)
				{
				case "1":
				 	 return "所有数据";
				case "2":
				  	return "所在公司及以下数据";
				case "3":
					return "所在公司及以下数据";
				case "4":
					return "所在公司数据";
				case "5":
					return "所在部门数据";
				case "8":
					return "仅本人数据";
				case "9":
					return "按明细设置";
				}
			}
			return "无";
		}
	</script>
</head>
<body>
	<div class="whiteBoxNav">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/user/list">用户列表</a></li>
			<shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/sys/user/form">用户添加</a></li></shiro:hasPermission>
		</ul>
	</div>
	
	<div class="clear"></div>
	<div class="whiteBox actionArea">
		<form action="" id="normalSearchForm">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tbody><tr>
				<td width="300" id="">
					<a id="btnExport" class="button  withIcon"> <span class="icon exportIcon"></span>
						<span class="text">导出</span>
					</a>
					<a id="btnImport" class="button  withIcon"> <span class="icon exportIcon"></span>
						<span class="text">导入</span>
					</a>
				</td>
				<td align="right" id="search">
				<a onclick="switchSearch(this)" class="button fr">展开高级搜索</a>
					<div class="search fr" style="display: block;">
					    <a class="button fr mr10" onclick="parentCancelReload()">搜索</a>	
						<input name="" type="text" id="SearchKeyWord" class="inputText fr mr5" title="搜索编号、名称" size="35" placeholder="搜索单号、账目类型、备注" maxlength="30" spellcheck="false" style="display: block;">
					</div>
				</td>
			</tr></tbody>
		</table>
		</form>
	</div>
	
	<div id="advancedSearchDiv" style="display: none;">
		<form:form action="" id="advancedSearchForm" modelAttribute="user">
			<table width="100%" border="0" cellspacing="0" cellpadding="20" class="expertSearch">
				<tbody>
				<tr>
					<td style="border-right: 1px solid #d7d7d7;">
						<div class="value">
							<div class="title" style="width: 75px; text-align: right">归属公司</div>
							<div class="text">
								<sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}" 
									title="公司" url="/sys/office/treeData?type=1" cssClass="form-control" allowClear="true"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">登录名</div>
							<div class="text">
								<form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 75px; text-align: right">归属部门</div>
							<div class="text">
								<sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}" 
									title="部门" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">姓&nbsp;&nbsp;&nbsp;名</div>
							<div class="text">
								<form:input path="name" htmlEscape="false" maxlength="50" class="form-control"/>
							</div>
						</div>
					</div></td>
					<td valign="middle" align="center" width="200"><a href="javascript:;" class="btn btn-primary" onclick="parentCancelReload()">搜索</a> 
						<button type="button" onclick="resetSearch()" class="btn btn-danger">清空搜索</button>
					</td>
				</tr>
				</tbody>
			</table>
		</form:form>
	</div>
	
	<sys:message content="${message}"/>
	
	<div class="clear"></div>
	<div class="whiteBox actionArea">
		<table id="resultList" style="border-top-width: 0px;" cellpadding="15" cellspacing="0" border="0" class="tableStyle table">
		</table>
	</div>
</body>
</html>