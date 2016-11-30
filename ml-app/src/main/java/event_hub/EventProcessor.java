package event_hub;

/**
 * Created by VladVin on 26.11.2016.
 */
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventprocessorhost.CloseReason;
import com.microsoft.azure.eventprocessorhost.IEventProcessor;
import com.microsoft.azure.eventprocessorhost.PartitionContext;
import storage.StorageContext;
import storage.StorageManager;

public class EventProcessor implements IEventProcessor
{
    private final StorageManager storageManager;

    public EventProcessor() {
        this.storageManager = StorageContext.getStorageManager();
    }

    @Override
    public void onOpen(PartitionContext context) throws Exception
    {
        System.out.println("SAMPLE: Partition " + context.getPartitionId() + " is opening");
    }

    @Override
    public void onClose(PartitionContext context, CloseReason reason) throws Exception
    {
        System.out.println("SAMPLE: Partition " + context.getPartitionId() + " is closing for reason " + reason.toString());
    }

    @Override
    public void onError(PartitionContext context, Throwable error)
    {
        System.out.println("SAMPLE: Partition " + context.getPartitionId() + " onError: " + error.toString());
    }

    @Override
    public void onEvents(PartitionContext context, Iterable<EventData> messages) throws Exception
    {
        System.out.println("SAMPLE: Partition " + context.getPartitionId() + " got message batch");
        for (EventData data : messages)
        {
            String json = new String(data.getBody(), "UTF8");

            System.out.println(json);

            if (storageManager != null) {
                storageManager.saveEventData(json);
            }
            context.checkpoint(data);
        }
    }
}

