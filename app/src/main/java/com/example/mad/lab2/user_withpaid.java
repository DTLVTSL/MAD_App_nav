package com.example.mad.lab2;

/**
 * Created by root on 11/05/17.
 */

public class user_withpaid {

    public User user;
    public boolean paid;

    public user_withpaid(){
        super();
    }

    public user_withpaid( User user, boolean paid){
        super();
        this.user=user;
        this.paid=paid;
    }


    public void setUser( User d){this.user=d;}
    public User getUser(){return user;}

    public void setPaid(boolean p){this.paid=p;}
    public boolean getPaid(){return this.paid;}


}
