var https = require('https');
var firebase = require('firebase')

var ref = new Firebase('https://cwprco304.firebaseio.com');
var matchRef = ref.child("premierleague/matches");



setInterval(function () {

    var rest_options = {
        host: 'football-api.com',
        port: 443,
        path: '/api/?Action=fixtures&APIKey=f49090a4-ef6b-a743-dd5c3dbf5711&comp_id=1204&match_date=12.03.2016&IP=81.156.123.17',
        method: 'GET'
    };

    var request = https.request(rest_options, function(response) {
        var content = "";

        // Handle data chunks
        response.on('data', function(chunk) {
            content += chunk;
        });

        // Once we're done streaming the response, parse it as json.
        response.on('end', function() {
            var date = new Date();
            var day = date.getDate();
            var month = date.getMonth();
            var year = date.getFullYear();
            var data = JSON.parse(content);
            // for (var i = 0; i < data.matches.length; i++) {
            //     console.log(data.matches[i].match_id);
            //     console.log(data.matches[i]);

            // }
            console.log(day + "/" + month + "/" + year);
            console.log(data.matches[1].match_events);
            

            //TODO: Do something with `data`.
        });
    });

    // Report errors
    request.on('error', function(error) {
        console.log("Error while calling endpoint.", error);
    });

    request.end();
}, 30000);