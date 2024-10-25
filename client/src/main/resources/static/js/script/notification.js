function onLoad(){
    const id = getURLParameter("id");
    if(!isNullOrEmpty(id)){
        loadContent(id);
    }
    $.getJSON("api/v1/notification/findAll-by-recipientId/" + currentUser.id + "?sort=read,desc", function (data) {
        data.content.forEach(function (notif){
            const notifElement = `<a href="javascript:loadContent(${notif.id})">
                                <div class="mail_list">
                                    <div class="left">
                                        ${!notif.read ? '<i class="fa fa-circle"></i>': ''} <i class="fa fa-edit"></i>
                                    </div>
                                    <div class="right">
                                        <h3>${notif.sender.selectTitle} <small>${toTimeString(notif.createdDate)}</small></h3>
                                        <p>${notif.subject.length > 98 ? notif.subject.substring(0,98) + "..." : notif.subject}</p>
                                    </div>
                                </div>
                            </a>`;
            $('.mail_list_column').append(notifElement);
        });
    });
}

function loadContent(id){
    $.getJSON("api/v1/notification/" + id, function (data) {
        $('.inbox-body').html(`<div class="mail_heading row">
                                    <div class="col-md-8">
                                        <div class="btn-group">
                                            <button class="btn btn-sm btn-primary" type="button"><i class="fa fa-reply"></i> Reply</button>
                                            <button class="btn btn-sm btn-default" type="button"  data-placement="top" data-toggle="tooltip" data-original-title="Forward"><i class="fa fa-share"></i></button>
                                            <button class="btn btn-sm btn-default" type="button" data-placement="top" data-toggle="tooltip" data-original-title="Print"><i class="fa fa-print"></i></button>
                                            <button class="btn btn-sm btn-default" type="button" data-placement="top" data-toggle="tooltip" data-original-title="Trash"><i class="fa fa-trash-o"></i></button>
                                        </div>
                                    </div>
                                    <div class="col-md-4 text-right">
                                        <p class="date"> ${toDateTimeString(data.createdDate)}</p>
                                    </div>
                                    <div class="col-md-12">
                                        <h4> ${data.subject}</h4>
                                    </div>
                                </div>
                                <div class="sender-info">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <strong>${data.sender.selectTitle}</strong>
                                            <span>(${data.sender.email})</span> to
                                            <strong>me</strong>
                                            <a class="sender-dropdown"><i class="fa fa-chevron-down"></i></a>
                                        </div>
                                    </div>
                                </div>
                                <div class="view-mail">
                                    <p>${data.body} </p>                                   
                                </div>
                                
                                <div class="btn-group">
                                    <button class="btn btn-sm btn-primary" type="button"><i class="fa fa-reply"></i> Reply</button>
                                    <button class="btn btn-sm btn-default" type="button"  data-placement="top" data-toggle="tooltip" data-original-title="Forward"><i class="fa fa-share"></i></button>
                                    <button class="btn btn-sm btn-default" type="button" data-placement="top" data-toggle="tooltip" data-original-title="Print"><i class="fa fa-print"></i></button>
                                    <button class="btn btn-sm btn-default" type="button" data-placement="top" data-toggle="tooltip" data-original-title="Trash"><i class="fa fa-trash-o"></i></button>
                                </div>`);
    })
}