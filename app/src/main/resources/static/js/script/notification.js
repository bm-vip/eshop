rules = {
    senderSelect2: "required",
    recipientSelect2: "required",
    subject: "required",
    body: "required"
};

messages = {
    senderSelect2: resources.pleaseSelect.format(resources.sender),
    recipientSelect2: resources.pleaseSelect.format(resources.recipient),
    subject: resources.pleaseEnter.format(resources.subject),
    body: resources.pleaseEnter.format(resources.body)
};

function loadEntityByInput() {
    let model = {
        id: isNullOrEmpty($("#hdf_id").val()) ? null : $("#hdf_id").val(),
        sender: isNullOrEmpty($("#senderSelect2").val())? null : {id: $("#senderSelect2").val()},
        recipient: isNullOrEmpty($("#recipientSelect2").val())? null : {id: $("#recipientSelect2").val()},
        subject: $("#subject").val(),
        body: $("#body").val()
    };
    return model;
}

columns = [{
    data: 'sender',
    render: function (data) { return "{0} {1}".format(data.firstName, data.lastName) }
},{
    data: 'recipient',
    render: function (data) { return "{0} {1}".format(data.firstName, data.lastName) }
},{
    data: 'subject'
}, {
    data: 'read'
},{
    data: 'modifiedDate',
    render: function (data) { return toLocalizingDateString(data, true) }
}, {
    data: 'id',
    searchable: false,
    sortable: false,
    render: function (data, type, row) { return `<a class="btn btn-default fa fa-pencil" id="${data}" data-bs-toggle="tooltip" title="Edit"></a> <a class="btn btn-danger fa fa-trash" id="${data}" data-bs-toggle="tooltip" title="Delete"></a> <a class="btn btn-info fa fa-list-alt" data-toggle="modal" data-target=".bs-example-modal-lg" id="${data}" modal-title="${row.subject}" data-bs-toggle="tooltip" title="${resources.body}"></a>` }
}];

function loadInputByEntity(model) {
    $("#hdf_id").val(model.id);
    $("#senderSelect2").html("<option value='" + get(() => model.sender.id) + "' selected>" + get(() => "{0} {1}".format(model.sender.firstName, model.sender.lastName)) + "</option>").trigger('change');
    $("#senderSelect2").val(get(() => model.sender.id)).trigger('change');
    $("#recipientSelect2").html("<option value='" + get(() => model.recipient.id) + "' selected>" + get(() => "{0} {1}".format(model.recipient.firstName, model.recipient.lastName)) + "</option>").trigger('change');
    $("#recipientSelect2").val(get(() => model.recipient.id)).trigger('change');
    $("#subject").val(model.subject);
    $("#body").val(model.body);
}
function onLoad() {
    $(".table:not(#myModal .table)").on("click", ".fa-list-alt", function (e) {
        initModal($(this))
    });
}
function initModal(element) {
    $("#myModal .modal-header #myModalLabel small").text(element.attr('modal-title'));
    $.get(`${ajaxUrl}/${element.attr('id')}`,function (data){
        $("#myModal .modal-body").html(data.body);
    });
}