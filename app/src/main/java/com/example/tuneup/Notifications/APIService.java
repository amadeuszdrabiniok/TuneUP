package com.example.tuneup.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAv8TNNwc:APA91bGnduM0zG4RUZdgGvSoVF6g7fkKgLgKnaNo1Y4bVxmQs-dulRc-J3ucYjxnqy8kk2kh8Wlq4k3UaZ2guz36tBjFvVEM7q9Su39Hq9ybKokwclfTNJ6Sw6dluN_ymVo6ORqEPMEp"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
