package vladvin.sleetmonitor.data_proc;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ServerSender {

    private static final String namespaceName = "winter-ns";
    private static final String eventHubName = "sdeh";
    private static final String sasKeyName = "RootManageSharedAccessKey";
    private static final String sasKey = "0Nsz3brk6a1M3J+oF3//VE+NXUG8b8x80FED5eAi0AI=";
    private static final String sasToken = "SharedAccessSignature sr=https%3a%2f%2fwinter-ns.servicebus.windows.net%2fsdeh%2fpublishers%2fvlad%2fmessages&sig=F00xtm%2b4P%2bzJ1nzKy0ONS%2fKNDL9kYedWncVI9UD%2bCm0%3d&se=1485564598&skn=RootManageSharedAccessKey";
    private static final String publisherId = "vlad";
    static final String connectionString = "Endpoint=sb://smehns.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=UHRh7IyPWU4JwIH19CB8el+LmV4VkuZij58P00a9S0U=";
    static final String CONNECTION_STRING = "Endpoint=amqps://smehns.servicebus.windows.net;EntityPath=sensor-data-eventhub;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=UHRh7IyPWU4JwIH19CB8el+LmV4VkuZij58P00a9S0U=;OperationTimeout=PT1M;RetryPolicy=Default";

    private static final String EH_URL = "https://" + namespaceName +
            ".servicebus.windows.net/" + eventHubName +
            "/publishers/" + publisherId + "/messages";

    private final Context context;
    private final AsyncHttpClient client;

    public ServerSender(Context context) {
        this.context = context;
        this.client = new AsyncHttpClient();
        client.addHeader("Authorization", sasToken);
    }

    public void sendMessage(String message) {
        try {
            StringEntity entity = new StringEntity(message);
            client.post(context, EH_URL, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
