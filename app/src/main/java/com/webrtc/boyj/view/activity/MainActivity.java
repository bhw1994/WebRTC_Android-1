package com.webrtc.boyj.view.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.widget.Toast;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityMainBinding;
import com.webrtc.boyj.model.dto.User;
import com.webrtc.boyj.utils.Constants;
import com.webrtc.boyj.view.adapter.MainAdapter;
import com.webrtc.boyj.viewmodel.MainViewModel;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    private MainAdapter adapter;

    private final static String CAMERA = Manifest.permission.CAMERA;
    private final static String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {CAMERA, RECORD_AUDIO }, 100);


        adapter = new MainAdapter();
        binding.recycler.setAdapter(adapter);
        binding.recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Fab 클릭시 액티비티 이동
        adapter.setOnFabClickListener(this::moveToCallActivity);

        // User 추가시 데이터 반영
        model.getUsers().observe(this, adapter::updateItems);
    }

    private void moveToCallActivity(User user) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra(Constants.EXTRA_USER, user);
        startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainViewModel getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){}
            else
                Toast.makeText(this, "권한 거부", Toast.LENGTH_SHORT).show();
        }
    }
}
