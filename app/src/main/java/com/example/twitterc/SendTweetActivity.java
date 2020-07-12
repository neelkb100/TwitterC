package com.example.twitterc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextSendTweet;
    private ListView listViewUsersTweet;
    private Button buttonViewTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        editTextSendTweet = findViewById(R.id.edtSendTweet);
        listViewUsersTweet = findViewById(R.id.listViewFeeds);
        buttonViewTweets = findViewById(R.id.btnGetUserFeeds);
        buttonViewTweets.setOnClickListener(this);

    }

    public void sendTweet(View view){

        ParseObject parseObject = new ParseObject("MyTweets");
        parseObject.put("tweet",editTextSendTweet.getText().toString());
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());
        final ProgressDialog dialog = new ProgressDialog(this);



        dialog.setMessage("Loading...");
        dialog.show();

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){

                    FancyToast.makeText(SendTweetActivity.this,
                            ParseUser.getCurrentUser().getUsername()+" 's tweet"+" ("+editTextSendTweet.getText().toString()+") "+" is saved !!",
                            FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                } else {

                    FancyToast.makeText(SendTweetActivity.this,
                            "Error : "+e.getMessage(),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }

                dialog.dismiss();
            }
        });


    }

    @Override
    public void onClick(View v) {

        final ArrayList<HashMap<String,String>> tweetsList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter( SendTweetActivity.this,
                tweetsList,android.R.layout.simple_list_item_2,new String[]{"tweetUserName","tweetValue"},
                new int[]{android.R.id.text1,android.R.id.text2});

        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweets");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if(objects.size()>0 && e == null){

                        for(ParseObject tweetObject : objects){

                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName",tweetObject.getString("user"));
                            userTweet.put("tweetValue",tweetObject.getString("tweet"));
                            tweetsList.add(userTweet);

                        }

                        listViewUsersTweet.setAdapter(adapter);
                    }


                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}