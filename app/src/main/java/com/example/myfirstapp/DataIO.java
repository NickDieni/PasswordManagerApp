package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataIO {

    String url = "http://10.131.202.224:8080/api/accounts";
    RequestQueue queue;
    private SharedPreferences prefs;
    private Gson gson;
    private static DataIO instance;
    private List<Account> accounts;

    private DataIO(Context context) {
        prefs = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        accounts = new ArrayList<>();
        gson = new Gson();
        queue = Volley.newRequestQueue(context);
        load();
        getAccountFromApi();
    }

    public void getAccountFromApi(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response-> {
                    try {
                        Log.d("Volley", "getAccountsFromApi: Success! " + response.toString());
                        if (response != null) {
                            Type type = new TypeToken<List<Account>>() {
                            }.getType();
                            accounts = gson.fromJson(response.toString(), type);

                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d("Volley", "getAccountsFromApi: Failed!");
                }
        );
        queue.add(jsonObjectRequest);

    }


    private void save(){
        String json = gson.toJson(accounts);
        prefs.edit().putString("Accounts",json).apply();
    }

    private void load(){
        String json = prefs.getString("Accounts",null);
        if(json != null){
            Type type = new TypeToken<List<Account>>(){}.getType();
            accounts = gson.fromJson(json, type);
        }
        else{save();}
    }

    public void addAccount(Account account){
        accounts.add(account);
        save();
    }

    public static DataIO getInstance(Context context) {
        if (instance == null)  instance = new DataIO(context);
        return instance;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
