const http = require('http');
const mongoose = require('mongoose');
const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const redisClient = require('redis').createClient;
const redis = redisClient(6379, 'localhost');
app.use(bodyParser.json());

var httpHandler = require('./httpHandler.js');

require('./groupSchema.js')();
var Group = mongoose.model('Group');

const mongo_url = "mongodb://dummyuser:dummypassword@ds137540.mlab.com:37540/heroku_stfxvtk2"

ObjectId = mongoose.Types.ObjectId


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
    mongoose.connect(mongo_url, function(error) {
      if (error) {
        httpHandler.handleDatabaseFail(response, error);
        return;
      }
      Group.create(newGroup).then(function(){
                httpHandler.handleCreated(response);
                mongoose.disconnect();
                return;
            }
        ).catch( function(error){
                httpHandler.handleWrongSchema(response, error);
                mongoose.disconnect();
                return;
        });
    });
});

app.post('/joinGroup', function(request, response){
    mongoose.connect(mongo_url, function(error) {
        if (error) {
            httpHandler.handleDatabaseFail(response, error);
            mongoose.disconnect();
            return;
        }
        Group.find({_id: ObjectId(request.body._id)}).then(function(res){
            if(res[0].people.indexOf(request.body.person) > -1 || res[0].owner === request.body.person){
                httpHandler.handleWrongSchema(response, 'Already in group.');
                mongoose.disconnect();
            }
            else{
                Group.update({people: res[0].people.concat([request.body.person])}).then(function(){
                    httpHandler.handleOK(response, {});
                    mongoose.disconnect();
                }).catch(function(err){
                    if(err){
                        httpHandler.handleWrongSchema(response,err);
                        mongoose.disconnect();
                    }
                });
            }
        }).catch(function(err){
            if(err){
                httpHandler.handleWrongSchema(response, err);
                mongoose.disconnect();
            }
        });
    });
});

app.post('/searchGroup/:maxGroups', function(request, response){
    var searchGroup = new Group(request.body);

    //check for schema errors
    var error = searchGroup.validateSync();
    if(error) {
        httpHandler.handleWrongSchema(response, error);
        return;
    }

    mongoose.connect(mongo_url, function(error) {
        if (error) {
            httpHandler.handleDatabaseFail(response, error);
            mongoose.disconnect();
            return;
        }
        var closest = searchGroup.findClosest(parseInt(request.params.maxGroups), function(err, res){
            if(err){
                httpHandler.handleDatabaseFail(response, err);
                mongoose.disconnect();
                return;
            }
            httpHandler.handleOK(response, res);
            mongoose.disconnect();
            return;
        });
    });
});



//Starts HTTP Server
var server = app.listen(process.env.PORT || 3000, function () {
   var host = server.address().address
   var port = server.address().port

   console.log("App listening at http://%s:%s", host, port)
})
