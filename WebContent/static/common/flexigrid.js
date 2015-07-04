var rememberCheckboxMap = {};
var flexgridpage = null;
var flexgridrp = null;
function getUserId() {
	try {
		var b = parent.document.getElementById("hidUserId").value;
		if (b && b.length > 0) {
			return b;
		}
	} catch (a) {
	}
	return "";
}
function isMoneyColum(b) {
	if (b && b.indexOf("余额") >= 0) {
		return true;
	}
	if (!b || b.indexOf("账户") >= 0 || b.indexOf("价格权限") >= 0
			|| b.indexOf("支付方") >= 0) {
		return false;
	}
	var a = (b.indexOf("比重") == -1)
			&& (b.indexOf("率") == -1)
			&& (b.indexOf("额") != -1 || b.indexOf("价") != -1
					|| b.indexOf("款") != -1 || b.indexOf("收入") != -1
					|| b.indexOf("支出") != -1 || b.indexOf("利") != -1
					|| b.indexOf("惠") != -1 || b.indexOf("费") != -1);
	return a;
}
function isPercentage(b) {
	var a = b.indexOf("率") != -1 || b.indexOf("比重") != -1;
	return a;
}
function isManyStore() {
	var b = 1;
	try {
		if (null != parent.document.getElementById("productversion")
				&& undefined != parent.document
						.getElementById("productversion")) {
			b = parent.document.getElementById("productversion").value;
		} else {
			b = parent.parent.document.getElementById("productversion").value;
		}
		if (!b) {
			b = 1;
		}
	} catch (a) {
	}
	return "2" == ("" + b);
}
function flexgridFormatMoney(a) {
	if (!/^(-?\d+)(\.\d+)?$/.test(a)) {
		return a;
	}
	var f = 2;
	try {
		if (null != parent.document.getElementById("PriceDecimalDigits")
				&& undefined != parent.document
						.getElementById("PriceDecimalDigits")) {
			f = parent.document.getElementById("PriceDecimalDigits").value;
		} else {
			f = parent.parent.document.getElementById("PriceDecimalDigits").value;
		}
		if (!f) {
			f = 2;
		}
	} catch (d) {
	}
	try {
		if (f > 4) {
			f = 4;
		}
		a = parseFloat(a);
		a = String(a.toFixed(f));
		var c = a.split(".");
		var b = /(-?\d+)(\d{3})/;
		while (b.test(c[0])) {
			c[0] = c[0].replace(b, "$1,$2");
		}
		if (f <= 0) {
			a = c[0];
		} else {
			a = c[0] + "." + c[1];
		}
	} catch (d) {
	}
	return a;
}
function createNoDataRow() {
	var d = document.createElement("tr");
	var e = 0;
	var a = document.getElementById("theader").getElementsByTagName("th");
	for (var b = 0; b < a.length; b++) {
		th = a[b];
		var c = "th" + b;
		if ($("#" + c).css("display") != "none") {
			e += 1;
		}
	}
	$(d)
			.html(
					'<td id="tdNoData" colspan="'
							+ e
							+ '" align="center" style="background:#FFF;">       <div class="listNodata">       <h1>当前列表无数据</h1>       </div>    </td>');
	return d;
}
function createDetailRow(c, a, s) {
	var o = document.createElement("tr");
	$(o).attr("style", "display:none;");
	o.id = "row" + c + "_detail";
	var f = document.createElement("td");
	$(f).attr("colspan", "20");
	$(f).attr("align", "center");
	$(f).attr("class", "yellowLine");
	var k = document.createElement("table");
	$(k).attr("width", "100%");
	$(k).attr("border", "0");
	$(k).attr("cellspacing", "0");
	$(k).attr("cellpadding", "3");
	$(k).attr("class", "noborderTable");
	var e;
	var j = new Array();
	
	for (var h = 0; h < a.colModel_Detail.length; h++) {
		r = a.colModel_Detail[h];
		j.push(a.colModel_Detail[h]);
	}
	e = document.createElement("tr");
	var q = document.createElement("td");
	$(q).attr("style", "border-bottom:1px solid #d7d7d7;border-right:0;");
	$(q).attr("colspan", "8");
	var d = "";
	for (var h = 0; h < j.length; h++) {
		if (j[h].isHeader == true) {
			var r = j[h];
			var names = r.name.split(".");
			var l = s[names[0]];

			for(var i=1;i<names.length;i++){
				if(!l){
					l = "";
					break;
				}
				l = l[names[i]];
			}
			var g = r.display;
			if (isMoneyColum(g)) {
				l = "￥" + flexgridFormatMoney(l);
			}
			if (j[h].isGeneral == true) {
				l = '<span  style="line-height:35px;" class="font999">' + g
						+ ":" + l + "</span>";
			} else {
				l = '<h2 class="fl" style="line-height:35px;">' + l + "</h2>";
			}
			d += l + "&nbsp;&nbsp;";
		}
	}
	$(q).html(d);
	$(e).append(q);
	$(k).append(e);
	var b = 0;
	for (var h = 0; h < j.length; h++) {
		r = j[h];
		if (r.isHeader != true) {
			if (b % 4 == 0) {
				e = document.createElement("tr");
				$(k).append(e);
			}
			b++;
			td_detail_display = document.createElement("td");
			td_detail_content = document.createElement("td");
			$(td_detail_display).attr("class", "font999 border0important");
			$(td_detail_display).attr("align", "right");
			$(td_detail_display).attr("height", "30");
			$(td_detail_content).attr("align", "left");
			$(td_detail_content).attr("class", "border0important");
			var m = r.display;
			var names = r.name.split(".");
			var l = s[names[0]];

			for(var i=1;i<names.length;i++){
				if (!l) {
					l = "无数据";
					break;
				}
				l = l[names[i]]
			}
			
			if (r.zero) {
				if (l == "0") {
					l = r.zero;
				}
				if (l == "1") {
					l = r.one;
				}
				if (l == "2") {
					l = r.two;
				}
			}
			if (l == undefined || l == "undefined" || (l + "").length == 0) {
				l = "无数据";
			} else {
				if (l.length > 10 && r.display.indexOf("日期") != -1) {
					l = l.substring(0, 10);
				} else {
					if (l.length > 19 && r.display.indexOf("时间") != -1) {
						l = l.substring(0, 19);
					} else {
						if (isMoneyColum(r.display)) {
							l = flexgridFormatMoney(l);
							l = "￥" + l;
						} else {
							if (isPercentage(r.display)) {
								l = l.toFixed(2);
								l = l + "%";
							}
						}
					}
				}
			}
			$(td_detail_display).html(m + ":");
			if (l.length > 11) {
				$(td_detail_content).html(
						'<div class="ellipsis" title="' + l + '">' + l
								+ "</div>");
			} else {
				$(td_detail_content).html(l);
			}
			$(e).append(td_detail_display);
			$(e).append(td_detail_content);
		}
	}
	$(o).append(f);
	$(f).append(k);
	return o;
}
function createGeneralRow(ths, transverseNumber, p, row) {
	var tr = document.createElement("tr");
	var rowid = "row" + transverseNumber;
	$(tr).attr("id", rowid);
	$(tr).attr("NormalRow", true);
	if (transverseNumber % 2 && p.striped) {
		tr.className = "greyLine";
	}
//	alert(JSON.stringify(row))
	for (var i = 0; i < ths.length; i++) {
		th = ths[i];
		verticalNumber = i;
		var td = document.createElement("td");
		var thid = "th" + verticalNumber;
		$(td).attr("thid", thid);
		if ($("#" + thid).css("display") == "none") {
			$(td).hide();
		}
		$(td).width(th.width);
		var idnumber = $(th).attr("number");
		var cm = p.colModel[idnumber];
		td.align = "center";
		if (cm.align) {
			td.align = cm.align;
		}
		var names = cm.name.split(".");
		var tdvalue = row[names[0]];

		for(var j=1;j<names.length;j++){
			if(!tdvalue){
				tdvalue = "";
				break;
			}
			tdvalue = tdvalue[names[j]];
		}
		
		if (tdvalue == undefined || tdvalue == "undefined"
				|| (tdvalue + "").length == 0) {
			tdvalue = "";
		} else {
			if (cm.display) {
				if (tdvalue.length > 10 && cm.display.indexOf("日期") != -1) {
					tdvalue = tdvalue.substring(0, 10);
				} else {
					if (tdvalue.length > 19 && cm.display.indexOf("时间") != -1) {
						tdvalue = tdvalue.substring(0, 19);
					} else {
						if (isMoneyColum(cm.display)) {
							tdvalue = flexgridFormatMoney(tdvalue);
						} else {
							if (isPercentage(cm.display)) {
								tdvalue = tdvalue.toFixed(2);
								tdvalue = tdvalue + "%";
							}
						}
					}
				}
			}
		}
		td.innerHTML = tdvalue;
		if (cm.customLineStyles != undefined) {
			var trClass = eval(cm.customLineStyles + "(" + transverseNumber
					+ ");");
			if (trClass != "") {
				$(tr).attr("class", trClass);
			}
		}
		if (cm.customElementsEvent != undefined) {
			var str1 = cm.customElementsEvent;
			var str = cm.customElementsEventFunction + "(" + transverseNumber
					+ ");";
			$(td).attr(str1, str);
		}
		if (cm.customTitle != undefined) {
			var str = eval(cm.customTitle + "(" + transverseNumber + ");");
			if (str != "") {
				$(td).attr("title", str);
			}
		}
		var clickflag = true;
		if (cm.isButton == true) {
			var div = document.createElement("div");
			$(div).attr("class", "tableButtonArea");
			$(td).append(div);
			clickflag = false;
			if (p.buttons) {
				for (var n = 0; n < p.buttons.length; n++) {
					var button = p.buttons[n];
					var a = document.createElement("a");
					$(a).attr("id", n);
					if (button.isHide != undefined) {
						var flag = eval(button.isHide + "(" + transverseNumber
								+ ");");
						if (flag == false || flag == undefined) {
							if (button.customHtml) {
								$(div).append(button.customHtml);
							} else {
								if (button.customChange != undefined) {
									$(div).append(
											eval(button.customChange + "("
													+ transverseNumber + ");"));
								} else {
									if (button.isText == true) {
										$(div).append(a);
										$(a).html(button.name);
										$(a).addClass(button.bclass);
										$(a).attr("title", button.name);
										$(a).attr(
												"onclickevent",
												button.onpress + "("
														+ transverseNumber
														+ ");");
										a.onclick = function() {
											eval($(this).attr("onclickevent"));
										};
									} else {
										$(div).append(a);
										$(a).addClass(button.bclass);
										$(a).attr("title", button.name);
										$(a).attr(
												"onclickevent",
												button.onpress + "("
														+ transverseNumber
														+ ");");
										a.onclick = function() {
											eval($(this).attr("onclickevent"));
										};
									}
								}
							}
						}
					} else {
						if (button.customHtml) {
							$(div).append(button.customHtml);
						} else {
							if (button.customChange != undefined) {
								$(div).append(
										eval(button.customChange + "("
												+ transverseNumber + ");"));
							} else {
								if (button.isText == true) {
									$(div).append(a);
									$(a).html(button.name);
									$(a).addClass(button.bclass);
									$(a).attr("title", button.name);
									$(a).attr(
											"onclickevent",
											button.onpress + "("
													+ transverseNumber + ");");
									a.onclick = function() {
										eval($(this).attr("onclickevent"));
									};
								} else {
									$(div).append(a);
									$(a).addClass(button.bclass);
									$(a).attr("title", button.name);
									$(a).attr(
											"onclickevent",
											button.onpress + "("
													+ transverseNumber + ");");
									a.onclick = function() {
										eval($(this).attr("onclickevent"));
									};
								}
							}
						}
					}
				}
			}
		}
		if (cm.isCheckbox == true) {
			clickflag = false;
			var page = p.page;
			var count = (p.page - 1) * (p.rp) + transverseNumber + 1;
			var id = "fcheckbox" + count;
			td.innerHTML = "<input count=" + count + " page=" + page
					+ ' onclick="rememberCheckbox(this)" id=' + id
					+ ' name="fcheckbox" number="' + transverseNumber
					+ '" listcheck="0" type="checkbox" value="'
					+ row[p.colModel[idnumber].name] + '"/>';
		}
		if (cm.isNo) {
			td.innerHTML = (p.page - 1) * p.rp + transverseNumber + 1;
		}
		if (cm.replaceHtmlFunction != undefined) {
			if (eval(cm.replaceHtmlFunction + "(" + transverseNumber + ");") != "") {
				var value = eval(cm.replaceHtmlFunction + "("
						+ transverseNumber + ");");
				if (value != "oneself") {
					td.innerHTML = value;
				}
			}
		}
		if (cm.replaceImageFunction != undefined) {
			if (eval(cm.replaceImageFunction + "(" + transverseNumber + ");") != "") {
				td.innerHTML = "<img src="
						+ eval(cm.replaceImageFunction + "(" + transverseNumber
								+ ");") + "></img>";
			}
		}
		if (cm.replaceTextFunction != undefined) {
			if (eval(cm.replaceTextFunction + "(" + transverseNumber + ");") != "") {
				td.innerHTML = eval(cm.replaceTextFunction + "("
						+ transverseNumber + ");")
						+ "";
			}
		}
		if (cm.showDiv != undefined) {
			var src = eval(cm.showDiv + "(" + transverseNumber + ");");
			if (src != null) {
				var div1 = document.createElement("div");
				$(div1).attr("class", "helpinfo");
				$(div1)
						.attr("style",
								"background:none; position:absolute; margin:3px 0 0 -95px;display:none");
				var div2 = document.createElement("div");
				$(div2).attr("class", "tips");
				var h2 = document.createElement("h2");
				$(h2).attr("style", "margin-left:150px;");
				var h1 = document.createElement("h1");
				$(h1).attr("style", "width:250px; text-align:center;");
				$(h1).append(src);
				$(div2).append(h2);
				$(div2).append(h1);
				$(div1).append(div2);
				$(div1).hide();
				$(td).append(div1);
				$(src)
						.hover(
								function() {
									$(div1)
											.attr("style",
													"background:none; position:absolute; margin:3px 0 0 -95px;display:none");
								},
								function() {
									$(div1)
											.attr("style",
													"background:none; position:absolute; margin:3px 0 0 -95px;display:none");
								});
				$(td)
						.hover(
								function() {
									$(src).attr("style", "");
									$(div1)
											.attr("style",
													"background:none; position:absolute; margin:3px 0 0 -95px;");
								},
								function() {
									$(div1)
											.attr("style",
													"background:none; position:absolute; margin:3px 0 0 -95px;display:none");
								});
			}
		}
		if (cm.zero != undefined) {
			var names = cm.name.split(".");
			var tdvalue = row[names[0]];

			for(var j=1;j<names.length;j++){
				if(!tdvalue){
					tdvalue = "";
					break;
				}
				tdvalue = tdvalue[names[j]];
			}
			if (tdvalue == 1) {
				td.innerHTML = cm.one;
			}
			if (tdvalue == 0) {
				td.innerHTML = cm.zero;
			}
		}
		if (cm.fixedValue != undefined) {
			if (transverseNumber == 0) {
				td.innerHTML = cm.fixedValue;
			}
		}
		if (clickflag == true) {
			$(td).click(function() {
				$(td).append();
				var tr_id = $(tr).attr("id");
				var detail_id = "#" + tr_id + "_detail";
				if ($(detail_id).css("display") == "none") {
					$(detail_id).css("display", "");
				} else {
					$(detail_id).css("display", "none");
				}
			});
		}
		if (td.innerHTML == "") {
			td.innerHTML = "&nbsp;";
		}
		if (cm.noDrag) {
		} else {
			var tableTextDiv = document.createElement("div");
			$(tableTextDiv).attr("class", "tableText");
			$(tableTextDiv).css("width",
					$("#" + thid).attr("personwidth") + "px");
			var tdChildren = $(td).contents();
			if (tdChildren && tdChildren.length > 0) {
				$(tableTextDiv).append(tdChildren);
			} else {
				$(tableTextDiv).append(td.innerHTML);
			}
			$(td).empty();
			$(td).append(tableTextDiv);
		}
		$(tr).append(td);
	}
	return tr;
}
function rememberCheckbox(d) {
	var c = $(d).val();
	var b = $(d).attr("count");
	if ($(d).attr("checked")) {
		rememberCheckboxMap[b] = c;
	} else {
		delete rememberCheckboxMap[b];
	}
	var a = 0;
	$("[name='fcheckbox']").each(function() {
		if ($(this).attr("checked")) {
			a++;
		}
	});
	if (a == flexgridrp) {
		$("#selectAllCheckbox").attr("checked", true);
	} else {
		$("#selectAllCheckbox").attr("checked", false);
	}
}
function createSumRow(a, b, l) {
	var h = document.createElement("tr");
	var c = "rowSum";
	$(h).attr("id", c);
	$(h).attr("SumRow", true);
	for (var e = 0; e < a.length; e++) {
		th = a[e];
		verticalNumber = e;
		var d = document.createElement("td");
		var g = "th" + verticalNumber;
		$(d).attr("thid", g);
		if ($("#" + g).is(":hidden") == true) {
			$(d).hide();
		}
		var k = $(th).attr("number");
		var j = b.colModel[k];
		d.align = "center";
		if (j.align) {
			d.align = j.align;
		}
		var f = "";
		if (k == 1) {
			f = "合计";
		}
		if (j.isSum) {
			f = l[j.name];
			if (f == null || f == "null" || f == "") {
				f = "0";
			}
			if (j.display) {
				if (isMoneyColum(j.display)) {
					f = "￥" + flexgridFormatMoney(f);
					f = '<span class="font16 fontRed money">' + f + "</span>";
				}
				if (j.fixedValue) {
					f = '<span class="font16 fontRed money">￥' + j.fixedValue
							+ "</span>";
				}
			}
		}
		d.innerHTML = '<div class="tableText">' + f + "</div>";
		$(h).append(d);
	}
	return h;
}
function emptyTables(b, a) {
	$(a).find("tr").each(function() {
		var c = $(this).attr("id");
		if (c != "theader") {
			$(this).remove();
		}
	});
}
function sleep(b) {
	var a = new Date();
	var c = a.getTime() + b;
	while (true) {
		a = new Date();
		if (a.getTime() > c) {
			return;
		}
	}
}
var myRows = null;
var n = 0;
(function($) {
	var myGrid = null;
	function doResize() {
		try {
			if (myGrid != null && myGrid.resizeTableWidth != null) {
				myGrid.resizeTableWidth();
			}
		} catch (e) {
		}
	}
	setInterval(doResize, 200);
	var browser = $.browser;
	$.addFlex = function(t, p) {
		p = $.extend({
			id : null,
			pricePointNum : 2,
			height : "auto",
			width : "auto",
			striped : true,
			minwidth : 23,
			minheight : 80,
			resizable : true,
			url : false,
			method : "POST",
			dataType : "json",
			errormsg : "连接失败",
			nowrap : false,
			page : 1,
			total : 1,
			useRp : true,
			rp : 15,
			rpOptions : [ 10, 15, 20, 30, 50 ],
			pagestat : "显示 {from} 到 {to} 项数据，共 {total} 条数据",
			pagetext : "页",
			procmsg : "正在加载数据 ...",
			query : "",
			qtype : "",
			nomsg : "无相关数据",
			minColToggle : 1,
			getGridClass : function(g) {
				return g;
			},
			colResize : true,
			colMove : true,
			detailRowNumber : 4,
			cellPadding : 5,
			thCellPadding : 5,
			trCellPadding : 5,
			showOrHideTh : 3,
			draging : false,
			isEnableShowOrHide : true
		}, p);
		var colModelCopy = new Array();
		var buttonColom = false;
		var hasCheckBox = false;
		for (var k = 0; k < p.colModel.length; k++) {
			var ccm = p.colModel[k];
			if (ccm.isCheckbox) {
				hasCheckBox = true;
				colModelCopy.push(ccm);
			} else {
				if (!isManyStore() && ccm.display.indexOf("门店") > -1) {
				} else {
					if (!ccm.isButton) {
						colModelCopy.push(ccm);
					} else {
						buttonColom = ccm;
					}
				}
			}
		}
		if (buttonColom) {
			if (hasCheckBox) {
				colModelCopy.splice(2, 0, buttonColom);
			} else {
				colModelCopy.splice(1, 0, buttonColom);
			}
		}
		p.colModel = colModelCopy;
		if (p.colModel_Detail) {
			var colModelDetailCopy = new Array();
			for (var k = 0; k < p.colModel_Detail.length; k++) {
				var ccm = p.colModel_Detail[k];
				if (!isManyStore() && ccm.display.indexOf("门店") > -1) {
				} else {
					colModelDetailCopy.push(ccm);
				}
			}
			p.colModel_Detail = colModelDetailCopy;
		}
		$(t).show().attr({
			cellPadding : 0,
			cellSpacing : 0,
			border : 0
		}).removeAttr("width");
		var g = {
			dragStart : function(dragtype, e, obj) {
				p.draging = true;
				if (dragtype == "colresize" && p.colResize === true) {
					var n = $(obj).attr("index");
					this.colresize = {
						startX : e.pageX,
						ol : parseInt(obj.style.left, 10),
						nl : parseInt(obj.style.left, 10),
						n : n
					};
					$("body").css("cursor", "col-resize");
				}
				$("body").noSelect(true);
			},
			dragMove : function(e) {
				if (this.colresize) {
					var n = this.colresize.n;
					var diff = e.pageX - this.colresize.startX;
					var nleft = this.colresize.ol + diff;
					this.colresize.nl = nleft;
					$("#dragLineDiv" + n).css("left", nleft);
				}
			},
			dragEnd : function() {
				p.draging = false;
				if (this.colresize) {
					var n = this.colresize.n;
					var nw = parseInt($("#th" + n).attr("personwidth"), 10)
							+ (this.colresize.nl - this.colresize.ol);
					if (nw < p.minwidth) {
						nw = p.minwidth;
					}
					$("#th" + n).attr("personwidth", nw);
					this.colresize = false;
					this.resizingTdWidth();
					this.setDragSettings();
				}
				$("body").css("cursor", "default");
				$("body").noSelect(false);
			},
			resizingTdWidth : function() {
				var g = this;
				$("tr:first td", g.bDiv).each(function(i) {
					var th = $("#th" + i);
					var width = th.attr("personwidth");
					width = parseInt(width);
					$(th).attr("width", width);
					$(th).find(".bg").each(function() {
						$(this).css("width", width + "px");
					});
					$("#dragLineDiv" + i).css({
						left : (width + 9) + "px",
						top : "0px"
					});
					if ($(this).attr("thid")) {
						$(this).css("width", width + "px");
						$(this).find(".tableText").each(function() {
							$(this).css("width", width + "px");
						});
					}
				});
				$("tr", g.bDiv)
						.each(
								function(n) {
									if (n > 0
											&& ($(this).attr("NormalRow") || $(
													this).attr("SumRow"))) {
										$("td", this)
												.each(
														function(i) {
															var th = $("#th"
																	+ i);
															var width = th
																	.attr("personwidth");
															width = parseInt(width);
															if ($(this).attr(
																	"thid")) {
																$(this)
																		.css(
																				"width",
																				width
																						+ "px");
																$(this)
																		.find(
																				".tableText")
																		.each(
																				function() {
																					$(
																							this)
																							.css(
																									"width",
																									width
																											+ "px");
																				});
															}
														});
									}
								});
			},
			delCustomSettings : function() {
				if (p.id == null) {
					return;
				}
				window.localStorage
						.setItem("tableSet" + p.id + getUserId(), "");
			},
			setCustomSettings : function() {
				if (p.id == null) {
					return;
				}
				var tableSet = "";
				tableSet = "[";
				$("#tblColumSetting").find(".showorhiderow").each(
						function() {
							tableSet += '{"name":"'
									+ $(this).attr("id").replace("table_set_",
											"") + '","checked":"'
									+ $(this).attr("checked") + '"},';
						});
				tableSet = tableSet.substring(0, tableSet.length - 1);
				tableSet += "]";
				window.localStorage.setItem("tableSet" + p.id + getUserId(),
						tableSet);
			},
			getCustomSettings : function() {
				if (p.id == null) {
					return null;
				}
				var customSettings = null;
				try {
					customSettings = window.localStorage.getItem("tableSet"
							+ p.id + getUserId());
					if (customSettings != null && customSettings != "") {
						customSettings = eval(customSettings);
					}
				} catch (e) {
				}
				return customSettings;
			},
			delDragSettings : function() {
				if (p.id == null) {
					return;
				}
				window.localStorage.setItem(
						"tableDragSet" + p.id + getUserId(), "");
			},
			setDragSettings : function() {
				if (p.id == null) {
					return;
				}
				var tableSet = "";
				tableSet = "[";
				for (var n = 0; n < 30; n++) {
					var th = $("#th" + n);
					if (!th) {
						break;
					}
					tableSet += '{"name":"' + $(th).attr("name")
							+ '","personwidth":"' + $(th).attr("personwidth")
							+ '"},';
				}
				tableSet = tableSet.substring(0, tableSet.length - 1);
				tableSet += "]";
				window.localStorage.setItem(
						"tableDragSet" + p.id + getUserId(), tableSet);
			},
			getDragSettings : function() {
				if (p.id == null) {
					return null;
				}
				var customSettings = null;
				try {
					customSettings = window.localStorage.getItem("tableDragSet"
							+ p.id + getUserId());
					if (customSettings != null && customSettings != "") {
						customSettings = eval(customSettings);
					}
				} catch (e) {
				}
				return customSettings;
			},
			addData : function(data) {
				var g = this;
				data = $.extend({
					rows : [],
					page : 0,
					total : 0
				}, data);
				this.loading = false;
				p.total = data.total;
				if (p.total === 0) {
					$("tr, a,td, div", t).unbind();
					p.pages = 1;
					p.page = 1;
					emptyTables(p, t);
					var tr = createNoDataRow();
					$(t).append(tr);
					this.buildpager();
					if (typeof loadedSuccess === "function") {
						loadedSuccess();
					}
					return false;
				}
				p.pages = Math.ceil(p.total / p.rp);
				p.page = data.page;
				this.buildpager();
				myRows = data.rows;
				emptyTables(p, t);
				if (!(data.rows instanceof Array) || data.rows.length <= 0) {
					var tr = createNoDataRow();
					$(t).append(tr);
				} else {
					var tableid = t.id;
					ths = $($(t).parent().parent()).find("#theader th");
					$.each(data.rows, function(i, row) {
						var transverseNumber = i;
						tr = createGeneralRow(ths, transverseNumber, p, row);
						$(t).append(tr);
						if (p.colModel_Detail != undefined) {
							var tr_detail = createDetailRow(transverseNumber,
									p, row);
							$(t).append(tr_detail);
						}
					});
					if (data.sumRow) {
						tr = createSumRow(ths, p, data.sumRow);
						$(t).append(tr);
					}
				}
				if (typeof loadedSuccess === "function") {
					loadedSuccess();
				}
				var rp = p.rp;
				var page = p.page;
				rp = parseInt(rp);
				page = parseInt(page);
				var flag = true;
				for (var i = rp * (page - 1) + 1; i <= rp * (page - 1) + rp; i++) {
					if (rememberCheckboxMap[i] != undefined) {
						$(t).find("#fcheckbox" + i).attr("checked", true);
					} else {
						flag = false;
					}
				}
				if (flag == false) {
					$("#selectAllCheckbox", g.hDiv).attr("checked", false);
				} else {
					$("#selectAllCheckbox", g.hDiv).attr("checked", true);
				}
				flexgridpage = p.page;
				flexgridrp = p.rp;
			},
			changeSort : function(th) {
				if (p.draging) {
					return;
				}
				p.sortname = th.abbr;
				if (p.sortorder == "desc") {
					p.sortorder = "asc";
				} else {
					p.sortorder = "desc";
				}
				$(t).find("tr:first th").each(function() {
					if ($(this).attr("sortable") == "true") {
						if ($(this).attr("abbr") == p.sortname) {
							if (p.sortorder == "desc") {
								$(this).attr("class", "sortable down");
								$(this).attr("sort", "desc");
							} else {
								$(this).attr("class", "sortable up");
								$(this).attr("sort", "asc");
							}
						} else {
							$(this).attr("class", "normal sortable");
							$(this).attr("sort", "");
						}
					} else {
						$(this).attr("class", "normal");
					}
				});
				this.populate();
			},
			customChangeSort : function() {
			},
			buildpager : function() {
				var r1 = p.total == 0 ? 0 : (p.page - 1) * p.rp + 1;
				var r2 = r1 + p.rp - 1;
				if (p.total < r2) {
					r2 = p.total;
				}
				var stat = p.pagestat;
				stat = stat.replace(/{from}/, r1);
				stat = stat.replace(/{to}/, r2);
				stat = stat.replace(/{total}/, p.total);
				var tableid = t.id;
				if (tableid != undefined && tableid != null && tableid != "") {
					$($("#" + tableid).parent().parent()).find("#page").val(
							p.page);
					$($("#" + tableid).parent().parent()).find("#pages").html(
							p.total);
				} else {
					$("#page").val(p.page);
					$("#pages").html(p.total);
				}
				if (p.total == 0) {
					if (tableid != undefined && tableid != null
							&& tableid != "") {
						var obj = $("#" + tableid).parent().parent();
						$(obj).find("#next").attr("class", "nextUnclickable");
						$(obj).find("#last").attr("class", "lastUnclickable");
						$(obj).find("#pre").attr("class", "preUnclickable");
						$(obj).find("#first").attr("class", "firstUnclickable");
					} else {
						$("#next").attr("class", "nextUnclickable");
						$("#last").attr("class", "lastUnclickable");
						$("#pre").attr("class", "preUnclickable");
						$("#first").attr("class", "firstUnclickable");
					}
				} else {
					if (p.page == 1) {
						if (tableid != undefined && tableid != null
								&& tableid != "") {
							var obj = $("#" + tableid).parent().parent();
							$(obj).find("#pre").attr("class", "preUnclickable");
							$(obj).find("#first").attr("class",
									"firstUnclickable");
						} else {
							$("#pre").attr("class", "preUnclickable");
							$("#first").attr("class", "firstUnclickable");
						}
					} else {
						if (tableid != undefined && tableid != null
								&& tableid != "") {
							var obj = $("#" + tableid).parent().parent();
							$(obj).find("#pre").attr("class", "pre");
							$(obj).find("#first").attr("class", "first");
						} else {
							$("#pre").attr("class", "pre");
							$("#first").attr("class", "first");
						}
					}
					var pageCount = 0;
					if (p.total % p.rp > 0) {
						pageCount = p.total / p.rp + 1;
					} else {
						pageCount = p.total / p.rp;
					}
					pageCount = Math.floor(pageCount);
					if (pageCount == p.page) {
						if (tableid != undefined && tableid != null
								&& tableid != "") {
							var obj = $("#" + tableid).parent().parent();
							$(obj).find("#next").attr("class",
									"nextUnclickable");
							$(obj).find("#last").attr("class",
									"lastUnclickable");
						} else {
							$("#next").attr("class", "nextUnclickable");
							$("#last").attr("class", "lastUnclickable");
						}
					} else {
						if (tableid != undefined && tableid != null
								&& tableid != "") {
							var obj = $("#" + tableid).parent().parent();
							$(obj).find("#next").attr("class", "pagenext");
							$(obj).find("#last").attr("class", "last");
						} else {
							$("#next").attr("class", "pagenext");
							$("#last").attr("class", "last");
						}
					}
				}
			},
			populate : function(i) {
				if (i != undefined) {
					p.newp = 1;
				}
				$(".loadding").show();
				var g = this;
				if (!p.newp) {
					p.newp = 1;
				}
				if (p.page > p.pages) {
					p.page = p.pages;
				}
				var param = [ {
					name : "page",
					value : p.newp
				}, {
					name : "rp",
					value : p.rp
				}, {
					name : "sortname",
					value : p.sortname
				}, {
					name : "sortorder",
					value : p.sortorder
				}, {
					name : "query",
					value : p.query
				}, {
					name : "qtype",
					value : p.qtype
				} ];
				if (p.setPageParams) {
					p.params = eval(p.setPageParams);
				}
				if (p.params.length) {
					for (var pi = 0; pi < p.params.length; pi++) {
						param[param.length] = p.params[pi];
					}
				}
				$.ajax({
					type : p.method,
					url : p.url,
					data : param,
					dataType : p.dataType,
					success : function(data) {
						g.addData(data);
						$(".loadding").hide();
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						$(".loadding").hide();
						try {
						} catch (e) {
							alert(e);
						}
					}
				});
			},
			changePage : function(ctype) {
				switch (ctype) {
				case "first":
					p.newp = 1;
					break;
				case "prev":
					if (p.page > 1) {
						p.newp = parseInt(p.page, 10) - 1;
					}
					break;
				case "next":
					if (p.page < p.pages) {
						p.newp = parseInt(p.page, 10) + 1;
					}
					break;
				case "last":
					p.newp = p.pages;
					break;
				case "input":
					var nv = $("#page").val();
					if (isNaN(nv)) {
						nv = 1;
					}
					if (nv < 1) {
						nv = 1;
					} else {
						if (nv > p.pages) {
							nv = p.pages;
						}
					}
					$("#page").val(nv);
					p.newp = nv;
					break;
				}
				if (p.newp == p.page) {
					return false;
				}
				this.populate();
			},
			addCellProp : function() {
				var g = this;
				$("tbody tr td", g.bDiv).each(function() {
					var tdDiv = document.createElement("div");
					if (p.nowrap == false) {
						$(tdDiv).css("white-space", "normal");
					}
					if (this.innerHTML == "") {
						this.innerHTML = "&nbsp;";
					}
					tdDiv.innerHTML = this.innerHTML;
					$(this).empty().append(tdDiv).removeAttr("width");
				});
			},
			addRowProp : function() {
				var g = this;
				$("tbody tr", g.bDiv).each(function() {
					$(this).click(function(e) {
					});
				});
			},
			resizeTableWidth : function() {
				var g = this;
				var pDivWidth = parseInt($(g.pDiv).css("width"));
				$(g.bDiv).css("width", pDivWidth);
				$(g.hDiv).css("width", pDivWidth);
				$(g.hDiv).css("height", "80px;");
				var tableWidth = 0;
				$("#theader th", g.hDiv).each(
						function() {
							if ($(this).is(":visible")
									|| $(this).css("display") == "") {
								tableWidth += parseInt($(this).attr(
										"personwidth")) + 11;
							}
						});
				$(t).attr("width", tableWidth + 1);
				$(g.hTable).attr("width", tableWidth + 1);
				try {
					if ("auto" != p.height) {
						$(g.bDiv).height(p.height - 50);
					} else {
						var nowHeight = 400;
						if (window.innerHeight) {
							nowHeight = window.innerHeight;
						} else {
							nowHeight = document.documentElement.clientHeight;
						}
						$(g.bDiv).height(
								nowHeight - $(g.bDiv).offset().top - 26);
					}
				} catch (e) {
				}
				try {
					$(t).find(".listNodata").width(pDivWidth - 3);
					$(t).find(".listNodata").height($(g.bDiv).height() - 70);
				} catch (e) {
				}
			},
			pager : 0
		};
		myGrid = g;
		g = p.getGridClass(g);
		g.gDiv = document.createElement("div");
		g.hDiv = document.createElement("div");
		g.bDiv = document.createElement("div");
		g.pDiv = document.createElement("div");
		g.gDiv.className = "flexigrid";
		$(t).before(g.gDiv);
		$(g.gDiv).append(t);
		$(g.bDiv).attr("style", "background-color:#fff;overflow-x:auto;");
		g.bDiv.className = "bDiv";
		t.cellPadding = p.trCellPadding;
		t.cellSpacing = 0;
		$(t).attr("class", "tableStyle table table-condensed");
		$(t).css("border-top", "0px");
		$(t).before(g.bDiv);
		$(g.bDiv).append(t);
		g.addCellProp();
		g.hDiv.className = "hDiv";
		$(g.bDiv).before(g.hDiv);
		$(g.hDiv).css("overflow", "hidden");
		$(g.hDiv).css("background-color", "#fff");
		$(g.hDiv).css("position", "relative");
		g.hTable = document.createElement("table");
		g.hTable.cellPadding = p.trCellPadding;
		g.hTable.cellSpacing = 0;
		$(g.hTable).attr("class", "tableStyle table table-condensed");
		$(g.hTable).css("position", "relative");
		var tr = document.createElement("tr");
		$(tr).attr("id", "theader");
		var customSettings = g.getCustomSettings();
		var dragSettings = g.getDragSettings();
		var tableWidth = 0;
		for (var i = 0; i < p.colModel.length; i++) {
			var cm = p.colModel[i];
			var th = document.createElement("th");
			if (cm.display == "操作") {
				cm.name = "Action";
			}
			if (p.colResize && dragSettings != null && dragSettings.length > 0) {
				if (dragSettings[i].name == cm.name) {
					cm.width = dragSettings[i].personwidth;
				}
			}
			if (cm.isHide) {
				$(th).hide();
			}
			if (cm.isConfigurable && customSettings != null
					&& customSettings.length > 0) {
				for (var n = 0; n < customSettings.length; n++) {
					if (customSettings[n].name == cm.name) {
						if (customSettings[n].checked == "checked") {
							$(th).show();
						} else {
							$(th).hide();
						}
					}
				}
			}
			$(th).attr("id", "th" + i);
			$(th).attr("number", "" + i);
			var personwidth = 0;
			if (cm.width) {
				personwidth = cm.width;
			} else {
				personwidth = 150;
			}
			if (!(p.colResize && dragSettings != null && dragSettings.length > 0)) {
				if (cm.isButton && p.buttons && p.buttons.length > 0) {
					if (p.buttons.length == 1) {
						personwidth = 80;
					} else {
						if (p.buttons.length == 2) {
							personwidth = 100;
						} else {
							if (p.buttons.length == 3) {
								personwidth = 150;
							} else {
								if (p.buttons.length == 4) {
									personwidth = 190;
								}
							}
						}
					}
				}
				if (cm.display && cm.display.length >= 2 && personwidth < 40) {
					personwidth = 40;
				}
			}
			$(th).attr("personwidth", personwidth);
			$(th).attr("width", personwidth);
			$(th).attr("info", false);
			if (isMoneyColum(cm.display) && cm.display.indexOf("元") == -1) {
				cm.display += "(元)";
			}
			$(th).attr("display", cm.display);
			$(th).attr("name", cm.name);
			th.innerHTML = '<span class="bg" style="width:' + personwidth
					+ 'px">' + cm.display + "</span>";
			if (cm.isRemark) {
				$(th).attr("info", true);
				$(th).attr("display", cm.display);
				th.innerHTML = '<span class="bg" style="width:'
						+ personwidth
						+ 'px">'
						+ cm.display
						+ '</span><span class="info" onmouseover="$(this).children().show()" onmouseout="$(this).children().hide()"><span class="tips" style="display:none"><h2></h2><h1>'
						+ cm.remarkContent + "</h1></span></span>";
			}
			$(th).attr("sortable", false);
			$(th).attr("abbr", cm.name);
			if (cm.sortable == true
					&& !(cm.isNo || cm.isCheckbox || cm.isNotSorted)) {
				$(th).attr("sortable", true);
				if (cm.sortName) {
					$(th).attr("abbr", cm.sortName);
				}
			}
			if ($(th).attr("sortable") == "true") {
				$(th).bind("mousedown", function() {
					g.changeSort(this);
				});
				if (cm.name == p.sortname) {
					if (p.sortorder == "desc") {
						$(th).attr("class", "sortable down");
						$(th).attr("sort", "desc");
					} else {
						$(th).attr("class", "sortable up");
						$(th).attr("sort", "asc");
					}
				} else {
					$(th).attr("class", "normal sortable");
					$(th).attr("sort", "");
				}
			} else {
				$(th).attr("class", "normal");
			}
			th.align = "center";
			$(th).attr("style", "text-align:center;");
			if (cm.align && cm.align == "left") {
				th.align = "left";
				$(th).attr("style", "text-align:left;");
			}
			$(th).attr("checkbox", false);
			if (cm.isCheckbox) {
				$(th).attr("checkbox", cm.isCheckbox);
				th.innerHTML = '<input id="selectAllCheckbox" type="checkbox"  onclick="selectAllCheckBox(this);"/>';
			}
			$(th).attr("button", false);
			if (cm.isButton) {
				$(th).attr("button", true);
			}
			$(tr).append(th);
			if ($(th).is(":visible") || $(th).css("display") == "") {
				tableWidth += parseInt($(th).attr("personwidth"));
			}
			if (cm.noDrag) {
			} else {
				var dragLineLeft = parseInt($(th).attr("personwidth")) + 9;
				var dragLineDiv = document.createElement("div");
				$(th).append(dragLineDiv);
				$(dragLineDiv).attr("id", "dragLineDiv" + i);
				$(dragLineDiv).attr("index", i);
				$(dragLineDiv).addClass("dragLine");
				$(dragLineDiv).css({
					left : dragLineLeft + "px",
					top : "0px"
				});
				$(dragLineDiv).mousedown(function(e) {
					g.dragStart("colresize", e, this);
				});
			}
		}
		$(t).attr("width", tableWidth);
		$(g.hTable).attr("width", tableWidth);
		$(g.hTable).append(tr);
		$(g.hDiv).append(g.hTable);
		$(g.bDiv).scroll(function() {
			$(g.hTable).css("left", -1 * $(g.bDiv).scrollLeft());
		});
		g.pDiv.className = "pDiv";
		$(g.hDiv).before(g.pDiv);
		g.pTable = document.createElement("table");
		g.pTable.cellPadding = 0;
		g.pTable.cellSpacing = 0;
		$(g.pDiv).append(g.pTable);
		$(g.pTable).attr("width", "100%");
		$(g.pTable).attr("border", "0");
		var tableString = "";
		var tdString = "";
		var trString = "<tr>";
		var a = 0;
		var customSettings = g.getCustomSettings();
		for (var i = 0; i < p.colModel.length; i++) {
			var cm = p.colModel[i];
			if (cm.isConfigurable) {
				var strChecked = "";
				a++;
				if (!cm.isHide) {
					strChecked = "checked";
				}
				if (customSettings != null && customSettings.length > 0) {
					for (var n = 0; n < customSettings.length; n++) {
						if (customSettings[n].name == cm.name) {
							if (customSettings[n].checked == "checked") {
								strChecked = "checked";
							} else {
								strChecked = "";
							}
						}
					}
				}
				tdString = '<td><input class="showorhiderow" name="showorhiderow" id="table_set_'
						+ cm.name
						+ '" type="checkbox" '
						+ strChecked
						+ ' value="'
						+ cm.display
						+ '">&nbsp;'
						+ cm.display
						+ "</td>";
				trString = trString + tdString;
				if (a % 2 == 0) {
					trString = trString + "</tr><tr>";
				}
			}
		}
		trString = trString + "</tr>";
		tableString = '<table width="100%" border="0" cellspacing="0" id="tblColumSetting" cellpadding="'
				+ p.cellPadding + '">' + trString + "</table>";
		selectString = selectString + "</select>";
		var settingsButton = "";
		if (p.isEnableShowOrHide) {
			settingsButton = '<a id="set" class="set"></a>';
		}
		var selectString = '<select id="changepagesize" name="" style="height:25px" class="fl mr5">';
		for (var i = 0; i < p.rpOptions.length; i++) {
			selectString = selectString + '<option value="' + p.rpOptions[i]
					+ '">' + p.rpOptions[i] + "</option>";
		}
		selectString = selectString + "<select/>";
		var trString = '<tr><td><div class="pageTop"><span>每页显示</span>'
				+ selectString
				+ '<span>条数据</span><a id="first" class="first"></a><a id="pre" class="pre"></a><span>当前在</span><input id="page" name="" style="height:19px" type="text" class="pageInput fl"  size="3" ><span>页</span><a id="next" class="pagenext"></a><a id="last" class="last"></a><span>共<b id="pages"></b>条数据</span></div></td><td align="right">'
				+ settingsButton
				+ '<a id="refresh" class="refresh"></a><div class="lineSet"><div style="display:none" class="arrow"></div><div class="clear"></div><div id="lineSetIn" style="display:none" class="lineSetIn"><div class="title"><a  id="resetTable" class="fr">恢复默认设置</a><span class="text">列表显示条目</span></div><div class="content">'
				+ tableString
				+ '</div><div class="buttonArea"> <a id="showorhiderowno" class="button">关闭</a></div></div></div></td></tr>';
		$(g.pTable).append(trString);
		$(g.pDiv).append('<div style="height:10px"></div>');
		var tableid = t.id;
		if (tableid != undefined && tableid != null && tableid != "") {
			var obj = $("#" + tableid).parent().parent();
			$(obj).find("#first,#last,#next").click(function() {
				g.changePage(this.id);
			});
			$(obj).find("#pre").click(function() {
				g.changePage("prev");
			});
			$(obj).find("#page").keydown(
					function() {
						var event = arguments.callee.caller.arguments[0]
								|| window.event;
						if (event.keyCode == 13) {
							g.changePage("input");
						}
					});
			$(obj).find("#refresh").click(function() {
				$("#SearchKeyWord").val("");
				$("#keyword").val("");
				g.populate();
				document.onselectstart = function() {
					return false;
				};
			});
			$(obj).find("#changepagesize").change(function() {
				p.newp = 1;
				p.page = 1;
				p.rp = $(this).val();
				g.populate();
				flexgridpage = 1;
				flexgridrp = $(this).val();
			});
		} else {
			$("#first").click(function() {
				g.changePage("first");
			});
			$("#last").click(function() {
				g.changePage("last");
			});
			$("#pre").click(function() {
				g.changePage("prev");
			});
			$("#next").click(function() {
				g.changePage("next");
			});
			$("#page").keydown(
					function() {
						var event = arguments.callee.caller.arguments[0]
								|| window.event;
						if (event.keyCode == 13) {
							g.changePage("input");
						}
					});
			$("#refresh").click(function() {
				$("#SearchKeyWord").val("");
				$("#keyword").val("");
				g.populate();
				document.onselectstart = function() {
					return false;
				};
			});
			$("#changepagesize").change(function() {
				p.newp = 1;
				p.page = 1;
				p.rp = $(this).val();
				g.populate();
				flexgridpage = 1;
				flexgridrp = $(this).val();
			});
		}
		$(g.pDiv)
				.find("#resetTable")
				.click(
						function() {
							for (var i = 0; i < p.colModel.length; i++) {
								var cm = p.colModel[i];
								if (cm.isConfigurable) {
									if (cm.isHide) {
										$(g.pDiv).find("#table_set_" + cm.name)
												.removeAttr("checked");
									} else {
										$(g.pDiv).find("#table_set_" + cm.name)
												.attr("checked", "true");
									}
								}
								var value = cm.display;
								var ischeck = !cm.isHide;
								var content = "";
								var number = "";
								$("#theader th", this.bDiv)
										.each(
												function() {
													content = $(this).attr(
															"display");
													if (value == content) {
														if (ischeck) {
															$(this).show();
														} else {
															$(this).hide();
														}
														number = $(this).attr(
																"number");
														$("tbody tr", t)
																.each(
																		function() {
																			var id = $(
																					this)
																					.attr(
																							"id");
																			if (id != undefined
																					&& id != null
																					&& id != ""
																					&& id
																							.indexOf("detail") == -1) {
																				if (ischeck) {
																					$(
																							"td:eq("
																									+ number
																									+ ")",
																							this)
																							.show();
																				} else {
																					$(
																							"td:eq("
																									+ number
																									+ ")",
																							this)
																							.hide();
																				}
																			}
																		});
													}
												});
								g.resizingTdWidth();
								g.delCustomSettings();
							}
						});
		$(g.pDiv).find("#set").click(function() {
			$(".arrow").toggle();
			$("#lineSetIn").toggle();
		});
		$(g.pDiv).find("#showorhiderowno").click(function() {
			$(".arrow").hide();
			$("#lineSetIn").hide();
		});
		$(g.pDiv)
				.find(".showorhiderow")
				.click(
						function() {
							var value = $(this).attr("value");
							var ischeck = $(this).attr("checked");
							var content = "";
							var number = "";
							$("#theader th", this.bDiv)
									.each(
											function() {
												content = $(this).attr(
														"display");
												if (value == content) {
													if (ischeck) {
														$(this).show();
													} else {
														$(this).hide();
													}
													number = $(this).attr(
															"number");
													$("tbody tr", t)
															.each(
																	function() {
																		var id = $(
																				this)
																				.attr(
																						"id");
																		if (id != undefined
																				&& id != null
																				&& id != ""
																				&& id
																						.indexOf("detail") == -1) {
																			if (ischeck) {
																				$(
																						"td:eq("
																								+ number
																								+ ")",
																						this)
																						.show();
																			} else {
																				$(
																						"td:eq("
																								+ number
																								+ ")",
																						this)
																						.hide();
																			}
																		}
																	});
												}
											});
							g.resizingTdWidth();
							g.setCustomSettings();
							if ($("#tdNoData", t)) {
								var colspan = 0;
								var ths = $(g.hDiv).find("th");
								for (var i = 0; i < ths.length; i++) {
									th = ths[i];
									var thid = "th" + i;
									if ($("#" + thid, g.hDiv).css("display") != "none") {
										colspan += 1;
									}
								}
								$("#tdNoData", t).attr("colspan", colspan);
							}
						});
		t.p = p;
		t.grid = g;
		if (p.url) {
			g.populate();
		}
		$(document).mousemove(function(e) {
			g.dragMove(e);
		}).mouseup(function(e) {
			g.dragEnd();
		}).hover(function() {
		}, function() {
			g.dragEnd();
		});
		return t;
	};
	var docloaded = false;
	$(document).ready(function() {
		docloaded = true;
	});
	$.fn.flexigrid = function(p) {
		return this.each(function() {
			if (!docloaded) {
				$(this).hide();
				var t = this;
				$(document).ready(function() {
					$.addFlex(t, p);
				});
			} else {
				$.addFlex(this, p);
			}
		});
	};
	$.fn.flexReload = function(p, isSearch) {
		if (isSearch) {
			return this.each(function() {
				if (this.grid && this.p.url) {
					this.grid.populate(1);
				}
			});
		}
		return this.each(function() {
			if (this.grid && this.p.url) {
				this.grid.populate();
			}
		});
	};
	$.fn.noSelect = function(p) {
		if (p) {
			$(".flexigrid").attr("unselectable", "on");
			$(".flexigrid").css("-webkit-user-select", "none");
			$(".flexigrid").css("-moz-user-select", "none");
		} else {
			$(".flexigrid").removeAttr("unselectable", "on");
			$(".flexigrid").css("-webkit-user-select", "");
			$(".flexigrid").css("-moz-user-select", "");
		}
	};
})(jQuery);
function selectAllCheckBox(d) {
	var a = $(d).parent().parent().parent().parent().parent().parent();
	var c = null;
	flexgridpage = parseInt(flexgridpage);
	flexgridrp = parseInt(flexgridrp);
	for (var b = (flexgridpage - 1) * flexgridrp + 1; b <= ((flexgridpage - 1)
			* flexgridrp + flexgridrp); b++) {
		c = $("#fcheckbox" + b, a).val();
		if (d.checked) {
			rememberCheckboxMap[b] = c;
		} else {
			delete rememberCheckboxMap[b];
		}
	}
	if (d.checked) {
		$("input[listcheck='0']", a).attr("checked", "true");
		$("#selectall", a).attr("checked", "true");
	} else {
		$("input[listcheck='0']", a).removeAttr("checked");
		$("#selectall", a).removeAttr("checked");
	}
}
function clearRememberCheckboxMap() {
	rememberCheckboxMap = [];
}
function getData(a) {
	return myRows[a];
}
function getSelectIds() {
	var a = "";
	$.each(rememberCheckboxMap, function(b, c) {
		if (c != "" && c != undefined) {
			a += c + ",";
		}
	});
	if (a != "") {
		a = a.substring(0, a.length - 1);
	}
	rememberCheckboxMap = {};
	return a;
}
function getSelectNumber() {
	var c = new Array();
	var b = $("input[listcheck='0']");
	for (var a = 0; a < b.length; a++) {
		if (b[a].checked) {
			c.push(a);
		}
	}
	return c;
}