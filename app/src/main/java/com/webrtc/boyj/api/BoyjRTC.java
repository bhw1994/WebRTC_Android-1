package com.webrtc.boyj.api;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;

import org.webrtc.MediaStream;
import org.webrtc.SessionDescription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public class BoyjRTC {
    @NonNull
    private final static SignalingClient signalingClient;
    @NonNull
    private final static PeerConnectionClient peerConnectionClient;
    @NonNull
    private final static UserMediaManager userMediaManager;
    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    static {
        signalingClient = new SignalingClient();
        peerConnectionClient = new PeerConnectionClient();
        userMediaManager = new UserMediaManager();
    }

    public BoyjRTC() {
    }

    public void release() {
        signalingClient.disconnect();
        peerConnectionClient.release();
        userMediaManager.stopCapture();
        compositeDisposable.dispose();
    }

    public void startCapture() {
        userMediaManager.startCapture();
    }


    public void stopCapture() {
        userMediaManager.stopCapture();
    }

    public MediaStream getUserMedia() {
        return userMediaManager.getUserMedia();
    }

    private void initRTC() {
        compositeDisposable.addAll(
                peerConnectionClient.getSdpSubject().subscribe(sessionDescription -> {
                    final SdpPayload sdpPayload = new SdpPayload.Builder(sessionDescription).build();
                    signalingClient.emitSdp(sdpPayload);
                }),
                peerConnectionClient.getIceCandidateSubject().subscribe(iceCandidate -> {
                    IceCandidatePayload iceCandidatePayload = new IceCandidatePayload.Builder(iceCandidate).build();
                    signalingClient.emitIceCandidate(iceCandidatePayload);
                }),
                peerConnectionClient.getRemoteMediaStreamSubject().subscribe(mediaStream -> {
                }),
                signalingClient.getSdpSubject().subscribe(sdp -> {
                    peerConnectionClient.setRemoteSdp(sdp);
                    if (sdp.type == SessionDescription.Type.OFFER) {
                        peerConnectionClient.createAnswer();
                    }
                }),
                signalingClient.getIceCandidateSubject().subscribe(peerConnectionClient::addIceCandidate)
        );
    }

    public void attachCalleeListener() {
        compositeDisposable.add(signalingClient.getReadySubject().subscribe(() -> {
            initRTC();
            peerConnectionClient.createPeerConnection();
            peerConnectionClient.addStreamToLocalPeer(getUserMedia());
        }));
    }

    public void attachCallerListener() {
        compositeDisposable.add(
                signalingClient.getReadySubject().subscribe(() -> {
                    initRTC();
                    peerConnectionClient.createPeerConnection();
                    peerConnectionClient.addStreamToLocalPeer(getUserMedia());
                    peerConnectionClient.createOffer();
                }));
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
        release();
        signalingClient.disconnect();
    }

    public void awaken(@NonNull final AwakenPayload payload) {
        signalingClient.emitAwaken(payload);
    }

    @NonNull
    public CompletableSubject knock() {
        return signalingClient.getKnockSubject();
    }

    public PublishSubject<MediaStream> remoteMediaStream() {
        return peerConnectionClient.getRemoteMediaStreamSubject();
    }
}
