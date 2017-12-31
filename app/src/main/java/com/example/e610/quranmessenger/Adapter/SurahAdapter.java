package com.example.e610.quranmessenger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.quranmessenger.Models.SurahOfQuran.SurahOfQuran;
import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.TafserActivity;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.ServiceUtils;


public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.MyViewHolder> {
    SurahOfQuran surahOfQuran;
    Context context;
    int LastPosition = -1;
    RecyclerViewClickListener ClickListener;

    public SurahAdapter() {
    }

    public SurahAdapter(SurahOfQuran surahOfQuran, Context context) {
        this.surahOfQuran = new SurahOfQuran();
        this.surahOfQuran = surahOfQuran;
        this.context = context;
    }


    public void setClickListener(RecyclerViewClickListener clickListener) {
        this.ClickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_recycler_view, parent, false);
        return new MyViewHolder(view);
    }
    String state;
    String sh_name;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.textView.setText(surahOfQuran.data.get(position).name);
        setAnimation(holder.cardView, position);

        state=MySharedPreferences.getMediaPlayerState();

        holder.imgSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=MySharedPreferences.getMediaPlayerState();
                sh_name=MySharedPreferences.getUserSetting("shekhName");

                if(state.equals("-1")) {
                    holder.imgSound.setImageResource(R.drawable.icon_pause);
                    ServiceUtils.startMediaService(context,position+1+"",sh_name);
                    MySharedPreferences.setMediaPlayerState("1");
                    Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();
                }else{
                    holder.imgSound.setImageResource(R.drawable.icon_play);
                    ServiceUtils.stopMediaService(context);
                    MySharedPreferences.setMediaPlayerState("-1");
                    Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,TafserActivity.class);
                Bundle b=new Bundle();
                b.putString("pageNumber",position+1+"");
                intent.putExtra("bundle",b);
                context.startActivity(intent);
                Toast.makeText(context,"read",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (surahOfQuran == null || surahOfQuran.data == null)
            return 0;
        return surahOfQuran.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgRead;
        ImageView imgSound;
        TextView textView;
         CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.text);
            cardView = (CardView) itemView.findViewById(R.id.card);
            imgRead = (ImageView) itemView.findViewById(R.id.tafser_img);
            imgSound = (ImageView) itemView.findViewById(R.id.play_pause_img);
        }

        @Override
        public void onClick(View view) {
            if (ClickListener != null)
                ClickListener.ItemClicked(view, getAdapterPosition());
        }

        public void clearAnimation() {
            cardView.clearAnimation();
        }
    }

    public interface RecyclerViewClickListener {

        public void ItemClicked(View v, int position);
    }

    private void setAnimation(View viewToAnimate, int position) {

        if (position > LastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            LastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }


}

