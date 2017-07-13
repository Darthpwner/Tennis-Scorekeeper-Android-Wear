package matthewallenlinsoftware.tennisscorekeeperapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ten_point_tiebreak);

        grabMatchLengthActivityData();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

    }
}
