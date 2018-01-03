package com.badexample.basya.shopifyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OpenItemActivity extends AppCompatActivity {

    ArrayList<String> productInfo;
    ListView itemInfoListView;
    ImageView itemImageView;
    TextView titleOfItem;
    ArrayAdapter basicAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_item);

        productInfo = new ArrayList<>();
        itemInfoListView = (ListView) findViewById(R.id.listViewItem);
        itemImageView = (ImageView) findViewById(R.id.imageViewItem);
        titleOfItem = (TextView) findViewById(R.id.titleTextView);

        basicAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productInfo);







    }
}
