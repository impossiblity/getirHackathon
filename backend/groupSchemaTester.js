var async = require('async');
var mongoose = require('mongoose');
require('./groupSchema.js')();
var Group = mongoose.model('Group');

var data = [
  { owner: 'me', location: [0.0, 0.0], startTime: new Date(), endTime: new Date(), people: [] }
];

mongoose.connect('mongodb://localhost/hackathonDatabase', function(err) {
  if (err) {
    throw err;
  }

  Group.on('index', function(err) {
    if (err) {
      throw err;
    }
    // create all of the dummy locations
    async.each(data, function(item, cb) {
      Group.create(item, cb);
    }, function(err) {
      if (err) {
        throw err;
      }
      // create the location we want to search for
      //var coords = {type: 'Point', coordinates: [-5, 5]};
      // search for it
      /*
      Location.find({loc: {$near: coords}}).limit(1).exec(function(err, res) {
        if (err) {
          throw err;
        }
        console.log('Closest to %s is %s', JSON.stringify(coords), res);
        cleanup();
      });
      */
    });
  });
});

function cleanup() {
  Group.remove(function() {
    mongoose.disconnect();
  });
}
