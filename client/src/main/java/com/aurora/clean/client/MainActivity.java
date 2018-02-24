package com.aurora.clean.client;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity {
    EditText client_ip;
    EditText client_port;
    EditText client_message;
    TextView client_fankui;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler=new Handler();
        client_ip = (EditText) findViewById(R.id.client_ip);
        client_port = (EditText) findViewById(R.id.client_port);
        client_message = (EditText) findViewById(R.id.client_message);
        client_fankui = (TextView) findViewById(R.id.client_fankui);

    }

    public void clientClick(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket();
                    SocketAddress socketAddress = new InetSocketAddress(client_ip.getText().toString(), Integer.valueOf(client_port.getText().toString()));
                    socket.connect(socketAddress, 2000);//连不上的0.1毫秒断掉连接
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());
                    pw.println(client_message.getText().toString());
                    pw.flush();

                    Log.e("Socket", "socket===");
                    BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final String ap = bf.readLine();
                    bf.close();
                    socket.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            client_fankui.setText(ap);
                        }
                    });
                } catch (SocketTimeoutException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            client_fankui.setText("连接超时");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
