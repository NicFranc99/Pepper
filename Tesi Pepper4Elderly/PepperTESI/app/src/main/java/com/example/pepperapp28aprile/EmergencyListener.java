package com.example.pepperapp28aprile;

import com.example.pepperapp28aprile.models.Emergency;

public interface EmergencyListener {

    void onNewEmergency(Emergency emergency);

    void onEmergencyHandled();
}
