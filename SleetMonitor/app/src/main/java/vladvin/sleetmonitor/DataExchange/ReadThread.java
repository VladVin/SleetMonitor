package vladvin.sleetmonitor.DataExchange;

import android.util.Log;

/**
 * Created by Carioca on 28/09/16.
 */
public class ReadThread implements Runnable {
    private String tagString = this.getClass().getName();

    @Override
    public void run() {
        while (true){
            for(int i=0; i<10; i++){
                Log.d(tagString, String.valueOf(i));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
