ajaxUrl = "/api/v1/user/";
var imageProfileUrl = '';
var myDropzone;
var referralDataTable1;
var referralDataTable2;
rules = {
    walletAddress: {
        validTronWallet: true
    }
};
messages = {
    walletAddress: {
        validTronWallet: resources.invalidFormat.format(resources.wallet)
    }
};
$(function () {
    loadLabelByEntity(currentUser.id);
});
function loadTable() {
    referralDataTable1 = initAjaxTable('#referral-table-1', [{
        data: 'profileImageUrl',
        searchable: false,
        sortable: false,
        render: function (data) { return `<img src="${isNullOrEmpty(data) ? '/images/find_user.png' : data}" style="width: 35px;"></img>` }
    },{
        data: 'firstName',
        render: function (data, type, row) { return data + " " + row.lastName }
    },{
        data: 'userName'
    },{
        data: 'email'
    },{
        data: 'uid'
    },{
        data: 'country.name'
    },{
        data: 'modifiedDate',
        render: function (data) { return toLocalizingDateString(data, true) }
    }], "/api/v1/user", "loadSearchReferralEntity1ByInput");
    $.subscribe('reloadReferral1Table', referralDataTable1.ajax.reload);

    referralDataTable2 = initAjaxTable('#referral-table-2', [{
        data: 'profileImageUrl',
        searchable: false,
        sortable: false,
        render: function (data) { return `<img src="${isNullOrEmpty(data) ? '/images/find_user.png' : data}" style="width: 35px;"></img>` }
    },{
        data: 'firstName',
        render: function (data, type, row) { return data + " " + row.lastName }
    },{
        data: 'userName'
    },{
        data: 'email'
    },{
        data: 'uid'
    },{
        data: 'country.name'
    },{
        data: 'modifiedDate',
        render: function (data) { return toLocalizingDateString(data, true) }
    }], "/api/v1/user", "loadSearchReferralEntity2ByInput");
    $.subscribe('reloadReferral2Table', referralDataTable2.ajax.reload);
}
function loadInputByEntity(id) {
    $.getJSON(ajaxUrl + id,function(entity) {
        $("#countrySelect2").html("<option value='" + get(() => entity.country.id) + "' selected>" + get(() => entity.country.name) + "</option>").trigger('change');
        $("#countrySelect2").val(get(() => entity.country.id)).trigger('change');
        $("#email").val(entity.email);
        $("#walletAddress").val(entity.walletAddress);
    });
}
function loadLabelByEntity(id) {
    $.getJSON(ajaxUrl + id, function(entity) {
        if(!isNullOrEmpty(entity.profileImageUrl))
            $("#profile-image-url").attr('src',entity.profileImageUrl);

        $("#full-name").text(`${entity.selectTitle}`);
        $("#country-label").html(`<strong><i class="fa fa-map-marker"></i> Country:</strong> ${entity.country.name}`);
        $("#email-label").html(`<strong><i class="fa fa-envelope"></i> Email:</strong> ${entity.email}`);
        $("#user-name").html(`<strong><i class="fa fa-id-card"></i> UserName:</strong> ${entity.userName}`);
        $("#wallet-label").html(`${isNullOrEmpty(entity.walletAddress)? '<strong><i class="fa fa-wallet"></i> Wallet:</strong> ---': '<strong><i class="fa fa-wallet"></i> Wallet:</strong> ' + entity.walletAddress}`);
        const referralLink = resources.siteUrl + `/login?referralCode=${entity.uid}#signup`;
        $("#referral-code").attr("href", referralLink);
        $("#referral-code").html(referralLink);
    });
}
function loadSaveEntityByInput() {
    let model = {
        id: currentUser.id,
        userName: currentUser.userName,
        firstName: currentUser.firstName,
        lastName: currentUser.lastName,
        version: currentUser.version,
        email: $("#email").val(),
        country: {id: $("#countrySelect2").val()},
        walletAddress: $("#walletAddress").val(),
        profileImageUrl: imageProfileUrl,
    };
    return model;
}
function loadSearchReferralEntity1ByInput() {
    return  {
        parentId:  currentUser.id
    };
}
function loadSearchReferralEntity2ByInput() {
    return  {
        treePath: currentUser.id + ",%,%"
    };
}
function updateUser() {
    let entity = loadSaveEntityByInput();
    $.blockUI(blockUiOptions());
    $("#updateUser").attr("disabled", 'disabled');
    $.ajax({
        type: "PATCH",
        url: ajaxUrl,
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(entity),
        success: function (data) {
            $.unblockUI();
            $("#updateUser").removeAttr("disabled");
            if (data.error == null) {
                clearAll();
                show_success(resources.saveSuccess);
                loadLabelByEntity(currentUser.id);
            } else {
                show_error(data.error);
            }
        },
        error: function (header, status, error) {
            $.unblockUI();
            $("#updateUser").removeAttr("disabled");
            if(isNullOrEmpty(get(() => header.responseJSON)))
                show_error('ajax answer post returned error: ' + header.responseText);
            else show_error(header.responseJSON.error + ' (' + header.responseJSON.status + ') <br>' + header.responseJSON.message);
        }
    });
}
function clearAll_() {
    $("#walletAddress,#email").val('');
    $("#countrySelect2").val('').trigger('change');
    myDropzone.removeAllFiles(true);
}

Dropzone.autoDiscover = false;
function onLoad() {
    myDropzone = new Dropzone(".dropzone", {
        url: "/api/v1/files",
        maxFiles: 1,
        init: function () {
            this.on("success", function (file,response) {
                // This event triggers after all files in the queue are processed
                imageProfileUrl = response.url;
            });
            this.on("maxfilesexceeded", function (file) {
                // Remove the previous file
                this.removeAllFiles();
                this.addFile(file);
                $.ajax({
                    type: "DELETE",
                    url: "/api/v1/files/" + imageProfileUrl.substring(imageProfileUrl.lastIndexOf('/')+1),
                    dataType: "json",
                    contentType: "application/json",
                    success: function (data) {
                        if (data == undefined) {
                            show_success(resources.deleteSuccess);
                        } else {
                            show_error(data.error);
                        }
                    },
                    error: function (header, status, error) {
                        if(isNullOrEmpty(get(()=>header.responseJSON)))
                            show_error('ajax answer post returned error: ' + header.responseText);
                        else show_error(header.responseJSON.error + ' (' + header.responseJSON.status + ') <br>' + header.responseJSON.message);
                    }
                });
            });
        }
    });
}