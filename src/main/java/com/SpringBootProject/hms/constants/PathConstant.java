package com.SpringBootProject.hms.constants;

public class PathConstant {
    public static final String LOGIN = "/login";
    public static final String REGISTRATION = "/registration";
//    ----------------------USERS--------------------------------------------
    public static final String GET_ALL_USERS = "/getUsers";
    public static final String GET_USER_BY_ID = "/getUser/{id}";
    public static final String GET_USER_BY_NAME = "/getUser/name/{name}";
    public static final String UPDATE_USER = "/updateUser/{id}";

//    ----------------------ROOM--------------------------------------------
    public static final String GET_ALL_ROOM = "/getRooms";
    public static final String GET_ROOM_BY_ID = "/getRoom/{id}";
    public static final String GET_ROOM_BY_TYPE = "/getRooms/{type}";
    public static final String UPDATE_ROOM = "/updateRoom/{id}";
    public static final String ADD_ROOM = "/addRoom";
//    ----------------------RESERVATION--------------------------------------------
    public static final String GET_ALL_RESERVATION = "/getReservation";
    public static final String GET_RESERVATION_OF_USER = "/getReservation/user/{name}";
    public static final String GET_RESERVATION_OF_TODAY = "/todayReservation";
    public static final String ADD_RESERVATION = "/addReservation";
    public static final String GET_RESERVATION_BY_ID = "/getReservation/{id}";
    public static final String UPDATE_RESERVATION = "/updateReservation/{id}";
//    ----------------------Role--------------------------------------------
    public static final String GET_ALL_ROLES = "/getRoles";
    public static final String GET_ROLE_BY_NAME = "/getRoles/{name}";
    public static final String ADD_ROLE = "/addRole";
    public static final String UPDATE_ROLE = "/updateRole/{id}";
    public static final String ADD_ROLE_TO_USER = "/addRoleToUser/{id}";
}
