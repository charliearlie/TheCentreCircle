package uk.ac.prco.plymouth.thecentrecircle;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.keys.RedditEndPoints;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;

public class VideoListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ArrayList<JSONObject> videos = new ArrayList<>();
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        // add back arrow to toolbar
        setupActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Show the Up button in the action bar.
            Transition exitTrans = new Slide();
            getWindow().setExitTransition(exitTrans);

            Transition reenterTrans = new Slide();
            getWindow().setReenterTransition(reenterTrans);
        }
        RedditEndPoints rep = new RedditEndPoints();
        try {
            //Retrieve videos which are 'hot' from reddit.com/r/soccer
            new RetrieveRedditVideos().execute(rep.getSoccerTopMonth());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     **/
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    /**
     * Asynchronous tasks retrieving the JSON data from Reddit
     */
    public class RetrieveRedditVideos extends AsyncTask<String, Void, JSONArray> {

        /**
         * Retrieve the JSON from reddit on a second thread
         *
         * @param params Array of parameters. Reddit URL at 0th element
         * @return the JSON array retireved
         */
        @Override
        protected JSONArray doInBackground(String... params) {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray();
            try {
                String param = params[0];
                URL url = new URL(param);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoInput(true);

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                //Read the buffered data using our own string builder
                String returnedJson = new CCUtilities().readAllJson(bufferedReader);
                jsonObject = new JSONObject(returnedJson);
                System.out.println(jsonObject);
                jsonArray = jsonObject.getJSONObject("data").getJSONArray("children");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonArray;
        }

        /**
         * After the JSON Array is retrieved this method lists it for the user
         *
         * @param jsonArray The JSON Array retrieved in doInBackground
         */
        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    //Retrieve the data object from the ith object
                    JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("data");
                    String domain = (String) jsonObject.get("domain");

                    //As of now we only support Streamable or Gfycat
                    //TODO: Consider implementing the YouTube API
                    if (domain.equals("streamable.com") || domain.equals("gfycat.com")) {
                        videos.add(jsonObject);
                    }
                }

                listView = (ListView) findViewById(R.id.video_listView);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(VideoListActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1);
                listView.setAdapter(adapter);

                //Add each video's title to the list
                for (JSONObject vid : videos) {
                    adapter.add(vid.getString("title"));
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            //When a video is selected, open the View Video Activity
                            Intent intent = new Intent(VideoListActivity.this, ViewVideoActivity.class);
                            intent.putExtra("title", videos.get(position).getString("title"));
                            intent.putExtra("url", videos.get(position).getString("url"));
                            intent.putExtra("redditComments", videos.get(position).getString("permalink"));
                            intent.putExtra("domain", videos.get(position).getString("domain"));
                            startActivity(intent);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
