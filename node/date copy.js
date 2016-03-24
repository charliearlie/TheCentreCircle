/*
    Work of Charles Waite
    Plymouth University
*/

var premTeams = ["9002", "9240", "9406", "9259", "9427", "9260", "9363",
"9378", "9249", "9092", "9426", "9158", "9053", "9423", "9387", "9127", 
"9296", "9384", "9287", "9008", "15702", "15692", "16110", "16270", 
"15934", "16175", "15679", "16050", "16006", "15999", "16117", "16009", 
"16107", "16261", "16040", "16098", "16025", "16017", "16180", "16043",
"10285", "10303", "10437", "10576", "10307", "10281", "10388", "10653",
"10476", "10453", "10646", "10419", "10329", "10677", "10269", "10442",
"10347", "10423"];

var http = require('http');
var firebase = require('firebase')
var ref = new Firebase('https://cwprco304.firebaseio.com');

for(var i = 0; i < premTeams.length; i++) {

    var options = {
      "method": "GET",
      "hostname": "api.football-api.com",
      "port": null,
      "path": "/2.0/team/2.0/competitions?Authorization=565ec012251f932ea4000001465e5017e24b4c3f49c5f59207d768b3"
    };

    var req = http.request(options, function (res) {
      var content = "";

      res.on("data", function (chunk) {
        content += chunk;
      });

      res.on("end", function () {
        var data = JSON.parse(content);
        teamRef.set(teamObj);
        console.log(data);

      });


    });

    req.on('error', function(error) {
        console.log("Error while calling endpoint.", error);
    });

    req.end();
}
