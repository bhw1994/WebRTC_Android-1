package com.webrtc.boyj.api.peer.manager;



import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

public class RendererManager {
    private final EglBase rootEglBase;
    public RendererManager(EglBase rootEglBase) {
        this.rootEglBase = rootEglBase;
    }
    public SurfaceViewRenderer initSurfaeView(SurfaceViewRenderer renderer) {
        renderer.init(rootEglBase.getEglBaseContext(), null);
        renderer.setMirror(true);
        renderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        renderer.setEnableHardwareScaler(true);
        renderer.setZOrderMediaOverlay(true);
        return renderer;
    }
}
