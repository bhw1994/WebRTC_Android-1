package com.webrtc.boyj.api.signalling;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.webrtc.boyj.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.subjects.PublishSubject;
import io.socket.client.IO;
import io.socket.client.Socket;

public class SignalingClient {
    private static final String URL = "https://10.20.7.162:";
    private static final int PORT = 1794;
    private static SignalingClient instance;
    private Socket socket;

    private boolean isConnected=false;
    /*
    public boolean isStarted = false;
    public boolean isChannelReady = false;
    */

    public PublishSubject<String> createdEventSubject = PublishSubject.create();
    public PublishSubject<String> readyEventSubject = PublishSubject.create();
    public PublishSubject<JSONObject> rsdpEventSubject = PublishSubject.create();
    public PublishSubject<JSONObject> riceEventSubject = PublishSubject.create();
    public PublishSubject<String> byeEventSubject = PublishSubject.create();
    public PublishSubject<String> errorEventSubject = PublishSubject.create();



    @SuppressLint("TrustAllX509TrustManager")
    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
        public void checkClientTrusted(X509Certificate[] chain, String authType) { }
        public void checkServerTrusted(X509Certificate[] chain, String authType) { }
    }};

    private SignalingClient() {
        initSocket();
    }
    public static SignalingClient getInstance() {
        if(instance == null)
            synchronized (SignalingClient.class) {
                if(instance == null){
                    instance = new SignalingClient();
                    instance.connect();
                }
            }
        return instance;
    }

    private void initSocket(){
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, trustAllCerts, null);
            IO.setDefaultHostnameVerifier((hostname, session) -> true);
            IO.setDefaultSSLContext(sslcontext);
            socket = IO.socket(URL + PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        socket.on(SignalingInterface.EVENT_CREATED, args -> createdEventSubject.onNext("created"));
        socket.on(SignalingInterface.EVENT_READY, args ->readyEventSubject.onNext("ready"));
        socket.on(SignalingInterface.EVENT_RECEIVE_SDP, args ->rsdpEventSubject.onNext((JSONObject) args[0]));
        socket.on(SignalingInterface.EVENT_RECEIVE_ICE, args -> riceEventSubject.onNext((JSONObject)args[0]));
        socket.on(SignalingInterface.EVENT_BYE, args -> byeEventSubject .onNext("bye"));
        socket.on(SignalingInterface.EVENT_BYE, args -> byeEventSubject .onNext("error"));
    }

    public void emitDial(String deviceToken) {

        //socket.emit(SignalingInterface.EVENT_DIAL,deviceToken);
        socket.emit(SignalingInterface.EVENT_DIAL,"fmF6SxZoCB0:APA91bE9BzUHWI1C8QMLV0Uff0jChJrkN1hAkVAHPfVil4owfzj1fwYJu46B7z4TiOg1Ua8oLZZj3rWjiYbxxG8V7D1UJEg-Jw1GcgEKqwezrAQth7XW1cbn4fdvCjRmoNpuBOu29tqq");
        Logger.signalingEvent(SignalingInterface.EVENT_DIAL);
        //그리고 서버응답대기
    }
    public void emitAwaken(String room){
        socket.emit(SignalingInterface.EVENT_AWAKEN,room);
        Logger.signalingEvent(SignalingInterface.EVENT_AWAKEN);
    }
    public void emitSice(IceCandidate iceCandidate) {
        try {
            JSONObject object = new JSONObject();
            object.put("type", "candidate");
            object.put("label", iceCandidate.sdpMLineIndex);
            object.put("id", iceCandidate.sdpMid);
            object.put("candidate", iceCandidate.sdp);
            socket.emit(SignalingInterface.EVENT_SEND_ICE, object);
            Logger.signalingEvent(SignalingInterface.EVENT_SEND_ICE);
        } catch (Exception e) { e.printStackTrace(); }
    }
    public void emitSsdp(SessionDescription sessionDescription) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", sessionDescription.type.canonicalForm());
            obj.put("sdp", sessionDescription.description);
            socket.emit(SignalingInterface.EVENT_SEND_SDP, obj);
            Logger.signalingEvent(SignalingInterface.EVENT_SEND_SDP);
        } catch (JSONException e) { e.printStackTrace(); }
    }
    public void emitBye() {
        Logger.signalingEvent(SignalingInterface.EVENT_BYE);
        close();
    }

    public void connect(){
        if(!isConnected){
            socket.connect();
            isConnected=true;
        }
    }
    public void close() {
        if(isConnected){
            socket.disconnect();
            socket.close();
            isConnected=false;
        }
    }
}
