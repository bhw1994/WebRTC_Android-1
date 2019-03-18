package com.webrtc.boyj.api.peer.manager;

import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.presentation.call.CallViewModel;

public class EventSupervisor {
    private SignalingClient signalingClient;
    private PeerConnectionClient peerConnectionClient;
    private CallViewModel callViewModel;
    public EventSupervisor(SignalingClient signalingClient , PeerConnectionClient peerConnectionClient , CallViewModel callViewModel , boolean isCaller){
        this.signalingClient = signalingClient;
        this.peerConnectionClient = peerConnectionClient ;
        this.callViewModel = callViewModel;

        peerConnectionClient.getIceCandidateSubject().subscribe(
                //signalingClient.emitIceCandidate();
        );
        peerConnectionClient.getSdpSubject().subscribe(
                //signalingClient.emitSDP();
        );
        peerConnectionClient.getRemoteMediaStreamSubject().subscribe(
                //callViewModel.setRemoteMediaStream();
        );

        if(isCaller){
        }
        else {

        }


    }
}
