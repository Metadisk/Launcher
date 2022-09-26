package com.ifveral.launcher;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext());
    }

    public static final class WidgetItem {

        public final String city;
        public final String city_detail;
        public final String country;
        public final String country_detail;
        public final String temperature;
        public final String temperature_detail;
        public final String description;
        public final String description_detail;

        public WidgetItem(String city, String city_detail,
                          String country, String country_detail,
                          String temperature, String temperature_detail,
                          String description, String description_detail) {
            this.city = city;
            this.city_detail = city_detail;
            this.country = country;
            this.country_detail = country_detail;
            this.temperature = temperature;
            this.temperature_detail = temperature_detail;
            this.description = description;
            this.description_detail = description_detail;

        }
    }

    public static class ListRemoteViewsFactory implements RemoteViewsFactory {
        private final List<WidgetItem> mWidgetItems = new ArrayList<>();
        private final Context mContext;

        public ListRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {
            // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
            // for example downloading or creating content etc, should be deferred to onDataSetChanged()
            // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        }

        @Override
        public void onDestroy() {
            mWidgetItems.clear();
        }

        @Override
        public int getCount() {
            return mWidgetItems.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            // Position will always range from 0 to getCount() - 1.
            WidgetItem widgetItem = mWidgetItems.get(position);

            // Construct remote views item based on the item xml file and set text based on position.
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            rv.setTextViewText(R.id.city, widgetItem.city);
            rv.setTextViewText(R.id.citydetails, widgetItem.city_detail);
            rv.setTextViewText(R.id.country, widgetItem.country);
            rv.setTextViewText(R.id.countrydetails, widgetItem.country_detail);
            rv.setTextViewText(R.id.temperature, widgetItem.temperature);
            rv.setTextViewText(R.id.temperaturedetails, widgetItem.temperature_detail);
            rv.setTextViewText(R.id.description, widgetItem.description);
            rv.setTextViewText(R.id.descriptiondetails, widgetItem.description_detail);
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            // You can create a custom loading view (for instance when getViewAt() is slow.) If you
            // return null here, you will get the default loading view.
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataSetChanged() {
            mWidgetItems.clear();
            String result = null;
            String[] cityName = {"beijing", "berlin", "cardiff", "edinburgh", "london", "nottingham"};
            for (String cty : cityName) {
                try {
                    URL url = new URL("https://weather.bfsah.com/" + cty);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    result = inputStreamToString(in);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String city = jsonObject.getString("city");
                        String country = jsonObject.getString("country");
                        String temperature = jsonObject.getString("temperature");
                        String description = jsonObject.getString("description");

                        WidgetItem item = new WidgetItem("City: ", city,
                                "Country:", country, "Temperature:", temperature,
                                "Description:", description);
                        mWidgetItems.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }

        private String inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();

            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader rd = new BufferedReader(isr);

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer.toString();
        }
    }
}


