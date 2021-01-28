package com.example.tuneup.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAv8TNNwc:APA91bGCtNZmbovkV8QfSTXtivQSbNlUdZRY5iZzxkQjnedmHSPGgPcBSyUX7a9f3vjmIr3amzYDixAcU72gvx3ic_3h6MZVK4r3gMDB4jBpROHiWZjgUweZQfb-kZ21lcxZQCpOsR6A"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
