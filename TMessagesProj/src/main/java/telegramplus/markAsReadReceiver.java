package telegramplus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationsController;

import java.util.Iterator;



public class markAsReadReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Iterator it = NotificationsController.getInstance().getPushMessages().iterator();
        Log.i("TAG", "onStartCommand: ");

        while (it.hasNext()) {
            MessageObject messageObject = (MessageObject) it.next();
            MessagesController.getInstance().markDialogAsRead(messageObject.getDialogId(), messageObject.getId(), Math.max(0, messageObject.getId()), messageObject.messageOwner.date, true, true);
        }
    }
}
