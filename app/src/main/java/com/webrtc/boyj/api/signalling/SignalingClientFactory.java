package com.webrtc.boyj.api.signalling;

public class SignalingClientFactory {
    public static MySignalingClient getSignalingClient(){
        return new MySignalingClient(new SocketIOClient());
    }
}
