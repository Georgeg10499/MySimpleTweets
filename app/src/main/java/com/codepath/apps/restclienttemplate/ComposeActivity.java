package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    EditText etCompose;
    Tweet tweet;
    TwitterClient client;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1DA1F2")));

        client = TwitterApp.getRestClient(this);
        etCompose = (EditText) findViewById(R.id.etCompose);
        progressBar = (ProgressBar) findViewById(R.id.pbLoading);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        String num = Integer.toString(140 - etCompose.getText().toString().length());
//        //tvCounter.setText(num);
//        return super.onKeyDown(keyCode, event);
//    }

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

    public void composeTweet(View view) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        client.sendTweet(etCompose.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    tweet = Tweet.fromJSON(response);

                    Intent data = new Intent();
                    // pass information back to previous intent call
                    data.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, data);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Send Text", errorResponse.toString());
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("Send Text", errorResponse.toString());
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Send Text", responseString);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });


    }

    // ActivityNamePrompt.java -- launched for a result
    public void onSubmit(View v) {
        EditText etName = (EditText) findViewById(R.id.etCompose);
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("name", etName.getText().toString());
        data.putExtra("code", 200); // ints work too
        // Activity finished ok, return_prev the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
