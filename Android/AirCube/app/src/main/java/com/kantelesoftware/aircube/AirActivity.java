package com.kantelesoftware.aircube;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class AirActivity extends AppCompatActivity {

    private static final int CLOCK_MIN = 0;
    private static final int CLOCK_MAX = 24;
    private String TAG = TempActivity.class.getSimpleName();


    private String field3;
    private String created_at;
    private String entry_id;


    private ListView lv;

    // URL to get contacts JSON
    private String url;

    ArrayList<HashMap<String, String>> airList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);

        airList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.listview_air);

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
                    JSONArray airquality = jsonObj.getJSONArray("feeds");

                    // looping through All data
                    for (int i = 0; i < airquality.length(); i++) {
                        JSONObject c = airquality.getJSONObject(i);

                        created_at = c.getString("created_at");
                        entry_id = c.getString("entry_id");
                        field3 = c.getString("field3");         // Humidity data


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


                        // tmp hash map for single dataset
                        HashMap<String, String> aq = new HashMap<>();

                        // adding each child node to HashMap key => value
                        aq.put("created_at", day+month+year+"  |  Klo "+hour+"."+minutes);
                        aq.put("field3", field3);

                        // adding contact to list
                        airList.add(aq);
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

            Collections.reverse(airList);

            ListAdapter adapter = new SimpleAdapter(
                    AirActivity.this, airList,
                    R.layout.list_item_air, new String[]{"created_at", "field3"},
                    new int[]{R.id.date, R.id.tv_airquality});

            lv.setAdapter(adapter);
        }

    }
}