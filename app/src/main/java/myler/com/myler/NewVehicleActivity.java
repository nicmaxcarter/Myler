package myler.com.myler;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class NewVehicleActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    public ProgressBar progressBar;

    public String xmlOutput;

    EditText vin, make, model, year, miles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vehicle);

//        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        // Get user id
        mUserId = mFirebaseUser.getUid();

         // Get auto populate TextView
        final TextView autoPop = (TextView) findViewById(R.id.autoPopulate);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        xmlOutput = "";

        // Set auto populate
        autoPop.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });

        // Get vehicle properties from activity
        vin = (EditText) findViewById(R.id.VinET);
        make = (EditText) findViewById(R.id.makeET);
        model = (EditText) findViewById(R.id.modelET);
        year = (EditText) findViewById(R.id.yearET);
        miles = (EditText) findViewById(R.id.milesET);

        final Button button = (Button) findViewById(R.id.addVehicleBtn);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                final Vehicle vehicle = new Vehicle();

                // set vehicle properties

                /////////////////////// COME BACK AND ADD SAFETY FEATURES TO PREVENT APP CRASH //////////////////////////////

                vehicle.setVin(vin.getText().toString());
                vehicle.setMake(make.getText().toString());
                vehicle.setModel(model.getText().toString());
                vehicle.setYear(Integer.parseInt(year.getText().toString()));
                vehicle.setOriginal_miles(Integer.parseInt(miles.getText().toString()));

                // add vehicle to database
                mDatabase.child("Users").child(mUserId).child("Vehicles").push().setValue(vehicle);

                Intent intent = new Intent(NewVehicleActivity.this, Garage.class);
                startActivity(intent);
            }
        });
    }

    public void afterPost() {
        Log.d("OUTPUT:", xmlOutput);
        HashMap<String, String> values = parseXml(xmlOutput);
//        responseView.setText("back to original");
        vin.setText(values.get("vin"));
        make.setText(values.get("make"));
        model.setText(values.get("model"));
        year.setText(values.get("year"));
    }

    public HashMap<String, String> parseXml(String str){

        StringBuilder sb = new StringBuilder();

        HashMap<String, String> values = new HashMap<>();

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput( new StringReader( str ) ); // pass input whatever xml you have
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
//                    Log.d("TAG","Start document");
                }  else if(eventType == XmlPullParser.END_TAG) {
//                    Log.d("TAG","End tag "+xpp.getName());
                } else if(eventType == XmlPullParser.TEXT) {
//                    Log.d("TAG","Text "+xpp.getText()); // here you get the text from xml
                } else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("VIN")) {
//                    sb.append(xpp.nextText());
//                    Log.d("TAG",xpp.nextText());
                    values.put("vin", xpp.nextText());
                } else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("Make")) {
                    values.put("make", xpp.nextText());
                } else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("Model")) {
                    values.put("model", xpp.nextText());
                } else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("ModelYear")) {
                    values.put("year", xpp.nextText());
                }
                eventType = xpp.next();
            }
            Log.d("TAG","End document-------");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("TAG:", sb.toString());
        return values;
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                URL url = new URL("https://vpic.nhtsa.dot.gov/api//vehicles/DecodeVinValues/1FMCU14T5LUA04498?format=xml");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            xmlOutput = response;
            afterPost();
        }

    }
}
