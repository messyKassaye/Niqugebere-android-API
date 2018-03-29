package com.example.meseret.niqugebere.profile.cfsc.cfscModel;

/**
 * Created by meseret on 2/25/18.
 */

public class Notifications {
    private int notification_category_id;
    private int notifable_id;

    public Notifications() {
    }

    public Notifications(int notification_category_id, int notifable_id) {
        this.notification_category_id = notification_category_id;
        this.notifable_id = notifable_id;
    }

    public int getNotification_category_id() {
        return notification_category_id;
    }

    public void setNotification_category_id(int notification_category_id) {
        this.notification_category_id = notification_category_id;
    }

    public int getNotifable_id() {
        return notifable_id;
    }

    public void setNotifable_id(int notifable_id) {
        this.notifable_id = notifable_id;
    }
}
