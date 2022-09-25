package com.ifveral.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private List<AppList> appsList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView,app_package;
        public ImageView img;


        //This is the subclass ViewHolder which simply
        //'holds the views' for us to show on each row
        public ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
            textView = itemView.findViewById(R.id.app_name);
            img = itemView.findViewById(R.id.icon);
            app_package = itemView.findViewById(R.id.app_package);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick (View v) {
            int pos = getAdapterPosition();
            Context context = v.getContext();

            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(pos).getAppPackage());
            context.startActivity(launchIntent);
        }
    }



    public AppListAdapter(Context c) {

        PackageManager pm = c.getPackageManager();
        appsList = new ArrayList<AppList>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities( mainIntent, 0);

        for (int j = 0; j < pkgAppsList.size(); j++) {
            ResolveInfo packageInfo = pkgAppsList.get(j);
            if (!isSystemPackage(packageInfo)) {
                String appName = packageInfo.loadLabel(pm).toString();
                Drawable appIcon = packageInfo.loadIcon(pm);
                String packageName = packageInfo.activityInfo.packageName;
                appsList.add(new AppList(appIcon, appName, packageName));
            }
        }

    }

    private boolean isSystemPackage(ResolveInfo packageInfo) {
        return (packageInfo.activityInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    @Override
    public void onBindViewHolder(AppListAdapter.ViewHolder viewHolder, int i) {

        //Here we use the information in the list we created to define the views
        String appPackage = appsList.get(i).getAppPackage();
        String appLabel = appsList.get(i).getAppName();
        Drawable appIcon = appsList.get(i).getAppIcon();

        TextView textView = viewHolder.textView;
        textView.setText(appLabel);
        ImageView imageView = viewHolder.img;
        imageView.setImageDrawable(appIcon);
        TextView app_package = viewHolder.app_package;
        app_package.setText(appPackage);
    }


    @Override
    public int getItemCount() {
        return appsList.size();
    }


    @NonNull
    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.appdrawer_recylerview, parent, false);

        return new ViewHolder(view);
    }
}