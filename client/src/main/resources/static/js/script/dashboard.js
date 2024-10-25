﻿$(function () {
    $.get("/api/v1/user/total-count", function (data) {
        $("#totalUsers").text(get(() => data, '0'));
    });

    $.get("/api/v1/user/total-online", function (data) {
        $("#totalOnline").text(get(() => data, 0));
    });

    $.getJSON("/api/v1/wallet/total-deposit/" + currentUser.id, function (data) {
        $("#totalDeposit").text(get(() => data[0].totalAmount, 0));
        $(".currency").text(get(() => data[0].currency, 'USDT'));
    });

    $.getJSON("/api/v1/wallet/total-bonus/" + currentUser.id, function (data) {
        $("#totalProfit").text(get(() => data[0].totalAmount, 0));
    });

    $.getJSON("/api/v1/wallet/total-profit/" + currentUser.id, function (data) {
        $("#totalBonus").text(get(() => data[0].totalAmount, 0));
    });

    $.getJSON("/api/v1/wallet/total-withdrawal/" + currentUser.id, function (data) {
        $("#totalWithdrawal").text(get(() => data[0].totalAmount, 0));
    });

    $.get("/api/v1/arbitrage/count-all-by-user/" + currentUser.id, function (data) {
        $("#totalArbitrage").text(data);
    });
    $.getJSON("/api/v1/user?page=0&size=2&sort=childCount,desc", function (data) {
        data.content.forEach(function (value) {
            const topReferralElement =
                `<div class="widget_summary">
                        <div class="w_left w_25">
                            <span>${value.firstName + ' ' + value.lastName}</span>
                        </div>
                        <div class="w_center w_55">
                            <div class="progress">
                                <div class="progress-bar bg-green" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${value.childCount}%;" data-transitiongoal="${value.childCount}">
                                    <span class="sr-only">60% Complete</span>
                                </div>
                            </div>
                        </div>
                        <div class="w_right w_20">
                            <span>${value.childCount == 0 ? 3 : value.childCount}k</span>
                        </div>
                        <div class="clearfix"></div>
                    </div>`;
            $("#top-referrals").append(topReferralElement);
        });
    });
    $.getJSON("/api/v1/user/count-by-country", function (data) {
        data.forEach(function (value) {
            const countryElement = `<tr>
                                        <td>${value.country}</td>
                                        <td class="fs15 fw700 text-right">${value.percent}%</td>
                                    </tr>`;
            $(".countries_list").append(countryElement);
        });
    });

    //chart
    var chart_01_settings = {
        series: {
            lines: {
                show: false,
                fill: true
            },
            splines: {
                show: true,
                tension: 0.4,
                lineWidth: 1,
                fill: 0.4
            },
            points: {
                radius: 0,
                show: true
            },
            shadowSize: 2
        },
        grid: {
            verticalLines: true,
            hoverable: true,
            clickable: true,
            tickColor: "#d5d5d5",
            borderWidth: 1,
            color: '#fff'
        },
        colors: ["rgba(38, 185, 154, 0.38)", "rgba(3, 88, 106, 0.38)"],
        xaxis: {
            tickColor: "rgba(51, 51, 51, 0.06)",
            mode: "time",
            tickSize: [1, "day"],
            //tickLength: 10,
            axisLabel: "Date",
            axisLabelUseCanvas: true,
            axisLabelFontSizePixels: 12,
            axisLabelFontFamily: 'Verdana, Arial',
            axisLabelPadding: 10
        },
        yaxis: {
            ticks: 8,
            tickColor: "rgba(51, 51, 51, 0.06)",
        },
        tooltip: false
    };

    if ($("#chart_01").length) {
        $.getJSON("/api/v1/wallet/get-date-range/" + new Date(2024,6,1).getTime() +"/"+ new Date(2024,6,30).getTime()+"/DEPOSIT", function (data) {
            const depositArray = Object.entries(data).map(([key, value]) => [key, value]);
            $.getJSON("/api/v1/wallet/get-date-range/" + new Date(2024,6,1).getTime() +"/"+ new Date(2024,6,30).getTime()+"/BONUS", function (data) {
                const bonusArray = Object.entries(data).map(([key, value]) => [key, value]);
                $.plot($("#chart_01"), [depositArray, bonusArray], chart_01_settings);
            });
        });
    }
});