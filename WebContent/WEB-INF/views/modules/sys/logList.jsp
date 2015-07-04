<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		init();
	});
	
	function init() {
		page_colmodel = [ {
			display : '序号',
			name : 'id',
			width : '45',
			isNo : true
		}, {
			display : '操作菜单',
			width : '125',
			name : 'title',
			isReplaceText:true
		}, {
			display : '操作用户',
			name : 'createBy.name',
			isConfigurable :true,
			align : 'left'
		}, {
			display : '所在公司',
			name : 'createBy.company.name',
			isConfigurable :true,
			align : 'left'
		}, {
			display : '所在部门',
			name : 'createBy.office.name',
			sortable : false,
			align : 'left'
		}, {
			display : 'URI',
			name : 'requestUri',
			align : 'right'
		}, {
			display : '提交方式',
			name : 'method',
			align : 'right'
		}, {
			display : '操作者IP',
			name : 'remoteAddr',
			align : 'right'
		}, {
			display : '操作时间',
			name : 'createDate',
			align : 'right'
		}, {
			display : '操作',
			name : '',
			width : '100',
			sortable : false,
			align : 'center',
			isButton : true
		} ];
		
		var page_buttons = [ {
			name : '作废',
			bclass : 'operate del',
			onpress : 'writebackRecord'
		} ];
		
		var page = {
			id : "Sys_role",		
			url : "${ctx}/sys/log/findPage",
			dataType : 'json',
			colModel : page_colmodel,
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
	
	</script>
</head>
<body>

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
		<form:form action="" id="advancedSearchForm" modelAttribute="log">
			<table width="100%" border="0" cellspacing="0" cellpadding="20" class="expertSearch">
				<tbody>
				<tr>
					<td style="border-right: 1px solid #d7d7d7;">
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">操作菜单</div>
							<div class="text">
								<input id="title" name="title" type="text" maxlength="50" class="inputText" value="${log.title}"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">用户ID</div>
							<div class="text">
								<input id="createBy.id" name="createBy.id" type="text" maxlength="50" class="inputText" value="${log.createBy.id}"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">用户名称</div>
							<div class="text">
								<input id="createBy.loginName" name="createBy.loginName" type="text" maxlength="50" class="inputText" value="${log.createBy.loginName}"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">URI</div>
							<div class="text">
								<input id="requestUri" name="requestUri" type="text" maxlength="50" class="input-mini" value="${log.requestUri}"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">日期范围</div>
							<div class="text">
								<input name="beginDate" type="text" class="inputText datepicker" value="${log.beginDate}" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'end_createdate\')}'})" id="start_createdate" spellcheck="false">
							</div>
							<div class="title">~</div>
							<div class="text">
								<input name="endDate" type="text" class="inputText datepicker" value="${log.endDate}" onclick="WdatePicker({minDate:'#F{$dp.$D(\'start_createdate\')}'})" id="end_createdate" spellcheck="false">
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