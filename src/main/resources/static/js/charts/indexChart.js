(function () {

    $('input,textarea').on('blur', function () {
        window.scroll(0, 0);
    });
    $('select').on('change', function () {
        window.scroll(0, 0);
    });
    $('#consumeDatetimepicker').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
        ignoreReadonly: true
    });
    //消费类别
    initConsumeCategory();
    $(".rdolist").labelauty("rdolist", "rdo");
    //分页查询
    const total = getPageDetail(1);
    //分页插件初始化
    initPagePlugin(total);
    //图表
    initChart();
    //下拉框change事件
    $(".selectMonitor").on("change", function () {
        const total = selectPage();
        initPagePlugin(total);
    })
    //消费分类选中赋值
    $(".consumeCategoryDiv").on("click", function () {
        //关闭模态框
        $("#closeModal").click();
        var consumeCategoryName = $(".rdobox.checked").find(".radiobox-content").text();
        $("#selectedCategory").text(consumeCategoryName)
        const total = selectPage();
        initPagePlugin(total);
    })

})();

//分页查询
function selectPage(pageNumer) {
    let consumeBy = $("#consumeBySelect").val() == 0 ? null : $("#consumeBySelect").val();
    let consumeCategory = $("input[name='consumeCategoryId']:checked").val();
    let consumeWay = $("#consumeWaySelect").val() == 0 ? null : $("#consumeWaySelect").val();
    let consumeDateType = $("#consumeDateSelect").val() == 0 ? null : $("#consumeDateSelect").val();
    const currentPage = pageNumer == null ? 1 : pageNumer;
    return getPageDetail(currentPage, consumeBy, consumeCategory, consumeWay, consumeDateType);
}

