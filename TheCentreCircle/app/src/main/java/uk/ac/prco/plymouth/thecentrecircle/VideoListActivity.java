package uk.ac.prco.plymouth.thecentrecircle;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.client.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.classes.Competition;
import uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.keys.RedditEndPoints;

public class VideoListActivity extends AppCompatActivity {


    ArrayList<JSONObject> videos = new ArrayList<>();
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        RedditEndPoints rep = new RedditEndPoints();
        try {
            new RetrieveRedditVideos().execute(rep.getSoccerHot());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public class RetrieveRedditVideos extends AsyncTask<String, Void, JSONArray> {

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

                String returnedJson = readAll(bufferedReader);
                jsonObject = new JSONObject(returnedJson);
                System.out.println(jsonObject);
                jsonArray = jsonObject.getJSONObject("data").getJSONArray("children");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("data");
                    String domain = (String) jsonObject.get("domain");
                    if(domain.equals("streamable.com") || domain.equals("gfycat.com")) {
                        String title = jsonObject.getString("title");
                        String videoUrl = (String) jsonObject.get("url");
                        String permalink = "https://reddit.com" +  (String) jsonObject.get("permalink");
                        videos.add(jsonObject);
                        System.out.println("Title: " + title +
                                " found at the following URL: " + videoUrl + " -- " +
                                "You can read the comments at: " + permalink );
                    }
                }

                listView = (ListView) findViewById(R.id.video_listView);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(VideoListActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1);
                listView.setAdapter(adapter);

                for (JSONObject vid : videos) {
                    adapter.add(vid.getString("title"));
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            String title = videos.get(position).getString("title");
                            String url = videos.get(position).getString("url");
                            String redditComments = videos.get(position).getString("permalink");
                            Intent intent = new Intent(VideoListActivity.this, ViewVideoActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("url", url);
                            intent.putExtra("redditComments", redditComments);
                            intent.putExtra("domain", videos.get(position).getString("domain"));
                            Toast.makeText(VideoListActivity.this, "my id " + id,
                                    Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        } catch (Exception ex) {

                        }
                    }
                });

            }
            catch (Exception ex) {
                ex.printStackTrace();
            }



        }

        /**
         * Convert JSON from it's byte representation to String
         * @param rd
         * @return
         * @throws IOException
         */
        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();

            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
    }
}
