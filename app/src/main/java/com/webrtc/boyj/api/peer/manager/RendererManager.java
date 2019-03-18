package com.webrtc.boyj.api.peer.manager;


import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

public class RendererManager {


    public void initSurfaeView(SurfaceViewRenderer renderer) {
        renderer.init(EglBaseManager.getEglBase().getEglBaseContext(), null);
        renderer.setMirror(true);
        renderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        renderer.setEnableHardwareScaler(true);
        renderer.setZOrderMediaOverlay(true);
    }
}
