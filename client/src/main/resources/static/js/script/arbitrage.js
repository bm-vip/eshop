let SERVER_ADDRESS; (async () => SERVER_ADDRESS = (await (await fetch("/api/v1/parameter/find-by-code/SERVER_ADDRESS")).json()).value)();

$(function () {
    //create subscription packages
    $.getJSON("/api/v1/subscription-package?size=100", function (subscriptionPackages) {
        $.get("/api/v1/subscription/find-active-by-user/" + currentUser.id, function (subscription) {
            subscriptionPackages.content.forEach(function (value) {
                const active = get(()=>value.id,0) == get(()=>subscription.subscriptionPackage.id,0);
                // Create the entire price element
                const priceElement = `
                <div class="col-md-3 col-sm-6">
                    <div class="pricing">
                        <div class="title">
                            <h2>${value.name}</h2>
                            <h1>${value.price} ${value.currency}</h1>
                        </div>
                        <div class="x_content">
                            <div class="">
                                <div class="pricing_features">
                                    <ul class="list-unstyled text-left">
                                        <li><i class="fa fa-check text-success"></i> <strong>Unlimited access</strong>.</li>
                                        <li><i class="fa fa-check text-success"></i> Order count <strong> ${value.orderCount}</strong> times</li>
                                        <li><i class="fa fa-check text-success"></i> Trading reward (<strong>${value.minTradingReward} - ${value.maxTradingReward}</strong>) ${value.currency}</li>
                                        <li><i class="fa fa-check text-success"></i> Self referral bonus <strong>${value.selfReferralBonus}</strong> ${value.currency}</li>
                                        <li><i class="fa fa-check text-success"></i> Parent referral bonus <strong>${value.parentReferralBonus}</strong> ${value.currency}</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="pricing_footer">
                                <a href="javascript:trade();" class="btn btn-success btn-block ${!active ? 'disabled' : ''}" aria-disabled="${!active}" role="button">Trade now!</a> 
                                <a href="javascript:loadPages('deposit?amount=${value.price}');" class="btn btn-primary btn-block ${active ? 'disabled' : ''}" aria-disabled="${active}" role="button">Purchase<span></span></a>                          
                            </div>
                        </div>
                    </div>
                </div>`;
                // Append the price element to the subscription package container
                $("#subscription-package").append(priceElement);
            });
        });
    });

});
async function trade() {
    $("#trading-content").show();
    goToByScroll("#trading-content");
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

                        <h3 class="name_title"><img width="40px" src="${SERVER_ADDRESS + coin.logo}"/> ${coin.name}</h3>
                        <p>From exchange <img width="20px" src="${SERVER_ADDRESS + exchanges[0].logo}"/> ${exchanges[0].name} to <img width="20px" src="${SERVER_ADDRESS + exchanges[1].logo}"/> ${exchanges[1].name}</p>

                        <div class="divider"></div>
                        <a href="javascript:initPieChart('#order-${i + 1}');" class="btn btn-primary btn-block" role="button">Buy</a> 
                    </div>
                </div>
            </div>`;
            // Append the order element to the trading container
            $("#trading-order").append(orderElement);
        }
    }
}
function initPieChart(id){
    $(id).easyPieChart({
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
        animate: {
            duration: 5000, // 5 seconds animation duration
            enabled: true
        }
    });
}