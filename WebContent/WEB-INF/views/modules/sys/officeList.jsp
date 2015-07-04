<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			init();
		});
		function init() {
			page_colmodel = [  {
				display : '序号',
				name : 'id',
				width : '45',
				isNo : true
			}, {
				display : '机构名称',
				width : '125',
				name : 'name',
				align : 'left',
				isReplaceText:true
			}, {
				display : '编码',
				name : 'code',
				isConfigurable :true,
				align : 'left'
			}, {
				display : '地址',
				name : 'address',
				sortable : false,
				align : 'left'
			}, {
				display : '机构类型',
				name : 'type',
				align : 'left',
				replaceHtmlFunction : 'replaceTypeScopeHtml'
			}, {
				display : '机构等级',
				name : 'grade',
				isConfigurable :true,
				align : 'left',
				replaceHtmlFunction : 'replaceGradeScopeHtml'
			}, {
				display : '机构流程',
				name : 'actType',
				isConfigurable :true,
				align : 'left',
				replaceHtmlFunction : 'replaceActTypeScopeHtml'
			}, {
				display : '操作',
				name : '',
				width : '100',
				sortable : false,
				align : 'center',
				isButton : true
			} ];
			
			var page_colmode_detail = [{
				display : '名称',
				name : 'name',
				isHeader:true	
			}, {
				display : '编码',
				name : 'code',
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
				id : "Sys_office",		
				url : "${ctx}/sys/office/findPage",
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
			window.open("${ctx}/sys/office/form?id="+id, '_self'); 
		}
		
		function replaceTypeScopeHtml(i){
			var data = getData(i);
			if(data!=null){
				var type = data.type;
				switch(type)
				{
				case "1":
				 	 return "公司";
				case "2":
				  	return "部门";
				case "3":
					return "小组";
				}
			}
			return "其他";
		}
		
		function replaceGradeScopeHtml(i){
			var data = getData(i);
			if(data!=null){
				var grade = data.grade;
				switch(grade)
				{
				case "1":
				 	 return "一级";
				case "2":
				  	return "二级";
				case "3":
					return "三级";
				case "4":
					return "四级";
				}
			}
			return "其他";
		}
		
		function replaceActTypeScopeHtml(i){
			var data = getData(i);
			if(data!=null){
				var actType = data.actType;
				switch(actType)
				{
				case "1":
				 	 return "市场部";
				case "2":
				  	return "设计部";
				}
			}
			return "";
		}
	</script>
</head>
<body>
	<div class="whiteBoxNav">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/office/list?id=${office.id}">机构列表</a></li>
			<shiro:hasPermission name="sys:office:edit"><li><a href="${ctx}/sys/office/form?parent.id=${office.id}">机构添加</a></li></shiro:hasPermission>
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
		<form:form action="" id="advancedSearchForm" modelAttribute="office">
			<table width="100%" border="0" cellspacing="0" cellpadding="20" class="expertSearch">
				<tbody>
				<tr>
					<td style="border-right: 1px solid #d7d7d7;">
						
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">名称</div>
							<div class="text">
								<form:input path="name" htmlEscape="false" maxlength="50" class="inputText"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">编码</div>
							<div class="text">
								<form:input path="code" htmlEscape="false" maxlength="50" class="inputText"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">类型</div>
							<div class="text">
								<form:select path="type" class="inputSelect" id="officd_type">
									<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">级别</div>
							<div class="text">
								<form:select path="grade" class="inputSelect">
									<form:options items="${fns:getDictList('sys_office_grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">上级机构</div>
							<div class="text">
								<sys:treeselect id="office" name="office.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}" 
									title="部门" url="/sys/office/treeData?type=2" cssClass="inputText" allowClear="true" notAllowSelectParent="true"/>
							</div>
						</div>
					</div></td>
					<td valign="middle" align="center" width="200"><a href="javascript:;" class="button blueButton" onclick="parentCancelReload()">搜索</a> 
						<a class="button" onclick="resetSearch()">清空搜索</a>
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