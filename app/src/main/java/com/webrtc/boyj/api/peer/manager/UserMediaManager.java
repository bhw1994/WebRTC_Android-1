package com.webrtc.boyj.api.peer.manager;

import com.webrtc.boyj.utils.Logger;

import org.webrtc.AudioTrack;
import org.webrtc.MediaStream;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoTrack;

public class UserMediaManager {
    private PeerConnectionFactoryManager peerConnectionFactoryManager;

    enum CAMERA_POSITION {NONE, FRONT, BACK}
    private CAMERA_POSITION position = CAMERA_POSITION.NONE;

    private VideoCapturer videoCapturer, frontCapturer, backCapturer;
    private VideoTrack videoTrack;
    private AudioTrack audioTrack;
    private MediaStream mediaStream;

    public UserMediaManager(){
        this.peerConnectionFactoryManager = new PeerConnectionFactoryManager();

        this.audioTrack = peerConnectionFactoryManager.createAudioTrack();
        this.videoTrack = peerConnectionFactoryManager.createVideoTrack();

        if (frontCapturer != null) {
            position = CAMERA_POSITION.FRONT;
            videoCapturer = frontCapturer;
        } else if (backCapturer != null) {
            position = CAMERA_POSITION.BACK;
            videoCapturer = backCapturer;
        }

    }
    public void localStreamTo(SurfaceViewRenderer view) {
        videoCapturer.startCapture(1024, 720, 30);
        videoTrack.addSink(view);
    }
    private void addStreamToLocalPeer() {
        mediaStream = peerConnectionFactory.createLocalMediaStream("102");
        mediaStream.addTrack(audioTrack);
        mediaStream.addTrack(videoTrack);
        localPeer.addStream(mediaStream);
        Logger.d("addStreamToLocalPeer");
    }
    public void muteToggle(boolean isMute) {
        if (isMute) mediaStream.removeTrack(audioTrack);
        else mediaStream.addTrack(audioTrack);
    }

    public void camToggle() {
        if (frontCapturer == null || backCapturer == null)
            return;
        try {
            videoCapturer.stopCapture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (position == CAMERA_POSITION.FRONT) {
            videoCapturer = backCapturer;
            position = CAMERA_POSITION.BACK;
        } else if (position == CAMERA_POSITION.BACK) {
            videoCapturer = frontCapturer;
            position = CAMERA_POSITION.FRONT;
        }
        videoCapturer.startCapture(1000, 720, 30);
    }

}
