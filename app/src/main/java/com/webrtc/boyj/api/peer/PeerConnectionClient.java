package com.webrtc.boyj.api.peer;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.webrtc.boyj.api.peer.manager.EglBaseManager;
import com.webrtc.boyj.api.peer.manager.VideoManager;
import com.webrtc.boyj.utils.App;
import com.webrtc.boyj.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

public class PeerConnectionClient {
    private static PeerConnectionClient instance;
    enum CAMERA_POSITION { NONE, FRONT, BACK }
    private CAMERA_POSITION position = CAMERA_POSITION.NONE;

    private PeerConnectionFactory peerConnectionFactory;
    private VideoCapturer videoCapturer, frontCapturer, backCapturer;
    private VideoTrack videoTrack;
    private AudioTrack audioTrack;

    private PeerConnection localPeer;
    private MediaStream mediaStream;

    private MediaConstraints constraints;
    private List<PeerConnection.IceServer> iceServers=new ArrayList<>();


    public PublishSubject<SessionDescription> sdpSubject=PublishSubject.create();
    public PublishSubject<IceCandidate> iceCandidateSubject=PublishSubject.create();
    public PublishSubject<MediaStream> remoteMediaStreamSubject=PublishSubject.create();

    private PeerConnectionClient(){
        init();
    }
    public static PeerConnectionClient getInstance() {
        if(instance == null)
            synchronized (PeerConnectionClient.class) {
                if(instance == null)
                    instance = new PeerConnectionClient();
            }
        return instance;
    }



    public void localStreamTo(SurfaceViewRenderer view){
        videoCapturer.startCapture(1024, 720, 30);
        videoTrack.addSink(view);
    }
    private boolean createVideoTrack() {
        VideoManager videoManager = new VideoManager(App.getContext(), EglBaseManager.getEglBase(), peerConnectionFactory);
        frontCapturer = videoManager.getFrontCameraCapturer();
        backCapturer = videoManager.getBackCameraCapturer();
        videoTrack = videoManager.getVideoTrack();
        return true;
    }
    private void createAudioTrack() {
        AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        audioTrack = peerConnectionFactory.createAudioTrack("AudioTrack", audioSource);
    }

    public void init() {
        initIceServers();
        initPeerConnectionFactory();
        createVideoTrack();
        createAudioTrack();

        if(frontCapturer != null) {
            position = CAMERA_POSITION.FRONT;
            videoCapturer = frontCapturer;
        } else if(backCapturer != null) {
            position = CAMERA_POSITION.BACK;
            videoCapturer = backCapturer;
        }

        constraints = new MediaConstraints();
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
    }


    public void createPeerConnection() {
        localPeer = peerConnectionFactory.createPeerConnection(createRTCConfiguration(), new CustomPeerConnectionObserver("localPeerCreation") {
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                onIceCandidateReceived(iceCandidate);
            }
            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                remoteMediaStreamSubject.onNext(mediaStream);
            }
        });
        addStreamToLocalPeer();
    }
    public void createOffer() {
        Logger.d("createoffer");
        localPeer.createOffer(new CustomSdpObserver("localCreateOffer") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocalDesc"), sessionDescription);
                //SignalingClient.getInstance().emitSessionDescription(sessionDescription);
                sdpSubject.onNext(sessionDescription);
            }
        }, constraints);
    }

    public void createAnswer() {
        Logger.d("createanswer");
        localPeer.createAnswer(new CustomSdpObserver("localCreateAnswer") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocal"), sessionDescription);
                //SignalingClient.getInstance().emitSessionDescription(sessionDescription);
                sdpSubject.onNext(sessionDescription);
            }
        }, constraints);
    }

    private void addStreamToLocalPeer() {
        mediaStream = peerConnectionFactory.createLocalMediaStream("102");
        mediaStream.addTrack(audioTrack);
        mediaStream.addTrack(videoTrack);
        localPeer.addStream(mediaStream);
        Logger.d("addStreamToLocalPeer");
    }
    public void setRemoteDescription(boolean isOffer, JSONObject data) {
        try {
            if (isOffer)
                localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.OFFER, data.getString("sdp")));
            else
                localPeer.setRemoteDescription(new CustomSdpObserver("localSetLocal"), new SessionDescription(SessionDescription.Type.ANSWER, data.getString("sdp")));
        } catch(Exception e) { e.printStackTrace(); }
    }
    public void addIceCandidate(JSONObject data) {
        try {
            localPeer.addIceCandidate(new IceCandidate(data.getString("id"), data.getInt("label"), data.getString("candidate")));
        } catch (JSONException e) { e.printStackTrace(); }
    }
    private PeerConnection.RTCConfiguration createRTCConfiguration() {
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;
        return rtcConfig;
    }
    private void onIceCandidateReceived(IceCandidate iceCandidate) {
        iceCandidateSubject.onNext(iceCandidate);
    }
    public void muteToggle(boolean isMute) {
        if(isMute) mediaStream.removeTrack(audioTrack);
        else mediaStream.addTrack(audioTrack);
    }
    public void camToggle() {
        if(frontCapturer == null || backCapturer == null)
            return;
        try { videoCapturer.stopCapture(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        if(position == CAMERA_POSITION.FRONT) {
            videoCapturer = backCapturer;
            position = CAMERA_POSITION.BACK;
        } else if(position == CAMERA_POSITION.BACK) {
            videoCapturer = frontCapturer;
            position = CAMERA_POSITION.FRONT;
        }
        videoCapturer.startCapture(1000, 720, 30);
    }
    public void release() {
        try { videoCapturer.stopCapture(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        localPeer.dispose();
        /*
        localView.release();
        remoteView.release();
        */
        peerConnectionFactory.dispose();
    }

    private void initIceServers() {
        byte[] data = new byte[0];
        try { data = ("tbtzpdlql:0e7392fa-07ef-11e9-a443-0242ac110003").getBytes("UTF-8"); }
        catch (UnsupportedEncodingException e) { e.printStackTrace(); }
        String authToken = "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
        Util.getInstance().getRetrofit().getIceCandidates(authToken).enqueue(new Callback<TurnServerPojo>() {
            @Override
            public void onResponse(@NonNull Call<TurnServerPojo> call, @NonNull Response<TurnServerPojo> response) {
                TurnServerPojo body = response.body();
                if (body != null) {
                    for (IceServer iceServer : body.iceServerList.iceServers) {
                        if (iceServer.credential == null) {
                            PeerConnection.IceServer peerIceServer = PeerConnection.IceServer.builder(iceServer.url).createIceServer();
                            //client.addIceServer(peerIceServer);
                            iceServers.add(peerIceServer);
                        } else {
                            PeerConnection.IceServer peerIceServer = PeerConnection.IceServer.builder(iceServer.url)
                                    .setUsername(iceServer.username)
                                    .setPassword(iceServer.credential)
                                    .createIceServer();
                            //client.addIceServer(peerIceServer);
                            iceServers.add(peerIceServer);
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<TurnServerPojo> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void initPeerConnectionFactory(){
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                .builder(App.getContext())
                .createInitializationOptions());

        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        DefaultVideoEncoderFactory defaultVideoEncoderFactory =
                new DefaultVideoEncoderFactory(EglBaseManager.getEglBase().getEglBaseContext(), true, true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(EglBaseManager.getEglBase().getEglBaseContext());

        this.peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();
    }
}
