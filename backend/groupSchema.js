var mongoose = require('mongoose');
var Schema = mongoose.Schema;

module.exports = function() {
    var GroupSchema = new Schema({
        owner: { type: String, required: [true, 'owner is required'] } ,
        location: {type: [Number], index: '2d',
            validate: {validator: function(v){ return v.length == 2}, message:'location must have 2 elements'},
            required: [true, 'location is required']},
        startTime: {type: Date, required: [true, 'startTime is required']},
        endTime: {type: Date, required: [true, 'endTime is required']},
        people: [String]
    });

    GroupSchema.methods.findClosest = function(cb, lim) {
        return this.model('Group').find({
            location: {$nearSphere: this.location},
            _id: {$ne: this._id}
        }).limit(lim).exec(cb);
    };

    mongoose.model('Group', GroupSchema);
};
