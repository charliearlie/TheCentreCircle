package uk.ac.prco.plymouth.thecentrecircle;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by charliewaite on 27/03/2016.
 */
public class TheCentreCircle extends android.app.Application {

    private Tracker mTracker;

    public void startTracking() {
        if (mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);

            mTracker = ga.newTracker(R.xml.track_app);

            ga.enableAutoActivityReports(this);
            mTracker.enableAdvertisingIdCollection(true);


        }
    }

    public Tracker getTracker() {
        startTracking();

        return mTracker;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        //Initialise Facebook SDK and Firebase SDK for the life cycle of the application
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
    }
}
