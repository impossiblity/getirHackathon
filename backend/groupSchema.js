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

    GroupSchema.methods.findClosest = function(cb, lim) {
        return this.model('Group').find({
            location: {$nearSphere: this.location},
            owner: {$ne: this.owner}
        }).limit(lim).exec(cb);
    };

    mongoose.model('Group', GroupSchema);
};
