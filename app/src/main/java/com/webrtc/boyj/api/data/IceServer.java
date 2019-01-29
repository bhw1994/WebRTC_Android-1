package com.webrtc.boyj.api.data;

public class IceServer {
    public String uri;
    public String hostname;
    public String password;
    public IceServer(String uri) {
        this.uri = uri;
    }
    public String toString() {
        return uri + "[" + hostname + ":" + "]";
    }
}
