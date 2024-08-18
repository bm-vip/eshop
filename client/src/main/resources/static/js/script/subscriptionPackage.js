rules = {
    name: "required",
    orderCount: "required",
    price: {
        required: true,
        currency: ["$", false]
    },
    maxPrice: {
        required: true,
        currency: ["$", false]
    },
    currency: "required",
    status: "required",
    minTradingReward: "required",
    maxTradingReward: "required",
    parentReferralBonus: "required",
    selfReferralBonus: "required"
};

messages = {
    name: resources.pleaseEnter.format(resources.name),
    orderCount: resources.pleaseEnter.format(resources.orderCount),
    price: {
        required: resources.pleaseEnter.format(resources.price),
        currency: resources.invalidFormat.format(resources.price)
    },
    maxPrice: {
        required: resources.pleaseEnter.format('{0} {1}'.format(resources.minimum, resources.price)),
        currency: resources.invalidFormat.format('{0} {1}'.format(resources.minimum, resources.price))
    },
    currency: resources.pleaseEnter.format(resources.currency),
    status: resources.pleaseSelect.format(resources.status),
    minTradingReward: resources.pleaseEnter.format('{0} {1} {2}'.format(resources.minimum, resources.awards, resources.trade)),
    maxTradingReward: resources.pleaseEnter.format('{0} {1} {2}'.format(resources.maximum, resources.awards, resources.trade)),
    parentReferralBonus: resources.pleaseEnter.format(resources.parentReferralBonus),
    selfReferralBonus: resources.pleaseEnter.format(resources.selfReferralBonus)
};

function loadEntityByInput() {
    let model = {
        id: isNullOrEmpty($("#hdf_id").val()) ? null : $("#hdf_id").val(),
        price: $("#price").val().replace(/,/g, ""),
        maxPrice: $("#maxPrice").val().replace(/,/g, ""),
        currency: isNullOrEmpty($("#currency").val())? null : $("#currency").val(),
        status: isNullOrEmpty($("#status").val()) ? null : $("#status").val(),
        name: $("#name").val(),
        minTradingReward: $("#minTradingReward").val(),
        maxTradingReward: $("#msxReward").val(),
        selfReferralBonus: $("#selfReferralBonus").val(),
        parentReferralBonus: $("#parentReferralBonus").val(),
    };
    return model;
}

columns = [{
    data: 'name'
},{
    data: 'duration'
}, {
    data: 'orderCount'
}, {
    data: 'price',
    render: function (data) { return addPeriod(data) }
}, {
    data: 'maxPrice',
    render: function (data) { return addPeriod(data) }
}, {
    data: 'currency'
}, {
    data: 'status'
}, {
    data: 'minTradingReward'
},{
    data: 'maxTradingReward'
}, {
    data: 'selfReferralBonus'
}, {
    data: 'parentReferralBonus'
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
    $("#price").val(addPeriod(model.price));
    $("#maxPrice").val(addPeriod(model.maxPrice));
    $('#currency').val(model.currency);
    $('#name').val(model.name);
    $('#duration').val(model.duration);
    $("#orderCount").val(model.orderCount);
    $("#status").val(model.status);
    $("#minTradingReward").val(model.minTradingReward);
    $("#maxTradingReward").val(model.maxTradingReward);
    $("#description").val(model.description);
    $("#selfReferralBonus").val(model.selfReferralBonus);
    $("#parentReferralBonus").val(model.parentReferralBonus);

}
function onLoad() {
    $("#price, #maxPrice").on('input',function () {
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
    $.getJSON("/api/v1/common/getEnum/EntityStatusType", function (json) {
        if (json.error == null) {
            json.forEach(function (value, index) {
                $("#status").append("<option value='" + value.id + "'>" + value.text + "</option>");
            });
        } else {
            show_error(json.error);
        }
    });
}