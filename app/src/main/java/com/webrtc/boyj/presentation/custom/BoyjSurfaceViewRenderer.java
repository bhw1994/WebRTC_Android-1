package com.webrtc.boyj.presentation.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.webrtc.boyj.api.peer.manager.EglBaseManager;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

public class BoyjSurfaceViewRenderer extends SurfaceViewRenderer {
    public BoyjSurfaceViewRenderer(Context context) {
        super(context);
        initView();
    }

    public BoyjSurfaceViewRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        init(EglBaseManager.getEglBaseContext(), null);
        setMirror(true);
        setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        setEnableHardwareScaler(true);
        setZOrderMediaOverlay(true);
    }
}