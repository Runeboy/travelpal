package com.rune.travelpal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rune.travelpal.R;
import rune.formatting.StringExpert;

// A splash screen activity.
public class SplashActivity extends AbstractActivity {

    // Fields

    private static final String DEFAULT_USERNAME = "traveler";

    // Constructor

    public SplashActivity() {
        super(R.layout.activity_splash, SplashActivity.class);
    }

    // Overrides

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMessage();
        doAnimations();
    }

    private void setMessage() {
        String username = getDataGateway().getUsername();

        ((TextView)findViewById(R.id.splashTextView)).setText(StringExpert.format(
            "Welcome, {0}!", (username == null)? DEFAULT_USERNAME : username
        ));
    }

    // Methods

    private void doAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        anim.reset();
        View l =  findViewById(R.id.splashActivityLayout);
        l.clearAnimation();
        l.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }
            public void onAnimationEnd(Animation animation) {
                log.d("Animation end");

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }
}
