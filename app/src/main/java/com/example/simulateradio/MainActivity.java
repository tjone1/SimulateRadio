package com.example.simulateradio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RadioService radioService;
    private Button playRadio, stopRadio;
    private ProgressBar progressBar;
    private TextView showProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定Service
        Intent intent = new Intent(this, RadioService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

        playRadio = findViewById(R.id.play_radio);
        stopRadio = findViewById(R.id.stop_radio);
        progressBar = findViewById(R.id.progress_bar);
        showProgress = findViewById(R.id.show_progress);
        progressBar.setMax(100);
        playRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioService.startPlayRadio();
            }
        });
        stopRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioService.stopPlayRadio();
            }
        });

    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            radioService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个RadioService对象
            radioService = ((RadioService.RadioBinder) service).getService();

            //注册回调接口来接收下载进度的变化
            radioService.setProgressListener(new OnPlayProgressListener() {

                @Override
                public void onProgress(int progress) {
                    progressBar.setProgress(progress);
                    showProgress.setText("%" + progress);
                }
            });

        }
    };

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }
}
