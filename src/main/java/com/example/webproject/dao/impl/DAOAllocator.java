package com.example.webproject.dao.impl;

public class DAOAllocator {
    private static final UserDAOImpl userDao;
    private static final FeedbackDAOImpl feedbackDao;
    private static final OrderDAOImpl orderDao;
    private static final ServiceDAOImpl serviceDao;
    private static final OrderedServiceDAOImpl orderedServiceDao;

    static {
        userDao = new UserDAOImpl();
        feedbackDao=new FeedbackDAOImpl();
        orderDao=new OrderDAOImpl();
        serviceDao=new ServiceDAOImpl();
        orderedServiceDao=new OrderedServiceDAOImpl();
    }

    public static UserDAOImpl getUserDao() {
        return userDao;
    }

    public static ServiceDAOImpl getServiceDao(){
        return serviceDao;
    }

    public static FeedbackDAOImpl getFeedbackDao(){
        return feedbackDao;
    }

    public static OrderDAOImpl getOrderDao(){
        return orderDao;
    }

    public static OrderedServiceDAOImpl getOrderedServiceDao(){
        return orderedServiceDao;
    }
}
