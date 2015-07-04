<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域导入</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.clear {
			font-size: 0;
			line-height: 0;
			clear: both;
			height: 0;
		}
	</style>
	<script type="text/javascript">
	//获取已经导入的数目
	function getHasImportNumber() {
		var setid = $("#setid").val();
		var totalnumber = $("#totalnumber").val();
		if (totalnumber == undefined || totalnumber == ''
				|| totalnumber == null) {
			totalnumber = 1;
		}
		var param = {};
		param["setid"] = setid;
		$.post("${ctx}/sys/area/getHasImportNumber", param, function(result,
				resultState) {
			if (resultState == "success") {
				var currentnumber = result.cursetnum;
				totalnumber = parseInt(totalnumber);
				
				if(currentnumber==-1){
					currentnumber = totalnumber;
				}
				currentnumber = parseInt(currentnumber);
				
				$("#validatesuccess").html(
						"需要导入" + totalnumber + ",已经导入" + currentnumber);
				if(isNaN(currentnumber)){
					currentnumber = 0;
				}
				$("#importBar").css("width",parseInt(currentnumber / totalnumber * 100)+"%");
				if(result.status==2){
					$('body').stopTime('A');
					$("#validatesuccess").html("导入完成！" + totalnumber);
				}
			} else {
				$('body').stopTime('A');
			}
		});
	}
	//下载模板
	function downloadTemplate() {
		var uploadpath = $("#uploadpath").val();
		window.location.href = uploadpath + "formwork/地域导入模版.xlsx";
	}
	function processFile(path) {
		$("#loadding").show();
		var param = {};
		param["path"] = path;
		$.post("${ctx}/sys/area/uploadTemplate", param, function(result,resultState) {
			if (resultState == "success") {
				var addnumber = result.addnumber;
				var updatenumber = result.updatenumber;
				var excelid = result.excelid;
				var totalnumber = result.totalnumber;
				var setid = result.setid;
				var cursetnum = result.cursetnum;

				$("#loadding").hide();
				$("#validateSpan").show();
				$("#excelid").val(excelid);
				$("#totalnumber").val(totalnumber);
				$("#setid").val(setid);
				$("#cursetnum").val(cursetnum);
				if (result.serviceResult.statu == 3) {
					$("#validatesuccessSpan").hide();
					$("#validatefailedSpan").show();
					$("#validatefailed").html(result.serviceResult.message);
				}
				if (result.serviceResult.statu == 1) {
					$("#reallyImport").show();
					$("#validatefailedSpan").hide();
					$("#validatesuccessSpan").show();
					$("#validatesuccess").html(
						result.serviceResult.message + "新增" + addnumber+ "条" + ",更新" + updatenumber + "条");
				}
			} else {
			}
		});
	}
	function reallyImport() {
		$("#spaceused").show();
		$("#reallyImport").hide();
		$('body').everyTime('1s', 'A', function() {
			getHasImportNumber();
		});
		var excelid = $("#excelid").val();
		var totalnumber = $("#totalnumber").val();
		var setid = $("#setid").val();
		var cursetnum = $("#cursetnum").val();
		var param = {};
		param["excelid"] = excelid;
		param["totalnumber"] = totalnumber;
		param["setid"] = setid;
		param["cursetnum"] = cursetnum;
		$.post("${ctx}/sys/area/reallyImport", param, function(result,resultState) {
			if (resultState == "success") {
				if (result.serviceResult.statu == 1) {
					showJboxMsg("导入客户信息成功","alert");
				}
			} else {
			}
		});
	}
	
</script>
</head>
<body style="background: none">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/area/list?id=${area.id}">地域列表</a></li>
		<shiro:hasPermission name="sys:area:edit"><li class="active"><a href="${ctx}/sys/area/importForm">地域导入</a></li></shiro:hasPermission>
	</ul>
	<div style="display: none">
		<input id="uploadpath" type="text" value="${ctxRoot }/userfiles/" /><br /> 
		<input id="writePath" type="text" value="" /><br /> 
		<input id="readPath" type="text" value="" /><br /> 
		<input id="path" type="text" value="" /><br />
		<input id="excelid" type="text" value="" /><br /> 
		<input id="totalnumber" type="text" value="" /><br /> 
		<input id="setid" type="text" value="" /><br /> 
		<input id="cursetnum" type="text" value="" /><br />
	</div>
	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr>
		<td style="background: #e9eef2;" valign="top">
			<div class="main">
				<div class="whiteBox noPadding wf100">
				<div class="importContact">
					<h1>导入地域</h1>
					<br>
					<div class="content">
						<div class="impotButtonArea fl wf100" style="padding-bottom: 40px;">
							<div class="step1">
								<a href="javascript:;" onclick="downloadTemplate()" class="downloadExcelTemp"></a>
							</div>
							<div class="step2End">
								<span class="choseFileArea"> 
									<div class="fileInputArea">
										<input id="selectFile" name="file" type="file" data-show-preview="false" value="选择文件">
										<script type="text/javascript">
										/* Initialize your widget via javascript as follows */
											$("#selectFile").fileinput({
												uploadUrl: "${ctx}/sys/area/importExcel", 
												browseClass: "btn choseFile",
												showCaption: false,
												showRemove: false,
												uploadAsync: false,
												overwriteInitial: false,
												language: "zh",
												showUpload: false,
												allowedFileExtensions: ["xls", "xlsx"]
											}).on("filebatchselected", function(event, files) {
											   $('#selectFile').fileinput("upload");
										    }).on('filebatchuploadsuccess', function(event, data, previewId, index) {
											    var path = data.response.initialPreviewConfig[0].key;
											    processFile(path);
										    });
										</script>
									</div>
								</span> 
								<span style="font-size:10px;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请选择一个根据模板<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;编辑的Excel文件
								</span>
							</div>
							<div class="clear"></div>
							<div class="importSuccees" id="validateSpan" style="display: none">
								<span id="validatesuccessSpan" style="display: none"> 
									<span id="validatesuccess" style="line-height: 40px;"></span> 
									<a id="reallyImport" href="javascript:;" class="button blueButton" onclick="reallyImport()">确认导入</a> 
									<div class="progress" id="spaceused" style="display: none">
									  <div class="progress-bar progress-bar-warning progress-bar-striped active" role="progressbar" id="importBar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
									  </div>
									</div>
								</span> 
								<span id="validatefailedSpan" style="display: none"> 
									<span id="validatefailed" class="fontRed" style="line-height: 40px;"></span> 
								</span>
							</div>
						</div>
						<div class="clear"></div>
						<p class="font14">导入指引</p>
						<p class="font666">1、下载固定模板制作您的客户表格 2、“点击选择文件” ---> “点击上传” ---> 确认导入 ---> 成功！</p>
					</div>
				</div>
				</div>
			</div> <!-- main -->
		</td>
		</tr>
	</table>
	<div id="loadding" class="loadding" style="display: none"></div>
</body>
</html>