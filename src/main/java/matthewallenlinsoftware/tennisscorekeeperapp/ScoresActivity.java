package matthewallenlinsoftware.tennisscorekeeperapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ScoresActivity extends Activity {

    String match_length, ten_point_tiebreaker_format;

    // Set scores
    TextView player_1_set_1_score_text_view;
    TextView player_2_set_1_score_text_view;
    TextView comma_between_set_1_and_2_text_view;

    TextView player_1_set_2_score_text_view;
    TextView set_2_dash_text_view;
    TextView player_2_set_2_score_text_view;
    TextView comma_between_set_2_and_3_text_view;

    TextView player_1_set_3_score_text_view;
    TextView set_3_dash_text_view;
    TextView player_2_set_3_score_text_view;

    // Game scores
    ImageView player_1_serving_image_view;
    TextView player_1_game_score_text_view;
    TextView player_2_game_score_text_view;
    ImageView player_2_serving_image_view;

    // Increment scores
    ImageButton increment_player_1_image_button;
    TextView player_1_text_view;
    TextView player_2_text_view;
    ImageButton increment_player_2_image_button;

    // Announcement
    TextView announcement_text_view;

    private void grabTenPointTiebreakActivityData() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            match_length = extras.getString("match_length");
            ten_point_tiebreaker_format = extras.getString("ten_point_tiebreaker_format");

            System.out.println("match_length: " + match_length);
            System.out.println("ten_point_tiebreaker_format: " + ten_point_tiebreaker_format);
        }
    }

    // onClick Transitions
    public void onClickResetButton(View view) {

    }

    public void onClickHomeButton(View view) {
        // Starting a new intent
        Intent nextScreen = new Intent(getApplicationContext(), MatchLengthActivity.class);

        // Sending data to another Activity
        startActivity(nextScreen);
    }

    // onClick Actions
    public void onClickIncrementPlayerOneScore(View view) {
        System.out.println("P1");
    }

    public void onClickIncrementPlayerTwoScore(View view) {
        System.out.println("P2");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        grabTenPointTiebreakActivityData();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
    }
}
