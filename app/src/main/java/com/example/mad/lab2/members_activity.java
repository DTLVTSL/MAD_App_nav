package com.example.mad.lab2;

import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class members_activity extends AppCompatActivity {

    android.widget.ListView ListView;

    final ArrayList member_list = new ArrayList();
    ArrayList<Boolean> pay_status= new ArrayList<>();
    //private User member= new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members);

        //The items_class are set in the list view progressively, not all of them at the same time


        Bundle bundle = getIntent().getExtras();
        final String group_id=bundle.getString("group_id");
        String group_name=bundle.getString("GroupName");
        setTitle(getString(R.string.members_into)+" "+group_name);


        //Toast.makeText(getApplicationContext(),group_name, Toast.LENGTH_SHORT).show();


        //DANIEL
        Firebase firebase = new Firebase(Config.FIREBASE_URL).child("Groups").child(group_id).child("members");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                member_list.clear();
                String groupID;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    //groupID=postSnapshot.child("groupID").getValue().toString();

                    member_list.add(postSnapshot.getKey().toString());


                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //sacar members de la lista anterior
        final ArrayList<user_withpaid> data_members_pay= new ArrayList<>();
        Log.d("DATA MEMBERS PAY", String.valueOf(data_members_pay.size()));

        final members_adapter adapter2 = new members_adapter(this, R.layout.listview_members_row, data_members_pay);

        // MOSTRAR LOS GRUPOS Q SE SACARON DE LA LISTA EN EL LIST
        Firebase.setAndroidContext(this);
        firebase = new Firebase(Config.FIREBASE_URL);

        //final members_adapter finalAdapter = adapter;
        firebase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override

            public void onDataChange(DataSnapshot snapshot) {
                //data_members_pay.clear();


                for (DataSnapshot postSnapshot : snapshot.child("Users").getChildren()) {
                    final User member=new User();

                    //Log.d("MEMBERS 2: ", member_list.toString());
                    //Log.d("MEMBERS 2: ", postSnapshot.getKey().toString());
                    //Log.d("MEMBERS 2: ", String.valueOf(member_list.contains(postSnapshot.getKey().toString())));

                    if (member_list.contains(postSnapshot.getKey().toString())) {

                        final String id = postSnapshot.getKey().toString();
                        final int photo = postSnapshot.child("photo").getValue(int.class);

                        final String name = postSnapshot.child("name").getValue().toString();

                        final String number=postSnapshot.child("phoneNumber").getValue().toString();
                        final String email=postSnapshot.child("email").getValue().toString();

                        //member = new User(id, name, number, photo);
                        member.setId(id);
                        member.setName(name);
                        member.setPhoneNumber(number);
                        member.setPhoto(photo);



                        ///peo1
                        data_members_pay.clear();
                        Firebase firebase2 = new Firebase(Config.FIREBASE_URL);
                        firebase2.child("Groups").child(group_id).child("members").child(id).child("paid").addValueEventListener(new ValueEventListener()
                        {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override

                            public void onDataChange(DataSnapshot snapshot) {




                                boolean pay_status= (boolean) snapshot.getValue();
                                User new_member= new User(id, name, number, photo);

                                data_members_pay.add(new user_withpaid(new_member,pay_status));


                                ListView = (ListView) findViewById(R.id.member_list);
                                ListView.setAdapter(adapter2);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        ///peo1 end



                        //ListView = (ListView) findViewById(R.id.member_list);
                        //ListView.setAdapter(adapter2);

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //sacar member de la lista anterior end

            //DANIEL END




        //Creating the adapter
        ListView = (ListView) findViewById(R.id.member_list);


        View header = (View) getLayoutInflater().inflate(R.layout.list_header_row, null);
        ListView.addHeaderView(header);
        ListView.setAdapter(adapter2);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v= (TextView)view.findViewById(R.id.member_number);
                String telephone = "0000";
                try{
                    telephone = (String) v.getText();
                } catch (Exception e){
                    telephone="no number registered";
                }

                final String finalTelephone = telephone;
                new AlertDialog.Builder(members_activity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Calling")
                        //.setTitle(getString(R.string.leaving))
                        .setMessage("do you want to call?")
                        //.setMessage(getString(R.string.leave_sure))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {

                            Intent call=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+ finalTelephone));


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    startActivity(call);
                                } catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"we cant call now", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                //Toast.makeText(getApplicationContext(),v.getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



}