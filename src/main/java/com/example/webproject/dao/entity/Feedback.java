package com.example.webproject.dao.entity;

import java.util.Objects;

public class Feedback extends BaseEntity{
    private OrderedService orderedService;
    private String comment;
    private Rating rating;

    public Feedback(int id, OrderedService orderedService, String comment, Rating rating) {
        super(id);
        this.orderedService = orderedService;
        this.comment = comment;
        this.rating = rating;
    }

    public OrderedService getOrderedService() {
        return orderedService;
    }

    public void setOrderedService(OrderedService orderedService) {
        this.orderedService = orderedService;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(orderedService, feedback.orderedService) && Objects.equals(comment, feedback.comment) && rating == feedback.rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderedService, comment, rating);
    }

    public enum Rating{
        TERRIBLE,
        NORMAL,
        GOOD
    }

    public static FeedbackBuilder getBuilder(){
        return new FeedbackBuilder();
    }

    public static class FeedbackBuilder{
        private int builtId=UNDEFINED_ID;
        private OrderedService builtOrderedService;
        private String builtComment;
        private Rating builtRating;

        public FeedbackBuilder id(int id){
            builtId=id;
            return this;
        }

        public FeedbackBuilder orderedService(OrderedService orderedService){
            builtOrderedService=orderedService;
            return this;
        }

        public FeedbackBuilder comment(String comment){
            builtComment=comment;
            return this;
        }

        public FeedbackBuilder rating(Rating rating){
            builtRating=rating;
            return this;
        }

        public Feedback build(){
            return new Feedback(builtId,builtOrderedService,builtComment,builtRating);
        }
    }
}
