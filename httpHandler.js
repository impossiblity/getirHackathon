const http = require('http');

//Error handler for database failures.
module.exports.handleDatabaseFail = function(response, error) {
    console.log(error)
    response.statusCode = 500;
    response.setHeader('Content-Type', 'text/plain');
    response.end(error.toString());
}

//Error handler for illegal arguments in POST requests.
module.exports.handleWrongSchema = function (response, error) {
    response.statusCode = 400;
    response.setHeader('Content-Type', 'text/plain');
    response.end(error.toString());
}

//Error handler for no results.
module.exports.handleNoResults = function(response) {
    response.statusCode = 404;
    response.setHeader('Content-Type', 'text/plain');
    response.end('No Results.\n');
}

module.exports.handleCreated = function(response){
    response.statusCode = 201;
    response.end();
}

module.exports.handleOK = function(response, content){
    response.statusCode = 200;
    response.setHeader('Content-Type', 'text/json');
    response.end(JSON.stringify(content));
}
