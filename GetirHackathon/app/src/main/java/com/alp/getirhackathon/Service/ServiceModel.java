package com.alp.getirhackathon.Service;

/**
 * Created by AlparslanSelçuk on 25.03.2017.
 */

public class ServiceModel {
    private static String BASEURL = "https://storm-getir-hackathon.herokuapp.com/";
    public static String CREATE_GROUP = BASEURL + "createGroup";
    public static String SEARCH_GROUP = BASEURL + "searchGroup/25";
    public static String JOIN_GROUP = BASEURL + "joinGroup";
    public static String GROUP_LIST = BASEURL + "listGroups";
    public static String MESSAGE_GROUPS = BASEURL + "postMessage";
    public static String DETAILS_GROUPS = BASEURL + "getGroupDetail";
}
