
package com.alp.getirhackathon.Service.ResponseModels;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyGroupListResult implements Serializable
{

    @SerializedName("person")
    @Expose
    private String person;
    @SerializedName("owns")
    @Expose
    private List<SearchGroupResponseModel> owns = null;
    @SerializedName("participates")
    @Expose
    private List<Participate> participates = null;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public List<SearchGroupResponseModel> getOwns() {
        return owns;
    }

    public void setOwns(List<SearchGroupResponseModel> owns) {
        this.owns = owns;
    }

    public List<Participate> getParticipates() {
        return participates;
    }

    public void setParticipates(List<Participate> participates) {
        this.participates = participates;
    }

}
