package com.inmovies.mycontactlist;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    public ContactAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    public ContactAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }
    public ContactAdapter(@NonNull Context context, int resource, @NonNull Contact[] objects) {
        super(context, resource, objects);
    }
    public ContactAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Contact[] objects) {
        super(context, resource, textViewResourceId, objects);
    }
    public ContactAdapter(@NonNull Context context, int resource, @NonNull List<Contact> objects) {
        super(context, resource, objects);
    }
    public ContactAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Contact> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    private ArrayList<Contact> items;
    private Context adapterContext;

    public ContactAdapter(Context context, ArrayList<Contact> items){
        super(context, R.layout.list_item, items);
        adapterContext = context;
        this.items = items;

    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        try {
            Contact contact = items.get(position);

            if (v == null){
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }

            TextView contactName = (TextView) v.findViewById(R.id.textContactName);
            TextView contactNumber = (TextView) v.findViewById(R.id.textPhoneNumber);
            Button b = (Button) v.findViewById(R.id.buttonDeleteContact);
            contactName.setText(contact.getContactName());
            contactNumber.setText(contact.getPhoneNumber());
            b.setVisibility(View.INVISIBLE);
        }
        catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return v;

    }

    public void showDelete(final int position, final View convertView, final Context context, final Contact contact){
        View v = convertView;
        final Button b = (Button) v.findViewById(R.id.buttonDeleteContact);

        if (b.getVisibility()==View.INVISIBLE){
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideDelete(position, convertView, context);
                    items.remove(contact);
                    deleteOption(contact.getContactID(),context);
                }
            });
        }
        else {
            hideDelete(position, convertView, context);
        }
    }

    private void deleteOption(int contactToDelete, Context context){
        ContactDataSource db = new ContactDataSource(context);
        try{
            db.open();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        db.deleteContact(contactToDelete);
        db.close();
        this.notifyDataSetChanged();
    }

    private void hideDelete(int position, View convertView, Context context){
        View v = convertView;
        final Button b = (Button) v.findViewById(R.id.buttonDeleteContact);
        b.setVisibility(View.INVISIBLE);
        b.setOnClickListener(null);
    }


}


