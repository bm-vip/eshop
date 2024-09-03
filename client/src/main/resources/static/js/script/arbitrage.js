$(function () {
    $.getJSON("/api/v1/subscription-package?size=100", function (data) {
        data.content.forEach(function (value) {
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
                                    <li><i class="fa fa-check text-success"></i> <strong>Unlimited access</strong> to this item.</li>
                                    <li><i class="fa fa-check text-success"></i> Order count <strong> ${value.orderCount}</strong> times</li>
                                    <li><i class="fa fa-check text-success"></i> Trading reward (<strong>${value.minTradingReward} - ${value.maxTradingReward}</strong>) ${value.currency}</li>
                                    <li><i class="fa fa-check text-success"></i> Self referral bonus <strong>${value.selfReferralBonus}</strong> ${value.currency}</li>
                                    <li><i class="fa fa-check text-success"></i> Parent referral bonus <strong>${value.parentReferralBonus}</strong> ${value.currency}</li>
                                </ul>
                            </div>
                        </div>
                        <div class="pricing_footer">
                            <a href="javascript:void(0);" class="btn btn-success btn-block" role="button">Trade <span>now!</span></a>                       
                        </div>
                    </div>
                </div>
            </div>`;
            // Append the price element to the subscription package container
            $("#subscription-package").append(priceElement);
        });
    });
});