package vladvin.sleetmonitor.DataExchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Scanner;

import vladvin.sleetmonitor.R;

public class testDataXChngActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data_xchng);
        NetModule testDataBroker = new NetModule();
        for(int i=1; i<6; i++){
            try {
                Thread.sleep(5000);
                testDataBroker.setDelayTimeWriteThread(i*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
