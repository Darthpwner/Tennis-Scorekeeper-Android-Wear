package matthewallenlinsoftware.tennisscorekeeperapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        grabTenPointTiebreakActivityData();

        // Initialize widgets
        // Set scores
        player_1_set_1_score_text_view = (TextView) findViewById(R.id.player_1_set_1_score_text_view);
        player_2_set_1_score_text_view = (TextView) findViewById(R.id.player_2_set_1_score_text_view);
        comma_between_set_1_and_2_text_view = (TextView) findViewById(R.id.comma_between_set_1_and_2_text_view);

        player_1_set_2_score_text_view = (TextView) findViewById(R.id.player_1_set_2_score_text_view);
        set_2_dash_text_view = (TextView) findViewById(R.id.set_2_dash_text_view);
        player_2_set_2_score_text_view = (TextView) findViewById(R.id.player_2_set_2_score_text_view);
        comma_between_set_2_and_3_text_view = (TextView) findViewById(R.id.comma_between_set_2_and_3_text_view);

        player_1_set_3_score_text_view = (TextView) findViewById(R.id.player_1_set_3_score_text_view);
        set_3_dash_text_view = (TextView) findViewById(R.id.set_3_dash_text_view);
        player_2_set_3_score_text_view = (TextView) findViewById(R.id.player_2_set_3_score_text_view);

        // Game scores
        player_1_serving_image_view = (ImageView) findViewById(R.id.player_1_serving_image_view);
        player_1_game_score_text_view = (TextView) findViewById(R.id.player_1_game_score_text_view);
        player_2_game_score_text_view = (TextView) findViewById(R.id.player_2_game_score_text_view);
        player_2_serving_image_view = (ImageView) findViewById(R.id.player_2_serving_image_view);

        // Increment scores
        increment_player_1_image_button = (ImageButton) findViewById(R.id.increment_player_1_image_button);
        player_1_text_view = (TextView) findViewById(R.id.player_1_text_view);
        player_2_text_view = (TextView) findViewById(R.id.player_2_text_view);
        increment_player_2_image_button = (ImageButton) findViewById(R.id.increment_player_2_image_button);

        // Announcement
        announcement_text_view = (TextView) findViewById(R.id.announcement_text_view);

        // Set up initial Activity
        initialize();
    }

    public void initialize() {
        if(player_serving == 0) {   //P1 (left side) always starts serving
            player_1_serving_image_view.setVisibility(View.VISIBLE);
            player_2_serving_image_view.setVisibility(View.INVISIBLE);
        }

        //Set the Speech rate
//        myUtterance.rate = 0.3

        // Show only first set scores if best of 1 format
        if(match_length.equals("best_of_1")) {
            comma_between_set_1_and_2_text_view.setVisibility(View.INVISIBLE);;
            player_1_set_2_score_text_view.setVisibility(View.INVISIBLE);
            set_2_dash_text_view.setVisibility(View.INVISIBLE);
            player_2_set_2_score_text_view.setVisibility(View.INVISIBLE);
            comma_between_set_2_and_3_text_view.setVisibility(View.INVISIBLE);
            player_1_set_3_score_text_view.setVisibility(View.INVISIBLE);
            set_3_dash_text_view.setVisibility(View.INVISIBLE);
            player_2_set_3_score_text_view.setVisibility(View.INVISIBLE);
        }

        //If 10 point tiebreaker format for the final set, adjust certain conditions
        if(ten_point_tiebreaker_format.equals("yes")) {
            if(match_length.equals("best_of_1")) {
                is_tiebreak = true;
            }
        }

        // Link data together
        //updateApplicationContext();

//        print("Activated scores")
    }

    // onClick Transitions
    public void onClickResetButton(View view) {
        System.out.println("player_1_game_score_text_view: " + player_1_game_score_text_view);

        // Reset game score
        player_1_game_score_text_view.setText("0");
        player_2_game_score_text_view.setText("0");

        player_1_points_won_this_game = 0;
        player_2_points_won_this_game = 0;

        // Reset set scores
        player_1_set_1_score = 0;
        player_1_set_1_score_text_view.setText("0");

        player_2_set_1_score = 0;
        player_2_set_1_score_text_view.setText("0");

        player_1_set_2_score = 0;
        player_1_set_2_score_text_view.setText("0");

        player_2_set_2_score = 0;
        player_2_set_2_score_text_view.setText("0");

        player_1_set_3_score = 0;
        player_1_set_3_score_text_view.setText("0");

        player_2_set_3_score = 0;
        player_2_set_3_score_text_view.setText("0");

        current_set = 1;

        player_serving = 0;

        player_serving_to_start_tiebreak = -1;

        player_won = -1;

        player_1_game_score_string = "Love";
        player_2_game_score_string = "Love";

        player_1_text_view.setTextColor(Color.WHITE);
        player_1_text_view.setTextColor(Color.WHITE);

        if(player_serving == 0) {   //P1 (left side) always starts serving
            player_1_serving_image_view.setVisibility(View.VISIBLE);
            player_2_serving_image_view.setVisibility(View.INVISIBLE);
        }

        announcement_text_view.setVisibility(View.INVISIBLE);

        if(ten_point_tiebreaker_format.equals("yes") && match_length.equals("best_of_1")) {   //Best of 1 set and 10-point tiebreaker
            is_tiebreak = true;
        } else {    //Any other case of resetting
            is_tiebreak = false;
        }

        //Allow the player to use increment buttons if they hit reset at the end of match
        increment_player_1_image_button.setEnabled(true);
        increment_player_2_image_button.setEnabled(true);
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

        player_1_points_won_this_game += 1;

        if(!is_tiebreak) {
            if(player_1_points_won_this_game >= 4 && player_1_points_won_this_game - player_2_points_won_this_game >= 2) {  //Game: Player 1
                player_1_game_score_text_view.setText("0");
                player_2_game_score_text_view.setText("0");

                player_1_points_won_this_game = 0;
                player_2_points_won_this_game = 0;

                changeServer();

                // Update set score
                if(current_set == 1) {
                    player_1_set_1_score += 1;
                    player_1_set_1_score_text_view.setText(Integer.toString(player_1_set_1_score));

                    // Player 1 won Set 1
                    if(player_1_set_1_score == 6 && player_2_set_1_score <= 4) {    // 6-0, 6-1, 6-2, 6-3, 6-4

                        current_set += 1;

                        set_winners[0] = 1;

                        if(match_length.equals("best_of_1")) {   // If best of 1 set, Player 1 wins the match
                            gameSetMatchAnnouncement("P1");
                        } else {
                            // Announce "Set 1: P1"
                            setAnnouncement("P1", "1");
                            matchScoreAnnouncement();
                        }
                    } else if(player_1_set_1_score == 7 && player_2_set_1_score == 5) { // 7-5

                        current_set += 1;

                        set_winners[0] = 1;

                        if(match_length.equals("best_of_1")) {    //If best of 1 set, Player 1 wins the match
                            gameSetMatchAnnouncement("P1");
                        } else {
                            // Announce "Set 1: P1"
                            setAnnouncement("P1", "1");
                            matchScoreAnnouncement();
                        }
                    } else if(player_1_set_1_score == 6 && player_2_set_1_score == 6) { // Enter tiebreak
                        is_tiebreak = true;
                        player_serving_to_start_tiebreak = player_serving;

                        // Announce "Game: P1"
                        gameAnnouncement("P1");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_1_score, player_2_set_1_score);
                    } else {    //Normal Game announcement
                        // Announce "Game: P1"
                        gameAnnouncement("P1");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_1_score, player_2_set_1_score);
                    }
                } else if(current_set == 2) {
                    player_1_set_2_score += 1;
                    player_1_set_2_score_text_view.setText(Integer.toString(player_1_set_2_score));

                    // Player 1 won Set 2
                    if(player_1_set_2_score == 6 && player_2_set_2_score <= 4) {    //6-0, 6-1, 6-2, 6-3, 6-4

                        current_set += 1;

                        set_winners[1] = 1;

                        if(set_winners[0] == set_winners[1]) {  //Player 1 wins the match
                            gameSetMatchAnnouncement("P1");
                        } else {
                            // Announce "Set 2: P1"
                            setAnnouncement("P1", "2");
                            matchScoreAnnouncement();

                            if(ten_point_tiebreaker_format.equals("yes")) { //Start 10-point tiebreaker for the 3rd set
                                is_tiebreak = true;
                            }
                        }


                    } else if(player_1_set_2_score == 7 && player_2_set_2_score == 5) { //7-5

                        current_set += 1;

                        set_winners[1] = 1;

                        if(set_winners[0] == set_winners[1]) {  //Player 1 wins the match
                            gameSetMatchAnnouncement("P1");
                        } else {
                            // Announce "Set 2: P1"
                            setAnnouncement("P1", "2");
                            matchScoreAnnouncement();

                            if(ten_point_tiebreaker_format.equals("yes")) { //Start 10-point tiebreaker for the 3rd set
                                is_tiebreak = true;
                            }
                        }
                    } else if(player_1_set_2_score == 6 && player_2_set_2_score == 6) { //Enter tiebreak
                        is_tiebreak = true;
                        player_serving_to_start_tiebreak = player_serving;

                        // Announce "Game: P1"
                        gameAnnouncement("P1");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_2_score, player_2_set_2_score);
                    } else {    //Normal Game announcement
                        // Announce "Game: P1"
                        gameAnnouncement("P1");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_2_score, player_2_set_2_score);
                    }

                } else {    //Set 3
                    player_1_set_3_score += 1;
                    player_1_set_3_score_text_view.setText(Integer.toString(player_1_set_3_score));

                    // Player 1 won Set 3
                    if(player_1_set_3_score == 6 && player_2_set_3_score <= 4) {    //6-0, 6-1, 6-2, 6-3, 6-4
                        set_winners[2] = 1;

                        gameSetMatchAnnouncement("P1");

                        current_set += 1;
                    } else if(player_1_set_3_score == 7 && player_2_set_3_score == 5) {  //7-5
                        set_winners[2] = 1;

                        gameSetMatchAnnouncement("P1");

                        current_set += 1;
                    } else if(player_1_set_3_score == 6 && player_2_set_3_score == 6) { //Enter tiebreak
                        is_tiebreak = true;
                        player_serving_to_start_tiebreak = player_serving;

                        // Announce "Game: P1"
                        gameAnnouncement("P1");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_3_score, player_2_set_3_score);
                    } else {    //Normal Game announcement
                        // Announce "Game: P1"
                        gameAnnouncement("P1");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_3_score, player_2_set_3_score);
                    }
                }

                // Link data together
