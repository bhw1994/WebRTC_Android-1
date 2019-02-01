package com.webrtc.boyj.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.webrtc.boyj.api.data.stun.IceServer;
import com.webrtc.boyj.api.data.stun.TurnServerPojo;
import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.SignalingInterface;
import com.webrtc.boyj.model.dto.User;
import com.webrtc.boyj.utils.Logger;
import com.webrtc.boyj.utils.Util;

import org.json.JSONObject;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.SurfaceViewRenderer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallViewModel extends BaseViewModel {
    private ObservableBoolean isCalling = new ObservableBoolean();
    private MutableLiveData<Boolean> _isActive = new MutableLiveData<>();
    private LiveData<Boolean> isActive = _isActive;



    public CallViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        // do nothing

    }

    public void call(String deviceToken){
        PeerConnectionClient peerConnectionClient=PeerConnectionClient.getInstance();
        SignalingClient signalingClient=SignalingClient.getInstance();

        peerConnectionClient.sdpSubject.subscribe(sessionDescription -> {
            Logger.d("create offer success");
            signalingClient.emitSsdp(sessionDescription);});

        peerConnectionClient.iceCandidateSubject.subscribe(iceCandidate -> {
            Logger.d("ice create");
            signalingClient.emitSice(iceCandidate);});

        signalingClient.rsdpEventSubject.subscribe(jsonObject -> {
            Logger.signalingEvent(SignalingInterface.EVENT_RECEIVE_SDP);
            peerConnectionClient.setRemoteDescription(false,jsonObject);});

        signalingClient.riceEventSubject.subscribe(jsonObject -> {
            Logger.signalingEvent(SignalingInterface.EVENT_RECEIVE_ICE);
            peerConnectionClient.addIceCandidate(jsonObject);});

        signalingClient.createdEventSubject.subscribe(s -> {
            Logger.signalingEvent(SignalingInterface.EVENT_CREATED);
        });

        signalingClient.readyEventSubject.subscribe(signalingClient1 -> {
            Logger.signalingEvent(SignalingInterface.EVENT_READY);
            peerConnectionClient.createPeerConnection();
            peerConnectionClient.createOffer();
        });
        signalingClient.errorEventSubject.subscribe(jsonObject -> {
            Logger.signalingEvent(SignalingInterface.EVENT_SERVER_ERROR+" "+jsonObject.toString());
        });

        signalingClient.emitDial(deviceToken);
    }
    public void accept(String room){
        PeerConnectionClient peerConnectionClient=PeerConnectionClient.getInstance();
        SignalingClient signalingClient=SignalingClient.getInstance();

        peerConnectionClient.sdpSubject.subscribe(sessionDescription -> {
            Logger.d("create answer sucessed");
            signalingClient.emitSsdp(sessionDescription);});

        peerConnectionClient.iceCandidateSubject.subscribe(iceCandidate -> {
            Logger.d("ice create");
            signalingClient.emitSice(iceCandidate);});

        signalingClient.rsdpEventSubject.subscribe(jsonObject -> {
            Logger.signalingEvent(SignalingInterface.EVENT_RECEIVE_SDP);
            peerConnectionClient.createPeerConnection();
            peerConnectionClient.setRemoteDescription(true,jsonObject);
            peerConnectionClient.createAnswer();
        });

        signalingClient.riceEventSubject.subscribe(jsonObject -> {
            Logger.signalingEvent(SignalingInterface.EVENT_RECEIVE_ICE);
            peerConnectionClient.addIceCandidate(jsonObject);});

        signalingClient.errorEventSubject.subscribe(jsonObject -> {
            Logger.signalingEvent(SignalingInterface.EVENT_SERVER_ERROR+" "+jsonObject.toString());
        });

        signalingClient.readyEventSubject.subscribe(signalingClient1 -> {
            Logger.signalingEvent(SignalingInterface.EVENT_READY);
        });

        signalingClient.knockEventSubject.subscribe(s -> {
            signalingClient.emitAccept();
            Logger.signalingEvent(SignalingInterface.EVENT_ACCEPT);
        });

        signalingClient.emitAwaken(room);
    }

    public void onHangUp() {
        _isActive.setValue(false);
    }

    public ObservableBoolean getIsCalling() {
        return isCalling;
    }

    public LiveData<Boolean> getIsActive() {
        return isActive;
    }



}
