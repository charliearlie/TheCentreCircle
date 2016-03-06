package uk.ac.prco.plymouth.thecentrecircle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class MyBetsActivity extends AppCompatActivity {

    Query queryRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bets);

        //Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://cwprco304.firebaseio.com");
        Firebase betRef = new Firebase("https://cwprco304.firebaseio.com/accumulators");
        AuthData authData = ref.getAuth();
        if (authData != null) {
            System.out.println(authData.getUid());
            queryRef = betRef.child("userId").equalTo(authData.getUid());
        }

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot);
                Toast.makeText(MyBetsActivity.this, dataSnapshot.getKey(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}
