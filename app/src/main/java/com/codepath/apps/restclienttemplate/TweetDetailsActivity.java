package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailsActivity extends AppCompatActivity {
    // the Tweet
    Tweet tweet;

    TextView tvUserName;
    TextView tvScreenName;
    TextView tvBody;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1DA1F2")));

        // resolve the view objects
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        // unwrap the movie passed in via intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvScreenName.setText("@" + tweet.user.screenName);
        tvUserName.setText(tweet.user.name);
        tvBody.setText(tweet.body);

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, margin)))
                .into(ivProfileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.close_compose:
                closeCompose();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeCompose() {
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data); // set result code and bundle data for response
        finish(); // closes the activity
    }
}
