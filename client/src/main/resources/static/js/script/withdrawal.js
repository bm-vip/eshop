ajaxUrl = "/api/v1/wallet";
var businessRules;
rules = {
    walletAddress: {
        required: true,
        validTronWallet: true
    },
    currency: "required",
    amount: {
        required: true,
        currency: ["$", false]
    }
};
messages = {
    walletAddress: {
        required: resources.pleaseEnter.format(resources.amount),
        validTronWallet: resources.invalidFormat.format(resources.wallet)
    },
    currency:  resources.pleaseEnter.format(resources.currency),
    amount: {
        required: resources.pleaseEnter.format(resources.amount),
        currency: resources.invalidFormat.format(resources.amount)
    },
};

columns = [{
    data: 'network'
},{
    data: 'amount',
    render: function (data) { return addPeriod(data) }
},{
    data: 'transactionType'
},{
    data: 'status',
    render: function (data) { return `<span class="${data == 'Active' ? 'green':'red'}">${data}</span>`}
}, {
    data: 'modifiedDate',
    render: function (data) { return toLocalizingDateString(data, true) }
}];

function customTableOptions(){
    let tbl_option = tableOptions();
    tbl_option.order = [[4, 'desc']];
    return tbl_option;
}

function loadAllowedWithdrawalInformation(withdrawalType) {
    $.get(`api/v1/wallet/allowed-withdrawal-balance/${currentUser.id}/${withdrawalType}`, function (allowedAmount){
        let minWithdraw = businessRules.find(x => x.code == 'MIN_WITHDRAW').value ?? '15';
        let transferFee = businessRules.find(x => x.code == 'TRANSFER_FEE').value ?? '2';
        $("#amount").attr('placeholder',`${minWithdraw} ~ ${allowedAmount}`);
        $("#withdraw-notice .alert-content ul").html(`<li>${resources.minWithdrawalNotice.format(minWithdraw)}</li>
                                                      <li>${resources.maxWithdrawalNotice.format(allowedAmount)}</li>
                                                      <li>${resources.transferFee.format(transferFee)}</li>`);
    });
}

function onLoad() {
    $.getJSON("/api/v1/user/" + currentUser.id, function (user) {
        $("#walletAddress").val(user.walletAddress);
    });
    $.getJSON("/api/v1/parameter/find-by-group-code/BUSINESS_RULES", function (data) {
        businessRules = data;
        validate($("#withdrawalType").val());
    });
    $("#withdrawalType").on('change', function (){
        validate($("#withdrawalType").val());
    });
}
function validate(withdrawalType){
    loadAllowedWithdrawalInformation(withdrawalType);
    if(withdrawalType == 'WITHDRAWAL_PROFIT') {
        $.getJSON("/api/v1/wallet/total-profit/" + currentUser.id, function (totalProfit) {
            if (parseFloat(totalProfit) < parseFloat(businessRules.find(x => x.code == 'MIN_WITHDRAW').value)) {
                $("#saveWithdraw").addClass("disabled").attr("aria-disabled", true).attr('disabled',true);
                show_warning(`Profit balance ${totalProfit} is insufficient for withdrawal!`);
            } else {
                $("#saveWithdraw").removeClass("disabled").removeAttr("aria-disabled").removeAttr('disabled');
            }
        });
    } else if(withdrawalType == 'WITHDRAWAL') {
        $.get("/api/v1/subscription/find-active-by-user/" + currentUser.id, function (subscription) {
            if(isNullOrEmpty(subscription)) {
                $("#saveWithdraw").addClass("disabled").attr("aria-disabled", true).attr('disabled',true);
                show_warning(`Insufficient fund to withdraw!`);
            } else if (subscription.remainingWithdrawalPerDay > 0) {
                $("#saveWithdraw").addClass("disabled").attr("aria-disabled", true).attr('disabled',true);
                show_warning(`You can withdraw your funds after ${subscription.remainingWithdrawalPerDay} days!`);
            } else {
                $("#saveWithdraw").removeClass("disabled").removeAttr("aria-disabled").removeAttr('disabled');
            }
        });
    }
}
function loadSaveEntityByInput() {
    let model = {
        amount: $("#amount").val().replace(/,/g, ""),
        network: $("#network").val(),
        currency: 'USDT',
        transactionType: $("#withdrawalType").val(),
        user: {id: currentUser.id},
        address: $("#walletAddress").val()
    };
    return model;
}
function loadSearchEntityByInput() {
    let model = {
        transactionTypes: ['WITHDRAWAL','WITHDRAWAL_PROFIT'],
        userId: currentUser.id
    };
    return model;
}
function clearAll_(){
    $("#amount","#walletAddress").val('');
}
function afterSubmitForm(entity) {
    currentUser.walletAddress = $("#walletAddress").val();
    $.blockUI(blockUiOptions());
    $(".btn-primary").attr("disabled", 'disabled');
    $.ajax({
        type: "PATCH",
        url: 'api/v1/user/' + currentUser.id,
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(currentUser),
        success: function (data) {
            $.unblockUI();
            $(".btn-primary").removeAttr("disabled");
            if (data.error == null) {
                clearAll();
                show_success(resources.saveSuccess);
            } else {
                show_error(data.error);
            }
        },
        error: function (header, status, error) {
            $.unblockUI();
            $(".btn-primary").removeAttr("disabled");
            if(isNullOrEmpty(get(() => header.responseJSON)))
                show_error('ajax answer post returned error: ' + error.responseText);
            else show_error(header.responseJSON.error + ' (' + header.responseJSON.status + ') <br>' + header.responseJSON.message);
        }
    });
    $("#withdraw-alert").remove();
    let infoElement = `<div id="withdraw-alert" class="alert alert-info">
    <button onclick="$('#withdraw-alert').hide()" class="close">&times;</button>
    <div class="alert-content">${resources.withdrawalAlert}</div>
    </div>`;
    $(".btn-primary").after(infoElement);
}