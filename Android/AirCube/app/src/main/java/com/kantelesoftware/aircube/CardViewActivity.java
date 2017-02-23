package com.kantelesoftware.aircube;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Teemu on 22.11.2016.
 */

public class CardViewActivity extends Activity {

    TextView cardName;
    TextView cardData;
    ImageView cardPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.card_layout);
        cardName = (TextView) findViewById(R.id.tv_holder);
        cardData = (TextView) findViewById(R.id.tv_measurement);
        cardPhoto = (ImageView) findViewById(R.id.imageView);

        cardName.setText(R.string.temp);
        cardData.setText("DATA");
        cardPhoto.setImageResource(R.drawable.temp_icon);
    }
}
