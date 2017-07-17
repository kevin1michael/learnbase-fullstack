package com.uwaterloo.LearnBase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import android.util.Log;
import android.widget.Button;
import java.io.InputStreamReader;
import org.json.JSONObject;
import org.w3c.dom.Text;
import android.view.View;
import android.view.View.OnClickListener;

import java.net.URLEncoder;

import com.uwaterloo.LearnBase.R;
import com.uwaterloo.LearnBase.messages.MessagingActivity;

public class PageActivity extends AppCompatActivity {

    private int pageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        this.pageID = getIntent().getIntExtra("PAGE_ID",0);

        ImageView img = (ImageView) findViewById(R.id.imageView2);
        TextView storyText = (TextView)findViewById(R.id.textView2);
        Button btn1 = (Button) findViewById(R.id.button2);
        Button btn2 = (Button) findViewById(R.id.button3);

        new cloudGetPage(this, this.pageID,img,storyText,btn1,btn2).execute();

        btn1.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                String buttonText = b.getText().toString();

                if (buttonText == "..."){
                    return; //nothing to do
                }

                if (buttonText == "Summarize For Your Friends"){
                    Intent startMessagingActivity = new Intent(getApplicationContext(), MessagingActivity.class);
                    getApplicationContext().startActivity(startMessagingActivity);
                    return;
                }

                String[] split = buttonText.split(" - ");
                int path_id = Integer.parseInt(split[0]);

                Intent startPageActivity = new Intent(getApplicationContext(), PageActivity.class);
                startPageActivity.putExtra("PAGE_ID", path_id);
                getApplicationContext().startActivity(startPageActivity);
            }
        });

        btn2.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                String buttonText = b.getText().toString();

                String[] split = buttonText.split(" - ");
                int path_id = Integer.parseInt(split[0]);

                Intent startPageActivity = new Intent(getApplicationContext(), PageActivity.class);
                startPageActivity.putExtra("PAGE_ID", path_id);
                getApplicationContext().startActivity(startPageActivity);
            }
        });
    }

    private void nextPage() {
        NextPageActivity.start(this);
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

    private class cloudGetPage extends AsyncTask<String, String, String> {

        private String pathID;
        private Context ctx;
        private ImageView img;
        private TextView txt;
        private Button op1;
        private Button op2;

        public cloudGetPage(Context cont, int ID, ImageView img, TextView txt, Button op1, Button op2) {
            this.pathID = Integer.toString(ID);
            this.ctx = cont;
            this.img = img;
            this.txt = txt;
            this.op1 = op1;
            this.op2 = op2;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String response = makePostRequest("https://ancient-reef-73015.herokuapp.com/take_path/",
                        "{ \"username\": \"username\", \"path_id\":" + this.pathID + " }", getApplicationContext());
                return response;
            } catch (IOException ex) {
                ex.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject jsonResult = new JSONObject(result);
                new DownloadImageTask(this.img)
                    .execute(jsonResult.getString("image_url"));
                this.txt.setText(jsonResult.getString("text"));
                this.op1.setText(jsonResult.getString("path_1_id") + " - " + jsonResult.getString("path_1_text"));
                this.op2.setText(jsonResult.getString("path_2_id") + " - " + jsonResult.getString("path_2_text"));

            } catch (JSONException e){
                this.op1.setText("Summarize For Your Friends");
                this.op2.setVisibility(View.GONE);
            }

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
}
