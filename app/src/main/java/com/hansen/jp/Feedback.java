package com.hansen.jp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Feedback extends AppCompatActivity {
    Button btnSubmit;
    EditText txtFeed, txtEmail;
    String email;
    private AdView mBannerAd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnSubmit = (Button) findViewById(R.id.btnfeed);
        txtFeed = (EditText) findViewById(R.id.txtFeed);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        email = txtEmail.getText().toString();
        mBannerAd = (AdView) findViewById(R.id.banner_AdView);
        showBannerAd();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email != null) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "phelixolonde@gmail.com", null));
                    //intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Jackpot Predictions Feedback");
                    intent.putExtra(Intent.EXTRA_TEXT, txtFeed.getText());
                    startActivity(Intent.createChooser(intent, "Send using"));

                } else {
                    Toast.makeText(Feedback.this, "please provide your email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mBannerAd.loadAd(adRequest);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
