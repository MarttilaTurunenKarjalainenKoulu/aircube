package com.kantelesoftware.aircube;

/**
 * Created by Teemu on 22.11.2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.CardViewHolder> {


    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView cardHolder;
        TextView cardData;
        ImageView cardPhoto;

        Intent intent;
        String TAG = "Card ";

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            cardHolder = (TextView) itemView.findViewById(R.id.tv_holder);
            cardData = (TextView) itemView.findViewById(R.id.tv_measurement);
            cardPhoto = (ImageView) itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = itemView.getContext();
            int position = this.getAdapterPosition();

            switch (position){
                case 0:
                    Log.v(TAG, "index=" + position);
                    intent = new Intent(context, TempActivity.class);
                    break;

                case 1:
                    Log.v(TAG, "index=" + position);
                    intent = new Intent(context, HumActivity.class);
                    break;

                case 2:
                    Log.v(TAG, "index=" + position);
                    intent = new Intent(context, AirActivity.class);
                    break;
            }
            context.startActivity(intent);
        }
    }


    List<Card> cards;

    DataAdapter(List<Card> cards){
        this.cards = cards;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
        CardViewHolder pvh = new CardViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.cardHolder.setText(cards.get(i).holder);
        cardViewHolder.cardData.setText(cards.get(i).tv_data);
        cardViewHolder.cardPhoto.setImageResource(cards.get(i).photoId);

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
