package com.anandkumar.jsonlearn;

import android.app.ProgressDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Song> songsList;
    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();
    private String lis[],songlis[],lyricLis[];
    RealmHelper realmHelper=null;
    private JSONObject jsonSongsData;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        realm.init(this);
        realm=Realm.getDefaultInstance();
        Log.d("Realm status",realm.toString());

        songsList=new ArrayList();

        realmHelper=new RealmHelper(realm);

        saveData();
        realmHelper.save(songsList);

        ArrayList<String> al=realmHelper.retrieveAlphabets();


            Log.d("Alpht:: ",al.size()+"");

/*

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
*/


                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Total: "+songsList.size(),Toast.LENGTH_SHORT).show();
               // new SongsData().execute();

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
                    JSONObject alphabets=new JSONObject();
                    try {
                        mO = new JSONObject();

                        int count=0;
                        for (int i = 0; i < lis.length; i++) {
                            try {
                                JSONArray jObjectType = new JSONArray();
                                String songNames = songlis[i];
                                String result[] = songNames.split(",");
                                for (int j = 0; j < result.length; j++) {
                                    JSONObject song=new JSONObject();
                                    song.put("name",result[j]);
                                    song.put("lyric",lyricLis[count++]);
                                    song.put("audio","");
                                    song.put("video","");
                                    jObjectType.put(song);

                                }
                                mO.put(lis[i], jObjectType);
                                Log.d("Length: ",mO.length()+"");

                            }catch (JSONException exp){
                                exp.printStackTrace();
                            }
                        }
                        alphabets.put("alphabets",mO);
                        try {
                            jsonString = alphabets.toString(4);
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


            //checkExternalMedia();
            //writeToSDFile();


            String jsonData=readJSONFile();
            int count=1;

            try {
                // Parse the data into jsonobject to get original data in form of json.
                JSONObject jObject = new JSONObject(jsonData);


                Iterator iterator=jObject.keys();
                while (iterator.hasNext()){
                        String alphabet=iterator.next().toString();
                    JSONArray jsonSongNames = jObject.getJSONArray(alphabet);

                    for (int i = 0; i < jsonSongNames.length(); i++) {
                        Song song=new Song();
                        song.setNumber(count++);
                        song.setIndex(alphabet);
                        song.setName(jsonSongNames.getJSONObject(i).getString("name"));
                        song.setLyric(jsonSongNames.getJSONObject(i).getString("lyric"));
                        song.setAudio(jsonSongNames.getJSONObject(i).getString("audio"));
                        song.setVideo(jsonSongNames.getJSONObject(i).getString("video"));

                        songsList.add(song);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("Songs Length:: ",songsList.size()+"");





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
    public String readJSONFile(){
        InputStream inputStream = getResources().openRawResource(R.raw.songs);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return byteArrayOutputStream.toString();
    }

    public void saveData(){

        String jsonData=readJSONFile();
        int count=1;

        try {
            // Parse the data into jsonobject to get original data in form of json.
            JSONObject jObject = new JSONObject(jsonData);


            Iterator iterator=jObject.keys();
            while (iterator.hasNext()){
                String alphabet=iterator.next().toString();
                JSONArray jsonSongNames = jObject.getJSONArray(alphabet);

                for (int i = 0; i < jsonSongNames.length(); i++) {
                    Song song=new Song();
                    song.setNumber(count++);
                    song.setIndex(alphabet);
                    song.setName(jsonSongNames.getJSONObject(i).getString("name"));
                    song.setLyric(jsonSongNames.getJSONObject(i).getString("lyric"));
                    song.setAudio(jsonSongNames.getJSONObject(i).getString("audio"));
                    song.setVideo(jsonSongNames.getJSONObject(i).getString("video"));

                    songsList.add(song);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("Songs Length:: ",songsList.size()+"");




    }
}
