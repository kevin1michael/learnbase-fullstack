package com.uwaterloo.LearnBase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwaterloo.LearnBase.messages.MessagingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.OutputStreamWriter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
//import com.uwaterloo.learnbase.R;

public class StoryTitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Button start_reading = (Button) findViewById(R.id.start_mars);
        start_reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageOne();
            }
        });

        String title_pic = getIntent().getStringExtra("IMAGE_URL");
        String title = getIntent().getStringExtra("STORY_NAME");

        new StoryTitleActivity.DownloadImageTask((ImageView) findViewById(R.id.title))
                .execute(title_pic);

        TextView txtView = (TextView) findViewById(R.id.title_textview);
        txtView.setText(title);
    }

    public static void start(Context context) {
        Intent startReadingActivity = new Intent(context, StoryTitleActivity.class);
        context.startActivity(startReadingActivity);
    }

    private class cloudStartStory extends AsyncTask<String, String, String> {

        private String storyID;
        private Context ctx;

        public cloudStartStory(Context cont, int ID) {
            this.storyID = Integer.toString(ID);
            this.ctx = cont;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String response = makePostRequest("https://ancient-reef-73015.herokuapp.com/start_story/",
                        "{ \"username\": \"username\", \"story_id\":" + this.storyID + " }", getApplicationContext());
                return response;
            } catch (IOException ex) {
                ex.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Intent startPageActivity = new Intent(ctx, PageActivity.class);
            int pageNum = getIntent().getIntExtra("FIRST_PAGE_ID",0);
            startPageActivity.putExtra("PAGE_ID", pageNum);
            ctx.startActivity(startPageActivity);
        }

        public String makePostRequest(String stringUrl, String payload,
                                             Context context) throws IOException {
            URL url = new URL(stringUrl);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            String line;
            StringBuffer jsonString = new StringBuffer();

            uc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            uc.setRequestMethod("POST");
            uc.setDoInput(true);
            uc.setInstanceFollowRedirects(false);
            uc.connect();
            OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                while((line = br.readLine()) != null){
                    jsonString.append(line);
                }
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            uc.disconnect();
            return jsonString.toString();
        }
    }

    private void pageOne() {
        int story_id = getIntent().getIntExtra("STORY_ID",4);
        new cloudStartStory(this,story_id).execute();

        finish();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