//                updateApplicationContext()

                return;
            } else if(player_1_points_won_this_game == 1) {
                player_1_game_score_text_view.setText("15");
            } else if(player_1_points_won_this_game == 2) {
                player_1_game_score_text_view.setText("30");
            } else if(player_1_points_won_this_game == 3) {
                player_1_game_score_text_view.setText("40");
            } else if(player_1_points_won_this_game - player_2_points_won_this_game == 0) {
                player_1_game_score_text_view.setText("40");  //Deuce
                player_2_game_score_text_view.setText("40");
            } else if(player_1_points_won_this_game - player_2_points_won_this_game == 1) {
                player_1_game_score_text_view.setText("AD"); //Advantage: Player 1
            }

            //Announce game score
            obtainGameScore();

            if(player_serving == 0) {
                gameScoreAnnouncement(player_1_game_score_string, player_2_game_score_string);
            } else {
                gameScoreAnnouncement(player_2_game_score_string, player_1_game_score_string);
            }

            // Link data together
//            updateApplicationContext();
        } else {    // Tiebreaker
            player_1_game_score_text_view.setText(Integer.toString(player_1_points_won_this_game));

            if((player_1_points_won_this_game + player_2_points_won_this_game) % 2 == 1) {
                changeServer();
            }

            //10-point tiebreak logic
            if(ten_point_tiebreaker_format.equals("yes")) { //10-point tiebreaker for final set
                if(match_length.equals("best_of_1") || (match_length.equals("best_of_3") && current_set == 3)) {
                    if(player_1_points_won_this_game >= 10 && player_1_points_won_this_game - player_2_points_won_this_game >= 2) {
                        player_1_game_score_text_view.setText("0");
                        player_2_game_score_text_view.setText("0");

                        player_1_points_won_this_game = 0;
                        player_2_points_won_this_game = 0;

                        if(current_set == 1) {  //P1 wins 1st set 10-point tiebreak
                            player_1_set_1_score += 1;
                            player_1_set_1_score_text_view.setText(Integer.toString(player_1_set_1_score));
                            set_winners[0] = 1;

                            gameSetMatchAnnouncement("P1");

                            // Link data together
//                            updateApplicationContext()

                            return;
                        } else {    //P1 wins 3rd set 10-point tiebreak
                            player_1_set_3_score += 1;
                            player_1_set_3_score_text_view.setText(Integer.toString(player_1_set_3_score));
                            set_winners[2] = 1;

                            gameSetMatchAnnouncement("P1");

                            // Link data together
                            //updateApplicationContext();

                            return;
                        }
                    } else {    // Still in the the tiebreak so call the score
                        //Announce game score

                        if(player_serving == 0) {
                            gameScoreAnnouncement(Integer.toString(player_1_points_won_this_game), Integer.toString(player_2_points_won_this_game));
                        } else {
                            gameScoreAnnouncement(Integer.toString(player_2_points_won_this_game), Integer.toString(player_1_points_won_this_game));
                        }

                        // Link data together
                        //updateApplicationContext();

                        return;
                    }
                }
            }

            if(player_1_points_won_this_game >= 7 && player_1_points_won_this_game - player_2_points_won_this_game >= 2) {

                player_1_game_score_text_view.setText("0");
                player_2_game_score_text_view.setText("0");

                player_1_points_won_this_game = 0;
                player_2_points_won_this_game = 0;

                // Changes server to the player who received first to the start the tiebreaker
                if(player_serving_to_start_tiebreak == player_serving) {
                    changeServer();
                }

                //Update set score corresponding to set
                if(current_set == 1) {

                    current_set += 1;

                    player_1_set_1_score += 1;
                    player_1_set_1_score_text_view.setText(Integer.toString(player_1_set_1_score));

                    set_winners[0] = 1;

                    if(match_length.equals("best_of_1")) {    //If best of 1 set, Player 1 wins the match
                        gameSetMatchAnnouncement("P1");
                    } else {
                        // Announce "Set 1: P1"
                        setAnnouncement("P1", "1");
                        matchScoreAnnouncement();
                    }
                } else if (current_set == 2) {

                    current_set += 1;

                    player_1_set_2_score += 1;
                    player_1_set_2_score_text_view.setText(Integer.toString(player_1_set_2_score));

                    set_winners[1] = 1;

                    if(set_winners[0] == set_winners[1]) {  //Player 1 wins
                        gameSetMatchAnnouncement("P1");
                    } else {
                        // Announce "Set 2: P1"
                        setAnnouncement("P1", "2");
                        matchScoreAnnouncement();

                        if(ten_point_tiebreaker_format.equals("yes")) { //Start 10-point tiebreaker for the 3rd set
                            is_tiebreak = true;
                        }
                    }
                } else {    //Set 3

                    current_set += 1;

                    player_1_set_3_score += 1;
                    player_1_set_3_score_text_view.setText(Integer.toString(player_1_set_3_score));

                    set_winners[2] = 1;

                    gameSetMatchAnnouncement("P1");
                }

                is_tiebreak = false;
            } else {    // Still in the the tiebreak so call the score
                //Announce game score
                obtainGameScore();

                if(player_serving == 0) {
                    gameScoreAnnouncement(Integer.toString(player_1_points_won_this_game), Integer.toString(player_2_points_won_this_game));
                } else {
                    gameScoreAnnouncement(Integer.toString(player_2_points_won_this_game), Integer.toString(player_1_points_won_this_game));
                }
            }

            // Link data together
//            updateApplicationContext()
        }

