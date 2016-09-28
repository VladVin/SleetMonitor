package vladvin.sleetmonitor.DataExchange;

import android.location.Location;
import android.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import vladvin.sleetmonitor.MapViewer;
import vladvin.sleetmonitor.sensor_tracker.SensorData;

/**
 * Created by Carioca on 18/09/16.
 */
public class DataBroker {
    private ConcurrentLinkedQueue<SensorData> dataToSend;

    private WriteThread writeThread;

    private ReadThread readThread;

    public DataBroker(){
        dataToSend = new ConcurrentLinkedQueue<>();
        writeThread = new WriteThread();
        (new Thread(writeThread)).start();
        readThread = new ReadThread();
        (new Thread(readThread)).start();
    }



    //TODO: implementation
    public void putSensorData(ConcurrentLinkedQueue<SensorData> inputSensorData,
                              int count){
    }
    //TODO: determine parameters, return type and do implementation
    public void getMapUpdates(){

    }

    //TODO: implementation
    public void setMapListener(MapViewer mapViewer){

    }

    //TODO: implementation
    public void stop(){

    }


}
