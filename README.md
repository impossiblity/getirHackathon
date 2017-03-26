# getirHackathon
Submission of team Storm in Getir Hackathon.

## Members
* Kaan Bulut Tekelioglu
* Alparslan Develioglu

## Task Description
A utility service for people who are looking for companions to travel. The app should connect people who want to travel to the same place in the same time window.

## Folder Hierarchy
* Folder ```GetirHackathon``` contains files for the Android app.
* ```index.js``` is the HTTP server.
* ```groupSchema.js``` contains the Mongoose schema for the travel group objects.
* ```utils.js``` contains utility tools, such as distance calculation from gps coordinates and time window overlap.
* ```httpHandler.js``` contains handlers for commonly used HTTP protocols.

## REST API Documentation

* **Title:** Create a Group \
  **URL:** /createGroup \
  **Type:** POST \
  **Parameters:**
    * ```owner(String)``` Owner of the group. 
    * ```startTime(Date)``` Travel start time. 
    * ```endTime(Date)``` Travel end time. 
    * ```location([Double, Double])```  GPS coordinates of travel location. 
  
  **Response Codes:** 201 Created, 400 Bad Request, 500 Internal Server Error \
  **Remarks:** Throws 400 if the start time is later than the end time.
  
* **Title:** Join a Group \
  **URL:** /joinGroup \
  **Type:** POST \
  **Parameters:** 
    * ```_id(String)``` Group ID to be joined.
    * ```person(String)``` The person who's joining this group.
  **Response Codes:** 201 Created, 400 Bad Request, 500 Internal Server Error \
  **Remarks:** A person cannot join a group they own or one they have already joined.

* **Title:** Search for a Group \
  **URL:** /searchGroup/:maxGroups \
  **Type:** POST \
  **Parameters:** 
    * ```owner(String)``` Person who is searching for a group.
    * ```startTime(Date)``` Desired travel start time. 
    * ```endTime(Date)``` Desired travel end time. 
    * ```location([Double, Double])```  GPS coordinates of desired travel location. 
  **Response Codes:** 200 OK, 400 Bad Request, 500 Internal Server Error \
  **Remarks:** Returns the closest groups which have a time window overlap, to which the user does not own and has not joined yet. They are in the order of decreasing distance. Two utility parameters are added to the group object:
    * ```distance(Double)``` Distance to the group 
    * ```timeDifference(Double)``` Total overlapping time between you and the group's schedule.
    
* **Title:** List Groups of a Person \
  **URL:** /listGroups/:person \
  **Type:** GET \
  **Response Codes:** 200 OK, 400 Bad Request, 500 Internal Server Error \
  **Remarks:** Returns the groups in order of decreasing start time.
    * ```owns([Group])``` Groups the person owns.
    * ```participates([Group])```Groups the person participates in.

* **Title:** Post Message to a Group \
  **URL:** /postMessage \
  **Type:** POST \
  **Parameters:** 
    * ```user(String)``` User who is posting the message.
    * ```message(String)``` The message.
    * ```_id(String)``` Group ID where the message is posted.
  **Response Codes:** 201 Created, 400 Bad Request, 500 Internal Server Error \
  **Remarks:** A user can only post in a group they own or participate in.