//        print(player_1_points_won_this_game)
//        print(player_2_points_won_this_game)
    }

    public void onClickIncrementPlayerTwoScore(View view) {
        System.out.println("P2");

        player_2_points_won_this_game += 1;

//        print("is_tiebreak: \(is_tiebreak)")

        if(!is_tiebreak) {
            if(player_2_points_won_this_game >= 4 && player_2_points_won_this_game - player_1_points_won_this_game >= 2) {    //Game: Player 2
                player_1_game_score_text_view.setText("0");
                player_2_game_score_text_view.setText("0");

                player_1_points_won_this_game = 0;
                player_2_points_won_this_game = 0;

                changeServer();

                // Update set score
                if(current_set == 1) {
                    player_2_set_1_score += 1;
                    player_2_set_1_score_text_view.setText(Integer.toString(player_2_set_1_score));

                    // Player 2 won Set 1
                    if(player_2_set_1_score == 6 && player_1_set_1_score <= 4) {    //6-0, 6-1, 6-2, 6-3, 6-4

                        current_set += 1;

                        set_winners[0] = 2;

                        if(match_length.equals("best_of_1")) {    //If best of 1 set, Player 2 wins the match
                            gameSetMatchAnnouncement("P2");
                        } else {
                            // Announce "Set 1: P2"
                            setAnnouncement("P2", "1");
                            matchScoreAnnouncement();
                        }
                    } else if(player_2_set_1_score == 7 && player_1_set_1_score == 5) {  //7-5

                        current_set += 1;

                        set_winners[0] = 2;

                        if(match_length.equals("best_of_1")) {    //If best of 1 set, Player 2 wins the match
                            gameSetMatchAnnouncement("P2");
                        } else {
                            // Announce "Set 1: P2"
                            setAnnouncement("P2", "1");
                            matchScoreAnnouncement();
                        }
                    } else if(player_1_set_1_score == 6 && player_2_set_1_score == 6) { //Enter tiebreak
                        is_tiebreak = true;
                        player_serving_to_start_tiebreak = player_serving;

                        // Announce "Game: P2"
                        gameAnnouncement("P2");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_1_score, player_2_set_1_score);
                    } else {    //Normal Game Announcement
                        // Announce "Game: P2"
                        gameAnnouncement("P2");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_1_score, player_2_set_1_score);
                    }
                } else if(current_set == 2) {
                    player_2_set_2_score += 1;
                    player_2_set_2_score_text_view.setText(Integer.toString(player_2_set_2_score));

                    // Player 2 won Set 2
                    if(player_2_set_2_score == 6 && player_1_set_2_score <= 4) {    //6-0, 6-1, 6-2, 6-3, 6-4

                        current_set += 1;

                        set_winners[1] = 2;

                        if(set_winners[0] == set_winners[1]) {  //Player 2 wins
                            gameSetMatchAnnouncement("P2");
                        } else {
                            // Announce "Set 2: P2"
                            setAnnouncement("P2", "2");
                            matchScoreAnnouncement();

                            if(ten_point_tiebreaker_format.equals("yes")) { //Start 10-point tiebreaker for the 3rd set
                                is_tiebreak = true;
                            }
                        }


                    } else if(player_2_set_2_score == 7 && player_1_set_2_score == 5) { //7-5

                        current_set += 1;

                        set_winners[1] = 2;

                        if(set_winners[0] == set_winners[1]) {  //Player 2 wins
                            gameSetMatchAnnouncement("P2");
                        } else {
                            // Announce "Set 2: P2"
                            setAnnouncement("P2", "2");
                            matchScoreAnnouncement();

                            if(ten_point_tiebreaker_format.equals("yes")) { //Start 10-point tiebreaker for the 3rd set
                                is_tiebreak = true;
                            }
                        }
                    } else if(player_1_set_2_score == 6 && player_2_set_2_score == 6) { //Enter tiebreak
                        is_tiebreak = true;
                        player_serving_to_start_tiebreak = player_serving;

                        // Announce "Game: P2"
                        gameAnnouncement("P2");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_2_score, player_2_set_2_score);
                    } else {    //Normal Game announcement
                        // Announce "Game: P2"
                        gameAnnouncement("P2");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_2_score, player_2_set_2_score);
                    }

                } else {    //Set 3
                    player_2_set_3_score += 1;
                    player_2_set_3_score_text_view.setText(Integer.toString(player_2_set_3_score));

                    // Player 2 won Set 3
                    if(player_2_set_3_score == 6 && player_1_set_3_score <= 4) {    //6-0, 6-1, 6-2, 6-3, 6-4

                        current_set += 1;

                        set_winners[2] = 2;

                        gameSetMatchAnnouncement("P2");
                    } else if(player_2_set_3_score == 7 && player_1_set_3_score == 5) {  //7-5

                        current_set += 1;

                        set_winners[2] = 2;

                        gameSetMatchAnnouncement("P2");
                    } else if(player_1_set_3_score == 6 && player_2_set_3_score == 6) { //Enter tiebreak
                        is_tiebreak = true;
                        player_serving_to_start_tiebreak = player_serving;

                        // Announce "Game: P2"
                        gameAnnouncement("P2");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_3_score, player_2_set_3_score);
                    } else {    //Normal Game announcement
                        // Announce "Game: P2"
                        gameAnnouncement("P2");

                        // Announce set score
                        setScoreAnnouncement(current_set, player_1_set_3_score, player_2_set_3_score);
                    }
                }

                // Link data together
                //updateApplicationContext();

                return;
            } else if(player_2_points_won_this_game == 1) {
                player_2_game_score_text_view.setText("15");
            } else if(player_2_points_won_this_game == 2) {
                player_2_game_score_text_view.setText("30");
            } else if(player_2_points_won_this_game == 3) {
                player_2_game_score_text_view.setText("40");
            } else if(player_1_points_won_this_game - player_2_points_won_this_game == 0) {
                player_2_game_score_text_view.setText("40");  //Deuce
                player_1_game_score_text_view.setText("40");
            } else if(player_2_points_won_this_game - player_1_points_won_this_game == 1) {
                player_2_game_score_text_view.setText("AD"); //Advantage: Player 2
            }

            //Announce game score
            obtainGameScore();

            if(player_serving == 0) {
                gameScoreAnnouncement(player_1_game_score_string, player_2_game_score_string);
            } else {
                gameScoreAnnouncement(player_2_game_score_string, player_1_game_score_string);
            }

            // Link data together
            //updateApplicationContext()
        } else {    //Tiebreaker
            player_2_game_score_text_view.setText(Integer.toString(player_2_points_won_this_game));

            if((player_1_points_won_this_game + player_2_points_won_this_game) % 2 == 1) {
                changeServer();
            }

            //10-point tiebreak logic
            if(ten_point_tiebreaker_format.equals("yes")) { //10-point tiebreaker for final set
                if(match_length.equals("best_of_1") || (match_length.equals("best_of_3") && current_set == 3)) {
                    if(player_2_points_won_this_game >= 10 && player_2_points_won_this_game - player_1_points_won_this_game >= 2) {
                        player_1_game_score_text_view.setText("0");
                        player_2_game_score_text_view.setText("0");

                        player_1_points_won_this_game = 0;
                        player_2_points_won_this_game = 0;

                        if(current_set == 1) {  //P2 wins 1st set 10-point tiebreak
                            player_2_set_1_score += 1;
                            player_2_set_1_score_text_view.setText(Integer.toString(player_2_set_1_score));
                            set_winners[0] = 2;

                            gameSetMatchAnnouncement("P2");

                            // Link data together
                            //updateApplicationContext();

                            return;
                        } else {    //P2 wins 3rd set 10-point tiebreak
                            player_2_set_3_score += 1;
                            player_2_set_3_score_text_view.setText(Integer.toString(player_2_set_3_score));
                            set_winners[2] = 2;

                            gameSetMatchAnnouncement("P2");

                            // Link data together
                            //updateApplicationContext();

                            return;
                        }
                    } else {    // Still in the the tiebreak so call the score
                        //Announce game score

                        if(player_serving == 0) {
                            gameScoreAnnouncement(Integer.toString(player_1_points_won_this_game), Integer.toString(player_2_points_won_this_game));
                        } else {
                            gameScoreAnnouncement(Integer.toString(player_2_points_won_this_game), Integer.toString(player_1_points_won_this_game));
                        }

                        // Link data together
                        //updateApplicationContext();

                        return;
                    }
                }
            }

            if(player_2_points_won_this_game >= 7 && player_2_points_won_this_game - player_1_points_won_this_game >= 2) {

                player_1_game_score_text_view.setText("0");
                player_2_game_score_text_view.setText("0");

                player_1_points_won_this_game = 0;
                player_2_points_won_this_game = 0;

                // Changes server to the player who received first to the start the tiebreaker
                if(player_serving_to_start_tiebreak == player_serving) {
                    changeServer();
                }

                //Update set score corresponding to set
                if(current_set == 1) {

                    current_set += 1;

                    player_2_set_1_score += 1;
                    player_2_set_1_score_text_view.setText(Integer.toString(player_2_set_1_score));

                    set_winners[0] = 2;

                    if(match_length.equals("best_of_1")) {    //If best of 1 set, Player 2 wins the match
                        gameSetMatchAnnouncement("P2");
                    } else {
                        // Announce "Set 1: P2"
                        setAnnouncement("P2", "1");
                        matchScoreAnnouncement();
                    }
                } else if(current_set == 2) {

                    current_set += 1;

                    player_2_set_2_score += 1;
                    player_2_set_2_score_text_view.setText(Integer.toString(player_2_set_2_score));

                    set_winners[1] = 2;

                    if(set_winners[0] == set_winners[1]) {  //Player 2 wins
                        gameSetMatchAnnouncement("P2");
                    } else {
                        // Announce "Set 2: P2"
                        setAnnouncement("P2", "2");
                        matchScoreAnnouncement();

                        if(ten_point_tiebreaker_format.equals("yes")) { //Start 10-point tiebreaker for the 3rd set
                            is_tiebreak = true;
                        }
                    }
                } else {    //Set 3

                    current_set += 1;

                    player_2_set_3_score += 1;
                    player_2_set_3_score_text_view.setText(Integer.toString(player_2_set_3_score));

                    set_winners[2] = 2;

                    gameSetMatchAnnouncement("P2");
                }

                is_tiebreak = false;
            } else {    // Still in the the tiebreak so call the score
                //Announce game score

                if(player_serving == 0) {
                    gameScoreAnnouncement(Integer.toString(player_1_points_won_this_game), Integer.toString(player_2_points_won_this_game));
                } else {
                    gameScoreAnnouncement(Integer.toString(player_2_points_won_this_game), Integer.toString(player_1_points_won_this_game));
                }
            }

            // Link data together
            //updateApplicationContext();
        }

