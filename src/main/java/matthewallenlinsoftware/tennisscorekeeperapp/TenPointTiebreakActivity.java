package matthewallenlinsoftware.tennisscorekeeperapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;

public class TenPointTiebreakActivity extends Activity {

    String match_length;

    private void  grabMatchLengthActivityData() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            match_length = extras.getString("match_length");

            String temp = "";
            Log.i(temp, match_length);
            System.out.println("match_length: " + match_length);
        }
    }

    // onClick Transitions
    public void onClickYesButton(View view) {
        // Starting a new intent
        Intent nextScreen = new Intent(getApplicationContext(), ScoresActivity.class);

        nextScreen.putExtra("match_length", match_length);
        nextScreen.putExtra("ten_point_tiebreaker_format", "yes");

        // Sending data to another Activity
        startActivity(nextScreen);
    }

    public void onClickNoButton(View view) {
        // Starting a new intent
        Intent nextScreen = new Intent(getApplicationContext(), ScoresActivity.class);

        nextScreen.putExtra("match_length", match_length);
        nextScreen.putExtra("ten_point_tiebreaker_format", "no");

        // Sending data to another Activity
        startActivity(nextScreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ten_point_tiebreak);

        grabMatchLengthActivityData();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

    }
}
