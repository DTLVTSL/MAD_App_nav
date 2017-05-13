package com.example.mad.lab2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by daniel on 01/04/17.
 */
public class members_adapter extends ArrayAdapter<user_withpaid> {
    Context context;
    int layoutResourceId;
    private ArrayList<user_withpaid> data;

    //Constructor
    public members_adapter(Context context, int layoutResourceId, ArrayList<user_withpaid> data){
        super(context,layoutResourceId,data);
//        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;

    }

    // Create the view
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        members_holder holder = null;

        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new members_holder();
            holder.image = (ImageView) row.findViewById(R.id.member_image);
            holder.name = (TextView) row.findViewById(R.id.member_name);
            holder.number= (TextView) row.findViewById(R.id.member_number);
            holder.pay_status= (TextView) row.findViewById(R.id.member_pay_status);


            row.setTag(holder);
        }else{
            holder = (members_holder)row.getTag();
        }

        user_withpaid member_pay = data.get(position);

        User member= member_pay.getUser();
        holder.name.setText(member.getName());
        holder.number.setText(member.getPhoneNumber().toString());
        holder.image.setImageResource(member.getPhoto());

        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(member.getPhoto()));
        if(bitmap != null)
            holder.image.setImageResource(member.getPhoto());
        else{
            holder.image.setImageResource(R.drawable.man);
        }


        if(member_pay.getPaid()==false) {
            holder.pay_status.setText(R.string.Have_not_paid);
        }
        if (member_pay.getPaid()){
            holder.pay_status.setText(R.string.Have_paid);
            holder.pay_status.setTextColor(Color.parseColor("#009900"));
        }

        //In order to return the view
        return row;
    }

    //Keep the data ir order to be alble to work with them
    static class members_holder{
        ImageView image;
        TextView name;
        TextView number;
        TextView pay_status;


    }

}