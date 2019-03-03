package com.webrtc.boyj.api.signalling;

import com.webrtc.boyj.api.firebase.MyFirebaseMessagingService;

public class MySignalingClient {

    private SocketIOClient socket;

    public MySignalingClient(SocketIOClient socket) {
        this.socket = socket;
    }

    public void setCaller(){
        emitDial();
        socket.getReadyEventSubject().subscribe(s -> emitOffer());
        socket.getOfferEventSubject().subscribe(s -> emitAnswer());
    }

    public void setCallee(){
        MyFirebaseMessagingService.getFCMSubject().subscribe(o -> emitAwaken());
        socket.getOfferEventSubject().subscribe(s -> emitAnswer());

    }
    public void emitDial(){}

    public void emitAwaken() {}

    public void emitAcppect() {}

    public void emitReject() {}

    public void emitIceCandidate() {}

    public void emitOffer() {}

    public void emitAnswer() {}
}
