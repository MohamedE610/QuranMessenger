package com.example.e610.quranmessenger.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.e610.quranmessenger.Models.SurahOfQuran.SurahOfQuran;
import com.example.e610.quranmessenger.R;


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

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textView.setText(surahOfQuran.data.get(position).name);

        setAnimation(holder.cardView, position);

    }

    @Override
    public int getItemCount() {
        if (surahOfQuran == null || surahOfQuran.data == null)
            return 0;
        return surahOfQuran.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.text);
            cardView = (CardView) itemView.findViewById(R.id.card);
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

