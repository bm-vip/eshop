rules = {
    code: "required",
    title: "required"
};

messages = {
    code: resources.pleaseEnter.format(resources.code),
    title: resources.pleaseEnter.format(resources.title)
};
function loadSaveEntityByInput() {
    var entity = {
        id: isNullOrEmpty($("#hdf_id").val()) ? null : $("#hdf_id").val(),
        code: $("#code").val(),
        title: $("#title").val()
    };
    return entity;
}
function loadSearchEntityByInput() {
    var entity = {
        id: isNullOrEmpty($("#hdf_id").val()) ? null : $("#hdf_id").val(),
        code: $("#code").val(),
        title: $("#title").val()
    };
    return entity;
}
columns = [{
    data: 'code'
}, {    
    data: 'title'
}, {
    data: 'modifiedDate',
    render: function (data) { return toLocalizingDateString(data) }
},{
    data: 'id',
    searchable: false,
    sortable: false,
    render: function (data) { return "<a class='btn btn-default fa fa-pencil' id='" + data + "'></a>" }
}];

function loadInputByEntity(entity) {
    $("#hdf_id").val(entity.id);
    $('#code').val(entity.code).attr('disabled','disabled');
    $("#title").val(entity.title);
}


function afterSubmitForm() {
    $('#code').removeAttr('disabled');
}