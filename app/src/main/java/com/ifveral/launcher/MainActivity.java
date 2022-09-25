package com.ifveral.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AppListAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_drawers);

        recyclerView = findViewById(R.id.appList);
        adapter = new AppListAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private boolean isSystemPackage(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    private List<AppList> getInstalledApps() {
        List<AppList> apps = new ArrayList<>();
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (!isSystemPackage(packageInfo)) {
                String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
                String packageName = packageInfo.applicationInfo.packageName;
                apps.add(new AppList(appIcon, appName, packageName));
            }
        }
        return apps;
    }

}