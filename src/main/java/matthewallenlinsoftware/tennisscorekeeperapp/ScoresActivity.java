package matthewallenlinsoftware.tennisscorekeeperapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;

public class ScoresActivity extends Activity {

    String match_length, ten_point_tiebreaker_format;

    private void grabTenPointTiebreakActivityData() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            match_length = extras.getString("match_length");
            ten_point_tiebreaker_format = extras.getString("ten_point_tiebreaker_format");

            System.out.println("match_length: " + match_length);
            System.out.println("ten_point_tiebreaker_format: " + ten_point_tiebreaker_format);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        grabTenPointTiebreakActivityData();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
    }
}
