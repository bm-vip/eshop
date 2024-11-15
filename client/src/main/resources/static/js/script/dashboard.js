$(function () {
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
    $.get("api/v1/subscription/find-active-by-user/" + currentUser.id, function (data) {
        if(isNullOrEmpty(data)) {
            $(".reverse_timer").remove();
            $("#totalDeposit").after(`<span class="currency">USDT</span>`);
        } else $("#totalDepositReverseTimer").val(JSON.parse(data).remainingWithdrawalPerDay);
    });
    $.getJSON("/api/v1/wallet/total-bonus/" + currentUser.id, function (data) {
        $("#referralBonus").text(get(() => data[0].totalAmount, 0));
    });

    $.getJSON("/api/v1/wallet/total-profit/" + currentUser.id, function (data) {
        $("#totalProfit").text(get(() => data[0].totalAmount, 0));
    });

    $.getJSON("/api/v1/wallet/total-withdrawal/" + currentUser.id, function (data) {
        $("#totalWithdrawal").text(get(() => data[0].totalAmount, 0));
    });

    $.get("/api/v1/arbitrage/count-all-by-user/" + currentUser.id, function (data) {
        $("#totalArbitrage").text(data);
    });

    $.get("/api/v1/arbitrage/find-top-coins/5", function (data) {
        data.content.forEach(function (coin, i) {
            $("#coinName-" + (i+1)).text(coin.name);
            $("#coinPercentage-" + (i+1)).text(`${coin.usagePercentage}%`);
        });
        if ($('#top-coin-doughnut').length) {

            var chart_doughnut_settings = {
                type: 'doughnut',
                tooltipFillColor: "rgba(51, 51, 51, 0.55)",
                data: {
                    labels: data.content.map(x=>x.name),
                    datasets: [{
                        data: data.content.map(x=>x.usagePercentage),
                        backgroundColor: [
                            "#BDC3C7",
                            "#9B59B6",
                            "#E74C3C",
                            "#26B99A",
                            "#3498DB"
                        ],
                        hoverBackgroundColor: [
                            "#CFD4D8",
                            "#B370CF",
                            "#E95E4F",
                            "#36CAAB",
                            "#49A9EA"
                        ]
                    }]
                },
                options: {
                    legend: false,
                    responsive: false
                }
            }

            $('#top-coin-doughnut').each(function () {
                var chart_element = $(this);
                var chart_doughnut = new Chart(chart_element, chart_doughnut_settings);
            });

        }
    });
    // $.getJSON("/api/v1/user?page=0&size=2&sort=childCount,desc", function (data) {
    //     data.content.forEach(function (value) {
    //         const topReferralElement =
    //             `<div class="widget_summary">
    //                     <div class="w_left w_25">
    //                         <span>${value.firstName + ' ' + value.lastName}</span>
    //                     </div>
    //                     <div class="w_center w_55">
    //                         <div class="progress">
    //                             <div class="progress-bar bg-green" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${value.childCount}%;" data-transitiongoal="${value.childCount}">
    //                                 <span class="sr-only">60% Complete</span>
    //                             </div>
    //                         </div>
    //                     </div>
    //                     <div class="w_right w_20">
    //                         <span>${value.childCount == 0 ? 3 : value.childCount}k</span>
    //                     </div>
    //                     <div class="clearfix"></div>
    //                 </div>`;
    //         $("#top-referrals").append(topReferralElement);
    //     });
    // });
    // $.getJSON("/api/v1/user/count-by-country", function (data) {
    //     data.forEach(function (value) {
    //         const countryElement = `<tr>
    //                                     <td>${value.country}</td>
    //                                     <td class="fs15 fw700 text-right">${value.percent}%</td>
    //                                 </tr>`;
    //         $(".countries_list").append(countryElement);
    //     });
    // });

    //chart
    if ($("#chart_01").length) {
        $.getJSON("/api/v1/wallet/get-date-range/" + addDays(new Date(),-7).getTime() +"/"+ new Date().getTime() + "/DEPOSIT", function (depositData) {
            const depositArray = Object.entries(depositData).map(([key, value]) => [key, value]);
            $.getJSON("/api/v1/wallet/get-date-range/" + addDays(new Date(),-7).getTime() +"/"+ new Date().getTime() + "/WITHDRAWAL", function (withdrawalData) {
                const withdrawalArray = Object.entries(withdrawalData).map(([key, value]) => [key, value]);
                $.plot($("#chart_01"), [
                    {
                        label: "Deposit",
                        data: depositArray,
                        lines: {
                            fillColor: "rgb(253,249,4)",
                            lineWidth: 2
                        },
                        points: {
                            fillColor: "#e8e67d",
                            show: true,
                            radius: 4,
                            lineWidth: 2
                        }
                    },
                    {
                        label: "Withdrawal",
                        data: withdrawalArray,
                        lines: {
                            fillColor: "rgba(183,52,219,0.12)",
                            lineWidth: 2
                        },
                        points: {
                            fillColor: "#c38ff3",
                            show: true,
                            radius: 4,
                            lineWidth: 2
                        }
                    }], {
                    series: {
                        lines: {
                            show: true
                        },
                        points: {
                            show: true
                        },
                        curvedLines: {
                            apply: true,
                            active: true,
                            monotonicFit: true
                        }
                    },
                    colors: ["#fdf904","#c38ff3"],
                    grid: {
                        borderWidth: {
                            top: 0,
                            right: 0,
                            bottom: 1,
                            left: 1
                        },
                        borderColor: {
                            bottom: "#7F8790",
                            left: "#7F8790"
                        },
                        hoverable: true,
                        clickable: true
                    },
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
                    legend: {
                        show: true,
                        position: "ne"
                    },
                    tooltip: true,
                    tooltipOpts: {
                        content: "%s at %x: %y",
                        defaultTheme: true
                    }
                });
            });
        });
    }
    //chart
    if ($("#chart_02").length) {
        $.getJSON("/api/v1/wallet/get-date-range/" + addDays(new Date(),-7).getTime() +"/"+ new Date().getTime() + "/BONUS", function (bonusData) {
            const bonusArray = Object.entries(bonusData).map(([key, value]) => [key, value]);
            $.getJSON("/api/v1/wallet/get-date-range/" + addDays(new Date(),-7).getTime() +"/"+ new Date().getTime() + "/REWARD", function (rewardData) {
                const rewardArray = Object.entries(rewardData).map(([key, value]) => [key, value]);
                $.plot($("#chart_02"), [
                    {
                        label: "Bonus",
                        data: bonusArray,
                        lines: {
                            fillColor: "rgb(171,236,111)",
                            lineWidth: 2
                        },
                        points: {
                            fillColor: "#abec6f",
                            show: true,
                            radius: 4,
                            lineWidth: 2
                        }
                    },
                    {
                        label: "Reward",
                        data: rewardArray,
                        lines: {
                            fillColor: "rgba(52, 152, 219, 0.12)",
                            lineWidth: 2
                        },
                        points: {
                            fillColor: "#6ccbee",
                            show: true,
                            radius: 4,
                            lineWidth: 2
                        }
                    }
                ], {
                    series: {
                        lines: {
                            show: true
                        },
                        points: {
                            show: true
                        },
                        curvedLines: {
                            apply: true,
                            active: true,
                            monotonicFit: true
                        }
                    },
                    colors: ["#04f5c2", "#0794f3"],
                    grid: {
                        borderWidth: {
                            top: 0,
                            right: 0,
                            bottom: 1,
                            left: 1
                        },
                        borderColor: {
                            bottom: "#7F8790",
                            left: "#7F8790"
                        },
                        hoverable: true,
                        clickable: true
                    },
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
                    legend: {
                        show: true,
                        position: "ne"
                    },
                    tooltip: true,
                    tooltipOpts: {
                        content: "%s at %x: %y",
                        defaultTheme: true
                    }
                });
            });
        });
    }
});