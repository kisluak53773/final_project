package com.example.webproject.dao;

public class ColumnName {
    public ColumnName() {
    }

    /*Users*/
    public static final String ID_USER="iduser";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    public static final String USER_ROLE="user_role";
    public static final String IS_DELETED="is_deleted";
    public static final String PHONE="phone";

    /*Services*/
    public static final String ID_SERVICE="idservice";
    public static final String PRICE="price";
    public static final String SERVICE_NAME="service_name";
    public static final String SERVICE_DESCRIPTION="service_description";
    public static final String SERVICE_IMAGE="service_image";

    /*Feedbacks*/
    public static final String ID_FEEDBACK="idfeedback";
    public static final String COMMENT="comment";
    public static final String RATING="rating";

    /*Ordered services*/
    public static final String ID_ORDERED_SERVICE="idordered_service";
    public static final String TIME_ASSIGNED="time_assigned";
    public static final String TIME_REQUESTED="time_requested";

    /*Orders*/
    public static final String ID_ORDER="idorder";
    public static final String ORDER_STATE="order_state";
}
