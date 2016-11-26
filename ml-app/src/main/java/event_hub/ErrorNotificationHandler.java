package event_hub;

import com.microsoft.azure.eventprocessorhost.ExceptionReceivedEventArgs;

import java.util.function.Consumer;

/**
 * Created by VladVin on 26.11.2016.
 */
public class ErrorNotificationHandler implements Consumer<ExceptionReceivedEventArgs>
{
    @Override
    public void accept(ExceptionReceivedEventArgs t)
    {
        System.out.println("SAMPLE: Host " + t.getHostname() + " received general error notification during " + t.getAction() + ": " + t.getException().toString());
    }
}
