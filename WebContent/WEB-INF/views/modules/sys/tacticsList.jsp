<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户策略管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
		function viewTactis(href){
			top.$.jBox.open('iframe:'+href,'增加策略',750,390,{
				buttons:{"确定":true,"关闭":false},
				loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				},submit:function(v, h, f){
					if(v){//确认提交
						if(!h.find("iframe")[0].contentWindow.validate()){
							return false;
						}
						var form = h.find("iframe")[0].contentWindow.$("#inputForm");
						
						$.ajax({      
							type: "POST",      
							url: $(form).attr('action'),     
						 	data: $(form).serialize(),
						    success: function(result){
						    	top.$.jBox.tip.mess=1;
						    	top.$.jBox.tip(result.msg,"info",{persistent:true,opacity:0});
						    	$("#messageBox").show();
						    	location.replace(location);
						    }  
						 });
					}
				}
			});
			return false;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" action="${ctx}/sys/tactics/tacticsList" modelAttribute="tactics" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		    <li><label>用户：</label>
			<sys:treeselect id="tactics" name="user.id" value="${tactics.user.id}" labelName="user.name" labelValue="${tactics.user.name}"
				title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" notAllowSelectParent="true" />
			</li>
			<li >
			<label>运营商：</label>
			<form:select path="feeType" class="input-medium">
				<form:option value="">全部</form:option>
				<form:options items="${fns:getDictList('recharge_operators')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			<a href="${ctx}/sys/tactics/form?user.id=${user.id}&user.name=${user.name}" onclick="return viewTactis(this.href);">
			    <input id="btnSubmit" class="btn btn-primary" type="submit" value="增加策略"></a>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>用户</th><th>流量类型</th><th>最大消费</th><th>最小消费</th><th>商务费率</th><th>操作</th></thead>
		<tbody>
		<c:forEach items="${tacticsList}" var="tactics">
			<tr>
				<td>${tactics.user.name}</td>
				<td>${fns:getDictLabel(tactics.feeType, 'recharge_operators', '')}</td>
				<td>${tactics.maxConsume}</td>
				<td><strong>${tactics.minConsume}</strong></td>
				<td>${tactics.feeRate}</td>
				<td>
					<a href="${ctx}/sys/tactics/delete?id=${tactics.id}&user.id=${tactics.user.id}&user.name=${tactics.user.name}" onclick="return confirmx('确认要删除该策略吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>