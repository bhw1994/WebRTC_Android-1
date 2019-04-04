package com.webrtc.boyj.api.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webrtc.boyj.api.signalling.payload.FCMPayload;
import com.webrtc.boyj.data.repository.UserRepositoryImpl;
import com.webrtc.boyj.presentation.ringing.RingingActivity;

@SuppressWarnings("SpellCheckingInspection")
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull final RemoteMessage remoteMessage) {
        final FCMPayload payload = new FCMPayload(remoteMessage);

        final Intent intent = RingingActivity.getLaunchIntent(getApplicationContext(), payload);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onNewToken(@NonNull final String token) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit()
                .putString(UserRepositoryImpl.FIELD_USER_TOKEN, token)
                .putBoolean(UserRepositoryImpl.CHANGED, true)
                .apply();
    }
}