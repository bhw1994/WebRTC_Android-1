package com.webrtc.boyj.api.data.stun;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface TurnServer {
    @PUT("/_turn/TheBOYJ")
    Call<TurnServerPojo> getIceCandidates(@Header("Authorization") String authkey);
}
