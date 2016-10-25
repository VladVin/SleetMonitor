package vladvin.sleetmonitor.DataExchange;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.SyncFailedException;
import java.net.Socket;

/**
 * Created by Carioca on 28/09/16.
 */
public class ReadThread extends Thread {
    private String tagString = this.getClass().getName();
    private Socket socket;
    private ObjectInputStream objectInputStream;

    public ReadThread(Socket socket) {
        this.socket = socket;
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                Object obj = objectInputStream.readObject();
                // TODO: call method onUpdateReceived of NetModule
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("obj in stream " + (objectInputStream == null ? "null" : "not null"));
                try {
                    System.out.println("socket in stream " + (socket.getInputStream() == null ? "null" : "not null"));
                    objectInputStream = (ObjectInputStream) socket.getInputStream();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
//            for(int i=0; i<10; i++){
//                Log.d(tagString, String.valueOf(i));
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
