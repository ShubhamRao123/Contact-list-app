package com.inmovies.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    boolean isDeleting = false;
    ListView listView;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        listView = (ListView) findViewById (R.id.list);


        initListButton();

        initMapButton();

        initSettingsButton();

        ContactDataSource ds = new ContactDataSource( this);
        try {

            ds.open();

        } catch (SQLException e) {

            e.printStackTrace();

        }
        String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).getString("sortorder","ASC");
        final ArrayList<Contact> contacts = ds.getContacts(sortBy,sortOrder);
        ds.close();
        adapter = new ContactAdapter(this,contacts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact selectedContact = contacts.get(i);
                if (isDeleting){
                    adapter.showDelete(i,view,ContactListActivity.this,selectedContact);

                }else {
                    Intent intent =  new Intent(ContactListActivity.this,ContactActivity.class);
                    intent.putExtra("contactid",selectedContact.getContactID());
                    startActivity(intent);
                }
            }
        });
        initDeleteButton();
        initAddContactButton();


    }

    @Override
    protected void onResume() {
        super.onResume();
        ContactDataSource ds = new ContactDataSource( this);
        try {

            ds.open();

        } catch (SQLException e) {

            e.printStackTrace();

        }
        String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).getString("sortorder","ASC");
        final ArrayList<Contact> contacts = ds.getContacts(sortBy,sortOrder);
        ds.close();


        if (contacts.size()>0){
            adapter = new ContactAdapter(this,contacts);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Contact selectedContact = contacts.get(i);
                    if (isDeleting){
                        adapter.showDelete(i,view,ContactListActivity.this,selectedContact);

                    }else {
                        Intent intent =  new Intent(ContactListActivity.this,ContactActivity.class);
                        intent.putExtra("contactid",selectedContact.getContactID());
                        startActivity(intent);
                    }
                }
            });
        }else{
            startActivity(new Intent(this,ContactActivity.class));
        }
    }


    private void initListButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
    }
    private void initMapButton() {
        ImageButton map = (ImageButton) findViewById(R.id.imageButtonMap);
    }
    private void initSettingsButton() {
        ImageButton settings = (ImageButton) findViewById(R.id.imageButtonSettings);
    }

    private void initDeleteButton(){
        final Button deleteButton = (Button) findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDeleting){
                    deleteButton.setText("Delete");
                    isDeleting = false;
                    adapter.notifyDataSetChanged();
                }
                else {
                    deleteButton.setText("Done Deleting");
                    isDeleting = true;
                }
            }
        });
    }

    private void initAddContactButton(){
        Button newContact = (Button) findViewById(R.id.buttonAdd);
        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }

}