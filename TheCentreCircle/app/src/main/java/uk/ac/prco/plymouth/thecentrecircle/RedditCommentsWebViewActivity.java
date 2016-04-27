package uk.ac.prco.plymouth.thecentrecircle;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.keys.RedditEndPoints;

/**
 * This class is entirely experimental
 */
public class RedditCommentsWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit_comments_web_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Show the Up button in the action bar.
            Transition enterTrans = new Slide();
            getWindow().setEnterTransition(enterTrans);

            Transition returnTrans = new Slide();
            getWindow().setReturnTransition(returnTrans);
        }




        String url = getIntent().getStringExtra("redditPermalink");

        url = new RedditEndPoints().REDDIT_URL + url;

        System.out.println(url);

        WebView redditCommentsWebView = (WebView) findViewById(R.id.reddit_web_view);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_web);

        if (redditCommentsWebView != null) {
            redditCommentsWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }});
            redditCommentsWebView.loadUrl(url);
            progressBar.setVisibility(View.GONE);

        }

    }
}
