package com.blessapp.blessapp.Model;

public class Favourite {

    String fav_id, pid, fav_title, fav_price, fav_img, userid, fav_description, fav_time, fav_date;

    public Favourite(){

    }

    public Favourite(String fav_id, String pid, String fav_title, String fav_price, String fav_img, String userid, String fav_description, String fav_time, String fav_date) {
        this.fav_id = fav_id;
        this.pid = pid;
        this.fav_title = fav_title;
        this.fav_price = fav_price;
        this.fav_img = fav_img;
        this.userid = userid;
        this.fav_description = fav_description;
        this.fav_time = fav_time;
        this.fav_date = fav_date;
    }

    public String getFav_id() {
        return fav_id;
    }

    public void setFav_id(String fav_id) {
        this.fav_id = fav_id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFav_title() {
        return fav_title;
    }

    public void setFav_title(String fav_title) {
        this.fav_title = fav_title;
    }

    public String getFav_price() {
        return fav_price;
    }

    public void setFav_price(String fav_price) {
        this.fav_price = fav_price;
    }

    public String getFav_img() {
        return fav_img;
    }

    public void setFav_img(String fav_img) {
        this.fav_img = fav_img;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFav_description() {
        return fav_description;
    }

    public void setFav_description(String fav_description) {
        this.fav_description = fav_description;
    }

    public String getFav_time() {
        return fav_time;
    }

    public void setFav_time(String fav_time) {
        this.fav_time = fav_time;
    }

    public String getFav_date() {
        return fav_date;
    }

    public void setFav_date(String fav_date) {
        this.fav_date = fav_date;
    }
}
