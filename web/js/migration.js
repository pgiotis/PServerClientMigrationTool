var url = "http://localhost:8080/PServerClientMigrationTool/MigrationService";
var load = false;
var responseObj;
var pl = new SOAPClientParameters();

function Clear() {
    $('#FromPSUrl').val('');
    $('#FromPSPort').val('');
    $('#ToPSUrl').val('');
    $('#ToPSPort').val('');
    $('#ClientUsername').val('');
    $('#ClientPassword').val('');
    $('#APUsername').val('');
    $('#APPassword').val('');

}


function runMigration() {
    loading(true);
    $('.progress-bar').width("10%");
    //get settings for migration
    pl.add("FromPSUrl", $('#FromPSUrl').val());
    pl.add("FromPSPort", $('#FromPSPort').val());
    pl.add("ToPSUrl", $('#ToPSUrl').val());
    pl.add("ToPSPort", $('#ToPSPort').val());
    pl.add("ClientUsername", $('#ClientUsername').val());
    pl.add("ClientPassword", $('#ClientPassword').val());
    pl.add("APUsername", $('#APUsername').val());
    pl.add("APPassword", $('#APPassword').val());
//    Clear();
    SOAPClient.invoke(url, "MigrateClient", pl, true, MigrateAttributes);
}


function MigrateAttributes(response) {
    $('.progress-bar').width("30%");

    SOAPClient.invoke(url, "MigrateAttributes", pl, true, MigrateFeatures);
}


function MigrateFeatures(response) {
    $('.progress-bar').width("60%");

    SOAPClient.invoke(url, "MigrateFeatures", pl, true, MigrateUsers);
}


function MigrateUsers(response) {
    $('.progress-bar').width("90%");

    SOAPClient.invoke(url, "MigrateUsers", pl, true, MigrationComplete);
}
function MigrationComplete(response){
    loading(false);
}

function loading(bState) {
    if (bState) {
        $('#progress').show();
    } else {
        $('#progress').hide();
        $('.progress-bar').width("1%");
    }

}
