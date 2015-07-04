<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
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
				display : '键值',
				width : '125',
				name : 'value',
				isReplaceText:true
			}, {
				display : '标签',
				name : 'label',
				isConfigurable :true,
				align : 'left'
			}, {
				display : '类型',
				name : 'type',
				isConfigurable :true,
				align : 'left'
			}, {
				display : '描述',
				name : 'description',
				sortable : false,
				align : 'left'
			}, {
				display : '操作',
				name : '',
				width : '100',
				sortable : false,
				align : 'center',
				isButton : true
			} ];
			
			var page_colmode_detail = [{
				display : '标签',
				name : 'label',
				isHeader:true	
			}, {
				display : '健值',
				name : 'value',
				isHeader:true,
				isGeneral:true,
				align : 'right'
			},{
				display : '备注',
				name : 'description',
				isConfigurable :true,
				align : 'left'
			}];
			
			var page_buttons = [ {
				name : '详细',
				bclass : 'operate details',
				onpress : 'detail'
			},{
				name : '作废',
				bclass : 'operate del',
				onpress : 'writebackRecord'
			} ];
			
			var page = {
				id : "Sys_role",		
				url : "${ctx}/sys/dict/findPage",
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
		function detail(i){
			var data=getData(i);
			var id = data.id;
			window.open("${ctx}/sys/dict/form?id="+id, '_self'); 
		}
		
	</script>
</head>
<body>
	<div class="whiteBoxNav">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/dict/">字典列表</a></li>
			<shiro:hasPermission name="sys:dict:edit"><li><a href="${ctx}/sys/dict/form?sort=10">字典添加</a></li></shiro:hasPermission>
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
		<form:form action="" id="advancedSearchForm" modelAttribute="dict">
			<table width="100%" border="0" cellspacing="0" cellpadding="20" class="expertSearch">
				<tbody>
				<tr>
					<td style="border-right: 1px solid #d7d7d7;">
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">类型</div>
							<div class="text">
								<form:select id="type" path="type" class="form-control">
									<form:option value="" label=""/>
									<form:options items="${typeList}" htmlEscape="false"/>
								</form:select>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">名称</div>
							<div class="text">
								<form:input path="label" htmlEscape="false" maxlength="50" class="form-control"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">描述</div>
							<div class="text">
								<form:input path="description" htmlEscape="false" maxlength="50" class="form-control"/>
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