package com.example.jin.homeiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static Data data = Data.getInstance();
    private TextView tvWarning;
    private TextView tvWaterLine;
    private TextView tvTemperature;
    private Button btnFanOn;
    private Button btnFanOff;
    private Button btnLEDOn;
    private Button btnLEDOff;
    private Button btnConfirm;
    private Socket socket = null;
    private final String serverIP = "192.168.43.98";
    private String temp;
    private String op = "hi";
    private String DEFAULT_WARNING = "알림판 : ";
    private String DEFAULT_WATERLINE = "수위 : ";
    private String DEFAULT_TEPERATURE = "온도 : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvWarning = findViewById(R.id.tvWarning);
        tvWaterLine = findViewById(R.id.tvWaterLine);
        tvTemperature = findViewById(R.id.tvTemperature);
        btnFanOn = findViewById(R.id.btnFanOn);
        btnFanOff = findViewById(R.id.btnFanOff);
        btnLEDOn = findViewById(R.id.btnOnLED);
        btnLEDOff = findViewById(R.id.btnOffLED);
        btnConfirm =findViewById(R.id.btnConfirm);

        tvWarning.setText(DEFAULT_WARNING + "집은 안전합니다.");
        btnLEDOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = "o";
            }
        });
        btnLEDOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = "f";
            }
        });
        btnFanOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = "q";
            }
        });
        btnFanOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op = "w";
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvWarning.setText(DEFAULT_WARNING + "집은 안전합니다.");
            }
        });
        System.out.println("서버에 연결중입니다. 서버 IP : " + serverIP);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(data.isEntry()){
                                tvWarning.setText(DEFAULT_WARNING + "도둑이 침입했다.");
                            }
                            tvWaterLine.setText(DEFAULT_WATERLINE + data.getWaterLine());
                            tvTemperature.setText(DEFAULT_TEPERATURE + data.getTemperature());
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        socket = new Socket(serverIP, 5000);
                        InputStream in = socket.getInputStream();
                        DataInputStream dis = new DataInputStream(in);
                        data.setTemperature(dis.readInt());
                        data.setWaterLine(dis.readInt());
                        data.setThief(dis.readBoolean());
                        data.setBtn(dis.readBoolean());
                        data.setEntry(dis.readBoolean());
                        Log.e("run: ", data.toString());
                        //System.out.println("서버로부터 받은 메세지 : " + dis.readUTF());
                        //System.out.println("연결을 종료합니다.");
                        OutputStream out = socket.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(out);
                        dos.writeUTF(op);
                        op = "hi";
                        dos.close();
                        socket.close();
                        Thread.sleep(1000);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public TextView getTvWarning() {
        return tvWarning;
    }

    public void setTvWarning(TextView tvWarning) {
        this.tvWarning = tvWarning;
    }

    public TextView getTvWaterLine() {
        return tvWaterLine;
    }

    public void setTvWaterLine(TextView tvWaterLine) {
        this.tvWaterLine = tvWaterLine;
    }
}
