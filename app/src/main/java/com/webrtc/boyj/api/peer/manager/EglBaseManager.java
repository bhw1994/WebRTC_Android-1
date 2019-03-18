package com.webrtc.boyj.api.peer.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.webrtc.EglBase;

public class EglBaseManager {
    @Nullable
    private static volatile EglBaseManager INSTANCE;
    private static EglBase eglBase;

    private EglBaseManager() {
        eglBase = EglBase.create();
    }

    public static EglBaseManager getEglBase() {
        if (INSTANCE == null) {
            synchronized (EglBaseManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EglBaseManager();
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    public static EglBase.Context getEglBaseContext() {
        assert eglBase != null;
        return eglBase.getEglBaseContext();
    }
}
