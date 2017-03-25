module.exports.distance = function(coord1, coord2) {
    console.log(coord1);
    console.log(coord2);
  lat1 = coord1[0];
  lon1 = coord1[1];
  lat2 = coord1[0];
  lon2 = coord2[1];
  var p = 0.017453292519943295;    // Math.PI / 180
  var c = Math.cos;
  var a = 0.5 - c((lat2 - lat1) * p)/2 +
          c(lat1 * p) * c(lat2 * p) *
          (1 - c((lon2 - lon1) * p))/2;

  return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
}

module.exports.timeOverlap = function(startTime1, endTime1, startTime2, endTime2) {
    var diff = Math.min(Date.parse(endTime1), Date.parse(endTime2)) - Math.max(Date.parse(startTime1), Date.parse(startTime2));
    return Math.floor((diff/1000)/60);
}
