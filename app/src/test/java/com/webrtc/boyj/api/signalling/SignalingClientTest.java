package com.webrtc.boyj.api.signalling;

import com.webrtc.boyj.api.firebase.MyFirebaseMessagingService;
import com.webrtc.boyj.api.signalling.payload.FCMPayload;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.presentation.call.CallViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.socket.client.Socket;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SignalingClient.class, SignalingClientFactory.class})
public class SignalingClientTest {

    @Test
    public void shouldEmitDial() {
        //given

        Socket mockSocket = mock(Socket.class);
        SocketIOClient socketIOClient = new SocketIOClient(mockSocket);
        SocketIOClient spySocketIOClient = spy(socketIOClient);

        //assume socket connected. escape connection error.
        doNothing().when(spySocketIOClient).connect();

        SignalingClient signalingClient = SignalingClientFactory.getSignalingClient(spySocketIOClient);
        SignalingClient spySignalingClient = spy(signalingClient);


        mockStatic(SignalingClientFactory.class);
        when(SignalingClientFactory.getSignalingClient())
                .thenReturn(spySignalingClient);

        //when
        //call button click
        CallViewModel callViewModel = new CallViewModel(mock(User.class));
        callViewModel.call();

        //then
        //emit dial
        verify(spySignalingClient, times(1)).emitDial(any());
    }

    @Test
    public void shouldEmitAwaken() {
        //given
        Socket mockSocket = mock(Socket.class);
        SocketIOClient socketIOClient = new SocketIOClient(mockSocket);
        SocketIOClient spySocketIOClient = spy(socketIOClient);

        //assume socket connected. escape connection error.
        doNothing().when(spySocketIOClient).connect();

        SignalingClient signalingClient = SignalingClientFactory.getSignalingClient(spySocketIOClient);
        SignalingClient spySignalingClient = spy(signalingClient);


        mockStatic(SignalingClientFactory.class);
        when(SignalingClientFactory.getSignalingClient())
                .thenReturn(spySignalingClient);

        //when
        //fcm received
        MyFirebaseMessagingService service = new MyFirebaseMessagingService();
        FCMPayload payload = mock(FCMPayload.class);

        service.handleNow(payload);

        //then
        //emit awaken
        verify(spySignalingClient, times(1)).emitAwaken(any());
    }
}