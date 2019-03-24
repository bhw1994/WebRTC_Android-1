package com.webrtc.boyj.api.signalling;


import com.google.firebase.messaging.RemoteMessage;
import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.firebase.MyFirebaseMessagingService;
import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.presentation.call.CallViewModel;
import com.webrtc.boyj.presentation.ringing.RingingViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BoyjRTC.class,PeerConnectionClient.class,SignalingClient.class, CallViewModel.class})
public class SignalingClientTest {

    @Test
    public void shouldEmitDial() throws Exception {
        //given
        final SocketIOClient mockSocketIOClient = mock(SocketIOClient.class);
        PowerMockito.whenNew(SocketIOClient.class).withNoArguments().thenReturn(mockSocketIOClient);

        final SignalingClient spySignalingClient = spy(SignalingClient.class);
        PowerMockito.whenNew(SignalingClient.class).withNoArguments().thenReturn(spySignalingClient);


        final PeerConnectionClient mockPeerConnectionClient = mock(PeerConnectionClient.class);
        PowerMockito.whenNew(PeerConnectionClient.class).withNoArguments().thenReturn(mockPeerConnectionClient);
        //when
        //call button click

        final CallViewModel callViewModel = new CallViewModel(mock(User.class));
        callViewModel.call();

        //then
        //emit dial
        verify(spySignalingClient, times(1)).emitDial(any());
    }

    @Test
    public void shouldEmitAwaken() throws Exception {
        //given
        final SocketIOClient mockSocketIOClient = mock(SocketIOClient.class);
        PowerMockito.whenNew(SocketIOClient.class).withNoArguments().thenReturn(mockSocketIOClient);

        final SignalingClient spySignalingClient = spy(SignalingClient.class);
        PowerMockito.whenNew(SignalingClient.class).withNoArguments().thenReturn(spySignalingClient);

        //when
        //fcm received
        final MyFirebaseMessagingService service = new MyFirebaseMessagingService();

        final RemoteMessage remoteMessage = mock(RemoteMessage.class);
        service.onMessageReceived(remoteMessage);

        //then
        //emit awaken
        verify(spySignalingClient, times(1)).emitAwaken(any());
    }


    @Test
    public void shouldEmitAccpet() throws Exception {
        //given
        final SocketIOClient mockSocketIOClient = mock(SocketIOClient.class);
        PowerMockito.whenNew(SocketIOClient.class).withNoArguments().thenReturn(mockSocketIOClient);

        final SignalingClient spySignalingClient = spy(SignalingClient.class);
        PowerMockito.whenNew(SignalingClient.class).withNoArguments().thenReturn(spySignalingClient);

        //when
        //user accpet action
        final RingingViewModel ringingViewModel = new RingingViewModel();
        ringingViewModel.acceptAction();

        //then
        //emit accept
        verify(spySignalingClient, times(1)).emitAccept();
    }


    @Test
    public void shouldEmitReject() throws Exception {
        //given
        final SocketIOClient mockSocketIOClient = mock(SocketIOClient.class);
        PowerMockito.whenNew(SocketIOClient.class).withNoArguments().thenReturn(mockSocketIOClient);

        final SignalingClient spySignalingClient = spy(SignalingClient.class);
        PowerMockito.whenNew(SignalingClient.class).withNoArguments().thenReturn(spySignalingClient);

        //when
        //user reject action
        final RingingViewModel ringingViewModel = new RingingViewModel();
        ringingViewModel.rejectAction();

        //then
        //emit reject
        verify(spySignalingClient, times(1)).emitReject();
    }

    @Test
    public void shouldEmitSdp() throws Exception {
        //given
        final SocketIOClient mockSocketIOClient = mock(SocketIOClient.class);
        PowerMockito.whenNew(SocketIOClient.class).withNoArguments().thenReturn(mockSocketIOClient);

        final SignalingClient spySignalingClient = spy(SignalingClient.class);
        PowerMockito.whenNew(SignalingClient.class).withNoArguments().thenReturn(spySignalingClient);

        //when
        //user sdp created

        //then
        //emit Sdp
        verify(spySignalingClient, times(1)).emitSdp(any());
    }

    @Test
    public void shouldEmitIceCandidate() throws Exception {
        //given
        final SocketIOClient mockSocketIOClient = mock(SocketIOClient.class);
        PowerMockito.whenNew(SocketIOClient.class).withNoArguments().thenReturn(mockSocketIOClient);

        final SignalingClient spySignalingClient = spy(SignalingClient.class);
        PowerMockito.whenNew(SignalingClient.class).withNoArguments().thenReturn(spySignalingClient);

        //when
        //user iceCandidate created
        final RingingViewModel ringingViewModel = new RingingViewModel();
        ringingViewModel.rejectAction();

        //then
        //emit ice candidate
        verify(spySignalingClient, times(1)).emitIceCandidate(any());
    }

    @Test
    public void shouldEmitBye() throws Exception {
        //given
        final SocketIOClient mockSocketIOClient = mock(SocketIOClient.class);
        PowerMockito.whenNew(SocketIOClient.class).withNoArguments().thenReturn(mockSocketIOClient);

        final SignalingClient spySignalingClient = spy(SignalingClient.class);
        PowerMockito.whenNew(SignalingClient.class).withNoArguments().thenReturn(spySignalingClient);

        //when
        //user bye action
        final CallViewModel callViewModel = new CallViewModel(mock(User.class));
        callViewModel.hangUp();

        //then
        //emit bye
        verify(spySignalingClient, times(1)).emitBye();
    }
}




