package com.alp.getirhackathon.Service;

import java.io.Serializable;
import java.util.List;

import com.alp.getirhackathon.ToolBox.CustomDateManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AlparslanSelçuk on 25.03.2017.
 */

public class SearchGroupResponseModel implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("location")
    @Expose
    private List<Double> location = null;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("people")
    @Expose
    private List<String> people = null;

    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("timeDifference")
    @Expose
    private int timeDifference;

    public String getDistance() {
        if(distance.contains(","))
            return distance.split(",")[0];
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTimeDifference() {
        return CustomDateManager.printDifference(timeDifference);
    }

    public void setTimeDifference(Integer timeDifference) {
        this.timeDifference = timeDifference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

}