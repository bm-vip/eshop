$(function () {
    $.getJSON("/api/v1/wallet/total-balance/" + currentUser.id, function (data) {
        $("#totalBalance").text(get(() => data[0].totalAmount, 0));
    });

    $.getJSON("/api/v1/wallet/total-deposit/" + currentUser.id, function (data) {
        $("#totalDeposit").text(get(() => data[0].totalAmount, 0));
    });

    $.getJSON("/api/v1/wallet/total-bonus/" + currentUser.id, function (data) {
        $("#totalBonus").text(get(() => data[0].totalAmount, 0));
    });

    $.getJSON("/api/v1/wallet/total-withdrawal/" + currentUser.id, function (data) {
        $("#totalWithdrawal").text(get(() => data[0].totalAmount, 0));
    });

    $.getJSON("/api/v1/subscription/total-arbitrage/" + currentUser.id, function (data) {
        $("#totalArbitrage").text(data);
    });
});