package com.webrtc.boyj.api;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;

import org.webrtc.MediaStream;

import io.reactivex.subjects.CompletableSubject;

public class BoyjRTC {
    @NonNull
    private final static SignalingClient signalingClient;
    private final static PeerConnectionClient peerConnectionClient;
    private final static UserMediaManager userMediaManager;

    static {
        signalingClient = new SignalingClient();
        peerConnectionClient = new PeerConnectionClient();
        userMediaManager = new UserMediaManager();
    }

    public BoyjRTC() {
    }

    //앱 유저로 부터 온 이벤트 처리
    public void dial(@NonNull final DialPayload dialPayload) {
        signalingClient.emitDial(dialPayload);
    }

    public void accept() {
        signalingClient.emitAccept();
    }

    public void reject() {
        signalingClient.emitReject();
    }

    public void hangUp() {
        signalingClient.emitBye();
        signalingClient.disconnect();
    }

    public void awaken(@NonNull final AwakenPayload payload) {
        signalingClient.emitAwaken(payload);
    }

    @NonNull
    public CompletableSubject knock() {
        return signalingClient.getKnockSubject();
    }

    @NonNull
    public MediaStream getUserMedia() {
        return userMediaManager.getUserMedia();
    }

    public void startCapture() {
        userMediaManager.startCapture();
    }

    public void stopCapture() {
        userMediaManager.stopCapture();
    }

    public void release() {
        signalingClient.disconnect();
        peerConnectionClient.release();
        userMediaManager.stopCapture();
    }
}
