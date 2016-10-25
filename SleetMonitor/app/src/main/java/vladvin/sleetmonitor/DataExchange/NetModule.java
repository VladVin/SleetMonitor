package vladvin.sleetmonitor.DataExchange;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import vladvin.sleetmonitor.MapViewer;
import vladvin.sleetmonitor.sensor_tracker.SensorData;

/**
 * Created by Carioca on 18/09/16.
 */
public class NetModule extends Thread{
    private static final String SERVER_IP = "10.240.22.214";
    private static final int SERVER_PORT_NUM = 4444;
    private static final int SLEEP_DELAY = 1000;

    private MapViewer mapViewer;

    private PriorityQueue<DataPiece> dataToSend;

    private Socket socket;

    public NetModule(MapViewer mapViewer) {
        this.mapViewer = mapViewer;
        this.dataToSend = new PriorityQueue<>();
    }

    @Override
    public void run() {
        while (true){
            if (isAvailiableToSendData()) {
                sendData();
            } else {
                connect();
            }
        }
    }

    public void onConnectionFailed() {
        //TODO: terminate all sen-s & receiv-s

    }

    public void onUpdateReceived(Object o) {
        // TODO: Check type and send to corresponding module
        if (mapViewer != null) {
            // TODO: call corresponding method of map viewer
        }
    }

    //TODO: implementation
    public void putSensorData(ConcurrentLinkedQueue<SensorData> inputSensorData,
                              int count){
        ArrayList<SensorData> sensorDatas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            sensorDatas.add(inputSensorData.poll());
        }
        dataToSend.add(new DataPiece(sensorDatas));
    }
    //TODO: determine parameters, return type and do implementation
    public void getMapUpdates(Object o) {
        dataToSend.add(new DataPiece(o));
    }

    //TODO: implementation
    public void setMapListener(MapViewer mapViewer){
        this.mapViewer = mapViewer;
    }

    public void startUpdateReceiver() {
        startUpdateReceiver(socket);
    }

    public void stopUpdateReceiver() {
        // TODO: terminate receiver's thread
        socket = null;
    }

    public void sendError(String message) {
        // TODO: call send error from map viewer
    }

    private void startUpdateReceiver(Socket socket) {

    }

    private void startSensorDataSender() {

    }

    private void stopSensorDataSender() {

    }

    private void startRequestSender() {

    }

    private void stopRequestSender() {

    }

    private void initializeSensorDataSender(){

    }

    private void initializeRequestSender(Socket socket){

    }

    private void initializeUpdateReceiver(Socket socket){

    }

    private boolean connected() {

    }

    private boolean isAvailiableToSendData() {
        return socket != null;
    }

    private void connect() {
        if (socket == null && connected()) {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT_NUM);
                initializeSensorDataSender();
                initializeRequestSender(socket);
                initializeUpdateReceiver(socket);
            } catch (IOException e) {
                sendError(e.getMessage());
            }
        }
        try {
            Thread.sleep(SLEEP_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendData() {
        DataPiece dataPiece = dataToSend.poll();
        switch (DataPiece.checkDataType(dataPiece.data)) {
            case SENSOR_DATA:
                // TODO: send to SensorDataSender

                break;
            case UPDATE_REQUEST:
                // TODO: send to Request Sender
                break;
            default:
                break;
        }
    }
}
