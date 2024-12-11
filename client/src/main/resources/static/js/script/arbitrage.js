$(function () {
    if (parseInt($("#selectedSubscriptionPackageId").val()) > 0) {
        trade($("#selectedSubscriptionPackageId").val(), function () {
            $("#subscription-package-content .collapse-link").click();
            $("#trading-content").show();
        });
    }
});
async function trade(id, callback) {
    let purchaseLimit = $("#purchaseLimit").val();
    if(!isNullOrEmpty(purchaseLimit)) {
        show_warning(`You have reached the purchase limitation, please try ${purchaseLimit}`);
        $(`#subscription-package-item-${id} .btn-success`).text('Trade ' + purchaseLimit);
        return;
    } else {
        $(`#subscription-package-item-${id} .btn-success`).text(resources.tradeNow);
    }
    if(parseInt($("#selectedSubscriptionPackageId").val()) == 0) {
        $("#subscription-package-content .collapse-link").click();
        $("#trading-content").show();
        goToByScroll("#trading-content");
    }
    //create trading orders
    let subscription = await (await fetch("/api/v1/subscription/find-active-by-user/" + currentUser.id)).json();
    const orderCount = get(() => subscription.subscriptionPackage.orderCount, 0);
    if (orderCount > 0) {
        $("#trading-order").empty();
        for (let i = 0; i < orderCount; i++) {
            let coin = await (await fetch('api/v1/coin/buy/' + currentUser.id)).json();
            let exchanges = await (await fetch('api/v1/exchange/buy/' + currentUser.id)).json();
            const orderElement = `              
            <div class="col-md-3 widget widget_tally_box">
                <div class="x_panel">
                    <div class="x_title">
                        <h2>Order ${i + 1}</h2>
                        <div class="clearfix"></div>
                    </div>
                    <div class="x_content">

                        <div style="text-align: center; margin-bottom: 17px">
                         <span id="order-${i + 1}" class="chart" data-percent="100">
                              <span class="percent"></span>
                         </span>
                        </div>

                        <h3 class="name_title"><img width="40px" src="${coin.logo}"/> ${coin.name}</h3>
                        <p>From exchange <img width="20px" src="${exchanges[0].logo}"/> ${exchanges[0].name} to <img width="20px" src="${exchanges[1].logo}"/> ${exchanges[1].name}</p>

                        <div class="divider"></div>
                        <a id="buy-btn-${i + 1}" href="javascript:buy(${i + 1}, ${exchanges[0].id}, ${coin.id}, ${subscription.id});" class="btn btn-primary btn-block" role="button">Buy/Sell</a> 
                    </div>
                </div>
            </div>`;
            // Append the order element to the trading container
            $("#trading-order").append(orderElement);
        }
        if (typeof callback === 'function') {
            callback();
        }
    }
}
function buy(index, exchangeId, coinId, subscriptionId) {
    $("#buy-btn-" + index).addClass("disabled").attr("aria-disabled", "true");
    $("#order-" + index).easyPieChart({
        easing: 'easeOutElastic',
        delay: 3000,
        barColor: '#26B99A',
        trackColor: '#fff',
        scaleColor: false,
        lineWidth: 20,
        trackWidth: 16,
        lineCap: 'butt',
        onStep: function (from, to, percent) {
            $(this.el).find('.percent').text(Math.round(percent));
        },
        onStop:function (from, to) {
            console.log('Animation complete. Final percentage: ' + to);
            $.postJSON("/api/v1/arbitrage",{user:{id:currentUser.id}, exchange:{id:exchangeId}, coin:{id: coinId}, subscription:{id:subscriptionId}}, function (data) {
                $("#buy-btn-" + index).after(`<p>You received ${data.reward} profit from this purchase at ${new Date(data.createdDate).toLocaleString()}</p>`);
                $("#buy-btn-" + index).remove();
            },function (header, status, error) {
                if(isNullOrEmpty(get(() => header.responseJSON)))
                    show_error('ajax answer post returned error: ' + error.responseText);
                else if(header.responseJSON.status >= 400 && header.responseJSON.status < 500) show_warning(header.responseJSON.error + ' (' + header.responseJSON.status + ') <br>' + header.responseJSON.message);
                else show_error(header.responseJSON.error + ' (' + header.responseJSON.status + ') <br>' + header.responseJSON.message);
                $("#buy-btn-" + index).after(`<p class="red">${header.responseJSON.message}</p>`);
                $("#buy-btn-" + index).remove();
            });
        },
        animate: {
            duration: (Math.floor(Math.random() * (60 - 20 + 1)) + 20) * 1000, // random number 20000-60000 seconds animation duration
            enabled: true
        }
    });
}