package vladvin.sleetmonitor.DataExchange;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Carioca on 28/09/16.
 */
public class WriteThread extends Thread {
    private String tagString = this.getClass().getName();

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ConcurrentLinkedQueue<DataPiece> dataToSend;

    public WriteThread(ConcurrentLinkedQueue<DataPiece> dataToSend,
                       Socket socket) {
        this.dataToSend = dataToSend;
        try {
            this.socket = socket;
            OutputStream outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                objectOutputStream.writeObject("Hello!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        while (true){
//            if (!dataToSend.isEmpty()) {
//                try {
//                    objectOutputStream.writeObject(dataToSend.poll());
//                    objectOutputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    Thread.sleep(delayTime);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
