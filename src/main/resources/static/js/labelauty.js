﻿(function ($) {
    $.fn.labelauty = function (tag, tag2) {
        
        //判断是否选中
        rdochecked(tag);

        //单选or多选
        if (tag2 == "rdo") {
            //单选
            $(".rdobox").click(function () {
                $(this).prev().prop("checked", "checked");
                rdochecked(tag);
            });
        } else {
            //多选
            $(".chkbox").click(function () {
                //
                if ($(this).prev().prop("checked") == true) {
                    $(this).prev().removeAttr("checked");
                }
                else {
                    $(this).prev().prop("checked", "checked");
                }
                rdochecked(tag);
            });
        }

        //判断是否选中
        function rdochecked(tag) {
            $('.' + tag).each(function (i) {
                var rdobox = $('.' + tag).eq(i).next();
                if ($('.' + tag).eq(i).prop("checked") == false) {
                    rdobox.removeClass("checked");
                    rdobox.addClass("unchecked");
                    rdobox.find(".check-image").css("background", "url(static/img/input-unchecked.png)");
                }
                else {
                    rdobox.removeClass("unchecked");
                    rdobox.addClass("checked");
                    rdobox.find(".check-image").css("background", "url(static/img/input-checked.png)");
                }
            });
        }
    }
}(jQuery));