$(function () {
    //先判断是否有家庭//强制弹框//本来要后台security filter实现的,暂时不搞了...简单实现先
    $.ajax({
        url: '/user/getUser/',
        type: 'get',
        cache: false,
        processData: false,
        async: true,
    }).done(function (res) {
        const userStatus = res.data.userStatus
        if (userStatus == 0) {
            window.localStorage.setItem("phoneNumber", res.data.phoneNumber)
            window.location.href = "/page/myFamily"
        } else if (userStatus == 1) {
            initIndex();
        }
    }).fail(function (res) {

    });

});

function initIndex() {
    //计算器初始化
    $('#consumeAmount').calculator();
    $('#consumeDatetimepicker').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
        ignoreReadonly: true
    });
    $('#incomeDatetimepicker').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
        ignoreReadonly: true
    });
    initConsumeCategory();
    initincomeCategory();
    $(".rdolist").labelauty("rdolist", "rdo");

    let now = new Date();
    let month = Number(now.getMonth()) + 1;
    let date = now.getDate();
    if (month < 10) {
        month = "0" + month
    }
    if (date < 10) {
        date = "0" + date
    }
    let nowDate = now.getFullYear() + "-" + month + "-" + date;
    $("#consumeDate").val(nowDate)
    $("#incomeDate").val(nowDate)

    $(".consumeCategoryDiv").on("click", function () {
        //关闭模态框
        $("#closeModal").click();
        const consumeCategoryName = $(".rdobox.checked").find(".radiobox-content").text();
        $("#selectedCategory").text(consumeCategoryName)
    })
}

//初始化消费类别
function initConsumeCategory() {
    $.ajax({
        url: '/consume/category/getAll',
        type: 'get',
        cache: false,
        processData: false,
        async: false,//这里写true会导致选择不了,不懂为啥
    }).done(function (res) {
        var categorys = res.data;
        for (var index in categorys) {
            var category = categorys[index]
            $("<div style='float: left;padding-left: 3px' class='consumeCategoryDiv'><input type='radio' value=" + category.id + " name='consumeCategoryId' class='rdolist'/>\n" +
                "<label class='rdobox'>\n" +
                "<span class='check-image'></span>\n" +
                "<span class='radiobox-content'>" + category.categoryName + "</span>\n" +
                " </label></div>")
                .appendTo("#consumeCategoryRadio");
        }
    }).fail(function (res) {

    });
}

//初始化收益类别
function initincomeCategory() {
    $.ajax({
        url: '/income/category/getAll',
        type: 'get',
        cache: false,
        processData: false,
        async: true
    }).done(function (res) {
        const categorys = res.data;
        for (const index in categorys) {
            const category = categorys[index];
            $("<option style='text-align: center' value=" + category.id + ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + category.categoryName + "</option>").appendTo("#incomeCategoryId");
        }
    }).fail(function (res) {

    });
}

function addConsumeDetail() {
    const consumeAmount = $("#consumeAmount").val();
    const consumeCategoryId = $("input[name='consumeCategoryId']:checked").val();
    $.ajax({
        url: '/consume/detail/saveOrUpdate',
        type: 'POST',
        cache: false,
        data: JSON.stringify({
            "consumeCategoryId": consumeCategoryId,
            "consumeAmount": consumeAmount,
            "consumeWay": $("input[name='consumeWay']:checked").val(),
            "consumeDate": $("#consumeDate").val(),
            "consumeComment": $("#consumeComment").val()
        }),
        async: false,
        contentType: 'application/json',
        success: function (data) {
            layer.msg(data.message, {
                time: 2800
            })
            if (data.code == 0) {
                setTimeout(function () {
                    window.location.href = "/page/index";
                }, 1500);
            }
        },
        error: function (data) {
            layer.msg("记录失败!", {
                time: 2800
            })
        }
    });
}

function addIncomeDetail() {
    $.ajax({
        url: '/income/detail/saveOrUpdate',
        type: 'POST',
        cache: false,
        data: JSON.stringify({
            "incomeCategoryId": $("#incomeCategoryId").val(),
            "incomeAmount": $("#incomeAmount").val(),
            "incomeDate": $("#incomeDate").val(),
            "incomeComment": $("#incomeComment").val()
        }),
        async: false,
        contentType: 'application/json',
        success: function (data) {
            layer.msg(data.message, {
                time: 2800
            })
            if (data.code == 0) {
                setTimeout(function () {
                    window.location.href = "/page/index";
                }, 1500);
            }
        },
        error: function (data) {
            layer.msg("记录失败!", {
                time: 2800
            })
        }
    });
}

function logout() {
    $.ajax({
        url: '/user/logout',
        type: 'get',
        cache: false,
        async: false,
        contentType: 'application/json',
        success: function (data) {
            window.location.href = "/user/login"
        },
        error: function (data) {
            layer.msg("退出失败", {
                time: 2800
            })
        }
    });
}
