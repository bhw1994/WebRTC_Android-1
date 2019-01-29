package com.webrtc.boyj.api.peer;

import android.util.Log;

import com.webrtc.boyj.utils.Logger;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpTransceiver;

public class CustomPeerConnectionObserver implements PeerConnection.Observer {
    private String logTag = this.getClass().getCanonicalName();

    public CustomPeerConnectionObserver(String logTag) {
        this.logTag = this.logTag+" "+logTag;
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        Logger.d("CustomPeerConnectionObserver : CustomPeerConnectionObserver");
        //Log.d(logTag, "onSignalingChange() called with: signalingState = [" + signalingState + "]");
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        Logger.d("CustomPeerConnectionObserver : onIceConnectionChange");
        //Log.d(logTag, "onIceConnectionChange() called with: iceConnectionState = [" + iceConnectionState + "]");
    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {
        Logger.d("CustomPeerConnectionObserver : onIceConnectionReceivingChange");
        //Log.d(logTag, "onIceConnectionReceivingChange() called with: b = [" + b + "]");
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        Logger.d("CustomPeerConnectionObserver : onIceGatheringChange");
        //Log.d(logTag, "onIceGatheringChange() called with: iceGatheringState = [" + iceGatheringState + "]");
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        Logger.d("CustomPeerConnectionObserver : onIceCandidate");
        //Log.d(logTag, "onIceCandidate() called with: iceCandidate = [" + iceCandidate + "]");
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
        Logger.d("CustomPeerConnectionObserver : onIceCandidatesRemoved");
        //Log.d(logTag, "onIceCandidatesRemoved() called with: iceCandidates = [" + iceCandidates + "]");
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        Logger.d("CustomPeerConnectionObserver : onAddStream");
        //Log.d(logTag, "onAddStream() called with: mediaStream = [" + mediaStream + "]");
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        Logger.d("CustomPeerConnectionObserver : onRemoveStream");
        //Log.d(logTag, "onRemoveStream() called with: mediaStream = [" + mediaStream + "]");
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        Logger.d("CustomPeerConnectionObserver : onDataChannel");
        //Log.d(logTag, "onDataChannel() called with: dataChannel = [" + dataChannel + "]");
    }

    @Override
    public void onRenegotiationNeeded() {
        Logger.d("CustomPeerConnectionObserver : onRenegotiationNeeded");

        //Log.d(logTag, "onRenegotiationNeeded() called");
    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
        Logger.d("CustomPeerConnectionObserver : onAddTrack");
    }

    @Override
    public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
        Logger.d("CustomPeerConnectionObserver : onConnectionChange");
    }

    @Override
    public void onTrack(RtpTransceiver transceiver) {
        Logger.d("CustomPeerConnectionObserver : onTrack");
    }
}
