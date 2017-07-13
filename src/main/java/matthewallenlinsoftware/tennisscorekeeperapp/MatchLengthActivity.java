package matthewallenlinsoftware.tennisscorekeeperapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;

public class MatchLengthActivity extends WearableActivity {

    private BoxInsetLayout mContainerView;

    // onClick Transitions
    public void onClickBestOfOneButton(View view) {
        // Starting a new intent
        Intent nextScreen = new Intent(getApplicationContext(), TenPointTiebreakActivity.class);

        nextScreen.putExtra("match_length", "best_of_1");

        // Sending data to another Activity
        startActivity(nextScreen);
    }

    public void onClickBestOfThreeButton(View view) {
        // Starting a new intent
        Intent nextScreen = new Intent(getApplicationContext(), TenPointTiebreakActivity.class);

        nextScreen.putExtra("match_length", "best_of_3");

        // Sending data to another Activity
        startActivity(nextScreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_length);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
        } else {
            mContainerView.setBackground(null);
        }
    }
}
