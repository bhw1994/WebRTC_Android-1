package com.webrtc.boyj.api;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.peer.manager.PeerConnectionFactoryManager;
import com.webrtc.boyj.api.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;
import com.webrtc.boyj.utils.Logger;

import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public class BoyjRTC {
    @NonNull
    private SignalingClient signalingClient = new SignalingClient();
    private PeerConnectionClient peerConnectionClient;
    private UserMediaManager userMediaManager;
    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void initRTC() {
        final PeerConnectionFactory factory = PeerConnectionFactoryManager.createPeerConnectionFactory();
        userMediaManager = new UserMediaManager(factory);
        peerConnectionClient = new PeerConnectionClient(factory);

        compositeDisposable.addAll(
                peerConnectionClient.getSdpSubject().subscribe(sessionDescription -> {
                    final SdpPayload sdpPayload = new SdpPayload.Builder(sessionDescription).build();
                    signalingClient.emitSdp(sdpPayload);
                }),
                peerConnectionClient.getIceCandidateSubject().subscribe(iceCandidate -> {
                    final IceCandidatePayload iceCandidatePayload = new IceCandidatePayload.Builder(iceCandidate).build();
                    signalingClient.emitIceCandidate(iceCandidatePayload);
                }),
                signalingClient.getSdpSubject().subscribe(sdp -> {
                    peerConnectionClient.setRemoteSdp(sdp);
                    if (sdp.type == SessionDescription.Type.OFFER) {
                        peerConnectionClient.createAnswer();
                    }
                }),
                signalingClient.getIceCandidateSubject().subscribe(candidate -> {
                    Logger.d(candidate.toString());
                    peerConnectionClient.addIceCandidate(candidate);
                })
        );
    }

    public void readyToCall(final boolean isCaller) {
        compositeDisposable.add(signalingClient.getReadySubject().subscribe(() -> {
            peerConnectionClient.createPeerConnection();
            peerConnectionClient.addStreamToLocalPeer(getUserMedia());
            if (isCaller) {
                peerConnectionClient.createOffer();
            }
        }));
    }

    public void startCapture() {
        userMediaManager.startCapture();
    }

    public void stopCapture() {
        userMediaManager.stopCapture();
    }

    @NonNull
    public MediaStream getUserMedia() {
        return userMediaManager.getLocalMediaStream();
    }

    @NonNull
    public PublishSubject<MediaStream> remoteMediaStream() {
        return peerConnectionClient.getRemoteMediaStreamSubject();
    }

    /**
     * 처음으로 통화를 요청할 경우 room을 생성한다.
     *
     * @param payload room, callerId 가 담긴 페이로드
     */
    public void createRoom(@NonNull final CreateRoomPayload payload) {
        signalingClient.emitCreateRoom(payload);
    }

    //앱 유저로 부터 온 이벤트 처리
    public void dial(@NonNull final DialPayload dialPayload) {
        signalingClient.emitDial(dialPayload);
    }

    public void awaken(@NonNull final AwakenPayload payload) {
        signalingClient.emitAwaken(payload);
    }

    @NonNull
    public CompletableSubject knock() {
        return signalingClient.getKnockSubject();
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
    }

    @NonNull
    public CompletableSubject bye() {
        return signalingClient.getByeSubject();
    }

    public void release() {
        userMediaManager.stopCapture();
        compositeDisposable.dispose();
        signalingClient.disconnect();
        peerConnectionClient.dispose();
    }


}
