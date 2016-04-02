package uk.ac.prco.plymouth.thecentrecircle;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

/**
 * Created by charliewaite on 27/03/2016.
 */
public class TheCentreCircle extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialise Facebook SDK and Firebase SDK for the life cycle of the application
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
    }
}
