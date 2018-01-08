package com.badexample.basya.shopifyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class OpenItemActivity extends AppCompatActivity {

    ArrayList<String> productInfo;
    ListView itemInfoListView;
    ImageView itemImageView;
    TextView titleOfItem;
    ArrayAdapter basicAdapter;
    JSONObject currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_item);



        String itemJSON;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                itemJSON= null;
            } else {
                itemJSON= extras.getString("JSONItem");
            }
        } else {
            itemJSON= (String) savedInstanceState.getSerializable("JSONItem");

        }


        productInfo = new ArrayList<>();
        itemInfoListView = (ListView) findViewById(R.id.listViewItem);
        itemImageView = (ImageView) findViewById(R.id.imageViewItem);
        titleOfItem = (TextView) findViewById(R.id.titleTextViewItem);

        basicAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productInfo);
        itemInfoListView.setAdapter(basicAdapter);

        if (itemJSON!=null){
            try {
                listPopulater(itemJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    public void listPopulater(String s) throws JSONException {
        JSONObject currentItem = new JSONObject(s);
        Iterator<?> keys = currentItem.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            productInfo.add(key + ": " + currentItem.get(key));
            basicAdapter.notifyDataSetChanged();

        }
        JSONObject image1 = currentItem.getJSONObject("image");


        Glide.with(OpenItemActivity.this)
                .load(image1.getString("src"))
                .into(itemImageView);

        titleOfItem.setText(currentItem.getString("title"));

    }
}
