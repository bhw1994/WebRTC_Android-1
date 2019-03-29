package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

public class SdpPayload extends Payload {
    @NonNull
    private SessionDescription sdp;

    public void setSdp(@NonNull final SessionDescription sdp) {
        this.sdp = sdp;
    }

    @NonNull
    public static SessionDescription fromJson(String jsonString){
        Gson gson = new Gson();
        return gson.fromJson(jsonString , SessionDescription.class);
    }

    public static class Builder {
        @NonNull
        private final SessionDescription sdp;

        public Builder(@NonNull final SessionDescription sdp) {
            this.sdp = sdp;
        }

        @NonNull
        public SdpPayload build() {
            final SdpPayload sdpPayload = new SdpPayload();
            sdpPayload.setSdp(this.sdp);
            return sdpPayload;
        }
    }


}



