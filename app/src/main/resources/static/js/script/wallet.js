rules = {
    userSelect2: "required",
    currency: "required",
    amount: {
        required: true,
        currency: ["$", false]
    },
    transactionType: "required",
    transactionHash: "required",
    status: "required",
    address: "required"
};

messages = {
    userSelect2: resources.pleaseSelect.format(resources.user),
    currency: resources.pleaseEnter.format(resources.currency),
    amount: {
        required: resources.pleaseEnter.format(resources.amount),
        currency: resources.invalidFormat.format(resources.amount)
    },
    transactionType: resources.pleaseEnter.format(resources.transactionType),
    transactionHash: resources.pleaseEnter.format(resources.transactionHash),
    status: resources.pleaseEnter.format(resources.status),
    address: resources.pleaseEnter.format(resources.address)
};

function loadEntityByInput() {
    let model = {
        id: isNullOrEmpty($("#hdf_id").val()) ? null : $("#hdf_id").val(),
        amount: $("#amount").val().replace(/,/g, ""),
        currency: isNullOrEmpty($("#currency").val())? null : $("#currency").val(),
        transactionType: isNullOrEmpty($("#transactionType").val()) ? null : $("#transactionType").val(),
        user: isNullOrEmpty($("#userSelect2").val())? null : {id: $("#userSelect2").val()},
        userId: $("#userSelect2").val(),
        transactionHash: $("#transactionHash").val(),
        address: $("#address").val(),
        active: isNullOrEmpty($("#status").val())? null : $("#status").val(),
    };
    return model;
}
function loadSearchEntityByInput() {
    let filter = {
        amount: $("#amount").val().replace(/,/g, ""),
        currency: isNullOrEmpty($("#currency").val())? null : $("#currency").val(),
        transactionType: isNullOrEmpty($("#transactionType").val()) ? null : $("#transactionType").val(),
        user: isNullOrEmpty($("#userSelect2").val())? null : {id: $("#userSelect2").val()},
        userId: $("#userSelect2").val(),
        transactionHash: $("#transactionHash").val(),
        address: $("#address").val(),
        active: isNullOrEmpty($("#status").val())? null : $("#status").val(),
    };
    return filter;
}

columns = [{
    data: 'amount',
    render: function (data) { return addPeriod(data) }
}, {
    data: 'currency'
}, {
    data: 'transactionType'
},{
    data: 'transactionHash'
},{
    data: 'address'
}, {
    data: 'user',
    render: function (data) { return "{0} {1}".format(data.firstName, data.lastName) }
},{
    data: 'active',
    render: function (data) { return data ? `<span class='grteen'>${resources.active}</span>` :  `<span class='red'>${resources.inactive}</span>`}
}, {
    data: 'modifiedDate',
    render: function (data) { return toLocalizingDateString(data, true) }
}, {
    data: 'id',
    searchable: false,
    sortable: false,
    render: function (data) { return "<a class='btn btn-default fa fa-pencil' id='" + data + "'></a> <a class='btn btn-danger fa fa-trash' id='" + data + "'></a>" }
}];

function loadInputByEntity(model) {
    $("#hdf_id").val(model.id);
    $("#userSelect2").html("<option value='" + get(() => model.user.id) + "' selected>" + get(() => "{0} {1}".format(model.user.firstName, model.user.lastName)) + "</option>").trigger('change');
    $("#userSelect2").val(get(() => model.user.id)).trigger('change');
    $("#amount").val(addPeriod(model.amount));
    $('#currency').val(model.currency);
    $("#transactionType").val(model.transactionType);
    $("#transactionHash").val(model.transactionHash);
    $("#address").val(model.address);
    $("#status").val(model.active+"");

}
function onLoad() {
    $("#amount").on('input',function () {
        $(this).val(addPeriod($(this).val().replace(/,/g, "")));
    });
    $.getJSON("/api/v1/common/getEnum/CurrencyType", function (json) {
        if (json.error == null) {
            json.forEach(function (value, index) {
                $("#currency").append("<option value='" + value.id + "'>" + value.text + "</option>");
            });
        } else {
            show_error(json.error);
        }
    });
    $.getJSON("/api/v1/common/getEnum/TransactionType", function (json) {
        if (json.error == null) {
            json.forEach(function (value, index) {
                $("#transactionType").append("<option value='" + value.id + "'>" + value.text + "</option>");
            });
        } else {
            show_error(json.error);
        }
    });
    $('#userSelect2').on('select2:select', function(e) {
        let userId = e.params.data.id;
        $.getJSON("/api/v1/user/"+ userId, function (json) {
            $("#address").val(json.walletAddress);
        });
    });
}