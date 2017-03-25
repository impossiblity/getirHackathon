const http = require('http');
const mongoose = require('mongoose');
const express = require('express');
const bodyParser = require('body-parser');
const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
}));
app.use(express.json());       // to support JSON-encoded bodies
app.use(express.urlencoded()); // to support URL-encoded bodies

require('./groupSchema.js')();
var Group = mongoose.model('Group');

const mongo_url = "mongodb://dummyuser:dummypassword@ds137540.mlab.com:37540/heroku_stfxvtk2"

ObjectId = mongoose.Types.ObjectId


//Error handler for database failures.
function handleDatabaseFail(response, error) {
    response.statusCode = 500;
    response.setHeader('Content-Type', 'text/plain');
    response.end(error.toString());
}

//Error handler for illegal arguments in POST requests.
function handleWrongSchema(response, error) {
    response.statusCode = 400;
    response.setHeader('Content-Type', 'text/plain');
    response.end(error.toString());
    console.log(error);
}

//Error handler for no results.
function handleNoResults(response) {
    response.statusCode = 404;
    response.setHeader('Content-Type', 'text/plain');
    response.end('No Results.\n');
}

function handleCreated(response){
    response.statusCode = 201;
    response.end();
}

function handleOK(response, content){
    response.statusCode = 200;
    response.setHeader('Content-Type', 'text/json');
    response.end(JSON.stringify(content));
}

//create endpoint handler
app.post('/createGroup',function(request, response){
    var body = request.body || request.query;
    console.log(body)
    console.log(request)
    var newGroup = new Group(body);

    //check for schema errors
    var error = newGroup.validateSync();
    if(error) {
        console.log('here')
        handleWrongSchema(response, error);
        return;
    }

    //check for connection errors
    mongoose.connect(mongo_url, function(error) {
        if (error) {
        handleDatabaseFail(response, error);
        return;
      }
      Group.create(newGroup).then(function(){
                handleCreated(response);
                mongoose.disconnect();
                return;
            }
        ).catch( function(error){
                console.log('here2')
                handleWrongSchema(response, error);
                mongoose.disconnect();
                return;
        });
    });
});

app.post('/joinGroup', function(request, response){
    var body = request.body || request.query;
    mongoose.connect(mongo_url, function(error) {
        if (error) {
            handleDatabaseFail(response, error);
            mongoose.disconnect();
            return;
        }
        Group.find({_id: ObjectId(body._id)}).then(function(res){
            if(res[0].people.indexOf(body.person) > -1 || res[0].owner === body.person){
                handleWrongSchema(response, 'Already in group.');
                mongoose.disconnect();
            }
            else{
                Group.update({people: res[0].people.concat([body.person])}).then(function(){
                    handleOK(response, {});
                    mongoose.disconnect();
                }).catch(function(err){
                    if(err){
                        handleWrongSchema(response,err);
                        mongoose.disconnect();
                    }
                });
            }
        }).catch(function(err){
            if(err){
                handleWrongSchema(response, err);
                mongoose.disconnect();
            }
        });
    });
});

app.post('/searchGroup/:maxGroups', function(request, response){
    var body = request.body || request.query;
    var searchGroup = new Group(body);

    //check for schema errors
    var error = searchGroup.validateSync();
    if(error) {
        handleWrongSchema(response, error);
        return;
    }

    mongoose.connect(mongo_url, function(error) {
        if (error) {
            handleDatabaseFail(response, error);
            mongoose.disconnect();
            return;
        }
        var closest = searchGroup.findClosest(parseInt(request.params.maxGroups), function(err, res){
            if(err){
                handleDatabaseFail(response, err);
                mongoose.disconnect();
                return;
            }
            handleOK(response, res);
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
