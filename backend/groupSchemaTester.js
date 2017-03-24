var async = require('async');
var mongoose = require('mongoose');
require('./groupSchema.js')();
var Group = mongoose.model('Group');

var data = [
  { owner: 'me', location: [0.0, 0.0], startTime: new Date(), endTime: new Date(), people: [] },
  { owner: 'a', location: [2.0, 0.0], startTime: new Date(), endTime: new Date(), people: [] },
  { owner: 'b', location: [10.0, 1.0], startTime: new Date(), endTime: new Date(), people: [] },
  { owner: 'c', location: [0.0, 5.0], startTime: new Date(), endTime: new Date(), people: [] },
  { owner: 'd', location: [0.0, 1.0], startTime: new Date(), endTime: new Date(), people: [] },
];

mongoose.connect('mongodb://localhost/hackathonDatabase', function(err) {
  if (err) {
    throw err;
  }

  Group.on('index', function(err) {
    if (err) {
      throw err;
    }
    //Test group creation
    async.each(data, function(item, cb) {
      Group.create(item, cb);
    }, function(err) {
      if (err) {
        throw err;
      }
      //Test closest group finding
      Group.find({owner: 'me'}, function(err, res) {
        if (err) {
          throw err;
        }

        res[0].findClosest(function(err, closest) {
          if (err) {
            console.log(err)
            throw err;
          }

          console.log('%s is closest to %s', res[0], closest);
      }, 2 );
        });
        });
    });
});

function cleanup() {
  Group.remove(function() {
    mongoose.disconnect();
  });
}
