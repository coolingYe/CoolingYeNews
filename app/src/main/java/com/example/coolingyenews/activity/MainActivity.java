package com.example.coolingyenews.activity;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.bean.User;
import com.example.coolingyenews.service.UserHandler;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.UserClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/Main/Main")
public class MainActivity extends AppCompatActivity {
    private List<User> list = new ArrayList<>();
    private List<News> list1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.ALPHA_CHANGED, WindowManager.LayoutParams.ALPHA_CHANGED);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard,R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        if (UserClass.getUname() == null) {
            //new UserHandler().loginSendRequestWithHttpURLConnection(MainActivity.this, list, new HttpURL().HTTP_LOGIN_STATUS_URL("CoolingYe", "1"));
        }
    }
}