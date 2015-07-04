<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
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
				display : '标题',
				width : '125',
				name : 'title',
				isReplaceText:true
			}, {
				display : '类型',
				name : 'type',
				isConfigurable :true,
				align : 'left',
				replaceHtmlFunction : 'replaceTypeHtml'
			}, {
				display : '状态',
				name : 'status',
				sortable : false,
				align : 'left',
				replaceHtmlFunction : 'replaceStatusHtml'
			}, {
				display : '查阅状态',
				name : 'readFlag',
				align : 'right',
				replaceHtmlFunction : 'replaceReadFlagHtml'
			}, {
				display : '创建者',
				name : 'createBy.name',
				align : 'right'
			}, {
				display : '创建时间',
				name : 'createDate',
				isConfigurable :true,
				align : 'left'
			}, {
				display : '操作',
				name : '',
				width : '100',
				sortable : false,
				align : 'center',
				isButton : true
			} ];
			
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
				url : "${ctx}/oa/oaNotify/findPage?self=${oaNotify.self}",
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
		
		//打开新标签
		function detailUser(i){
			var data=getData(i);
			var id = data.id;
			window.open("${ctx}/oa/oaNotify/form?id="+id, '_self'); 
		}
		var notifyType = null;
		function replaceTypeHtml(i){
			if(!notifyType){
				notifyType = ${fns:getDictList('oa_notify_type')};
			}
			var data = getData(i);
			if(data!=null){
				var type = data.type;
				for(var j in notifyType){
					var label = notifyType[j].label;
					label = label.substring(0,label.length-1);
					if(notifyType[j].value==type){return label;}
				}
			}
			return "其他";
		}
		
		var notifyStatus = null;
		function replaceStatusHtml(i){
			if(!notifyStatus){
				notifyStatus = ${fns:getDictList('oa_notify_status')};
			}
			var data = getData(i);
			if(data!=null){
				var status = data.status;
				for(var j in notifyStatus){
					if(notifyStatus[j].value==status){return notifyStatus[j].label;}
				}
			}
			return "未知";
		}
		var readFlag = null;
		function replaceReadFlagHtml(i){
			
			if(!readFlag){
				readFlag = ${fns:getDictList('oa_notify_read')};
			}
			var data = getData(i);
			if(data!=null){
				var selfFlag = data.self;
				if(selfFlag){
					var flag = data.readFlag;
					for(var j in readFlag){
						if(readFlag[j].value==flag){return readFlag[j].label;}
					}
				}else{
					return data.readNum+"/"+(parseInt(data.readNum) + parseInt(data.unReadNum))
				}
			}
			return "未知";
		}
	</script>
</head>
<body>
<div class="whiteBoxNav">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/oaNotify/${oaNotify.self?'self':''}">通知列表</a></li>
		<c:if test="${!oaNotify.self}"><shiro:hasPermission name="oa:oaNotify:edit"><li><a href="${ctx}/oa/oaNotify/form">通知添加</a></li></shiro:hasPermission></c:if>
	</ul>
	
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
		<form:form action="" id="advancedSearchForm" modelAttribute="oaNotify">
			<table width="100%" border="0" cellspacing="0" cellpadding="20" class="expertSearch">
				<tbody>
				<tr>
					<td style="border-right: 1px solid #d7d7d7;">
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">标题</div>
							<div class="text">
								<form:input path="title" htmlEscape="false" maxlength="200" class="inputText"/>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">类型</div>
							<div class="text">
								<form:select path="type" class="inputSelect">
									<form:option value="" label="全部"/>
									<form:options items="${fns:getDictList('oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
						</div>
						<div class="value">
							<div class="title" style="width: 65px; text-align: right">状态</div>
							<div class="text">
								<form:select path="status" cssClass="inputSelect">
									<form:option value="">全部</form:option>
									<form:options items="${fns:getDictList('oa_notify_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
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
</div>
</body>
</html>