package com.webrtc.boyj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.webrtc.boyj.BR;
import com.webrtc.boyj.R;
import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.peer.manager.EglBaseManager;
import com.webrtc.boyj.api.peer.manager.RendererManager;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.model.dto.User;
import com.webrtc.boyj.utils.Constants;
import com.webrtc.boyj.utils.Logger;
import com.webrtc.boyj.viewmodel.CallViewModel;

import org.webrtc.VideoTrack;

public class CallActivity extends BaseActivity<ActivityCallBinding, CallViewModel> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();

        boolean iscaller=intent.getBooleanExtra("caller",true);


        RendererManager rendererManager=new RendererManager(EglBaseManager.getEglBase());
        rendererManager.initSurfaeView(binding.localView);
        rendererManager.initSurfaeView(binding.remoteView);

        model.getIsActive().observe(this, isActive -> finish());
        if(iscaller){
            String deviceToken=intent.getStringExtra("deviceToken");
            model.call(deviceToken);
        }

        else{
            String room=intent.getStringExtra("room");
            model.accept(room);
        }


        PeerConnectionClient.getInstance().remoteMediaStreamSubject.subscribe(mediaStream -> {
            Logger.d("remotemediastream received");
            VideoTrack videoTrack = mediaStream.videoTracks.get(0);
            videoTrack.addSink(binding.remoteView);
            PeerConnectionClient.getInstance().localStreamTo(binding.localView);
        });


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }

    @Override
    protected CallViewModel getViewModel() {
        return ViewModelProviders.of(this).get(CallViewModel.class);
    }
}
