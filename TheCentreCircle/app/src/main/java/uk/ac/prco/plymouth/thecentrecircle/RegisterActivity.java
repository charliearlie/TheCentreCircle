package uk.ac.prco.plymouth.thecentrecircle;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

public class RegisterActivity extends AppCompatActivity {

    //Profile details
    private String mDisplayName;
    private String mEmail;
    private String mPassword;
    private String mPasswordConfirm;
    private String mFavTeam;

    //Inputs to retrieve profile information
    private AutoCompleteTextView mDisplayNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;
    private AutoCompleteTextView mFavTeamView;
    private Spinner mFavTeamSpinner;

    //Team list to populate spinner
    private ArrayList<String> teamList;

    //Firebase URL for reference
    private Constants consts = new Constants();
    Firebase ref = new Firebase(consts.FIREBASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialise the AutoCompleteTextViews and Edit Texts
        mDisplayNameView = (AutoCompleteTextView) findViewById(R.id.register_display_name);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mPasswordConfirmView = (EditText) findViewById(R.id.register_confirm_password);
        mFavTeamSpinner = (Spinner) findViewById(R.id.register_spinner);

        //Query Firebase to retrieve all teams supported by the application in alphabetical order
        Query queryRef = ref.child("teams").orderByChild("name");

        //Single value event listener as the list only needs to be populated once
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teamList = new ArrayList<String>();
                teamList.add("Favourite team...");
                for (DataSnapshot teamSnapshot : dataSnapshot.getChildren()) {
                    teamList.add(teamSnapshot.child("name").getValue(String.class));
                }

                mFavTeamSpinner.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, teamList));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.complete_registration_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        mFavTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFavTeam = teamList.get(position);
                Log.v("TEAM SELECTED", teamList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    public Boolean registerUser() {
        //Retrieve the input from the user
        mDisplayName = mDisplayNameView.getText().toString();
        mEmail = mEmailView.getText().toString().trim();
        if (validateEmail(mEmail)) {
            mPassword = mPasswordView.getText().toString();
            mPasswordConfirm = mPasswordConfirmView.getText().toString();

            //Ensure the passwords match
            if (mPassword.equals(mPasswordConfirm)) {
                boolean isRegistered = false;
                //Create the user using the Firebase SDK
                ref.createUser(mEmail, mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Toast toast = Toast.makeText(RegisterActivity.this, mEmail + " successfully registered",
                                Toast.LENGTH_LONG);
                        toast.show();
                        //As the user was created successfully we'll log them in to the application
                        ref.authWithPassword(mEmail, mPassword, new Firebase.AuthResultHandler() {

                            @Override
                            public void onAuthenticated(AuthData authData) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("provider", authData.getProvider());
                                map.put("displayName", mDisplayName);
                                map.put("favTeam", mFavTeam);
                                Date date = new Date();
                                map.put("dateJoined", date.toString());
                                if (authData.getProviderData().containsKey("email")) {
                                    map.put("email", authData.getProviderData().get("email").toString());
                                }
                                ref.child("users").child(authData.getUid()).setValue(map);
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.putExtra("userEmail", mEmail);
                                intent.putExtra("userLogged", true);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        System.out.println(firebaseError);
                    }
                });
            } else {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setTitle("Password error")
                        .setMessage("Your passwords do not match")
                        .setPositiveButton("OK", null)
                        .setCancelable(true)
                        .setIcon(R.mipmap.ic_centrecircle)
                        .create().show();
                return false;
            }


            return true;
        } else {
            mEmailView.setError("This email address is invalid");
            return false;
        }

    }

    /**
     * Returns a boolean value dependant on if the users inputted email is valid
     * @param email Email the user inputted
     * @return True if the email is valid, false if not
     */
    public boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
