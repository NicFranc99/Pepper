package com.example.parenteapp.data;

import android.os.StrictMode;

import com.example.parenteapp.Globals;
import com.example.parenteapp.data.model.LoggedInUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        try {
            // TODO: handle loggedInUser authentication
            String sURL = "https://bettercallpepper.altervista.org/api/validUser.php?username=" + username + "&password=" + password;
            System.out.println(sURL);
            String fullname = "";
            // Connect to the URL using java's native library
            URL url = null;
            try {
                url = new URL(sURL);
                URLConnection request = url.openConnection();
                request.connect();

                // Convert to a JSON object to print data
                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootelem = root.getAsJsonObject();
                fullname = rootelem.get("fullname").getAsString();
                Globals.myAppID = rootelem.get("id").getAsInt();

            } catch (Exception e) {
                e.printStackTrace();
                return new Result.Error(new IOException("Error logging in", e));
            }

            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            fullname);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}