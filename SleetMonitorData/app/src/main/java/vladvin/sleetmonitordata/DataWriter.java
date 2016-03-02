package vladvin.sleetmonitordata;

import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

import vladvin.sleetmonitordata.Exceptions.DataWriterException;

/**
 * Created by VladVin on 19.10.2015.
 */
public class DataWriter extends AsyncTask<Void, Void, Void> {

    private enum DataType { MEASUREMENTS, LABEL, NONE }
    private static final String TAG = "DataWriter";

    private Queue<Pair<Long, SensorData>> currentData;
    private int count = 0;
    private int currentLabel = 0;
    private boolean isCanceled = false;
    private final Object syncObj = new Object();
    private static final String dirname = "SleetMonitorData";
    private String filepath;
    private BufferedWriter bufferedWriter;
    private boolean isFall = false;

    private DataType currentDataType = DataType.NONE;

    public DataWriter() throws DataWriterException {
        openFile();
        writeInitializationMessage();
    }

    public void fetData(final Queue<Pair<Long, SensorData>> data, int count) {
        synchronized (syncObj) {
            this.currentData = data;
            this.count = count;
            this.currentDataType = DataType.MEASUREMENTS;
            syncObj.notifyAll();
        }
    }

    public void fetData(int label) {
        synchronized (syncObj) {
            this.currentLabel= label;
            this.currentDataType = DataType.LABEL;
            syncObj.notifyAll();
        }
    }

    public void changeFallState(boolean isFall) {
        // Assume that synchronization is redundant
        this.isFall = isFall;
    }

    public void writePlay() {
        writeToFile("PLAY");
    }

    public void openFile() throws DataWriterException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new DataWriterException("External storage not mounted");
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dirname);
        if (!file.mkdirs() && !file.isDirectory()) {
            throw new DataWriterException("Cannot create directory");
        }

        if (file.isDirectory()) {
            filepath = file.getAbsoluteFile() + "/data.txt";
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(filepath, true);
            } catch (IOException e) {
                throw new DataWriterException("Cannot create file");
            }
            bufferedWriter = new BufferedWriter(fileWriter);
        }
    }

    public void releaseFile() throws DataWriterException {
        try {
            bufferedWriter.close();
        } catch (Exception e) {
            throw new DataWriterException("Cannot close BufferedWriter");
        }
        MediaScannerConnection.scanFile(ApplicationCtx.getContext(), new String[]{filepath}, null, null);
    }

    public void cancel() throws DataWriterException {
//        cancel(true);
        isCanceled = true;
        releaseFile();
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (!isCanceled) {
            synchronized (syncObj) {
                try {
                    syncObj.wait();
                } catch (InterruptedException e) {
                    try {
                        cancel();
                    } catch (DataWriterException dataWriterException) {
                        dataWriterException.printStackTrace();
                    }
                }
                switch (currentDataType) {
                    case MEASUREMENTS:
                        writeDataToFile(currentData, count);
                        break;
                    case LABEL:
                        writeDataToFile(currentLabel);
                        break;
                }
            }
        }
        return null;
    }

    private void writeDataToFile(final Queue<Pair<Long, SensorData>> data, final int count) {
        if (data == null || bufferedWriter == null) {
            return;
        }
        for (int i = 0; i < count; i++) {
            try {
                Pair<Long, SensorData> item = data.remove();
//                Log.d(TAG, String.valueOf(item.first.longValue()));
                bufferedWriter.write(String.valueOf(item.first.longValue()) + ": " + String.valueOf(item.second.getX()) + " " +
                        String.valueOf(item.second.getY()) + " " + String.valueOf(item.second.getZ()) + " ");
            } catch (Exception e) {
                break;
            }
        }
        try {
            bufferedWriter.write(isFall + "\n");
            bufferedWriter.flush();
            Log.d(TAG, "Data written");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDataToFile(int label) {
        switch (label) {
            case 0:
                writeToFile("GOOD");
                break;
            default:
                writeToFile("Label" + String.valueOf(label));
                break;
        }
    }

    private void writeInitializationMessage() {
        writeToFile("DataWriter was initialized");
    }

    private void writeToFile(String text) {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.write(text + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printData(final Queue<Pair<Long, SensorData>> data, final int count) {
        for (int i = 0; i < count; i++) {
            try {
                Pair<Long, SensorData> item = data.remove();
                Log.d(TAG, String.valueOf(item.first) + ": " + String.valueOf(item.second.getX()) + " " +
                        String.valueOf(item.second.getY()) + " " + String.valueOf(item.second.getZ()));
            } catch (Exception e) {
                break;
            }
        }
    }
}
