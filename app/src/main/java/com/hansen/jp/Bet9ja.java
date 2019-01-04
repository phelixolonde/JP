package com.hansen.jp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Bet9ja extends Fragment {


    View view;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    TextView loading;
    DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter<Model, ItemViewHolder> firebaseRecyclerAdapter;


    TextView txtLoading;
    private AdView mBannerAd;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tips, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("jackpot").child("bet9ja");

        txtLoading = (TextView) view.findViewById(R.id.jp);


        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mBannerAd = (AdView) view.findViewById(R.id.banner_AdView);
        mBannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mBannerAd.setVisibility(View.VISIBLE);
            }
        });

        showBannerAd();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==0){
                    txtLoading.setText("No tips at the moment. Please check again later.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mBannerAd.loadAd(adRequest);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //  int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ItemViewHolder>(
                Model.class,
                R.layout.post_row,
                ItemViewHolder.class,
                mDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, final Model model, int position) {
                final String item_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setPrice(model.getBody());
                viewHolder.setTime(model.getTime());
                txtLoading.setVisibility(View.GONE);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent adDetails = new Intent(v.getContext(), Post_Details.class);
                        adDetails.putExtra("selection","bet9ja");
                        adDetails.putExtra("postKey", item_key);
                        startActivity(adDetails);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {


        View mView;

        public ItemViewHolder(View v) {
            super(v);
            mView = v;

        }

        public void setTitle(String title) {
            TextView tvTitle = (TextView) mView.findViewById(R.id.postTitle);
            tvTitle.setText(title);
        }

        public void setPrice(String price) {

            TextView txtPrice = (TextView) mView.findViewById(R.id.post);
            txtPrice.setText(price);

        }

        public void setTime(Long time) {

            TextView txtTime = (TextView) mView.findViewById(R.id.postTime);
            //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
            long elapsedTime;
            long currentTime = System.currentTimeMillis();
            int elapsed = (int) ((currentTime - time) / 1000);
            if (elapsed < 60) {
                if (elapsed < 2) {
                    txtTime.setText("Just Now");
                } else {
                    txtTime.setText(elapsed + " sec ago");
                }
            } else if (elapsed > 604799) {
                elapsedTime = elapsed / 604800;
                if (elapsedTime == 1) {
                    txtTime.setText(elapsedTime + " week ago");
                } else {

                    txtTime.setText(elapsedTime + " weeks ago");
                }
            } else if (elapsed > 86399) {
                elapsedTime = elapsed / 86400;
                if (elapsedTime == 1) {
                    txtTime.setText(elapsedTime + " day ago");
                } else {
                    txtTime.setText(elapsedTime + " days ago");
                }
            } else if (elapsed > 3599) {
                elapsedTime = elapsed / 3600;
                if (elapsedTime == 1) {
                    txtTime.setText(elapsedTime + " hour ago");
                } else {
                    txtTime.setText(elapsedTime + " hours ago");
                }
            } else if (elapsed > 59) {
                elapsedTime = elapsed / 60;
                txtTime.setText(elapsedTime + " min ago");


            }

        }
    }
}



