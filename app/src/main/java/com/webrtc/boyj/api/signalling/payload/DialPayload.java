package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;


public class DialPayload extends Payload {

    @NonNull
    private String deviceToken;

    public void setDeviceToken(@NonNull final String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @NonNull
    public String getDeviceToken() {
        return deviceToken;
    }

    public static class Builder {

        @NonNull
        private final String deviceToken;

        public Builder(@NonNull final String deviceToken) {
            this.deviceToken = deviceToken;
        }

        @NonNull
        public DialPayload build() {
            final DialPayload payload = new DialPayload();
            payload.setDeviceToken(this.deviceToken);
            return payload;
        }
    }
}
