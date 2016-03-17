var date = new Date();
var day = date.getDate();
var month = date.getMonth() + 1;
var year = date.getFullYear();

var dayString = day.toString();
var monthString = month.toString();
var yearString = year.toString();

console.log(dayString.concat(monthString, yearString));
