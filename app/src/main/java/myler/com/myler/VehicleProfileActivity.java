package myler.com.myler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.data;

public class VehicleProfileActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    TextView profileMake;
    TextView profileModel;
    TextView profileYear;
    TextView profileMiles;
    TextView profileVin;
    TextView profileLastOilChange;
    TextView profileNextOilChange;

    String specificVin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUserId = mFirebaseUser.getUid();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                specificVin = "no vin supplied";
            } else {
                specificVin = extras.getString("specific_vin");
            }
        } else {
            specificVin = (String) savedInstanceState.getSerializable("specific_vin");
        }


        final Vehicle[] vehicle = new Vehicle[1];

        profileMake = (TextView) findViewById(R.id.profileMakeTV);
        profileModel = (TextView) findViewById(R.id.profileModelTV);
        profileYear = (TextView) findViewById(R.id.profileYear);
        profileMiles = (TextView) findViewById(R.id.profileMiles);
        profileVin = (TextView) findViewById(R.id.profileVin);
        profileLastOilChange = (TextView) findViewById(R.id.profileLastOilChangeActual);
        profileNextOilChange = (TextView) findViewById(R.id.profileNextOilChangeActual);

        DatabaseReference vehiclesRef = mDatabase.child("Users").child(mUserId).child("Vehicles");
        vehiclesRef.orderByChild("vin").equalTo(specificVin).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                vehicle[0] = dataSnapshot.getValue(Vehicle.class);
                Vehicle veh = vehicle[0];
                if(veh.getLast_oil_change() != 0) {
                    profileLastOilChange.setText("" + veh.getLast_oil_change());
                    int nextChange = veh.getLast_oil_change()+3000;
                    int currentMileage = veh.getOriginal_miles();
                    if(currentMileage>nextChange) {
                        profileNextOilChange.setTextColor(Color.RED);
                        profileNextOilChange.setText("" + nextChange + " (Overdue)");
                    } else{
                        profileNextOilChange.setText("" + nextChange);
                    }
                } else{
                    profileLastOilChange.setText("N/A");
                    profileNextOilChange.setText("N/A");
                }
                if(!veh.getMake().isEmpty())
                    profileMake.setText(veh.getMake());
                if(!veh.getModel().isEmpty())
                    profileModel.setText(veh.getModel());
                if(veh.getYear() != 0)
                    profileYear.setText(("" + veh.getYear()));
                if(veh.getOriginal_miles() != 0)
                    profileMiles.setText("" + veh.getOriginal_miles());
                if(!veh.getVin().isEmpty())
                    profileVin.setText(veh.getVin());

                applyNA();
//                new AlertDialog.Builder(VehicleProfileActivity.this).setTitle(vehicle[0].getMake().toString()).setMessage("").show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    public void onUpdateMileage(View view) {

        final View copyView = view;

        final int[] newMileage = new int[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mileage (larger than current)");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newMileage[0] = Integer.parseInt(input.getText().toString());
                if (newMileage[0] < Integer.parseInt(profileMiles.getText().toString())){
                    onUpdateMileage(copyView);
                }else {
                    updateAndReloadMileage(newMileage[0]);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void updateAndReloadMileage(int i) {
        final int newMiles = i;
        DatabaseReference vehiclesRef = mDatabase.child("Users").child(mUserId).child("Vehicles");
        vehiclesRef.orderByChild("vin").equalTo(specificVin).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataSnapshot.getRef().child("original_miles").setValue(newMiles);
                Intent intent = new Intent(VehicleProfileActivity.this, VehicleProfileActivity.class);
                intent.putExtra("specific_vin", specificVin);
                startActivity(intent);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void onUpdateOilChangeMileage(View view) {

        final View copyView = view;

        final int[] newMileage = new int[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mileage (larger than current)");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newMileage[0] = Integer.parseInt(input.getText().toString());
                if (newMileage[0] < Integer.parseInt(profileLastOilChange.getText().toString())){
                    onUpdateOilChangeMileage(copyView);
                }else {
                    updateAndReloadOilMileage(newMileage[0]);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void updateAndReloadOilMileage(int i) {
        final int newMiles = i;
        DatabaseReference vehiclesRef = mDatabase.child("Users").child(mUserId).child("Vehicles");
        vehiclesRef.orderByChild("vin").equalTo(specificVin).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataSnapshot.getRef().child("last_oil_change").setValue(newMiles);
                Intent intent = new Intent(VehicleProfileActivity.this, VehicleProfileActivity.class);
                intent.putExtra("specific_vin", specificVin);
                startActivity(intent);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void onViewRecServicing(View view) {
        Intent intent = new Intent(VehicleProfileActivity.this, RecommendedServiceActivity.class);
        intent.putExtra("CurrentMiles", profileMiles.getText());
        startActivity(intent);
    }

    public void applyNA() {
        if(profileMiles.getText().equals(""))
            profileMiles.setText("N/A");
    }

    public void onNotifyMe(View view) {

//        <Button
//        android:id="@+id/notifyMeBtn"
//        android:layout_width="match_parent"
//        android:layout_height="10dp"
//        android:layout_marginTop="@dimen/activity_vertical_margin"
//        android:text="Notify Me"
//        android:onClick="onNotifyMe"
//        android:layout_gravity="bottom"/>





        int mId = 001;

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.myler_logo_icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setVisibility(Notification.VISIBILITY_PUBLIC).
                                setPriority(Notification.PRIORITY_MAX).
                        setVibrate(new long[0]).
                        setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, Garage.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Garage.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());

    }

    public void onDeleteVehicle(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");

        // Set up the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference vehiclesRef = mDatabase.child("Users").child(mUserId).child("Vehicles");
                vehiclesRef.orderByChild("vin").equalTo(specificVin).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                        Intent intent = new Intent(VehicleProfileActivity.this, Garage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
