$(function () {
    //
    // $.getJSON('/common/chart/', function (data) {
    //     if (data.error == null) {
    //         var e = document.getElementById("sentProgressBarChart");
    //         new Chart(e, {
    //             type: "bar",
    //             data: {
    //                 labels: data.data.labels,
    //                 datasets: [
    //                     {label: "درصد ارسال شده", backgroundColor: "#26B99A", data: data.data.send},
    //                     {label: "درصد ارسال نشده", backgroundColor: "#990000", data: data.data.notSend}
    //                 ]
    //             },
    //             options: {scales: {yAxes: [{ticks: {beginAtZero: !0}}]}}
    //         });
    //     } else {
    //         show_error(data.error);
    //     }
    // })

    $.getJSON("/api/v1/company/countAll", jsonToUrlSearchParams({}), function (data) {
        $("#totalCompanies").text(data);
    });

    $.getJSON("/api/v1/station/countAll", jsonToUrlSearchParams({}), function (data) {
        $("#totalStations").text(data);
    });

    $.getJSON("/api/v1/user/countAll", jsonToUrlSearchParams({}), function (data) {
        $("#totalUsers").text(data);
    });


    $.getJSON("api/v1/role/countAll", {filter: JSON.stringify({})}, function (data) {
        $("#totalRoles").text(data);
    });
});