package com.webrtc.boyj;

import org.junit.Test;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.CompletableSubject;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void connectTest() {
        Socket socket = null;
        try {
            socket = IO.socket("http://127.0.0.1:3000");
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (socket == null) {
            System.out.println("NULL");
        }

        socket.on("conncet", args -> System.out.println("Call()"));

        System.out.println("TEST");

        socket.emit("dial", "메롱");
    }

    @Test
    public void test2() {
        CompletableSubject subject = CompletableSubject.create();

        Disposable d = subject.subscribe(() -> {
            System.out.println("subscribe");
        });


        System.out.println("" + d.isDisposed());
        subject.onComplete();
        System.out.println("" + d.isDisposed());
        subject.onComplete();


    }
}