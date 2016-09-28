package vladvin.sleetmonitor.DataExchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vladvin.sleetmonitor.R;

public class testDataXChngActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data_xchng);
        DataBroker testDataBroker = new DataBroker();
    }
}
