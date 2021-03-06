/**
 * Created by randyr on 5/16/16.
 */
var $rows;
$(document).ready(function () {
    getHistory(100);

    $rows = $('#historyTable tbody tr');
    $('#search').keyup(function search() {
        var val = '^(?=.*\\b' + $.trim($(this).val()).split(/\s+/).join('\\b)(?=.*\\b') + ').*$',
            reg = RegExp(val, 'i'),
            text;

        $rows.show().filter(function () {
            text = $(this).text().replace(/\s+/g, ' ');
            return !reg.test(text);
        }).hide();
    });
});

function getHistory(limit) {
    var data = {};
    data["limit"] = limit;

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/gethistory",
        data: JSON.stringify(data),
        dataType: 'json',
        timeout: 100000,
        success : function(data) {
            if (data.code == "200") {
                fillTable(data.result);
            } else {
                alert(data.result);
            }
        },
        error : function(e) {
            //error
        }
    });
}

function fillTable(data) {
    $.each($.parseJSON(data), function (index, obj) {
        //Format values to not display 'null' but rather stay empty. Also format dates.
        var user = obj.user != null ? obj.user.username : "";
        var userName = obj.user != null ? obj.user.firstname + " " + obj.user.lastname : "";
        var date_updated = dateFormat(new Date(obj.date_updated), "yyyy-mm-dd HH:MM:ss");
        var timestamp = obj.timestamp != null ? dateFormat(new Date(obj.timestamp), "yyyy-mm-dd HH:MM:ss") : "";
        var dateExpired = obj.date != null ? dateFormat(new Date(obj.date), "yyyy-mm-dd HH:MM:ss") : "";
        var locker = obj.locker != null ? obj.locker.lockerTower + obj.locker.lockerFloor + obj.locker.lockerNumber : "";
        var lockerid = obj.locker != null ? obj.locker.lockerid : "";

        $('#historyTable tbody').append("<tr class=\"locker-row\"> " +
            "<td class=\"col-md-1\"> " + index  + "</td> " +
            "<td class=\"col-md-1\"><a href=\"/locker/" + lockerid + "\">"+ locker + "</a></td> " +
            "<td class=\"col-md-2\"> " + date_updated + "</td> " +
            "<td class=\"col-md-1\" title=\"" + userName + "\"> " + user + "</td> " +
            "<td class=\"col-md-2\"> " + timestamp + "</td> " +
            "<td class=\"col-md-2\"> " + dateExpired + "</td> " +
            "<td class=\"col-md-3\"> " + obj.action + "</td> " +
            "</tr>");
    });
    $rows = $('#historyTable tbody tr');
}

$(document).on('change','#limit',function(){
    $('#historyTable tbody').html("");
    getHistory($('#limit').val());
});