//初始化消费类别
function initConsumeCategory() {
    $.ajax({
        url: 'consume/category/getAll',
        type: 'get',
        cache: false,
        processData: false,
        async: false,
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

//初始化分页插件
function initPagePlugin(total) {
    let pageSize = 10;
    $("#pagination").html("");
    $("#pagination").pagination({
        pages: Math.ceil(total / pageSize),
        edges: 2,
        cssStyle: 'pagination-sm',
        displayedPages: 5,
        onPageClick: function (pageNumber, event) {
            selectPage(pageNumber);
        },
        onInit: function (getid) {
            const consumeBy = $("#consumeBySelect").val() == 0 ? null : $("#consumeBySelect").val()
            //刷新时调用
            // getPageDetail(getid, consumeBy, null, null)
        }
    });
}

//分页消费详情
function getPageDetail(currentPage, consumeBy, consumeCategoryId, consumeWay, consumeDateType) {
    const pageSize = 10;
    let total = 0;
    $.ajax({
        url: 'consume/detail/pageDetail',
        data: JSON.stringify({
            "currentPage": currentPage,
            "pageSize": pageSize,
            "consumeBy": consumeBy,
            "consumeCategoryId": consumeCategoryId,
            "consumeWay": consumeWay,
            "consumeDateType": consumeDateType
        }),
        contentType: 'application/json',
        type: 'post',
        cache: false,
        async: false,
        processData: false,
    }).done(function (res) {
        const details = res.data.consumeDetails;
        initConsumeDetailBody();
        for (const index in details) {
            let row = null;
            const consumeDetail = details[index];
            if (index == 0) {
                row = $("#consumeDetailTemplate");
                total = res.data.total;
            } else {
                row = $("#consumeDetailTemplate").clone();
            }
            let consumeBy = consumeDetail.consumeBy;
            if (consumeBy == 1) {
                consumeBy = "可";
            } else if (consumeBy == 2) {
                consumeBy = "欣"
            }
            row.find("#consumeBy").text(consumeBy);
            row.find("#consumeDetailId").text(consumeDetail.id)
            row.find("#consumeCategoryName").text(consumeDetail.consumeCategoryName);
            let consumeWay = consumeDetail.consumeWay;
            if (consumeWay == 1) {
                consumeWay = "支付宝"
            } else if (consumeWay == 2) {
                consumeWay = "微信"
            } else if (consumeWay == 3) {
                consumeWay = "现金"
            } else {
                consumeWay = "-"
            }
            row.find("#consumeWay").text(consumeWay);
            let tempDate = new Date(consumeDetail.consumeDate);
            let month = tempDate.getMonth() + 1;
            if (month < 10) {
                month = "0" + month;
            }
            let date = tempDate.getDate();
            if (date < 10) {
                date = "0" + date;
            }
            row.find("#consumeDate").text(consumeDetail.consumeDate == null ? "-" : month + "-" + date);
            row.find("#consumeDate").attr("title", consumeDetail.consumeDate)
            // row.find("#consumeDate").text(consumeDetail.consumeDate == null ? "-" : consumeDetail.consumeDate);
            row.find("#consumeAmount").text(consumeDetail.consumeAmount + "元");
            let comment = consumeDetail.consumeComment;
            row.find("#consumeComment").attr("title", comment)
            if (comment) {
                row.find("#consumeComment").text(comment.substring(0, 3));
            }else {
                row.find("#consumeComment").text("");
            }

            row.appendTo("#consumeDetailTable");//添加到模板的容器中
        }
    }).fail(function (res) {
    });
    return total;
}

//重置body
function initConsumeDetailBody() {
    $("#consumeDetailBody").html(
        "<tr  id=\"consumeDetailTemplate\" data-toggle=\"modal\" onclick='editConsumeDetail(this)' data-target=\"#editConsumeDetailModal\">\n" +
        "<td style=\"text-align: center\" id=\"consumeBy\"></td>\n" +
        "<td style=\"text-align: center\" id=\"consumeCategoryName\"></td>\n" +
        "<td style=\"text-align: center\" id=\"consumeWay\"></td>\n" +
        "<td style=\"text-align: center;font-size: xx-small\" id=\"consumeDate\"></td>\n" +
        "<td style=\"text-align: center\" id=\"consumeAmount\"></td>\n" +
        "<td style=\"text-align: center\" id=\"consumeComment\"></td>\n" +
        "<td style='display: none' id='consumeDetailId'></td>" +
        "</tr>"
    )
}

//完整的日期
function showFullDate(obj) {
    layer.msg($(obj).attr("title"), {
        time: 1300
    })
}

//编辑消费详情
function editConsumeDetail(obj) {

    //计算器初始化
    $('#editConsumeAmount').calculator();

    //日期选择器初始化
    $('#consumeDatetimepicker').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
        ignoreReadonly: true
    });

    let consumeBy = $(obj).find("#consumeBy").text()
    if (consumeBy == '可') {
        consumeBy = 1;
    } else if (consumeBy == '欣') {
        consumeBy = 2;
    }
    $(":radio[name='editConsumeBy'][value='" + consumeBy + "']").prop("checked", "checked");

    let consumeAmount = $(obj).find("#consumeAmount").text().replace("元", "");
    $("#editConsumeAmount").val(consumeAmount)

    let consumeWay = $(obj).find("#consumeWay").text().replace("元", "");
    if (consumeWay == '支付宝') {
        consumeWay = 1;
    } else if (consumeWay == '微信') {
        consumeWay = 2;
    } else if (consumeWay == '现金') {
        consumeWay = 3;
    }
    $(":radio[name='editConsumeWay'][value='" + consumeWay + "']").prop("checked", "checked");

    let consumeDate = $(obj).find("#consumeDate").attr("title");
    $("#editConsumeDate").val(consumeDate)

    let consumeComment = $(obj).find("#consumeComment").attr("title");
    $("#editConsumeComment").val(consumeComment)

    //查出所有分类
    var categoryMap = {};
    $.ajax({
        url: 'consume/category/getAll',
        type: 'get',
        cache: false,
        processData: false,
        async: false,
    }).done(function (res) {
        var categorys = res.data;
        $("#editConsumeCategoryRadio").text("")
        for (var index in categorys) {
            var category = categorys[index]
            $("<div style='float: left;padding-left: 3px' class='consumeCategoryDiv'><input type='radio' value=" + category.id + " name='editConsumeCategoryId' class='rdolist'/>\n" +
                "<label class='rdobox'>\n" +
                "<span class='check-image'></span>\n" +
                "<span class='radiobox-content'>" + category.categoryName + "</span>\n" +
                " </label></div>")
                .appendTo("#editConsumeCategoryRadio");
            categoryMap[category.categoryName] = category.id
        }
    }).fail(function (res) {
    });
    //回显分类
    let consumeCategoryName = $(obj).find("#consumeCategoryName").text();
    $("#editSelectedCategory").text(consumeCategoryName)
    //初始化selectbox
    $(".rdolist").labelauty("rdolist", "rdo");
    //选中
    let categoryId = categoryMap[consumeCategoryName];
    let categoryRow = $(":radio[name='editConsumeCategoryId'][value='" + categoryId + "']");
    categoryRow.prop("checked", "checked");
    categoryRow.next().addClass("checked")
    categoryRow.next().find(".check-image").css("background", "url(static/img/input-checked.png)");
    //id
    $("#consumeDetailId").val($(obj).find("#consumeDetailId").text())
}

//更新消费详情
function updateConsumeDetail() {

    let ss = $("input[name='editConsumeCategoryId']:checked").val();

    $.ajax({
        url: 'consume/detail/saveOrUpdate',
        type: 'POST',
        cache: false,
        data: JSON.stringify({
            "id": $("#consumeDetailId").val(),
            "consumeAmount": $("#editConsumeAmount").val(),
            "consumeCategoryId": $("input[name='editConsumeCategoryId']:checked").val(),
            "consumeWay": $("input[name='editConsumeWay']:checked").val(),
            "consumeDate": $("#editConsumeDate").val(),
            "consumeBy": $("input[name='editConsumeBy']:checked").val(),
            "consumeComment": $("#editConsumeComment").val()
        }),
        async: false,
        contentType: 'application/json',
        success: function (data) {
            layer.msg(data.message, {
                time: 2800
            })
            $("#closeConsumeDetailModal").click()
            window.location.href = "/admin"
        },
        error: function (data) {
            layer.msg("记录失败!", {
                time: 2800
            })
        }
    });
}

//收支总览
function getIncomeAndExpenditure() {
    getMonthlyConsumeAmount();
    getWeeklyConsumeAmount();
    getMonthlyIncomeAmount();
}

//月度消费金额
function getMonthlyConsumeAmount() {
    $.ajax({
        url: 'getMonthlyConsumeAmount',
        type: 'get',
        cache: false,
        processData: false,
    }).done(function (res) {
        $("#monthlyTotalConsumeAmount").text(res.data + "元")
    }).fail(function (res) {
    });
}

//周度消费金额
function getWeeklyConsumeAmount() {

    $.ajax({
        url: 'getWeeklyConsumeAmount',
        type: 'get',
        cache: false,
        processData: false,
    }).done(function (res) {
        $("#weeklyTotalConsumeAmount").text(res.data + "元")
    }).fail(function (res) {
    });
}

//月度收入金额
function getMonthlyIncomeAmount() {

    $.ajax({
        url: 'getMonthlyIncomeAmount',
        type: 'get',
        cache: false,
        processData: false,
    }).done(function (res) {
        $("#monthlyIncomeAmount").text(res.data + "元")
    }).fail(function (res) {
    });
}

function initChart() {
    var labelTop = {

        normal: {
            label: {
                show: true,
                position: 'center',
                formatter: '{b}',
                textStyle: {
                    baseline: 'bottom'
                }
            },
            labelLine: {
                show: false
            }

        }
    };
    var labelFromatter = {
        normal: {
            label: {
                formatter: function (params) {
                    return 100 - params.value + '%'
                },
                textStyle: {
                    baseline: 'center'
                }
            }
        },
    }
    var labelBottom = {
        normal: {
            color: '#ccc',
            label: {
                show: true,
                position: 'center'
            },
            labelLine: {
                show: false
            }
        },
        emphasis: {
            color: '#ccc'
        }
    };
    var radius = [40, 35];
    option = {
        legend: {
            x: 'center',
            y: 'center',
        },
        grid: {
            x: 0,
            y: 0,
            x2: 0,
            y2: 0
        },
        toolbox: {
            show: true,
            feature: {
                magicType: {
                    show: true,
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            width: '20%',
                            height: '30%',
                            itemStyle: {
                                normal: {
                                    label: {
                                        formatter: function (params) {
                                            return 'other\n' + params.value + '%\n'
                                        },
                                        textStyle: {
                                            baseline: 'middle'
                                        }
                                    }
                                },
                            }
                        }
                    }
                }

            }
        },
        series: [
            {
                type: 'pie',

                radius: radius,
                x: '0%', // for funnel
                itemStyle: labelFromatter,
                data: [
                    {name: 'other', value: 46, itemStyle: labelBottom},
                    {name: '', value: 54, itemStyle: labelTop}
                ]
            }

        ],
        animation: false
    };
}

