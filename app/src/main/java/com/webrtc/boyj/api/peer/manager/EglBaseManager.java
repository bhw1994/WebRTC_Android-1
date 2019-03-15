package com.webrtc.boyj.api.peer.manager;

import org.webrtc.EglBase;

public class EglBaseManager {
    private static EglBase eglBase;

    public static EglBase getEglBase() {
        if (eglBase == null) {
            synchronized (EglBaseManager.class) {
                if (eglBase == null) {
                    eglBase = EglBase.create();
                }
            }
        }
        return eglBase;
    }


}
