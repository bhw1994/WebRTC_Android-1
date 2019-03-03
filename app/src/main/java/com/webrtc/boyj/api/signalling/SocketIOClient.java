package com.webrtc.boyj.api.signalling;

import org.json.JSONObject;

import io.reactivex.subjects.PublishSubject;
import io.socket.client.Socket;

public class SocketIOClient {

    private PublishSubject<String> readyEventSubject = PublishSubject.create();
    private PublishSubject<String> knockEventSubject = PublishSubject.create();

    public PublishSubject<String> getOfferEventSubject() {
        return offerEventSubject;
    }

    private PublishSubject<String> offerEventSubject = PublishSubject.create();

    public SocketIOClient() {

    }


    public PublishSubject<String> getReadyEventSubject() {
        return readyEventSubject;
    }

    public PublishSubject<String> getKnockEventSubject() {
        return knockEventSubject;
    }


}
