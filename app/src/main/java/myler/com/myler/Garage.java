package myler.com.myler;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Garage extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();

            // Set up ListView
            final ListView listView = (ListView) findViewById(R.id.listView);
//            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1);
            final List<Map<String, String>> data = new ArrayList<>();
            final SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                    new String[] {"title", "data"}, new int[] {android.R.id.text1, android.R.id.text2});
            listView.setAdapter(adapter);

            // Add items via the Button and EditText at the bottom of the view.
            final Button button = (Button) findViewById(R.id.addButton);
            button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent i1 = new Intent(Garage.this, NewVehicleActivity.class);
                    startActivity(i1);
                }
            });

            // Add button for sign out
//            final Button signOutButton = (Button) findViewById(R.id.logOutButton);
//            button.setOnClickListener(new View.OnClickListener(){
//                public void onClick(View v) {
////                    Intent intent = new Intent(Garage.this, LogInActivity.class);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                    startActivity(intent);
////                    mFirebaseAuth.signOut();
//                }
//            });

            // Navigate to Vehicle Profile
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    mDatabase.child("Users").child(mUserId).child("Vehicles")
//                            .orderByChild("")
                    HashMap<String, String> item = (HashMap<String, String>)listView.getItemAtPosition(position);
//                    String data = item.get("data");
//                    new AlertDialog.Builder(Garage.this).setTitle(data).setMessage("").show();

                    Intent intent = new Intent(Garage.this, VehicleProfileActivity.class);
                    intent.putExtra("specific_vin", item.get("data"));
                    startActivity(intent);
                }
            });


            // Use Firebase to populate list.
            mDatabase.child("Users").child(mUserId).child("Vehicles").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, String> item = new HashMap<String, String>(2);
                    item.put("title", getVehicleTitle(dataSnapshot));
                    item.put("data", dataSnapshot.child("vin").getValue().toString());
                    data.add(item);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Map<String, String> item = new HashMap<String, String>(2);
                    item.put("title", getVehicleTitle(dataSnapshot));
                    item.put("data", dataSnapshot.child("vin").getValue().toString());
                    data.add(item);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public void onLogOut(View view) {
        mFirebaseAuth.signOut();
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public String getVehicleTitle(DataSnapshot dataSnapshot) {
        StringBuilder str = new StringBuilder();
        str.append(dataSnapshot.child("year").getValue());
        str.append("  ");
        str.append(dataSnapshot.child("make").getValue());
        str.append("  ");
        str.append(dataSnapshot.child("model").getValue());
        return str.toString();
    }

    // Loads the log in activity and does not allow them to return until accurate login
    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
