package uk.ac.prco.plymouth.thecentrecircle;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;

import java.util.Random;

import uk.ac.prco.plymouth.thecentrecircle.classes.Match;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MatchNotificationService extends IntentService {
    public MatchNotificationService() {
        super("MatchNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Match match = (Match) intent.getSerializableExtra("match");
        showNotification(match);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Method which builds the notification then alerts the user to a change in score
     *
     * @param match The match in which the score has changed
     */
    private void showNotification(final Match match) {

        //Store the app logo as a bitmap so it can be the icon of the notification
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_centrecircle);

        //Build the intent to open the match detail activity form the notification
        Intent intent = new Intent(this, MatchDetailTabbedActivity.class);
        intent.putExtra("matchId", match.getMatchId());
        intent.putExtra("matchDate", match.getDate());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MatchDetailTabbedActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_centrecircle)
                .setLargeIcon(bitmap)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText("GOAL! " + match.getHomeTeam() + " " + match.getHomeScore() +
                        " : " + match.getAwayScore() + " " + match.getAwayTeam())
                .build();

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m, notification);
    }
}
