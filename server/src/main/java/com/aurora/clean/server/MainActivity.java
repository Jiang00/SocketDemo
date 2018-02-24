package com.aurora.clean.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TextView server_address;
    TextView server_port;
    TextView server_message;
    private ServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        server_address = (TextView) findViewById(R.id.server_address);
        server_port = (TextView) findViewById(R.id.server_port);
        server_message = (TextView) findViewById(R.id.server_message);
    }

    boolean isKai;

    public void openServer(View view) {
        if (isKai) {
            return;
        }
        isKai = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(8000);
                    final String ip = InetAddress.getLocalHost().getHostAddress();
                    final String port = String.valueOf(serverSocket.getLocalPort());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            server_address.setText(ip);
                            server_port.setText(port);
                        }
                    });
                    Log.e("Socket", "===0");
                    Socket socket = serverSocket.accept();
                    BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    final String ap = bf.readLine();

                    Log.e("Socket", ap + "===1");
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());
                    pw.println("服务器连接成功");
                    pw.flush();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            server_message.setText(ap);
                        }
                    });
                    bf.close();
                    socket.close();
                } catch (IOException e) {
                    Log.e("Socket", "===2");
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void closeServer(View view) {
        if (!isKai) {
            return;
        }
        isKai = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
