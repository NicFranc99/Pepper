package com.example.parenteapp.ui.main;

import com.example.parenteapp.R;

public class CallBackup {
    private Persona in;
    private Persona out;
    private int calltime;
    private int type;       //0 ingoing - 1 outgoing - 2 missed

    public CallBackup(Persona in, Persona out, int type)
    {
        this.in = in;
        this.out = out;
        this.type = type;
        calltime = 0;
    }

    public Persona getIn()
    {
        return in;
    }

    public Persona getOut()
    {
        return out;
    }

    public int getType()
    {
        return type;
    }

    public int getImage()
    {
        if(type == 0)
            return R.drawable.ingoing;
        if(type == 1)
            return R.drawable.outgoing;
        if(type == 2)
            return R.drawable.missed;
        return 0;
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

    public void setCalltime(int calltime) {
        this.calltime = calltime;
    }

    public void setType(int type) {
        this.type = type;
    }
}
