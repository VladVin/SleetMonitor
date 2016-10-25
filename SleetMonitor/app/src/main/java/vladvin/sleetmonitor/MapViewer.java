package vladvin.sleetmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.ConcurrentLinkedQueue;

import vladvin.sleetmonitor.DataExchange.NetModule;
import vladvin.sleetmonitor.sensor_tracker.SensorData;

public class MapViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread() {
            @Override
            public void run() {
                NetModule netModule = new NetModule();
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                Ъ
//                ConcurrentLinkedQueue<SensorData> q = new ConcurrentLinkedQueue<SensorData>();
//                q.add(new SensorData(1.0f, 1.0f, 2.0f, 3.0f, 123456789));
//                netModule.putSensorData(q, 1);
            }
        }.start();
    }
}
