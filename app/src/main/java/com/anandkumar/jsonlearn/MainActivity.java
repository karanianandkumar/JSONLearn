package com.anandkumar.jsonlearn;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
        private void checkExternalMedia(){
            boolean mExternalStorageAvailable = false;
            boolean mExternalStorageWriteable = false;
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // Can read and write the media
                mExternalStorageAvailable = mExternalStorageWriteable = true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                // Can only read the media
                mExternalStorageAvailable = true;
                mExternalStorageWriteable = false;
            } else {
                // Can't read or write
                mExternalStorageAvailable = mExternalStorageWriteable = false;
            }

        }

        private void writeToSDFile(){

            // Find the root of the external storage.
            // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

            File root = android.os.Environment.getExternalStorageDirectory();

            // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

            File dir = new File (root.getAbsolutePath() + "/download");
            dir.mkdirs();
            File jsonFile = new File(dir, "songs.txt");

            try {
                FileOutputStream f = new FileOutputStream(jsonFile);
                PrintWriter pw = new PrintWriter(f);

                String jsonString = null;
                // parse existing/init new JSON
                try {
                    //jsonFile = new FileWriter("/data/data/" + getPackageName() + "/" + "songs.json");

                    JSONObject mO = null;
                    try {
                        mO = new JSONObject();
                        int count=0;
                        for (int i = 0; i < lis.length; i++) {
                            try {
                                JSONObject jObjectType = new JSONObject();
                                String songNames = songlis[i];
                                String result[] = songNames.split(",");
                                for (int j = 0; j < result.length; j++) {
                                    jObjectType.put(result[j], lyricLis[count++]);

                                }
                                mO.put(lis[i], jObjectType);
                                Log.d("Length: ",mO.length()+"");

                            }catch (JSONException exp){
                                exp.printStackTrace();
                            }
                        }
                        try {
                            jsonString = mO.toString(4);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(jsonFile!=null){
                            pw.println(jsonString.toString());
                        }
                    } catch ( Exception e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
               }finally{
                    try {
                        pw.flush();
                        pw.close();
                        f.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }




            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i(TAG, "******* File not found. Did you" +
                        " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {


            checkExternalMedia();
            writeToSDFile();

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
