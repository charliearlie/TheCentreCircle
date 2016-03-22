/*
    Work of Charles Waite
    Plymouth University
*/
var https = require('https');
var firebase = require('firebase')

var ref = new Firebase('https://cwprco304.firebaseio.com');


console.log("The Centre Circle server side Node application\n");
console.log("Application is to poll the API every 15 seconds");


setInterval(function () {

    var rest_options = {
        host: 'football-api.com',
        port: 443,
        path: '/api/?Action=today&APIKey=' + getAPIKey() + '&IP=81.156.123.17',
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
          console.log(getTodayDate());

          var dateString = getTodayDate();

          var matchRef = ref.child("matches/" + dateString);
          var data = JSON.parse(content);



            for (var i = 0; i < data.matches.length; i++) {
                if(data.matches[i].match_status !== "FT" || data.matches[i].match_status !== "HT") {
                    if (parseInt(data.matches[i].match_timer) > 0 && parseInt(data.matches[i].match_timer) <= 120) {
                        console.log(data.matches[i].match_localteam_name + " " + data.matches[i].match_localteam_score
                        + " - " + data.matches[i].match_visitorteam_score + " "  + data.matches[i].match_visitorteam_name + "   '" + data.matches[i].match_timer);
                        console.log();
                }
                
            }

                

                matchRef.child(data.matches[i].match_id).set({
                        matchCompId : data.matches[i].match_comp_id,

                        awayBadge : 2130837638,
                        awayScore : data.matches[i].match_visitorteam_score,
                        awayTeam : data.matches[i].match_visitorteam_name,
                        awayTeamId : data.matches[i].match_visitorteam_id,
                        homeBadge : 2130837574,
                        homeScore : data.matches[i].match_localteam_score,
                        homeTeam : data.matches[i].match_localteam_name,
                        homeTeamId : data.matches[i].match_localteam_id,
                        matchId : data.matches[i].match_id,
                        matchStatus : data.matches[i].match_status,
                        halfTimeScore : data.matches[i].match_ht_score,
                        fullTimeScore : data.matches[i].match_ft_score,
                        extraTimeScore : data.matches[i].match_et_score,
                        matchTime : data.matches[i].match_time,
                });
                var events = data.matches[i].match_events;
                var eventRef = matchRef.child(data.matches[i].match_id +
                "/events");

                if (events != null) {
                  for(var j =0; j < events.length; j++) {
                    if ((events[j].event_minute === data.matches[i].match_status) 
                        && events[j].event_type === "goal") {
                        if(events[j].event_team === 'localteam') {
                            
                            console.log("GOAL " + data.matches[i].match_localteam_name + ". Scored by " + events[j].event_player);
                            console.log();

                        } else if (events[j].event_team === 'visitorteam') {
                            
                            console.log("GOAL " + data.matches[i].match_visitorteam_name + ". Scored by " + events[j].event_player);
                            console.log();
                        }
                        

                    }
                    var queryRef = eventRef.child(events[j].event_id);
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

            console.log("Fetching data....")
                console.log();


            //TODO: Do something with `data`.
        });
    });

    // Report errors
    request.on('error', function(error) {
        console.log("Error while calling endpoint.", error);
    });

    request.end();
}, 15000);

function getTodayDate() {
    var date = new Date();
    var day = date.getDate();
    var month = date.getMonth() + 1;

    var year = date.getFullYear();

    var dayString = day.toString();
    var monthString = month.toString();
    if (parseInt(monthString) <= 10) {
      monthString = "0".concat(monthString);
    }
    var yearString = year.toString();

    var dateString = dayString.concat(monthString, yearString);

    return dateString;
}

function getAPIKey() {
    return "f49090a4-ef6b-a743-dd5c3dbf5711";
}