var https = require('https');
var firebase = require('firebase')

var ref = new Firebase('https://cwprco304.firebaseio.com');
var matchRef = ref.child("premierleague/matches");

setInterval(function () {

    var rest_options = {
        host: 'football-api.com',
        port: 443,
        path: '/api/?Action=today&APIKey=f49090a4-ef6b-a743-dd5c3dbf5711&IP=81.156.123.17',
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
            var data = JSON.parse(content);
            for (var i = 0; i < data.matches.length; i++) {
                console.log(data.matches[i].match_id);
                matchRef.child(data.matches[i].match_id).set({
                        awayBadge : 2130837638,
                        awayScore : data.matches[i].match_visitorteam_score,
                        awayTeam : data.matches[i].match_visitorteam_name,
                        homeBadge : 2130837574,
                        homeScore : data.matches[i].match_localteam_score,
                        homeTeam : data.matches[i].match_localteam_name,
                        matchId : data.matches[i].match_id,
                        matchStatus : data.matches[i].match_status
                });
                console.log(data.matches[i]);
            }
            

            //TODO: Do something with `data`.
        });
    });

    // Report errors
    request.on('error', function(error) {
        console.log("Error while calling endpoint.", error);
    });

    request.end();
}, 30000);