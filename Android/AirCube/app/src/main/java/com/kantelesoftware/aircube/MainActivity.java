package com.kantelesoftware.aircube;


import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    private String field1;
    private String field2;
    private String field3;
    private String created_at;
    private String entry_id;

    Handler h = new Handler();
    final int delay = 15000; //milliseconds

    private boolean celsius_check = true;
    private int myFah = 0;

    // URL to get contacts JSON
    private String url;

    private List<Card> cards;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        rv=(RecyclerView)findViewById(R.id.card_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        new GetData().execute();

    }


    private void initializeData(){
        clearData();

        cards = new ArrayList<>();
        if (celsius_check) {
            cards.add(new Card(R.string.temp,   "" + field1 + getString(R.string.cels),     R.drawable.temp_icon));
        } else {

            try {
                myFah = Integer.parseInt(field1);
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            cards.add(new Card(R.string.temp,   "" + myFah + getString(R.string.fah),   R.drawable.temp_icon));
        }
        cards.add(new Card(R.string.hum,    "" + field2 + getString(R.string.perc),     R.drawable.hum_icon));
        cards.add(new Card(R.string.aq,    "" + field3 + getString(R.string.ppm),  R.drawable.aq_icon));
    }

    private void clearData(){
        if(cards != null){
            cards.clear();
        }
    }

    private void initializeAdapter(){
        DataAdapter adapter = new DataAdapter(cards);
        rv.setAdapter(adapter);
    }

    /**********************************************************************************************/

    class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog = new ProgressDialog(MainActivity.this);
            /*
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            */
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            url = "" + getString(R.string.URL);

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray feeds = jsonObj.getJSONArray("feeds");

                    // looping through All Contacts
                    for (int i = 0; i < feeds.length(); i++) {
                        JSONObject c = feeds.getJSONObject(i);

                        created_at = c.getString("created_at");
                        entry_id = c.getString("entry_id");
                        field1 = c.getString("field1");         // Temperature data
                        field2 = c.getString("field2");         // Humidity data
                        field3 = c.getString("field3");         // AQ data

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server.",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            initializeData();
            initializeAdapter();

            h.removeCallbacksAndMessages(null);         // stop possible timers

            h.postDelayed(new Runnable() {
                public void run() {                       // make a new timer for when to run the GetData() again
                    new GetData().execute();
                    h.postDelayed(this, delay);
                }
            }, delay);
        }
    }

}