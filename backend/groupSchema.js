var mongoose = require('mongoose');
var Schema = mongoose.Schema;

module.exports = function() {
    var GroupSchema = new Schema({
        owner: String,
        location: {type: [Number], index: '2d'},
        startTime: Date,
        endTime: Date,
        people: [String]
    });

    GroupObject.methods.findClosest = function(cb) {
        return this.model('Group').find({
            location: {$nearSphere: this.location},
            name: {$ne: this.name}
        }).limit(1).exec(cb);
    };

    mongoose.model('Group', GroupSchema);
};
