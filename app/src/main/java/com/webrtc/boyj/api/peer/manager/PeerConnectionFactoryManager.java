package com.webrtc.boyj.api.peer.manager;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.App;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;

public class PeerConnectionFactoryManager {

    @NonNull
    private final static List<PeerConnection.IceServer> iceServers = new ArrayList<>();
    @NonNull
    private final static PeerConnectionFactory factory;
    @NonNull
    private final static PeerConnection.RTCConfiguration rtcConfiguration;

    static {
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(App.getContext()).createInitializationOptions());

        final PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        final DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(EglBaseManager.getEglBase().getEglBaseContext(), true, true);
        final DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(EglBaseManager.getEglBase().getEglBaseContext());

        factory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();


        rtcConfiguration = new PeerConnection.RTCConfiguration(iceServers);
        rtcConfiguration.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfiguration.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfiguration.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfiguration.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        rtcConfiguration.keyType = PeerConnection.KeyType.ECDSA;

    }

    public PeerConnectionFactoryManager() {
    }


    @NonNull
    public AudioTrack createAudioTrack() {
        final AudioSource audioSource = factory.createAudioSource(new MediaConstraints());
        final AudioTrack audioTrack = factory.createAudioTrack("AudioTrack", audioSource);
        return audioTrack;
    }

    @NonNull
    public VideoTrack createVideoTrack() {
        final VideoSource videoSource = factory.createVideoSource(true);
        final VideoTrack videoTrack = factory.createVideoTrack("VideoTrack", videoSource);
        return videoTrack;
    }

    @NonNull
    public PeerConnection createPeerConnection(PeerConnection.Observer peerConnectionObserver) {
        final PeerConnection peer = factory.createPeerConnection(rtcConfiguration, peerConnectionObserver);
        return peer;

    }

    public void dispose() {
        factory.dispose();
    }

}
