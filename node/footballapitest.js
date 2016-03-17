var https = require('https');
var firebase = require('firebase')

var ref = new Firebase('https://cwprco304.firebaseio.com');
var matchRef = ref.child("premierleague/matches");



setInterval(function () {

    var rest_options = {
        host: 'football-api.com',
        port: 443,
        path: '/api/?Action=fixtures&APIKey=f49090a4-ef6b-a743-dd5c3dbf5711&match_date=15.03.2016&IP=81.156.123.17',
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
            for (var i = 0; i < data.matches.length; i++) {

                console.log(data.matches[i].match_localteam_name + " " +
                    data.matches[i].match_localteam_score + " - " +
                    data.matches[i].match_visitorteam_score + " " + data.matches[i].match_visitorteam_name +
                    "  match status: " + data.matches[i].match_status);


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
                var events = data.matches[i].match_events;
                var eventRef = matchRef.child(data.matches[i].match_id +
                "/events");
                console.log(events);
                if (events != null) {
                  for(var j =0; j < events.length; j++) {
                    console.log(events[j].event_player);
                    eventRef.child(events[j].event_id).set({
                      eventId : events[j].event_id,
                      eventMatchId : events[j].event_match_id,
                      eventType : events[j].event_type,
                      eventMinute : events[j].event_minute,
                      eventTeam : events[j].event_team,
                      eventPlayer : events[j].event_player,
                      eventPlayerId : events[j].event_player_id,
                      eventResult : events[j].event_result


                    });
                  }
                }


            }


            //TODO: Do something with `data`.
        });
    });

    // Report errors
    request.on('error', function(error) {
        console.log("Error while calling endpoint.", error);
    });

    request.end();
}, 20000);
