package com.example.adrian.lagemademarvel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FavMenuFragment.OnFragmentInteractionListener,
        NewsViewFragment.OnFragmentInteractionListener,
        PersonalProfileFragment.OnFragmentInteractionListener,
        CharacterViewFragment.OnFragmentInteractionListener,
        ComicViewFragment.OnFragmentInteractionListener,
        MapsFragment.OnFragmentInteractionListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null){

        }else{
            Intent intent = new Intent(this, LoginPanel.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.

        /*int id = item.getItemId();

        if (id == R.id.nav_personajes) {
            // Handle the camera action
        } else if (id == R.id.nav_comics) {
            //Intent intent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_noticias) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_LogOut) {
            mAuth.signOut();
            setTitle("La Gema de Marvel");
            Intent intent = new Intent(MainActivity.this, RegisterPanel.class);
            startActivity(intent);
        }*/

        final android.app.FragmentManager fm = getFragmentManager();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (item.getItemId()) {

                    case R.id.nav_news:
                       fm.beginTransaction().replace(R.id.content_frame, new NewsViewFragment()).commit();
                        setTitle(getString(R.string.app_title));
                        break;

                    case R.id.nav_profile:
                        fm.beginTransaction().replace(R.id.content_frame, new PersonalProfileFragment()).commit();
                        setTitle(getString(R.string.app_title));
                        break;

                    case R.id.nav_stores:
                        fm.beginTransaction().replace(R.id.content_frame, new MapsFragment()).commit();
                        setTitle(getString(R.string.app_title));

                        break;

                    case R.id.nav_characters:
                        fm.beginTransaction().replace(R.id.content_frame, new CharacterViewFragment()).commit();
                        setTitle("Nombre del personaje");

                        break;

                    case R.id.nav_comics:
                        fm.beginTransaction().replace(R.id.content_frame, new ComicViewFragment()).commit();
                        setTitle("Nombre del comic");

                        break;

                    case R.id.nav_fav_characters:
                        fm.beginTransaction().replace(R.id.content_frame, new FavMenuFragment()).commit();
                        setTitle("Personajes Favoritos");

                        break;

                    case R.id.nav_fav_comics:
                        fm.beginTransaction().replace(R.id.content_frame, new FavMenuFragment()).commit();
                        setTitle("Comics Favoritos");

                        break;

                    case R.id.nav_LogOut:
                        mAuth.signOut();
                        Intent intent = new Intent(MainActivity.this, RegisterPanel.class);
                        startActivity(intent);
                        break;

                    default:
                }
            }


        }, 50);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
