package utils.jhy.ledger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import utils.jhy.ledger.dialog.AlldayNotification;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.buttonnoti).setOnClickListener(notiOnListner);
    }


    //노티관련
    View.OnClickListener notiOnListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NotificationManager manager = (NotificationManager) TestActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel("channel_id");
            if (null != channel && channel.getImportance() != NotificationManager.IMPORTANCE_NONE) {
                Log.i("jhy", "true");
                manager.cancel(9);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationChannel.setDescription("channel description");
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.GREEN);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
                    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                    manager.createNotificationChannel(notificationChannel);
                }
                AlldayNotification.notify(TestActivity.this, "asdf", 9);
            }

        }
    };
}
