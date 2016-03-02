package vladvin.sleetmonitordata;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import vladvin.sleetmonitordata.Exceptions.DataWriterException;

public class MainActivity extends Activity {

    private DataWriter dataWriter = null;
    private SensorTracker sensorTracker;

    private TextView fallsCountField;
    private TextView nonFallsCountField;
    private Button label1Btn;
    private Button label2Btn;
    private Button label3Btn;
    private Button goodLabelBtn;
    private Button trackFallBtn;
    private Button trackNonFallBtn;
    private Button playBtn;
    private Button pauseBtn;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            dataWriter = new DataWriter();
        } catch (Exception e) {
            showToast(e.getMessage());
            return;
        }
        sensorTracker = new SensorTracker(this, dataWriter);

        fallsCountField = (TextView)findViewById(R.id.fallsCountField);
        nonFallsCountField = (TextView) findViewById(R.id.nonFallsCountField);
        label1Btn = (Button) findViewById(R.id.label1Button);
        label2Btn = (Button) findViewById(R.id.label2Button);
        label3Btn = (Button) findViewById(R.id.label3Button);
        goodLabelBtn = (Button) findViewById(R.id.goodLabelButton);
        trackFallBtn = (Button) findViewById(R.id.trackFallButton);
        trackNonFallBtn = (Button) findViewById(R.id.trackNonFallButton);
        playBtn = (Button) findViewById(R.id.playButton);
        pauseBtn = (Button) findViewById(R.id.pauseButton);

        label1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Label 1 sent to DataWriter");
                dataWriter.fetData(1);
            }
        });

        label2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Label 2 sent to DataWriter");
                dataWriter.fetData(2);
            }
        });

        label3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Label 3 sent to DataWriter");
                dataWriter.fetData(3);
            }
        });

        goodLabelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("GOOD label sent to DataWriter");
                dataWriter.fetData(0);
            }
        });

        trackFallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Fall tracker is registered");
                sensorTracker.changeFallState(true);
            }
        });

        trackNonFallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Non-Fall tracker is registered");
                sensorTracker.changeFallState(false);
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Play");
                sensorTracker.play();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Pause");
                sensorTracker.pause();
            }
        });
    }

    public void updateFallsCount(boolean isFall, int count) {
        if (isFall) {
            fallsCountField.setText(String.valueOf(count));
        } else {
            nonFallsCountField.setText(String.valueOf(count));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (dataWriter != null) {
                dataWriter.openFile();
            }
        } catch (DataWriterException dataWriterException) {
            showToast(dataWriterException.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        sensorTracker.pause();
//        try {
//            if (dataWriter != null) {
//                dataWriter.releaseFile();
//            }
//        } catch (DataWriterException dataWriterException) {
//            showToast(dataWriterException.getMessage());
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
