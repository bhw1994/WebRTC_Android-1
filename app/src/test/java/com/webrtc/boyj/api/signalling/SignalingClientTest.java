package com.webrtc.boyj.api.signalling;

import com.webrtc.boyj.api.firebase.MyFirebaseMessagingService;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class SignalingClientTest {

    @Test
    public void shouldEmitDial() {
        //given
        SocketIOClient socket = new SocketIOClient();
        MySignalingClient signalingClient = spy(new MySignalingClient(socket));

        //when
        //set caller
        signalingClient.setCaller();

        //then
        //emit awaken
        verify(signalingClient, times(1)).emitDial();
    }

    @Test
    public void shouldEmitAwaken() {
        //given
        //callee
        SocketIOClient socket = new SocketIOClient();
        MySignalingClient signalingClient = spy(new MySignalingClient(socket));
        signalingClient.setCallee();

        //when
        //fcm received
        MyFirebaseMessagingService.getFCMSubject().onNext("received");

        //then
        //emit awaken
        verify(signalingClient, times(1)).emitAwaken();
    }

    @Test
    public void shouldEmitAccept() {
        //given
        SocketIOClient socket = new SocketIOClient();
        MySignalingClient signalingClient = spy(new MySignalingClient(socket));
        signalingClient.setCallee();

        //when
        //knock received
        socket.getKnockEventSubject().onNext("knock");

        //then
        //emit Accept
        verify(signalingClient, times(1)).emitAcppect();
    }

    @Test
    public void shouldEmitReject() {
        //given
        SocketIOClient socket = new SocketIOClient();
        MySignalingClient signalingClient = spy(new MySignalingClient(socket));
        signalingClient.setCallee();

        //when
        //knock received
        socket.getKnockEventSubject().onNext("knock");

        //then
        //emit Reject
        verify(signalingClient, times(1)).emitReject();
    }

    @Test
    public void shouldEmitIceCandidate() {
        //given
        //socket connected
        SocketIOClient socket = new SocketIOClient();
        MySignalingClient signalingClient = spy(new MySignalingClient(socket));
        //signalingClient.setCaller();

        //when
        //icecandidate received

        //then
        //emit iceCandidate
        verify(signalingClient, times(1)).emitIceCandidate();
    }

    @Test
    public void shouldEmitOffer() {
        //given
        SocketIOClient socket = new SocketIOClient();
        MySignalingClient signalingClient = spy(new MySignalingClient(socket));
        //signalingClient.setCaller();

        //when
        //ready received
        socket.getReadyEventSubject().onNext("ready");

        //then
        //emit offer
        verify(signalingClient, times(1)).emitOffer();

    }


    @Test
    public void shouldEmitAnswer() {

        //given
        SocketIOClient socket = new SocketIOClient();
        MySignalingClient signalingClient = spy(new MySignalingClient(socket));
        signalingClient.setCallee();

        //when
        //offer received
        socket.getOfferEventSubject().onNext("received offer");

        //then
        //emit answer
        verify(signalingClient, times(1)).emitAnswer();


    }
}