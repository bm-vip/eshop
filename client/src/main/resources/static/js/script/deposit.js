ajaxUrl = "/api/v1/wallet";
rules = {
    walletAddress: {
        required: true,
        validTronWallet: true
    },
    transactionHash: {
        required: true,
        validTronHash: true
    },
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
    transactionHash: {
        required: resources.pleaseEnter.format(resources.transactionHash),
        validTronHash: resources.invalidFormat.format(resources.transactionHash)
    },
    amount: {
        required: resources.pleaseEnter.format(resources.amount),
        currency: resources.invalidFormat.format(resources.amount)
    },
};

columns = [{
    data: 'amount',
    render: function (data) { return addPeriod(data) }
},{
    data: 'currency'
},{
    data: 'transactionType'
},{
    data: 'transactionHash'
},{
    data: 'address'
},{
    data: 'active',
    render: function (data) { return data ? `<span class='grteen'>${resources.active}</span>` :  `<span class='red'>${resources.inactive}</span>`}
},{
    data: 'modifiedDate',
    render: function (data) { return toLocalizingDateString(data, true) }
}];

function onLoad() {
    $.getJSON("/api/v1/parameter/find-by-code/WALLET_ADDRESS", function (parameter) {
        $("#walletAddress").val(parameter.value);
        $("#walletAddress-title").html(parameter.title);
        initQrCode(parameter.value);
    });
}
function initQrCode(value) {
    QRCode.toCanvas(value, { width: 200, margin: 2 }, function(error, canvas) {
        if (error) {
            console.error(error);
        } else {
            $("#qrcode").html(canvas);
        }
    });
}

function loadSaveEntityByInput() {
    let model = {
        amount: $("#amount").val().replace(/,/g, ""),
        currency: 'USDT',
        transactionType: 'DEPOSIT',
        user: {id: currentUser.id},
        transactionHash: $("#transactionHash").val(),
        address: $("#walletAddress").val()
    };
    return model;
}
function loadSearchEntityByInput() {
    let model = {
        transactionTypes: ['DEPOSIT','BONUS','REWARD'],
        userId: currentUser.id
    };
    return model;
}
function clearAll_(){
    $("#amount,#transactionHash").val('');
}
function afterSubmitForm(entity) {
    $("#deposit-alert").remove();
    let infoElement = `<div id="deposit-alert" class="alert alert-info">
    <button onclick="$('#deposit-alert').hide()" class="close">&times;</button>
    <div class="alert-content"><i class="fa fa-info-circle" style="font-size:20px"></i>&nbsp;${resources.depositAlert}</div>
</div>`;
    $(".btn-primary").after(infoElement);
}