package vladvin.sleetmonitor.DataExchange;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Carioca on 24/10/2016.
 */

public class RequestSender extends Thread {
    private NetModule netModule;
    private ObjectOutputStream outputStream;
    private ConcurrentLinkedQueue<DataPiece> dataToSend;

    public RequestSender(NetModule netModule, Socket socket) {
        this.netModule = netModule;
        this.dataToSend = new ConcurrentLinkedQueue<>();
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            netModule.sendError(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!dataToSend.isEmpty()) {
                DataPiece dataPiece = dataToSend.peek();
                try {
                    outputStream.writeObject(dataPiece);
                    dataToSend.poll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendReqest(DataPiece dataPiece) {
        // TODO: put piece into queue
    }
}
