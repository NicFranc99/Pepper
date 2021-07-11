package com.example.parenteapp.ui.main;

import com.example.parenteapp.R;

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
        String fulltime;
        hours = hours / 60;
        if(hours < 10)
            fulltime = "0" + hours + " : ";
        else
            fulltime = hours + " : ";
        if(minutes < 10)
            fulltime += "0" + minutes + " : ";
        else
            fulltime += minutes + " : ";
        if(seconds < 10)
            fulltime += "0" + seconds;
        else
            fulltime += seconds;
        return fulltime;
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
