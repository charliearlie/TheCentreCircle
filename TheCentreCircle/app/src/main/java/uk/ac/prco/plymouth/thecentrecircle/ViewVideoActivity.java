package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class ViewVideoActivity extends AppCompatActivity {
    VideoView videoView;
    MyMediaController videoController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        String domain = getIntent().getStringExtra("domain");
        String videoUrl = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        //https://streamable.com/c2u1

        setupActionBar();

        if (!videoUrl.endsWith(".mp4") && domain.equals("streamable.com")) {
            String[] array = videoUrl.split("/");

            for (int i = 0; i < array.length; i++) {
                System.out.println(i + ": " + array[i]);
            }
            String streamableRef = array[3];

            videoUrl = "https://cdn.streamable.com/video/mp4/" + streamableRef + ".mp4";
        }

        videoView  = (VideoView) findViewById(R.id.videoView);
        videoController = new MyMediaController(this, (FrameLayout) findViewById(R.id.controllerAnchor));
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);


//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
        videoView.setMediaController(videoController);

        videoView.start();
    }

    public void onPrepared(MediaPlayer mp) {
        videoView.start();

        FrameLayout controllerAnchor = (FrameLayout) findViewById(R.id.controllerAnchor);
        videoController.setAnchorView(controllerAnchor);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public class MyMediaController extends MediaController
    {
        private FrameLayout anchorView;


        public MyMediaController(Context context, FrameLayout anchorView)
        {
            super(context);
            this.anchorView = anchorView;
        }

        @Override
        protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
        {
            super.onSizeChanged(xNew, yNew, xOld, yOld);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) anchorView.getLayoutParams();
            lp.setMargins(0, 0, 0, yNew);

            anchorView.setLayoutParams(lp);
            anchorView.requestLayout();
        }
    }
}
