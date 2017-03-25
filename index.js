const http = require('http');
const mongoose = require('mongoose');
const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const redis = require('redis').createClient(process.env.REDIS_URL || 'http://localhost:6379');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
}));

var httpHandler = require('./httpHandler.js');

require('./groupSchema.js')();
var Group = mongoose.model('Group');

const mongo_url = "mongodb://dummyuser:dummypassword@ds137540.mlab.com:37540/heroku_stfxvtk2"

ObjectId = mongoose.Types.ObjectId

mongoose.connect(mongo_url, function(error) {
  if (error) {
    httpHandler.handleDatabaseFail(response, error);
    throw error;
  }
  //create endpoint handler
  app.post('/createGroup',function(request, response){
      var newGroup = new Group(request.body);

      //check for schema errors
      var error = newGroup.validateSync();
      if(error) {
          httpHandler.handleWrongSchema(response, error);
          return;
      }

      //check for connection errors
      Group.create(newGroup).then(function(res){
          redis.set(res._id, JSON.stringify(res), function(error){
              if(error){
                  httpHandler.handleDatabaseFail(response, error);
              }
              else {
                  httpHandler.handleCreated(response);
              }
          });
          return;
      }
        ).catch( function(error){
          httpHandler.handleWrongSchema(response, error);
          return;
      });
  });

  app.post('/joinGroup', function(request, response){
      Group.find({_id: ObjectId(request.body._id)}).then(function(res){
         if(res[0].people.indexOf(request.body.person) > -1 || res[0].owner === request.body.person){
             httpHandler.handleWrongSchema(response, 'Already in group.');
         }
         else{
             Group.update({people: res[0].people.concat([request.body.person])}).then(function(res){
                 redis.set(res._id, JSON.stringify(res), function(error){
                     if(error){
                         httpHandler.handleDatabaseFail(response, error);
                     }
                     else{
                         httpHandler.handleOK(response);
                     }
                 });
             }).catch(function(err){
                if(err){
                 httpHandler.handleWrongSchema(response,err);
                }
             });
        }
      }).catch(function(err){
      if(err){
            httpHandler.handleWrongSchema(response, err);
       } 
    });
  });

  app.post('/searchGroup/:maxGroups', function(request, response){
      var searchGroup = new Group(request.body);


      //check for schema errors
      var error = searchGroup.validateSync();
      if(error) {
          httpHandler.handleWrongSchema(response, error);
      }
      else{
          var closest = searchGroup.findClosest(parseInt(request.params.maxGroups)).then(function(res){
              httpHandler.handleOK(response, res);
          }). catch(function(err){
              httpHandler.handleDatabaseFail(response, err);
          });
    }
  });

  app.get('/listGroups/:person', function(request, response){
      var user = {};
      user.person = request.params.person;

      Group.find({owner: {$eq: user.person}}).then(function(res){
          user.owns = res;
          Group.find({people: user.person}).then(function(res){
              user.participates = res;
              httpHandler.handleOK(response, user);
          });
      });

  });



  //Starts HTTP Server
  var server = app.listen(process.env.PORT || 3000, function () {
     var host = server.address().address
     var port = server.address().port

     console.log("App listening at http://%s:%s", host, port)
  })


});