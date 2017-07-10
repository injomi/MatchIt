package com.example.matchit.app;

public class AppConfig {
    public static String HOST  =  "172.27.139.71";
    // Server user login url
    public static String URL_LOGIN = "http://" + HOST + "/matchit/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://" + HOST + "/matchit/register.php";

    //Event probe
    public static String URL_EVENTS = "http://" + HOST + "/matchit/events.php";

    //Update Profile
    public static String URL_UPDATE = "http://" + HOST + "/matchit/profile.php";

    public static String URL_ORGINFO = "http://" + HOST + "/matchit/include/DB_Functions.php";

    public static String URL_USEREVENTS = "http://" + HOST + "/matchit/userEvents.php";

    public static String URL_AVAILABILITY = "http://" + HOST + "/matchit/availability.php";

}
