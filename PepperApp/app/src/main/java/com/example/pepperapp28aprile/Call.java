package com.example.pepperapp28aprile;

public class Call {
    private int calltime;
    private int type;       //0 ingoing - 1 outgoing - 2 missed
    private String callmsg;
    private String image;

    public Call(int type, String callmsg, String image)
    {
        this.type = type;
        this.callmsg = callmsg;
        this.image = image;
        calltime = 0;
    }

    public Call(int type, String callmsg, String image, int calltime)
    {
        this.type = type;
        this.callmsg = callmsg;
        this.image = image;
        this.calltime = calltime;
    }

    public int getType()
    {
        return type;
    }

    public String getImage()
    {
        return image;
    }

    public int getCalltime()
    {
        return calltime;
    }

    public String getFancyCallTime()
    {
        int seconds = calltime % 60;
        int hours = calltime / 60;
        int minutes = hours % 60;
        hours = hours / 60;
        return hours + ":" + minutes + ":" + seconds;

    }

    public String getCallmsg()
    {
        return callmsg;
    }

    public void setCalltime(int calltime) {
        this.calltime = calltime;
    }

    public void setType(int type) {
        this.type = type;
    }
}