//        print(player_1_points_won_this_game)
//        print(player_2_points_won_this_game)
    }

    /* Alternate server each game */
    public void changeServer() {
        if(player_serving == 0) {   //P2 (right side) to serve
            player_serving = 1;

            player_1_serving_image_view.setVisibility(View.INVISIBLE);
            player_2_serving_image_view.setVisibility(View.VISIBLE);
        } else {    //P1 (left side) to serve
            player_serving = 0;

            player_1_serving_image_view.setVisibility(View.VISIBLE);
            player_2_serving_image_view.setVisibility(View.INVISIBLE);
        }
    }

    /* Calculate normal game scores (non-tiebreaker games) */
    public void obtainGameScore() {
        //Basic P1 scores
        if(player_1_points_won_this_game == 0) {
            player_1_game_score_string = "Love";
        } else if(player_1_points_won_this_game == 1) {
            player_1_game_score_string = "15";
        } else if(player_1_points_won_this_game == 2) {
            player_1_game_score_string = "30";
        } else if(player_1_points_won_this_game == 3 || player_1_points_won_this_game == player_2_points_won_this_game) {
            player_1_game_score_string = "40";
        } else if(player_1_points_won_this_game - player_2_points_won_this_game == 1) {
            player_1_game_score_string = "AD";
        }

        //Basic P2 scores
        if(player_2_points_won_this_game == 0) {
            player_2_game_score_string = "Love";
        } else if(player_2_points_won_this_game == 1) {
            player_2_game_score_string = "15";
        } else if(player_2_points_won_this_game == 2) {
            player_2_game_score_string = "30";
        } else if(player_2_points_won_this_game == 3 || player_1_points_won_this_game == player_2_points_won_this_game) {
            player_2_game_score_string = "40";
        } else if(player_2_points_won_this_game - player_1_points_won_this_game == 1) {
            player_2_game_score_string = "AD";
        }
    }

    /* Score announcements */
    // Call the game score
    public void gameScoreAnnouncement(String server_score, String receiver_score) {
        if(!is_tiebreak) {
            if(server_score.equals("40") && receiver_score.equals("40")) {
//                myUtterance = AVSpeechUtterance(string: "Deuce")
            } else if(server_score.equals("AD")) {
//                myUtterance = AVSpeechUtterance(string: "Ad In")
            } else if(receiver_score.equals("AD")) {
//                myUtterance = AVSpeechUtterance(string: "Ad Out")
            } else if(server_score.equals(receiver_score)) { //<Server score>-All
//                myUtterance = AVSpeechUtterance(string: "\(server_score)-All")
            } else { // "<Server score>-<Receiver score>"
//                myUtterance = AVSpeechUtterance(string: "\(server_score) \(receiver_score)")
            }
        }  else {   //Announce tiebreak score
            if(server_score == receiver_score) { //<Server score>-All
//                myUtterance = AVSpeechUtterance(string: "\(server_score)-All")
            } else { // "<Server score>-<Receiver score>"
                if(player_serving == 0) {   //P1 serving
//                    myUtterance = AVSpeechUtterance(string: "\(server_score) \(receiver_score), P1")
                } else {    //P2 serving
//                    myUtterance = AVSpeechUtterance(string: "\(server_score) \(receiver_score), P2")
                }
            }
        }

//        synth.speak(myUtterance)
//
//        preventButtonSelection()
//        delayGameScoreAnnouncement()
    }

    public void setScoreAnnouncement(int current_set, int player_1_set_score, int player_2_set_score) {
        if(player_1_set_score == player_2_set_score) {
//            myUtterance = AVSpeechUtterance(string: "\(player_1_set_score)-All. Set \(current_set)")
        } else if(player_1_set_score > player_2_set_score) {
//            myUtterance = AVSpeechUtterance(string: "P1 leeds \(player_1_set_score) \(player_2_set_score). Set \(current_set)")
        } else {    //P2 leads P1 in this set
//            myUtterance = AVSpeechUtterance(string: "P2 leeds \(player_2_set_score) \(player_1_set_score). Set \(current_set)")
        }

//        synth.speak(myUtterance)
//
//        preventButtonSelection()
//        delayAnnouncement()
    }

    public void matchScoreAnnouncement() {
        if(current_set == 2) {
            if(set_winners[0] == 1) {   // P1 won the first set
//                myUtterance = AVSpeechUtterance(string: "P1 leeds 1 set to love")
            } else {    // P2 won the first set
//                myUtterance = AVSpeechUtterance(string: "P2 leeds 1 set to love")
            }
        } else {    //3rd set announcement
//            myUtterance = AVSpeechUtterance(string: "1 set all")
        }

//        synth.speak(myUtterance)
//
//        preventButtonSelection()
//        delayAnnouncement()
    }

    /* Umpire announcements */
    public void gameAnnouncement(String player) {
        announcement_text_view.setVisibility(View.VISIBLE);
        announcement_text_view.setText("Game: " + player);

//        myUtterance = AVSpeechUtterance(string: "Game: \(player)")
//        synth.speak(myUtterance)
//
//        preventButtonSelection()
//
//        // This line could possibly be deprecated. Need to test
//        delayAnnouncement()
    }

    public void setAnnouncement(String player, String set_number) {
        announcement_text_view.setVisibility(View.VISIBLE);
        announcement_text_view.setText("Set " + set_number + ": " + player);

//        myUtterance = AVSpeechUtterance(string: "Game and Set \(set_number): \(player)")
//        synth.speak(myUtterance)
//
//        preventButtonSelection()
//        delayAnnouncement()
    }

    public void gameSetMatchAnnouncement(String player) {
        announcement_text_view.setVisibility(View.VISIBLE);
        announcement_text_view.setText("Game, Set, Match");

        if(match_length.equals("best_of_1")) {    //Best of 1 set format
            if(player.equals("P1")) {    //P1 wins
//                myUtterance = AVSpeechUtterance(string: "Game, Set, Match: \(player). \(player_1_set_1_score) \(player_2_set_1_score)")
            } else {    //P2 wins
//                myUtterance = AVSpeechUtterance(string: "Game, Set, Match: \(player). \(player_2_set_1_score) \(player_1_set_1_score)")
            }
        } else {    //Best of 3 set format
            if(player.equals("P1")) {
                if(set_winners[0] == set_winners[1]) {  //P1 won in straight sets
//                    myUtterance = AVSpeechUtterance(string: "Game, Set, Match: \(player). \(player_1_set_1_score) \(player_2_set_1_score). \(player_1_set_2_score) \(player_2_set_2_score)")
                } else {    //P1 won in 3 sets
//                    myUtterance = AVSpeechUtterance(string: "Game, Set, Match: \(player). \(player_1_set_1_score) \(player_2_set_1_score). \(player_1_set_2_score) \(player_2_set_2_score). \(player_1_set_3_score) \(player_2_set_3_score)")
                }
            } else {
                if(set_winners[0] == set_winners[1]) {  //P2 won in straight sets
//                    myUtterance = AVSpeechUtterance(string: "Game, Set, Match: \(player). \(player_2_set_1_score) \(player_1_set_1_score). \(player_2_set_2_score) \(player_1_set_2_score)")
                } else {    //P1 won in 3 sets
//                    myUtterance = AVSpeechUtterance(string: "Game, Set, Match: \(player). \(player_2_set_1_score) \(player_1_set_1_score). \(player_2_set_2_score) \(player_1_set_2_score). \(player_2_set_3_score) \(player_1_set_3_score)")
                }
            }
        }

//        synth.speak(myUtterance)

        // Change color for the winner
        if(player.equals("P1")) {
            player_1_text_view.setTextColor(Color.GREEN);
            player_won = 0;
        } else {
            player_2_text_view.setTextColor(Color.GREEN);
            player_won = 1;
        }

        player_1_serving_image_view.setVisibility(View.INVISIBLE);
        player_2_serving_image_view.setVisibility(View.INVISIBLE);

//        preventButtonSelection()
//        delayGameSetMatch()
    }
}