function getPieChat() {
    pieChart();
}

function getLineChat() {
    lineChart();
}

//扇形图
function pieChart() {
    const myChart = echarts.init(document.getElementById("index-pie-1"));

    let monthlyConsumeData = null;

    $.ajax({
        url: 'getMonthlyConsumeProportion',
        type: 'get',
        cache: false,
        processData: false,
        async: false
    }).done(function (res) {
        monthlyConsumeData = res.data;
    }).fail(function (res) {
    });
    let categoryData = [];
    for (const p in monthlyConsumeData) {
        categoryData[p] = monthlyConsumeData[p].name;
    }
    option = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            bottom: 1,
            left: 'center',
            data: categoryData
        },
        toolbox: {
            show: false,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {
                    show: true,
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            x: '25%',
                            width: '50%',
                            funnelAlign: 'center',
                            max: 1548
                        }
                    }
                },
                restore: {show: false},
                saveAsImage: {show: false}
            }
        },
        calculable: true,
        series: [
            {
                name: '消费占比',
                type: 'pie',
                radius: '60%',
                center: ['50%', '50%'],
                selectedMode: 'single',
                label: {
                    show: false,
                },
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        labelLine: {
                            show: false
                        }
                    },
                    emphasis: {
                        label: {
                            show: true,
                            position: 'center',
                            textStyle: {
                                fontSize: '20',
                                fontWeight: 'bold'
                            }
                        }
                    }
                },
                data: monthlyConsumeData
            }
        ]
    };
    myChart.setOption(option);
}

