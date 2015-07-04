/**
 * #1,图片放大镜
 * #2,图片放大缩小
 */
//1，图片放大镜
$(document).ready(function(){
	//*************************************************************//
	//#1,图片放大镜
	// 定义图像的实际尺寸、
	var native_width = 0;
	var native_height = 0;
	// 首先、我们应该获得图像的实际尺寸、（本地的图片）
	$('.small').load(function(){
		// 这里我们需要重新创建一个和之前相同的图像对象、
		// 因为我们不能直接获得图像尺寸的宽高、
		// 因为我们在HTML里已经指定了图片宽度为200px、
		var img_obj = new Image();
		img_obj.src = $(this).attr('src');

		//  在这里这段代码写在这里是非常有必要的、
		//  如果在图像加载之前就访问的话、return的宽高值为0、
		native_width = img_obj.width;
		native_height = img_obj.height;

		// 现在、我来开始写鼠标移动的函数、mousemove()
		$('.magnify').mousemove(function(e){
			// 获得鼠标X轴和Y轴的坐标
			//  先获得magnify相对与document的定位position
			var magnify_offset = $(this).offset();

			// 这里我们用鼠标相对与文档的位置减去鼠标相对于magnify这个人容器的位置 来得到鼠标的位置
			var mouse_x = e.pageX - magnify_offset.left;
			var mouse_y = e.pageY - magnify_offset.top;


			// 现在、我们来调整一下放大镜的隐藏与显示、
			if( mouse_x > 0 && mouse_y > 0 && mouse_x < $(this).width() && mouse_y < $(this).height() ){
				$('.large').fadeIn(100);
			}else{
				$('.large').fadeOut(100);
			}
			if($('.large').is(':visible')){
				// 放大镜图片背景的定位是根据鼠标在小图片上改变的位置来改变的、
				// 因此、我们应该先得到放大的比例、来定位这个放大镜里背景图片的定位、
				
				/*
				var ratio_x = mouse_x/$('.small').width();//得到的是缩放的比例		
				var large_x = ratio_x*native_width;
				// 我们需要让它在放大镜的中间位置显示、
				large_x = large_x - $('.large').width()/2;
				// 因为背景图片的定位、这里需要转化为负值、
				large_x = large_x*-1;
				// 现在我们来整合一下所有的计算步骤、
				*/
				var rx = Math.round(mouse_x/$('.small').width()*native_width - $('.large').width()/2)*-1;
				var ry = Math.round(mouse_y/$('.small').height()*native_height - $('.large').height()/2)*-1;
				var bgp = rx + 'px ' + ry + 'px';

				// 现在我们应该来写放大镜跟随鼠标的效果、
				// 放大镜移动的位置 相对于文档的位置 减去 放大镜相对于放大这个层的offset的位置、
				// 再减去放大镜宽高的一半、保证放大镜的中心跟随鼠标

				var gx = mouse_x - $('.large').width()/2;
				var gy = mouse_y - $('.large').height()/2;

				$('.large').css({
					'left':gx,
					'top':gy,
					'backgroundPosition':bgp
				})
			}
		})
	})
	// 最后、我们来把这个mousemove()这个函数来放在这个load这个函数里
	//*************************************************************//
	//#2,图片放大
	// 图片上下滚动
	var count = $("#imageMenu li").length - 5; /* 显示 6 个 li标签内容 */
	var interval = $("#imageMenu li:first").width();
	var curIndex = 0;
	
	$('.scrollbutton').click(function(){
		if( $(this).hasClass('disabled') ) return false;
		
		if ($(this).hasClass('smallImgUp')) --curIndex;
		else ++curIndex;
		
		$('.scrollbutton').removeClass('disabled');
		if (curIndex == 0) $('.smallImgUp').addClass('disabled');
		if (curIndex == count-1) $('.smallImgDown').addClass('disabled');
		
		$("#imageMenu ul").stop(false, true).animate({"marginLeft" : -curIndex*interval + "px"}, 600);
	});	
	// 解决 ie6 select框 问题
	$.fn.decorateIframe = function(options) {
        if ($.browser.msie && $.browser.version < 7) {
            var opts = $.extend({}, $.fn.decorateIframe.defaults, options);
            $(this).each(function() {
                var $myThis = $(this);
                //创建一个IFRAME
                var divIframe = $("<iframe />");
                divIframe.attr("id", opts.iframeId);
                divIframe.css("position", "absolute");
                divIframe.css("display", "none");
                divIframe.css("display", "block");
                divIframe.css("z-index", opts.iframeZIndex);
                divIframe.css("border");
                divIframe.css("top", "0");
                divIframe.css("left", "0");
                if (opts.width == 0) {
                    divIframe.css("width", $myThis.width() + parseInt($myThis.css("padding")) * 2 + "px");
                }
                if (opts.height == 0) {
                    divIframe.css("height", $myThis.height() + parseInt($myThis.css("padding")) * 2 + "px");
                }
                divIframe.css("filter", "mask(color=#fff)");
                $myThis.append(divIframe);
            });
        }
    }
    $.fn.decorateIframe.defaults = {
        iframeId: "decorateIframe1",
        iframeZIndex: -1,
        width: 0,
        height: 0
    }
    //放大镜视窗
    $("#bigView").decorateIframe();
    //点击到中图
    var midChangeHandler = null;
	
    $("#imageMenu li img").bind("click", function(){
		if ($(this).attr("id") != "onlickImg") {
			midChange($(this).attr("src").replace("small", "mid"));
			$("#imageMenu li").removeAttr("id");
			$(this).parent().attr("id", "onlickImg");
		}
	}).bind("mouseover", function(){
		if ($(this).attr("id") != "onlickImg") {
			window.clearTimeout(midChangeHandler);
			midChange($(this).attr("src").replace("small", "mid"));
			$(this).css({ "border": "3px solid #959595" });
		}
	}).bind("mouseout", function(){
		if($(this).attr("id") != "onlickImg"){
			$(this).removeAttr("style");
			midChangeHandler = window.setTimeout(function(){
				midChange($("#onlickImg img").attr("src").replace("small", "mid"));
			}, 1000);
		}
	});
    function midChange(src) {
        $("#midimg").attr("src", src).load(function() {
            changeViewImg();
        });
    }
    //大视窗看图
    function mouseover(e) {
        if ($("#winSelector").css("display") == "none") {
            $("#winSelector,#bigView").show();
        }
        $("#winSelector").css(fixedPosition(e));
        e.stopPropagation();
    }
    function mouseOut(e) {
        if ($("#winSelector").css("display") != "none") {
            $("#winSelector,#bigView").hide();
        }
        e.stopPropagation();
    }
    $("#midimg").mouseover(mouseover); //中图事件
    $("#midimg,#winSelector").mousemove(mouseover).mouseout(mouseOut); //选择器事件

    var $divWidth = $("#winSelector").width(); //选择器宽度
    var $divHeight = $("#winSelector").height(); //选择器高度
    var $imgWidth = $("#midimg").width(); //中图宽度
    var $imgHeight = $("#midimg").height(); //中图高度
    var $viewImgWidth = $viewImgHeight = $height = null; //IE加载后才能得到 大图宽度 大图高度 大图视窗高度

    function changeViewImg() {
        $("#bigView img").attr("src", $("#midimg").attr("src").replace("mid", "big"));
    }
    changeViewImg();
    $("#bigView").scrollLeft(0).scrollTop(0);
    function fixedPosition(e) {
        if (e == null) {
            return;
        }
        var $imgLeft = $("#midimg").offset().left; //中图左边距
        var $imgTop = $("#midimg").offset().top; //中图上边距
        X = e.pageX - $imgLeft - $divWidth / 2; //selector顶点坐标 X
        Y = e.pageY - $imgTop - $divHeight / 2; //selector顶点坐标 Y
        X = X < 0 ? 0 : X;
        Y = Y < 0 ? 0 : Y;
        X = X + $divWidth > $imgWidth ? $imgWidth - $divWidth : X;
        Y = Y + $divHeight > $imgHeight ? $imgHeight - $divHeight : Y;

        if ($viewImgWidth == null) {
            $viewImgWidth = $("#bigView img").outerWidth();
            $viewImgHeight = $("#bigView img").height();
            if ($viewImgWidth < 200 || $viewImgHeight < 200) {
                $viewImgWidth = $viewImgHeight = 800;
            }
            $height = $divHeight * $viewImgHeight / $imgHeight;
            $("#bigView").width($divWidth * $viewImgWidth / $imgWidth);
            $("#bigView").height($height);
        }
        var scrollX = X * $viewImgWidth / $imgWidth;
        var scrollY = Y * $viewImgHeight / $imgHeight;
        $("#bigView img").css({ "left": scrollX * -1, "top": scrollY * -1 });
        $("#bigView").css({ "top": 75, "left": $(".preview").offset().left + $(".preview").width() + 15 });

        return { left: X, top: Y };
    }
});
