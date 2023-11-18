package com.example.telebeetle.cometchatapi;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CometChatApiRest {

    String appID = "24272539e9e89a98";
    String apiKey = "9ab4636d565de4ad3ba6e57e3d361608b9d33986";
    public void crearGrupoEventoCometChat(String evento_uid, String group_name, String delegado_act_uid, String group_description) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"type\":\"private\",\"metadata\":{\"newKey\":\"New Value\"},\"guid\":\"" + evento_uid + "\",\"name\":\"" + group_name + "\",\"description\":\"" +group_description + "\",\"owner\":\"" + delegado_act_uid+"\"}");
        Request request = new Request.Builder()
                .url("https://" + appID + ".api-us.cometchat.io/v3/groups")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("apikey", apiKey)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response)
                    throws IOException {
                String res = response.body().string();
            }

            public void onFailure(Call call, IOException e) {
                Log.d("cometchat-msg-test", "Algo paso al crear el grupo de evento en cometchat");
            }
        });


    }

    public void addMemberToGroupEventCometChat(String evento_uid, String participante_array_strings) {

        String evento_uidLowerCase = evento_uid.toLowerCase();
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String content = "{\"participants\":" + participante_array_strings + "}";
        Log.d("msg_test",content);
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url("https://"+ appID +".api-us.cometchat.io/v3/groups/" + evento_uidLowerCase + "/members")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("apikey", apiKey)
                .build();

        Log.d("msg_test","INSIDE addUsertoEventoGroupCometChat");
        Log.d("msg_test",evento_uid);
        Log.d("msg_test",evento_uidLowerCase);
        Log.d("msg_test",participante_array_strings);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response)
                    throws IOException {
                String res = response.body().string();
                Log.d("msg_test",res);
            }

            public void onFailure(Call call, IOException e) {
                Log.d("cometchat-msg-test", "Algo paso al aniadir el usuario al grupo evento en cometchat");
            }
        });

    }

    public void removeMemberFromGroupEventCometChat(String evento_uid, String kick_user_uid) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://"+ appID +".api-us.cometchat.io/v3/groups/"+evento_uid+"/members/"+ kick_user_uid)
                .delete(null)
                .addHeader("accept", "application/json")
                .addHeader("apikey", apiKey)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response)
                    throws IOException {
                String res = response.body().string();
            }

            public void onFailure(Call call, IOException e) {
                Log.d("cometchat-msg-test", "Algo paso al remover el usuario del grupo evento en cometchat");
            }
        });

    }

    public void createUserinCometChat(String user_uid, String user_name) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"uid\":\""+ user_uid + "\",\"name\":\"" + user_name + "\",\"withAuthToken\":true}");
        Request request = new Request.Builder()
                .url("https://"+ appID +".api-us.cometchat.io/v3/users")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("apikey", apiKey)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response)
                    throws IOException {
                String res = response.body().string();
            }

            public void onFailure(Call call, IOException e) {
                Log.d("cometchat-msg-test", "Algo paso al crear el usuario en cometchat");
            }
        });
    }


}
