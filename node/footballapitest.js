var https = require('https');
var http = require('http')
var firebase = require('firebase')

var ref = new Firebase('https://cwprco304.firebaseio.com');

console.log("The Centre Circle server side Node application\n");
console.log("Application is to poll the API every 15 seconds");

setInterval(function () {

    var dateString = getTodayDate();

    var http = require("http");

    var options = {
      "method": "GET",
      "hostname": "api.football-api.com",
      "port": null,
      //"path": "/2.0/matches?from_date=27.10.2015&to_date=05.11.2015&Authorization=565ec012251f932ea4000001465e5017e24b4c3f49c5f59207d768b3"
      "path": "/2.0/matches?match_date=" + dateString + "&Authorization=565ec012251f932ea4000001465e5017e24b4c3f49c5f59207d768b3"
    };

    var req = http.request(options, function (res) {
      var content = "";

      res.on("data", function (chunk) {
        content += chunk;
      });

      res.on("end", function () {
        var data = JSON.parse(content);

        for (var i = 0; i < data.length; i++) {
            var date = getMatchDate(data[i].formatted_date);
            var matchRef = ref.child("matches/" + date);
            if(data[i].status !== "FT" || data[i].status !== "HT") {
                if (parseInt(data[i].timer) > 0 && parseInt(data[i].timer) <= 120) {
                    console.log(data[i].localteam_name + " " + data[i].localteam_score
            + " - " + data[i].visitorteam_score + " "  + data[i].visitorteam_name + "   " + data[i].timer);
                }
                
            }
            

            matchRef.child(data[i].id).set({
                        matchCompId : data[i].comp_id,
                        awayBadge : 2130837638,
                        awayScore : data[i].visitorteam_score,
                        awayTeam : data[i].visitorteam_name,
                        awayTeamId : data[i].visitorteam_id,
                        homeBadge : 2130837574,
                        homeScore : data[i].localteam_score,
                        homeTeam : data[i].localteam_name,
                        homeTeamId : data[i].localteam_id,
                        matchId : data[i].id,
                        matchStatus : data[i].status,
                        halfTimeScore : data[i].ht_score,
                        fullTimeScore : data[i].ft_score,
                        matchTime : data[i].time,
                        venue : data[i].venue,
                        venueId : data[i].venue_id,
                        date : data[i].formatted_date

            });
            var events = data[i].events;
                var eventRef = matchRef.child(data[i].id +
                "/events");
                if (events != null) {
                  for(var j =0; j < events.length; j++) {
                    if ((events[j].minute === data[i].status) 
                        && events[j].type === "goal") {
                        if(events[j].team === 'localteam') {
                            
                            console.log("GOAL " + data[i].localteam_name + ". Scored by " + events[j].player);
                            console.log();

                        } else if (events[j].team === 'visitorteam') {
                            
                            console.log("GOAL " + data[i].visitorteam_name + ". Scored by " + events[j].player);
                            console.log();
                        }
                        

                    }
                    eventRef.child(events[j].id).set({
                      eventId : events[j].id,
                      eventType : events[j].type,
                      eventMinute : events[j].minute,
                      eventExtraMin : events[j].extra_min,
                      eventTeam : events[j].team,
                      eventPlayer : events[j].player,
                      eventPlayerId : events[j].player_id,
                      eventAssist : events[j].assist,
                      eventAssistId : events[j].assist_id
                      //eventResult : events[j].event_result

                    });
                  }
                }
            
        }

        console.log();
        console.log("Fetching data.....")
      });


    });

    req.on('error', function(error) {
        console.log("Error while calling endpoint.", error);
    });

    req.end();
}, 10000);


function getMatchDate(matchDate) {
    var matchSplit = matchDate.split(".");
    matchDate = matchSplit[0].concat(matchSplit[1], matchSplit[2]);
    return matchDate;
}



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

    var dateString = dayString.concat(".", monthString, ".", yearString);

    return dateString;
}

function getTodayDateForPath() {
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
