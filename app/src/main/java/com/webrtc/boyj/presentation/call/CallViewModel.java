package com.webrtc.boyj.presentation.call;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.presentation.BaseViewModel;

import org.webrtc.MediaStream;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public class CallViewModel extends BaseViewModel {
    @NonNull
    private final User otherUser;
    @NonNull
    private final ObservableBoolean isCalling = new ObservableBoolean(false);
    @NonNull
    private final ObservableInt callTime = new ObservableInt(0);

    @NonNull
    private final BoyjRTC boyjRTC;

    @NonNull
    private final MediaStream localMediaStream;

    public CallViewModel(@NonNull User otherUser) {

        this.otherUser = otherUser;
        this.boyjRTC = new BoyjRTC();

        boyjRTC.startCapture();
        this.localMediaStream = boyjRTC.getUserMedia();

        boyjRTC.attachEvent();

    }

    public PublishSubject<MediaStream> remoteMediaStream() {
        return boyjRTC.remoteMediaStream();
    }

    //전화 거는 요청
    public void dial() {
        final DialPayload dialPayload = new DialPayload.Builder(otherUser.getDeviceToken()).build();
        boyjRTC.dial(dialPayload);

        boyjRTC.attachCallerListener();

    }

    public void join() {
        boyjRTC.attachCalleeListener();
    }

    //전화 연결 되었을때 작업
    public void call() {
        isCalling.set(true);

        addDisposable(Observable.interval(1, TimeUnit.SECONDS)
                .map(Long::intValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callTime::set));
    }

    public void hangUp() {
        boyjRTC.hangUp();
    }

    @NonNull
    public User getOtherUser() {
        return otherUser;
    }

    @NonNull
    public ObservableBoolean getIsCalling() {
        return isCalling;
    }

    @NonNull
    public ObservableInt getCallTime() {
        return callTime;
    }
}
