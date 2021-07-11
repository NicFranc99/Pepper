package com.example.pepperapp28aprile.adapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Locale;

import com.example.pepperapp28aprile.models.*;
public class EmergencyAdapter implements JsonDeserializer<Emergency> {

    private static final class Keys {
        private static final String TIMESTAMP_KEY = "tmstp";
        private static final String ROOM_KEY = "room";
        private static final String TYPE_KEY = "type";
        private static final String BED_ID_KEY = "bed_id";
    }

    private static final Gson emergencyJsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(Adapter.JSON_DATETIME_FORMAT)
            .create();

    @Override
    public Emergency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Emergency emergency;

        int type = jsonObject.get(Keys.TYPE_KEY).getAsInt();
        switch (type){
            case 0:
                Room room = emergencyJsonConverter.fromJson(jsonObject.get(Keys.ROOM_KEY), Room.class);
                jsonObject.remove(Keys.ROOM_KEY);
                jsonObject.remove(Keys.TIMESTAMP_KEY);

                emergency = emergencyJsonConverter.fromJson(jsonObject.toString(), Emergency.class);
                emergency.getEnvData().setRoom(room);
                break;
            case 1:
            case 2:
            case 3:
                int bedId = jsonObject.get(Keys.BED_ID_KEY).getAsInt();
                String bedLabel = String.format(Locale.getDefault(), "Letto %d", bedId);
                jsonObject.remove(Keys.BED_ID_KEY);

                emergency = emergencyJsonConverter.fromJson(jsonObject.toString(), Emergency.class);
                emergency.setBedLabel(bedLabel);
                break;
            default:
                emergency = null;
                break;
        }

        return emergency;

    }

}
