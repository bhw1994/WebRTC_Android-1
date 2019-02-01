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
    private static final String URL = "http://13.124.41.104:";
    private static final int PORT = 3000;
    private static SignalingClient instance;
    private Socket socket;

    private boolean isConnected=false;
    /*
    public boolean isStarted = false;
    public boolean isChannelReady = false;
    */

    public PublishSubject<String> createdEventSubject = PublishSubject.create();
    public PublishSubject<String> knockEventSubject = PublishSubject.create();
    public PublishSubject<String> readyEventSubject = PublishSubject.create();
    public PublishSubject<JSONObject> rsdpEventSubject = PublishSubject.create();
    public PublishSubject<JSONObject> riceEventSubject = PublishSubject.create();
    public PublishSubject<String> byeEventSubject = PublishSubject.create();
    public PublishSubject<JSONObject> errorEventSubject = PublishSubject.create();



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
            /*


            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, trustAllCerts, null);
            IO.setDefaultHostnameVerifier((hostname, session) -> true);
            IO.setDefaultSSLContext(sslcontext);
            */
            socket = IO.socket(URL + PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(SignalingInterface.EVENT_CREATED, args -> createdEventSubject.onNext("created"));
        socket.on(SignalingInterface.EVENT_KNOCK, args -> knockEventSubject.onNext("knock"));
        socket.on(SignalingInterface.EVENT_READY, args ->readyEventSubject.onNext("ready"));
        socket.on(SignalingInterface.EVENT_RECEIVE_SDP, args ->rsdpEventSubject.onNext((JSONObject) args[0]));
        socket.on(SignalingInterface.EVENT_RECEIVE_ICE, args -> riceEventSubject.onNext((JSONObject)args[0]));
        socket.on(SignalingInterface.EVENT_BYE, args -> byeEventSubject .onNext("bye"));
        socket.on(SignalingInterface.EVENT_SERVER_ERROR, args -> byeEventSubject .onNext("error"));
    }

    public void emitDial(String deviceToken) {

        //socket.emit(SignalingInterface.EVENT_DIAL,deviceToken);

        try {
            JSONObject object = new JSONObject();
            object.put("deviceToken", deviceToken);
            socket.emit(SignalingInterface.EVENT_DIAL,object);
            Logger.signalingEvent(SignalingInterface.EVENT_DIAL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //그리고 서버응답대기
    }
    public void emitAwaken(String room){
        try {
            JSONObject object = new JSONObject();
            object.put("room", room);
            socket.emit(SignalingInterface.EVENT_AWAKEN,object);
            Logger.signalingEvent(SignalingInterface.EVENT_AWAKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void emitAccept(){
        socket.emit(SignalingInterface.EVENT_ACCEPT);
        Logger.signalingEvent(SignalingInterface.EVENT_ACCEPT);
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
