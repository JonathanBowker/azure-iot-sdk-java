/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package com.microsoft.azure.sdk.iot.device.transport.amqps;

import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnection;
import org.apache.qpid.proton.message.impl.MessageImpl;

import java.io.IOException;

public class AmqpsIotHubConnection extends IotHubConnection implements ServerListener
{
    private AmqpsIotHubBaseHandler amqpsIotHubBaseHandler;

    public AmqpsIotHubConnection(DeviceClientConfig config)
    {
        this.amqpsIotHubBaseHandler = new AmqpsIotHubBaseHandler(config);
    }

    protected void addListener(ServerListener serverListener)
    {
        this.amqpsIotHubBaseHandler.addListener(serverListener);
    }

    protected void open() throws IOException
    {
        this.amqpsIotHubBaseHandler.open();
    }

    protected void close() throws IOException
    {
        this.amqpsIotHubBaseHandler.close();
    }

    protected void authenticate() throws IOException
    {
        this.amqpsIotHubBaseHandler.authenticate();
    }

    protected AmqpsConvertToProtonReturnValue convertToProton(Message message) throws IOException
    {
        return this.amqpsIotHubBaseHandler.convertToProton(message);
    }

    public Integer sendMessage(MessageImpl messageImpl, MessageType messageType, IotHubConnectionString iotHubConnectionString) throws IOException
    {
        return this.amqpsIotHubBaseHandler.sendMessage(messageImpl, messageType, iotHubConnectionString);
    }

    public void addDeviceOperationSession(DeviceClientConfig config)
    {
        this.amqpsIotHubBaseHandler.addDeviceOperationSession(config);
    }

    public AmqpsConvertFromProtonReturnValue convertFromProton(AmqpsMessage receivedMessage, DeviceClientConfig deviceClientConfig) throws IOException
    {
        return this.amqpsIotHubBaseHandler.convertFromProton(receivedMessage, deviceClientConfig);
    }

    public Boolean sendMessageResult(AmqpsMessage receivedMessage, IotHubMessageResult result)
    {
        return this.amqpsIotHubBaseHandler.sendMessageResult(receivedMessage, result);
    }

    public void onConnectionEstablished()
    {
        this.connectionEstablished();
    }

    public void onConnectionLost()
    {
        //this.connectionLostAndNotRetrying();
    }

    public void onMessageSent(Integer messageHash, Boolean deliveryState) {}

    public void onMessageReceived(AmqpsMessage message) {}
}
