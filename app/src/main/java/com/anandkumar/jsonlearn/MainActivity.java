package com.anandkumar.jsonlearn;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Song> songsList;
    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();
    private String lis[],songlis[],lyricLis[];
    private JSONObject jsonSongsData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        songsList=new ArrayList();



        Resources res = getResources();

         lis = res.getStringArray(R.array.Alphabets);

         songlis = res.getStringArray(R.array.SongList);
         lyricLis = res.getStringArray(R.array.lyrics);



            int count = 0;
            for (int i = 0; i < lis.length; i++) {

                String alphabet = lis[i];
                String songNames = songlis[i];
                String result[] = songNames.split(",");
                for (int j = 0; j < result.length; j++) {

                    Song s = new Song();
                    s.setIndex(alphabet);
                    s.setName(result[j]);
                    s.setLyric(lyricLis[count]);
                    songsList.add(s);

                    count++;
                }
            }


                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Total: "+songsList.size(),Toast.LENGTH_SHORT).show();
                new SongsData().execute();

            }
        });

    }

    public void mCreateAndSaveFile(String params, String mJsonResponse) {
        try {
            FileWriter file = new FileWriter("/data/data/" + getApplicationContext().getPackageName() + "/" + params);
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String mReadJsonData(String params) {
        String mResponse=null;
        try {
            File f = new File("/data/data/" + getPackageName() + "/" + params);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
             mResponse = new String(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mResponse;
    }

    public void addEntryToJsonFile(String params,JSONObject object,int id) {

        // parse existing/init new JSON
        File jsonFile = new File("/data/data/" + getPackageName() + "/" + params);
        String previousJson = null;
        if (jsonFile.exists()) {

                previousJson = mReadJsonData(params);

        } else {
            previousJson = "{}";
        }

        // create new "complex" object
        JSONObject mO = null;

        try {
            mO = new JSONObject(previousJson);

            mO.put(lis[id], object); //thanks "retired" answer
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // generate string from the object
        String jsonString = null;
        try {
            jsonString = mO.toString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // write back JSON file
        this.mCreateAndSaveFile(params, jsonString);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class SongsData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Downloading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {




            try {

                int count=0;
                for (int i = 0; i < lis.length; i++) {
                    try {
                        JSONObject jObjectType = new JSONObject();
                        String alphabet = lis[i];
                        String songNames = songlis[i];
                        String result[] = songNames.split(",");
                        for (int j = 0; j < result.length; j++) {

                            jObjectType.put(result[i], lyricLis[count++]);

                        }
                        addEntryToJsonFile("songs.json",jObjectType,count);


                    }catch (JSONException exp){
                        exp.printStackTrace();
                    }


                }



            } catch ( Exception e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the downloading dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


        }
    }
}
