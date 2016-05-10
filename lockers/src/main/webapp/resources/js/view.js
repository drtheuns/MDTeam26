/**
 * Created by randyr on 20-4-16.
 */
var currentId = 0;
var usernames = [];
var allUsernames = [];

$(document).ready(function() {
    $('#lockerWithUsernameExists').hide();

    getUsers(); // prepare modal autocomplete

    $('#user-form').submit(function(e) {
        var submittedUsername = $('#locker-user').val();
        if (usernames.indexOf(submittedUsername) > -1) {
            //username has already claimed a locker
            e.preventDefault();
            $('#warning-message').html("<p><strong>This username already has a locker!</strong> Cannot assign a second locker.</p>");
            $('#lockerWithUsernameExists').show();
        }
        if (allUsernames.indexOf(submittedUsername) == -1 && submittedUsername.length > 0) {
            // username doesnt exist in DB
            e.preventDefault();
            $('#warning-message').html("<p><strong>Username doesn't exist!</strong> Please enter a valid username</p>");
            $('#lockerWithUsernameExists').show();
        }

    });

    $('.close').click(function (e) {
        e.preventDefault();
        $('#lockerWithUsernameExists').hide();
    })
});

$(document).ready(function(){
    $('#lockerModal').modal({
        keyboard: true,
        backdrop: false,
        show:false,
    }).on('show.bs.modal', function(event){
        var id = event.relatedTarget.valueOf('id').id;
        $(this).find('.modal-title').html($('<b>Locker ' + id + '</b>'));
        $(this).find('#locker-id').val(id);
    });
});

function getUsers() { //Get list of username, first- and lastname for autocomplete in modal.
    $.get("/getusers", function(data) {
        var sourceArr = [];
        $.each(data, function(index, obj) {
            var all = obj.username + " " + obj.firstname + " " + obj.lastname;
            sourceArr.push(all);
            allUsernames.push(obj.username);
        });
        initializeAutocomplete(sourceArr);
    });
    $.get("/gettakenusers", function (data) {
        console.log(data);
        usernames = data;
    });
}

function initializeAutocomplete(source) {

    var substringMatcher = function(strs) {
        return function findMatches(q, cb) {
            var matches, substringRegex;

            // an array that will be populated with substring matches
            matches = [];

            // regex used to determine if a string contains the substring `q`
            substrRegex = new RegExp(q, 'i');

            // iterate through the pool of strings and for any string that
            // contains the substring `q`, add it to the `matches` array
            $.each(strs, function(i, str) {
                if (substrRegex.test(str)) {
                    matches.push(str.split(" ")[0]);
                }
            });

            cb(matches);
        };
    };

    $('#locker-field .typeahead').typeahead({
            hint: false,
            highlight: true,
            minLength: 1
        },
        {
            name: 'users',
            source: substringMatcher(source)
        });
}

function submitUser() {
    $('#user-form').submit();
}

function clearUserFromLocker(id) {
    $('#locker-id').val(id);
    $('#locker-user').val("");
    $('#user-form').submit();
}
