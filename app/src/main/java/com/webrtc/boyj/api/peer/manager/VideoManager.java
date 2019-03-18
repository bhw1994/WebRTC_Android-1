package com.webrtc.boyj.api.peer.manager;

import android.content.Context;


import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.EglBase;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class VideoManager {
    private final Context context;
    private final CameraEnumerator enumerator;
    private final SurfaceTextureHelper helper;
    private String[] deviceNames;

    public VideoManager(Context context, EglBase rootEglBase, PeerConnectionFactory factory) {
        this.context = context;
        if (isCamera2Supported()) {
            enumerator = new Camera2Enumerator(context);
        } else {
            enumerator = new Camera1Enumerator(false);
        }

        helper = SurfaceTextureHelper.create("SurfaceTexture", rootEglBase.getEglBaseContext());
        deviceNames = enumerator.getDeviceNames();

    }

    private boolean isCamera2Supported() {
        return Camera2Enumerator.isSupported(context);
    }

    public VideoCapturer getFrontCameraCapturer() {
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                videoCapturer.initialize(helper, context, videoSource.getCapturerObserver());
                return videoCapturer;
            }
        }
        return null;
    }

    public VideoCapturer getBackCameraCapturer() {
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                videoCapturer.initialize(helper, context, videoSource.getCapturerObserver());
                return videoCapturer;
            }
        }
        return null;
    }

}