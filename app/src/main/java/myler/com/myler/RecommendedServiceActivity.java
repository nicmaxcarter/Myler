package myler.com.myler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RecommendedServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_service);


        ListView listView = (ListView) findViewById(R.id.servicesList);

        ArrayList<String> services = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, services);

        listView.setAdapter(adapter);

        adapter.add("Test1");
        adapter.add("Test2");
        adapter.add("Test3");
        adapter.add("Test4");
        adapter.add("Test5");

        services.add("Test6");
        services.add("Test7");
        services.add("Test8");
        services.add("Test9");
        services.add("Test10");
    }
}
