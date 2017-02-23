package com.kantelesoftware.aircube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HumActivity extends AppCompatActivity {

    private static final int CLOCK_MIN = 0;
    private static final int CLOCK_MAX = 24;
    private String TAG = TempActivity.class.getSimpleName();


    private String field2;
    private String created_at;
    private String entry_id;


    private ListView lv;

    // URL to get contacts JSON
    private String url;

    ArrayList<HashMap<String, String>> humList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hum);

        humList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.listview_hum);

        new GetHumidities().execute();
    }

    /**********************************************************************************************/

    class GetHumidities extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                    JSONArray humidities = jsonObj.getJSONArray("feeds");

                    // looping through All Contacts
                    for (int i = 0; i < humidities.length(); i++) {
                        JSONObject c = humidities.getJSONObject(i);

                        created_at = c.getString("created_at");
                        entry_id = c.getString("entry_id");
                        field2 = c.getString("field2");         // Humidity data


                        StringTokenizer tokens = new StringTokenizer(created_at, "- |\\T |\\: |\\Z");
                        String year = tokens.nextToken();
                        String month = tokens.nextToken()+".";
                        String day = tokens.nextToken()+".";
                        String hour = tokens.nextToken();
                        String minutes = tokens.nextToken();

                        try {
                            int myNum = Integer.parseInt(hour);

                            myNum = myNum + 2;

                            if (myNum == CLOCK_MAX)
                            {
                                myNum = CLOCK_MIN;
                            }
                            else if (myNum > CLOCK_MAX)
                            {
                                myNum = CLOCK_MIN + myNum - CLOCK_MAX;
                            }

                            hour = Integer.toString(myNum);
                        } catch(NumberFormatException nfe) {
                            System.out.println("Could not parse " + nfe);
                        }


                        // tmp hash map for single contact
                        HashMap<String, String> humidity = new HashMap<>();

                        // adding each child node to HashMap key => value
                        humidity.put("created_at", day+month+year+"  |  Klo "+hour+"."+minutes);
                        humidity.put("field2", field2);

                        // adding contact to contact list
                        humList.add(humidity);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
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
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Collections.reverse(humList);

            ListAdapter adapter = new SimpleAdapter(
                    HumActivity.this, humList,
                    R.layout.list_item_hum, new String[]{"created_at", "field2"},
                    new int[]{R.id.date, R.id.tv_humidity});

            lv.setAdapter(adapter);
        }

    }
}

