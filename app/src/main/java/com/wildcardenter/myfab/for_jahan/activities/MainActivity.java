package com.wildcardenter.myfab.for_jahan.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wildcardenter.myfab.for_jahan.R;
import com.wildcardenter.myfab.for_jahan.fragments.AssistantFragment;
import com.wildcardenter.myfab.for_jahan.fragments.FromAsifFragment;
import com.wildcardenter.myfab.for_jahan.fragments.SecretFragment;
import com.wildcardenter.myfab.for_jahan.fragments.SwipeImageFragment;
import com.wildcardenter.myfab.for_jahan.helpers.SharedPrefHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    private SharedPrefHelper sharedPrefHelper;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        sharedPrefHelper = new SharedPrefHelper(this);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SwipeImageFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_camera);
            getSupportActionBar().setTitle(R.string.Your_photos);
        }
        reference= FirebaseDatabase.getInstance().getReference("OnlineStatus").child("isOnline");

    }

    @Override
    protected void onResume() {
        super.onResume();
        reference.setValue("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.setValue("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.setValue("Destroyed");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            if (!getSupportActionBar().isShowing()) {
                getSupportActionBar().show();
            }

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addPhotoAct) {

            Intent intent = new Intent(MainActivity.this, AddPhotoActivity.class);
            startActivity(intent);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getSupportActionBar().setTitle(R.string.Your_photos);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SwipeImageFragment())
                    .commit();

        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this, MusicActivity.class));

        } else if (id == R.id.nav_slideshow) {
            getSupportActionBar().setTitle(R.string.your_assistant);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AssistantFragment())
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_share) {
            Boolean isAuthorized = (Boolean) sharedPrefHelper.getData(SharedPrefHelper.AUTHORIZATION_KEY, SharedPrefHelper.TYPE_BOOLEAN);
            if (isAuthorized) {
                getSupportActionBar().setTitle("Secret Messages For You");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SecretFragment())
                        .addToBackStack(null).commit();
            } else {
                new MaterialStyledDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle("NOT AUTHORIZED")
                        .setDescription(getResources().getString(R.string.notAuthorizedDialogMsg))
                        .setHeaderColorInt(Color.parseColor("#BB5757"))
                        .setHeaderDrawable(R.drawable.notauthorized)
                        .setPositiveText("OK")
                        .withDialogAnimation(true, Duration.NORMAL)
                        .withIconAnimation(true)
                        .build()
                        .show();
            }

        } else if (id == R.id.nav_send) {
            getSupportActionBar().hide();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FromAsifFragment())
                    .addToBackStack(null).commit();
        } else if (id == R.id.exit_App) {
            new MaterialStyledDialog.Builder(this)
                    .setHeaderDrawable(R.drawable.ic_exit)
                    .setHeaderColorInt(Color.parseColor("#BB5757"))
                    .setTitle("Exit From Application?")
                    .setDescription("Are you sure you want to exit from the application?")
                    .setPositiveText("Exit")
                    .onPositive((dialog, which) -> {
                        finish();
                    })
                    .setNegativeText("cancel")
                    .withDialogAnimation(true)
                    .build().show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
