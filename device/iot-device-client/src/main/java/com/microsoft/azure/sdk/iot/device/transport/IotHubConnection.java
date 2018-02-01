/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package com.microsoft.azure.sdk.iot.device.transport;

import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.transport.amqps.AmqpsConvertToProtonReturnValue;

//maybe should not be abstract. Does HTTP have any difference with MQTT/AMQP? Keep abstract just so it is only used by implementations
public abstract class IotHubConnection
{
    protected IotHubConnectionStateCallback connectionStateCallback;
    protected Object connectionStateCallbackContext;

    protected IotHubConnectionStatusChangeCallback connectionStatusChangeCallback;
    protected Object connectionStatusChangeCallbackContext;
    protected IotHubConnectionStatus currentConnectionStatus;

    public IotHubConnection()
    {
        this.currentConnectionStatus = IotHubConnectionStatus.DISCONNECTED;

    }

    public void registerConnectionStateCallback(
            IotHubConnectionStateCallback connectionStateCallback,
            Object connectionStateCallbackContext)
    {
        this.connectionStateCallback = connectionStateCallback;
        this.connectionStateCallbackContext = connectionStateCallbackContext;
    }

    public void registerConnectionStatusChangeCallback(
            IotHubConnectionStatusChangeCallback connectionStatusChangeCallback,
            Object connectionStatusChangeCallbackContext)
    {
        this.connectionStatusChangeCallback = connectionStatusChangeCallback;
        this.connectionStatusChangeCallbackContext = connectionStatusChangeCallbackContext;
    }

    public void connectionEstablished()
    {
        if (this.connectionStateCallback != null)
        {
            this.connectionStateCallback.execute(IotHubConnectionState.CONNECTION_SUCCESS, this.connectionStateCallbackContext);
        }

        if (this.currentConnectionStatus != IotHubConnectionStatus.CONNECTED && this.connectionStatusChangeCallback != null)
        {
            this.currentConnectionStatus = IotHubConnectionStatus.CONNECTED;
            this.connectionStatusChangeCallback.execute(IotHubConnectionStatus.CONNECTED, IotHubConnectionStatusChangeReason.CONNECTION_OK, this.connectionStatusChangeCallbackContext);
        }
    }

    public void connectionLostAndRetrying(IotHubConnectionStatusChangeReason reason)
    {
        if (this.connectionStateCallback != null)
        {
            this.connectionStateCallback.execute(IotHubConnectionState.CONNECTION_DROP, this.connectionStateCallbackContext);
        }

        if (true)
        {
            //TODO
            //throw for cases where retrying is not appropriate?
        }

        if (this.connectionStatusChangeCallback != null)
        {
            this.connectionStatusChangeCallback.execute(IotHubConnectionStatus.DISCONNECTED_RETRYING, IotHubConnectionStatusChangeReason.NO_NETWORK, this.connectionStatusChangeCallbackContext);
        }
    }

    public void connectionLostAndNotRetrying(IotHubConnectionStatusChangeReason reason)
    {
        if (this.connectionStateCallback != null)
        {
            this.connectionStateCallback.execute(IotHubConnectionState.CONNECTION_DROP, this.connectionStateCallbackContext);
        }




        if (this.connectionStatusChangeCallback != null)
        {
            this.connectionStatusChangeCallback.execute(IotHubConnectionStatus.DISCONNECTED, reason, this.connectionStatusChangeCallbackContext);
        }
    }

    public void sasTokenExpired()
    {
        if (this.connectionStateCallback != null)
        {
            this.connectionStateCallback.execute(IotHubConnectionState.SAS_TOKEN_EXPIRED, this.connectionStateCallbackContext);
        }

        if (this.connectionStatusChangeCallback != null)
        {
            this.connectionStatusChangeCallback.execute(IotHubConnectionStatus.DISCONNECTED, IotHubConnectionStatusChangeReason.EXPIRED_SAS_TOKEN, this.connectionStatusChangeCallbackContext);
        }
    }
}
