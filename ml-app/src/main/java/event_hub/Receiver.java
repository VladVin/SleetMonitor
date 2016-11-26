package event_hub;

import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorOptions;
import com.microsoft.azure.servicebus.ConnectionStringBuilder;

import java.util.concurrent.ExecutionException;

/**
 * Created by VladVin on 26.11.2016.
 */
public class Receiver
{
    private static final String consumerGroupName = "$Default";
    private static final String namespaceName = "smehns";
    private static final String eventHubName = "sensor-data-eventhub";
    private static final String sasKeyName = "RootManageSharedAccessKey";
    private static final String sasKey = "UHRh7IyPWU4JwIH19CB8el+LmV4VkuZij58P00a9S0U=";

    private static final String storageAccountName = "smsdstorage";
    private static final String storageAccountKey = "0+PXyala92hE8uOuguA0XKg/wOG9YEm2YfGtdEpxuwuLyJt9CIReyfQ1gVRO8M7ggsjJCt2C88l2MItomaw2xA==";
    private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=" + storageAccountName + ";AccountKey=" + storageAccountKey;

    public static void main(String args[])
    {
        ConnectionStringBuilder eventHubConnectionString = new ConnectionStringBuilder(namespaceName, eventHubName, sasKeyName, sasKey);

        EventProcessorHost host = new EventProcessorHost(eventHubName, consumerGroupName, eventHubConnectionString.toString(), storageConnectionString);

        System.out.println("Registering host named " + host.getHostName());
        EventProcessorOptions options = new EventProcessorOptions();
        options.setExceptionNotification(new ErrorNotificationHandler());
        try
        {
            host.registerEventProcessor(EventProcessor.class, options).get();
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

        System.out.println("Press enter to stop");
        try
        {
            System.in.read();
            host.unregisterEventProcessor();

            System.out.println("Calling forceExecutorShutdown");
            EventProcessorHost.forceExecutorShutdown(120);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        System.out.println("End of sample");
    }
}

