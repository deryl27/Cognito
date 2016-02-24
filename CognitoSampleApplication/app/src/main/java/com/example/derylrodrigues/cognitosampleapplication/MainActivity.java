package com.example.derylrodrigues.cognitosampleapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.mobileconnectors.cognito.*;
import com.amazonaws.mobileconnectors.cognito.exceptions.*;
import com.amazonaws.AmazonClientException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText playerName, playerScore, cognitoplayerName, cognitoplayerScore;
    CognitoSyncManager syncClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerName = (EditText) findViewById(R.id.playerNameEditText);
        playerScore = (EditText) findViewById(R.id.playerScoreEditText);
        cognitoplayerName = (EditText) findViewById(R.id.cognitoplayerNameEditText);
        cognitoplayerScore = (EditText) findViewById(R.id.cognitoplayerScoreEditText);

        playerName.setText("NUMAD16S");
        playerScore.setText("123045");


        // COGNITO CODE
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "Your Pool ID", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );



        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void submit(View v)
    {
        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        Log.d("DERYL", "dataset >>>>>>>>>>>" + dataset);
        dataset.put("playerName", playerName.getText().toString());
        dataset.put("currentLevel","29");
        dataset.put("highScore", playerScore.getText().toString());
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                Log.d("DERYL", "SUCCESS >>>>");
            }
        });

    }



    public void retrieve(View v)
    {
        // Get a record in a dataset and display on the interface
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        Log.d("DERYL", "dataset >>>>>>>>>>>" + dataset);
        cognitoplayerName.setText(dataset.get("playerName"));
        cognitoplayerScore.setText(dataset.get("highScore"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
