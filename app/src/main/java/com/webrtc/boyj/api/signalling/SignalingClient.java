package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public class SignalingClient {

    @NonNull
    private static final SocketIOClient socketIOClient;

    static {
        socketIOClient = new SocketIOClient();
    }

    private CompletableSubject knockSubject = CompletableSubject.create();
    private CompletableSubject readySubject = CompletableSubject.create();
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    private PublishSubject<SessionDescription> sdpSubject = PublishSubject.create();


    public PublishSubject<IceCandidate> getIceCandidateSubject() {
        return iceCandidateSubject;
    }

    public PublishSubject<SessionDescription> getSdpSubject() {
        return sdpSubject;
    }

    public SignalingClient() {
        Gson gson = new Gson();

        socketIOClient.on(SignalingEventString.EVENT_KNOCK, args -> knockSubject.onComplete());
        socketIOClient.on(SignalingEventString.EVENT_READY, args -> readySubject.onComplete());

        socketIOClient.on(SignalingEventString.EVENT_RECEIVE_SDP, args -> {
            SessionDescription sdp = SdpPayload.fromJson((String) args[0]);
            sdpSubject.onNext(sdp);
        });
        socketIOClient.on(SignalingEventString.EVENT_RECEIVE_ICE, args -> {
            IceCandidate iceCandidate = IceCandidatePayload.fromJson((String) args[0]);
            iceCandidateSubject.onNext(iceCandidate);
        });
        socketIOClient.connect();
    }

    public void emitDial(@NonNull final DialPayload dialPayload) {
        socketIOClient.emit(SignalingEventString.EVENT_DIAL, dialPayload.toJson());
    }

    public void emitAwaken(@NonNull final AwakenPayload awakenPayload) {
        socketIOClient.emit(SignalingEventString.EVENT_AWAKEN, awakenPayload.toJson());
    }

    public void emitAccept() {
        socketIOClient.emit(SignalingEventString.EVENT_ACCEPT);
    }

    public void emitReject() {
        socketIOClient.emit(SignalingEventString.EVENT_REJECT);
    }

    public void emitSdp(@NonNull final SdpPayload sdpPayload) {
        socketIOClient.emit(SignalingEventString.EVENT_SEND_SDP, sdpPayload.toJson());
    }

    public void emitIceCandidate(@NonNull final IceCandidatePayload iceCandidatePayload) {
        socketIOClient.emit(SignalingEventString.EVENT_SEND_ICE, iceCandidatePayload.toJson());
    }

    public void emitBye() {
        socketIOClient.emit(SignalingEventString.EVENT_BYE);
    }

    public void disconnect() {
        socketIOClient.disconnect();
    }

    public CompletableSubject getKnockSubject() {
        return knockSubject;
    }

    public CompletableSubject getReadySubject() {
        return readySubject;
    }


}
