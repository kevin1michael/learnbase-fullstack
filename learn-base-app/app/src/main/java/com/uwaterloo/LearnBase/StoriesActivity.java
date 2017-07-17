package com.uwaterloo.LearnBase;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;

import com.uwaterloo.LearnBase.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StoriesActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        new cloudStoriesGetTask(this, "https://ancient-reef-73015.herokuapp.com/get_stories/").execute();

    }

    public static void start(Context context) {
        Intent startStoriesActivity = new Intent(context, StoriesActivity.class);
        context.startActivity(startStoriesActivity);
    }

    @Override
    public void onBackPressed() {
        navigation.start(this);
    }

    private class cloudStoriesGetTask extends AsyncTask<Void,Void,JSONArray> {
        ListView lv;
        String url;
        Context ctx;
        JSONArray stories;

        public cloudStoriesGetTask(Context cont, String url) {
            this.lv = (ListView) findViewById(R.id.list_stories);
            this.url = url;
            this.ctx = cont;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }

        protected JSONArray doInBackground(Void... params) {
            try{
                HttpURLConnection urlConnection = null;
                URL url = new URL(this.url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */ );
                urlConnection.setConnectTimeout(15000 /* milliseconds */ );

                //Added pieces //
                urlConnection.setDoInput (true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches (false);
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                String jsonString = sb.toString();
                this.stories = new JSONArray(jsonString);

                return this.stories;

            } catch(IOException e){

            } catch (JSONException f){

            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {

            ArrayList<String> items = new ArrayList<String>();
            for(int i=0; i < result.length() ; i++) {
                try {
                    JSONObject json_data = result.getJSONObject(i);
                    String story_title=json_data.getString("story_name");
                    items.add(story_title);
                } catch (JSONException e){/**Do Nothing**/}

            }

            ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this.ctx,
                    android.R.layout.simple_expandable_list_item_1, items);

            lv.setAdapter(mArrayAdapter);


            lv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                    try{
                        JSONObject story = stories.getJSONObject(position);

                        Intent intent = new Intent(StoriesActivity.this, StoryTitleActivity.class);
                        intent.putExtra("STORY_ID", story.getInt("story_id"));
                        intent.putExtra("STORY_NAME", story.getString("story_name"));
                        intent.putExtra("FIRST_PAGE_ID", story.getInt("first_page_id"));
                        intent.putExtra("IMAGE_URL", story.getString("image_url"));
                        startActivity(intent);
                    }catch (JSONException e) {}
                }
            });
        }

    }
}
