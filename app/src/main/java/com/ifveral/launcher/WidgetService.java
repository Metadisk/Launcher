package com.ifveral.launcher;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext());
    }

    public static final class WidgetItem {

        /**
         * Label to display in the list.
         */
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
            // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
            // on the collection view corresponding to this factory. You can do heaving lifting in
            // here, synchronously. For example, if you need to process an image, fetch something
            // from the network, etc., it is ok to do it here, synchronously. The widget will remain
            // in its current state while work is being done here, so you don't need to worry about
            // locking up the widget.

            mWidgetItems.clear();
            WidgetItem item = new WidgetItem("City: ", "Beijing: ",
                    "Country:","China","Temperature:","5",
                    "Description:","Sunny intervals and light winds");
            mWidgetItems.add(item);

        }
    }
}
