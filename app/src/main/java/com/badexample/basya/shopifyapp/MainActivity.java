package com.badexample.basya.shopifyapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView shoppingListView;
    String shopifyMainURL;
    String data;
    ArrayList<String> productNames;
    ArrayList<String> productDescriptions;
    ArrayList<String> productImages;
    myListAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productNames = new ArrayList<>();
        productDescriptions = new ArrayList<>();
        productImages = new ArrayList<>();
        customAdapter = new myListAdapter(this, R.layout.main_search_layout, productNames);

        shoppingListView= (ListView) findViewById(R.id.shoppingListView);
        shoppingListView.setAdapter(customAdapter);


        shopifyMainURL="https://shopicruit.myshopify.com/admin/products.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
        String[] array = new String[]{shopifyMainURL};
        try {
            data = new JSONGetter().execute(array).get();
            dataRetriever(data);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        shoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    JSONObject item = singleItemData(data, i);
                    Intent itemViewIntent = new Intent(MainActivity.this, OpenItemActivity.class);
                    itemViewIntent.putExtra("JSONItem", item.toString());
                    startActivity(itemViewIntent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public JSONObject singleItemData(String s, int position) throws JSONException{
        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonProducts = jsonObject.getJSONArray("products");
        JSONObject product = jsonProducts.getJSONObject(position);
        return product;
    }

    public void dataRetriever(String s) throws JSONException {

        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonProducts = jsonObject.getJSONArray("products");
        Log.i("response56", jsonProducts.toString());
        for(int i=0; i<jsonProducts.length(); i++){
            JSONObject product = jsonProducts.getJSONObject(i);
            productNames.add(product.getString("title"));
            productDescriptions.add(product.getString("body_html"));
            JSONObject image1 = product.getJSONObject("image");
            productImages.add(image1.getString("src"));

        }

    }

    static class JSONGetter extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder().url(strings[0]).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("response123", response.toString());
            String response1 = null;
            try {
                response1 = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response1;


        };


    }

    private class myListAdapter extends ArrayAdapter<String> {

        private int layout;
        public myListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            MainActivity.ViewHolder mainViewHolder= null;
            MainActivity.ViewHolder viewHolder = new ViewHolder();


            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            viewHolder.thumbnail= (ImageView) convertView.findViewById(R.id.productImageView);
            viewHolder.title = (TextView) convertView.findViewById(R.id.titleTextView);
            viewHolder.description =(TextView) convertView.findViewById(R.id.descriptionTextView);

            if(!String.valueOf(getItem(position)).equals("")) {
                viewHolder.title.setText(getItem(position));
            }
            Glide.with(MainActivity.this)
                    .load(productImages.get(position))
                    .into(viewHolder.thumbnail);
            viewHolder.description.setText(productDescriptions.get(position));


            convertView.setTag(viewHolder);

            return convertView;
        }
    }

    public class ViewHolder{
        ImageView thumbnail;
        TextView title;
        TextView description;
    }
}









