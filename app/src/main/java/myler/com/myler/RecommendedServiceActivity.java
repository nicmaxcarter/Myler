package myler.com.myler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecommendedServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_service);

        final int currentMiles;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                currentMiles = 130000;
            } else {
                currentMiles = extras.getInt("CurrentMiles");
            }
        } else {
            currentMiles = Integer.parseInt(savedInstanceState.getSerializable("CurrentMiles").toString());
        }
        final ListView listView = (ListView) findViewById(R.id.servicesList);

        Button previous = (Button) findViewById(R.id.pastServices);
        Button future = (Button) findViewById(R.id.futureServices);

        final ServiceRecommender serviceRec = new ServiceRecommender();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecommendedServiceActivity.this, android.R.layout.simple_list_item_1, serviceRec.getPreviousServices(currentMiles));
                listView.setAdapter(adapter);
            }
        });

        future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecommendedServiceActivity.this, android.R.layout.simple_list_item_1, serviceRec.getFutureServices(currentMiles));
                listView.setAdapter(adapter);
            }
        });

    }
}
