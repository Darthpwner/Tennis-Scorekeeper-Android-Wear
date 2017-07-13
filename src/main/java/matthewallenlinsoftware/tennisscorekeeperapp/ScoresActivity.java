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

    // Data from previous Activities
    String match_length, ten_point_tiebreaker_format;

    // Current set [1, 3]
    int current_set = 1;

    boolean is_tiebreak = false;

    // Player who won the match [0, 1] where 0 is P1 and 1 is P2
    // Used to send data back to Android app
    int player_won = -1;

    // Player serving [0, 1] where 0 is P1 and 1 is P2
    int player_serving = 0;

    // Tracks which player started serving in the tiebreak to handle the start of the next set
    int player_serving_to_start_tiebreak = -1;

    // Game score values
    int player_1_points_won_this_game = 0;
    int player_2_points_won_this_game = 0;

    //For score announcements
    String player_1_game_score_string = "Love";
    String player_2_game_score_string = "Love";

    // Set score values
    int player_1_set_1_score = 0;
    int player_2_set_1_score = 0;
    int player_1_set_2_score = 0;
    int player_2_set_2_score = 0;
    int player_1_set_3_score = 0;
    int player_2_set_3_score = 0;

    int[] set_winners = {-1, -1, -1};   // Indices can be 1 or 2

    // Widget connections
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
