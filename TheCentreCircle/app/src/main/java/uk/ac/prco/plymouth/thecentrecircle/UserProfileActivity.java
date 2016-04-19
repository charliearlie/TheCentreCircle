package uk.ac.prco.plymouth.thecentrecircle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

public class UserProfileActivity extends AppCompatActivity {

    Constants cons = new Constants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        Firebase ref = new Firebase(cons.getFirebaseUrl());
        AuthData authData = ref.getAuth();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImageView profileImageView = (ImageView) findViewById(R.id.user_profile_image);
        if (authData != null) {
            Picasso.with(getApplicationContext()).load((String) authData.getProviderData().get("profileImageURL"))
                    .into(profileImageView);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