//折线图
function lineChart() {
    let everyDayAmountArr = [];
    let categoryArr = [];
    $.ajax({
        url: 'getEveryDayConsumeAmount?days=' + 7,
        type: 'get',
        cache: false,
        processData: false,
        async: false
    }).done(function (res) {
        const datas = res.data;
        for (const p in datas) {
            everyDayAmountArr[p] = datas[p].amount;
            categoryArr[p] = datas[p].date;
        }

    }).fail(function (res) {
    });
    const myChart = echarts.init(document.getElementById("index-line-1"));
    option = {
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['每日消费'],
            x: "right"
        },
        xAxis: {
            name: "日期",
            type: 'category',
            boundaryGap: false,
            data: categoryArr
        },
        yAxis: {
            name: "金额",
            type: 'value',
            axisLabel: {}
        },
        grid: {
            x: 80,
            y: 60,
            x2: 80,
            y2: 60,
            left: 35,
            // width: {totalWidth} - x - x2,
            // height: {totalHeight} - y - y2,
            backgroundColor: 'rgba(0,0,0,0)',
            borderWidth: 1,
            borderColor: '#ccc'
        },
        series: [
            {
                name: '消费金额',
                type: 'line',
                data: everyDayAmountArr,
                markPoint: {
                    symbol: 'roundRect',
                    symbolSize: [38, 20],// 容器大小
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };
    myChart.setOption(option);
}

(function () {
    const myChart = echarts.init(document.getElementById("index-pie-1"));

    let monthlyConsumeData = null;

    $.ajax({
        url: 'getMonthlyConsumeProportion',
        type: 'get',
        cache: false,
        processData: false,
        async: false
    }).done(function (res) {
        monthlyConsumeData = res.data;
    }).fail(function (res) {
    });
    let categoryData = [];
    for (const p in monthlyConsumeData) {
        categoryData[p] = monthlyConsumeData[p].name;
    }
    option = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            bottom: 1,
            left: 'center',
            data: categoryData
        },
        toolbox: {
            show: false,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {
                    show: true,
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            x: '25%',
                            width: '50%',
                            funnelAlign: 'center',
                            max: 1548
                        }
                    }
                },
                restore: {show: false},
                saveAsImage: {show: false}
            }
        },
        calculable: true,
        series: [
            {
                name: '消费占比',
                type: 'pie',
                radius: '60%',
                center: ['50%', '50%'],
                selectedMode: 'single',
                label: {
                    show: false,
                },
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        labelLine: {
                            show: false
                        }
                    },
                    emphasis: {
                        label: {
                            show: true,
                            position: 'center',
                            textStyle: {
                                fontSize: '20',
                                fontWeight: 'bold'
                            }
                        }
                    }
                },
                data: monthlyConsumeData
            }
        ]
    };
    myChart.setOption(option);
})();

