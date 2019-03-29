package com.webrtc.boyj.presentation.call;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CallViewModel extends BaseViewModel {
    @NonNull
    private final String tel;
    @NonNull
    private final ObservableBoolean isCalling = new ObservableBoolean(false);
    @NonNull
    private final ObservableInt callTime = new ObservableInt(0);
    @NonNull
    private final BoyjRTC boyjRTC;

    CallViewModel(@NonNull final String tel) {
        this.tel = tel;
        boyjRTC = new BoyjRTC();

        addDisposable(boyjRTC.ready()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::call));
    }

    //전화 거는 요청
    void dial(@NonNull final String room) {
        final DialPayload dialPayload = new DialPayload.Builder(room).build();
        boyjRTC.dial(dialPayload);
    }

    void join() {
        boyjRTC.accept();
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
    public String getTel() {
        return tel;
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
