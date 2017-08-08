package com.example.matchit.app;

public class AppConfig {
    public static String HOST  =  "192.168.0.18";
    // Server user login url
    public static String URL_LOGIN = "http://" + HOST + "/matchit/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://" + HOST + "/matchit/register.php";

    //Event probe
    public static String URL_EVENTS = "http://" + HOST + "/matchit/events.php";

    //Update Individual Profile
    public static String URL_UPDATE = "http://" + HOST + "/matchit/profile.php";

    //update Corporate profile
    public static String URL_UPDATE2 = "http://" + HOST + "/matchit/profile2.php";

    //get orgnisation info
    public static String URL_ORGINFO = "http://" + HOST + "/matchit/orgInfo.php";

    //get user registered events
    public static String URL_USEREVENTS = "http://" + HOST + "/matchit/userEvents.php";

    //get user availability
    public static String URL_AVAILABILITY = "http://" + HOST + "/matchit/availability.php";

    //update user attendance
    public static String UPDATE_ATTENDANCE = "http://" + HOST + "/matchit/attendance.php";

    //update user feedback
    public static String UPDATE_FEEDBACK = "http://" + HOST + "/matchit/feedback.php";
}
