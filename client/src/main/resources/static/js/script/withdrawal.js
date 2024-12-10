ajaxUrl = "/api/v1/wallet";
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
    data: 'amount',
    render: function (data) { return addPeriod(data) }
}, {
    data: 'currency'
},{
    data: 'transactionType'
}, {
    data: 'transactionHash'
},{
    data: 'address'
},{
    data: 'active',
    render: function (data) { return data ? `<span class='grteen'>${resources.active}</span>` :  `<span class='red'>${resources.inactive}</span>`}
}, {
    data: 'modifiedDate',
    render: function (data) { return toLocalizingDateString(data, true) }
}];

function customTableOption(){
    let tbl_option = tableOptions();
    tbl_option.order = [[5, 'asc']];
    return tbl_option;
}

function onLoad() {
    $.getJSON("/api/v1/user/" + currentUser.id, function (user) {
        $("#walletAddress").val(user.walletAddress);
    });
    $.getJSON("/api/v1/parameter/find-by-group-code/BUSINESS_RULES", function (data) {
        $("#withdraw-notice .alert-content").append(`<li>${resources.withdrawalNotice.format(data.find(x => x.code == 'MIN_WITHDRAW').value ?? '15')}</li>`);
        $("#withdraw-notice .alert-content").append(`<li>${resources.transferFee.format(data.find(x => x.code == 'TRANSFER_FEE').value ?? '2')}</li>`);
    });
}

function loadSaveEntityByInput() {
    let model = {
        amount: $("#amount").val().replace(/,/g, ""),
        currency: 'USDT',
        transactionType: 'WITHDRAWAL',
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
    $("#amount").val('');
}
function afterSubmitForm(entity) {
    currentUser.walletAddress = $("#walletAddress").val();
    $.blockUI(blockUiOptions());
    $(".btn-primary").attr("disabled", 'disabled');
    $.ajax({
        type: "PATCH",
        url: 'api/v1/user',
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