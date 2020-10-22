(function () {
    //最近消费记录
    getLastDetail();
    //月度消费金额
    getMonthlyConsumeAmount();
    //周度消费金额
    getWeeklyConsumeAmount();
    //本月收入
    getMonthlyIncomeAmount();


    // var myChart2 = echarts.init(document.getElementById("widget-chart-box-2"));

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


    // myChart.setOption(option);
    // myChart2.setOption(option);
})();

//最近消费记录
function getLastDetail() {
    $.ajax({
        url: 'consume/detail/getLastDetail?size=' + 15,
        type: 'get',
        cache: false,
        processData: false,
    }).done(function (res) {
        const details = res.data;
        for (const index in details) {
            const consumeDetail = details[index];
            let row = null;
            if (index == 0) {
                row = $("#consumeDetailTemplate");
            } else {
                row = $("#consumeDetailTemplate").clone();
            }
            const consumeBy = consumeDetail.consumeBy == 1 ? "许可夫" : "刘欣";
            row.find("#consumeBy").text(consumeBy);
            row.find("#consumeCategoryName").text(consumeDetail.consumeCategoryName);
            row.find("#consumeAmount").text(consumeDetail.consumeAmount + "元");
            let consumeWay = consumeDetail.consumeWay;
            if (consumeWay == 1) {
                consumeWay = "支付宝"
            } else if (consumeWay == 2) {
                consumeWay = "微信"
            } else {
                consumeWay = "现金"
            }
            row.find("#consumeWay").text(consumeWay);
            row.find("#consumeDate").text(consumeDetail.consumeDate);
            row.appendTo("#consumeDetailTable");//添加到模板的容器中
        }
    }).fail(function (res) {
    });
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

//扇形图
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


/*(function () {
    var myChart = echarts.init(document.getElementById("index-bar-1"));

    option = {
        color: ['#3398DB'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '直接访问',
                type: 'bar',
                barWidth: '60%',
                data: [10, 52, 200, 334, 390, 330, 220]
            }
        ]
    };


    myChart.setOption(option);
})();*/


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

