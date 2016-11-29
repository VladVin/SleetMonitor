package event_hub;

import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorOptions;
import com.microsoft.azure.servicebus.ConnectionStringBuilder;
import storage.StorageManager;

import java.util.concurrent.ExecutionException;

/**
 * Created by VladVin on 26.11.2016.
 */
public class Receiver
{
    private static final String namespaceName = "winter-ns";
    private static final String eventHubName = "sdeh";
    private static final String sasKeyName = "RootManageSharedAccessKey";
    private static final String sasKey = "0Nsz3brk6a1M3J+oF3//VE+NXUG8b8x80FED5eAi0AI=";

    private static final String consumerGroupName = "$Default";

    private static final String storageAccountName = "smsdstorage";
    private static final String storageAccountKey = "0+PXyala92hE8uOuguA0XKg/wOG9YEm2YfGtdEpxuwuLyJt9CIReyfQ1gVRO8M7ggsjJCt2C88l2MItomaw2xA==";
    private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=" + storageAccountName + ";AccountKey=" + storageAccountKey;

    private final EventProcessorHost eph;

    public Receiver() {
        ConnectionStringBuilder eventHubConnectionString =
                new ConnectionStringBuilder(
                        namespaceName, eventHubName,
                        sasKeyName, sasKey);
        this.eph = new EventProcessorHost(
                eventHubName, consumerGroupName,
                eventHubConnectionString.toString(),
                storageConnectionString);
    }

    public void register() {
        System.out.println("Registering host named " + eph.getHostName());
        EventProcessorOptions options = new EventProcessorOptions();
        options.setExceptionNotification(new ErrorNotificationHandler());
        try
        {
            eph.registerEventProcessor(EventProcessor.class, options).get();
        }
        catch (Exception e)
        {
            System.out.print("Failure while registering: ");
            if (e instanceof ExecutionException)
            {
                Throwable inner = e.getCause();
                System.out.println(inner.toString());
            }
            else
            {
                System.out.println(e.toString());
            }
        }
    }
}