//一周消费折线图
(function () {
    let everyDayAmountArr = [];
    let categoryArr = [];
    $.ajax({
        url: 'getEveryDayConsumeAmount?days=' + 7,
        type: 'get',
        cache: false,
        processData: false,
        async: false
    }).done(function (res) {
        const datas = res.data;
        for (const p in datas) {
            everyDayAmountArr[p] = datas[p].amount;
            categoryArr[p] = datas[p].date;
        }

    }).fail(function (res) {
    });
    const myChart = echarts.init(document.getElementById("index-line-1"));
    option = {
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['每日消费'],
            x: "right"
        },
        xAxis: {
            name: "日期",
            type: 'category',
            boundaryGap: false,
            data: categoryArr
        },
        yAxis: {
            name: "金额",
            type: 'value',
            axisLabel: {}
        },
        grid: {
            x: 80,
            y: 60,
            x2: 80,
            y2: 60,
            left: 35,
            // width: {totalWidth} - x - x2,
            // height: {totalHeight} - y - y2,
            backgroundColor: 'rgba(0,0,0,0)',
            borderWidth: 1,
            borderColor: '#ccc'
        },
        series: [
            {
                name: '消费金额',
                type: 'line',
                data: everyDayAmountArr,
                markPoint: {
                    symbol: 'roundRect',
                    symbolSize: [38, 20],// 容器大小
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };
    myChart.setOption(option);
})();

//新增分类
function addCategory(type) {
    let url = "";
    let categoryName = "";
    if (type === 1) {
        categoryName = $("#addConsumeCategory").val();
        url = "consume/category/add?categoryName=" + categoryName;
    } else if (type === 2) {
        categoryName = $("#addIncomeCategory").val();
        url = "income/category/add?categoryName=" + categoryName;
    }
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            layer.msg(data.message, {
                time: 1800
            })
            $("#closeConfigModal").click();
        },
        error: function (data) {
            layer.msg('添加失败!', {
                time: 1800
            })
        }
    });
}

