// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package samples.com.microsoft.azure.sdk.iot;

import com.microsoft.azure.sdk.iot.device.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/** Sends a number of event messages to an IoT Hub. */
public class SendEvent
{
    
    private  static final int D2C_MESSAGE_TIMEOUT = 2000; // 2 seconds
    private  static List failedMessageListOnClose = new ArrayList(); // List of messages that failed on close
  
    protected static class EventCallback implements IotHubEventCallback
    {
        public void execute(IotHubStatusCode status, Object context)
        {
            Message msg = (Message) context;
            
            System.out.println("IoT Hub responded to message "+ msg.getMessageId()  + " with status " + status.name());
            
            if (status==IotHubStatusCode.MESSAGE_CANCELLED_ONCLOSE)
            {
                failedMessageListOnClose.add(msg.getMessageId());
            }
        }
    }

    /**
     * Sends a number of messages to an IoT Hub. Default protocol is to 
     * use MQTT transport.
     *
     * @param args 
     * args[0] = IoT Hub connection string
     * args[1] = number of requests to send
     * args[2] = protocol (optional, one of 'mqtt' or 'amqps' or 'https' or 'amqps_ws')
     * args[3] = path to certificate to enable one-way authentication over ssl for amqps (optional, default shall be used if unspecified).
     */
    public static void main(String[] args)
            throws IOException, URISyntaxException, InterruptedException
    {
        String connString = "HostName=TimsPOCHub.azure-devices.net;DeviceId=3;SharedAccessKey=DLd3FOtkBCDF/MOToi3WTqqTwhpdT0qDRGzpF1SYRfs=";
        IotHubClientProtocol protocol = IotHubClientProtocol.AMQPS;

        DeviceClient deviceClient = null;

        if (protocol == IotHubClientProtocol.AMQPS)
        {
            TransportClient transportClient = new TransportClient(protocol);
            deviceClient = new DeviceClient(connString, transportClient);
            transportClient.open();
        }
        else
        {
            deviceClient = new DeviceClient(connString, protocol);
        }



        deviceClient.registerConnectionStatusChangeCallback(new IotHubConnectionStatusChangeCallback()
        {
            @Override
            public void execute(IotHubConnectionStatus status, IotHubConnectionStatusChangeReason statusChangeReason, Object callbackContext)
            {
                System.out.println(status + " " + statusChangeReason);
            }
        }, new Object());

        deviceClient.open();


        while (true)
        {
            deviceClient.sendEventAsync(new Message("asdf"), new EventCallback(), new Message("asdf"));
            Thread.sleep(1000);
            System.out.println("Running...");
        }
    }
}
