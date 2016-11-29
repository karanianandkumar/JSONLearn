package com.anandkumar.jsonlearn;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Anand on 11/26/2016.
 */

public class RealmHelper {

    Realm realm;

    public RealmHelper(Realm realm){
        this.realm=realm;
    }

    //WRITE
    public void save(final Song song){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Song s=realm.copyToRealm(song);
            }
        });
    }
    public void save(final ArrayList<Song> songs){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(Song s:songs){
                    Song song=realm.copyToRealm(s);
                }

            }
        });
    }
    //Retrieval

    public ArrayList<String> retrieveNames(String index){

        ArrayList<String> songsList=new ArrayList();
        RealmQuery<Song> Query=realm.where(Song.class);
        RealmResults<Song> songs=Query.equalTo("index",index).findAll();

        for(Song s:songs){
            songsList.add(s.getName());
        }

        return songsList;

    }
    public ArrayList<String> retrieveAlphabets(){

        ArrayList<String> booksList=new ArrayList();
        RealmQuery<Song> Query=realm.where(Song.class);
        RealmResults<Song> songs=Query.findAll();

        for(Song s:songs){
            booksList.add(s.getIndex());
        }

        return booksList;

    }
    public String retrieveLyric(String name){


        RealmQuery<Song> Query=realm.where(Song.class);
        Song song=Query.equalTo("name",name).findFirst();
        return song.getLyric();

    }

}
