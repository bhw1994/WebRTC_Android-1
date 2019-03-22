package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;

import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;

public class SignalingClient {

    @NonNull
    private static final SocketIOClient storedSocketIOClient;

    static {
        storedSocketIOClient = new SocketIOClient();
    }

    public void emitDial(@NonNull final DialPayload dialPayload) {
        storedSocketIOClient.emit(SignalingEventString.EVENT_DIAL, dialPayload);
    }

    public void emitAwaken(@NonNull final AwakenPayload awakenPayload) {
        storedSocketIOClient.emit(SignalingEventString.EVENT_AWAKEN, awakenPayload);
    }

    public void emitAccept() {
        storedSocketIOClient.emit(SignalingEventString.EVENT_ACCEPT);
    }

    public void emitReject() {
        storedSocketIOClient.emit(SignalingEventString.EVENT_REJECT);
    }

    public void emitBye() {
        storedSocketIOClient.emit(SignalingEventString.EVENT_BYE);
    }
}
