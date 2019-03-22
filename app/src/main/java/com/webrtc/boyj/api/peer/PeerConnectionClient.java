package com.webrtc.boyj.api.peer;

import com.webrtc.boyj.api.peer.manager.PeerConnectionFactoryManager;

import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

import io.reactivex.subjects.PublishSubject;

public class PeerConnectionClient {


    private static PeerConnectionFactoryManager peerConnectionFactoryManager;
    private static MediaConstraints constraints = new MediaConstraints();

    static {
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        peerConnectionFactoryManager = new PeerConnectionFactoryManager();
    }
    
    private PeerConnection peer;
    private PublishSubject<SessionDescription> sdpSubject = PublishSubject.create();
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    private PublishSubject<MediaStream> remoteMediaStreamSubject = PublishSubject.create();

    public PublishSubject<SessionDescription> getSdpSubject() {
        return sdpSubject;
    }

    public PublishSubject<IceCandidate> getIceCandidateSubject() {
        return iceCandidateSubject;
    }

    public PublishSubject<MediaStream> getRemoteMediaStreamSubject() {
        return remoteMediaStreamSubject;
    }


    public void createPeerConnection() {
        peer = peerConnectionFactoryManager.createPeerConnection(new PeerConnectionObserver());
        PeerConnectionWrapper wrapper = new PeerConnectionWrapper(peer);
        wrapper.createOffer();
    }


    public void release() {
        try {
            videoCapturer.stopCapture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        peer.dispose();
        /*
        localView.release();
        remoteView.release();
        */
        peerConnectionFactoryManager.dispose();
    }

    private class PeerConnectionWrapper {
        private PeerConnection peer;

        public PeerConnectionWrapper(PeerConnection peer) {
            this.peer = peer;
        }

        private void createOffer() {
            peer.createOffer(new LocalSdpObserver(), constraints);
        }

        private void createAnswer() {
            peer.createAnswer(new LocalSdpObserver(), constraints);
        }

        public void addIceCandidate(IceCandidate iceCandidate) {
            peer.addIceCandidate(iceCandidate);
        }
    }

    private class PeerConnectionObserver extends DefaultPeerConnectionObserver {
        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            iceCandidateSubject.onNext(iceCandidate);
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            remoteMediaStreamSubject.onNext(mediaStream);
        }
    }

    private class LocalSdpObserver extends DefaultSdpObserver {
        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            peer.setLocalDescription(null, sessionDescription);
            sdpSubject.onNext(sessionDescription);
        }
    }
}
