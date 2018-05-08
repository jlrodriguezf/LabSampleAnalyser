package mx.unam.cfata.labsampleanalyser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.karan.churi.PermissionManager.PermissionManager;
import com.mvc.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class MainActivity extends AnalyseOpenCVActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private CharSequence mTitle;
    private PermissionManager permissionManager;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.pickedImage);

        // Title
        mTitle = getTitle();

        // Toolbar Handle
        Toolbar mToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        // Drawer Handle
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mToggle.setDrawerIndicatorEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Navigation View Handle
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(MainActivity.this);

        // Main Fragment (Archive) set on create
        ArchiveFragment archiveFragment = new ArchiveFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.relLayout4F,
                        archiveFragment,
                        archiveFragment.getTag())
                .commit();
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout = findViewById(R.id.drawerLayout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void onPickImage(MenuItem item) {
        ImagePicker.pickImage(this, "Select your sample:");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap pickedImage = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        if (pickedImage != null) {

            //Bitmap conversion to fragment
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            pickedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //Fragment initialization
            AnalyseStatic analyseStaticFragment = new AnalyseStatic();
            //Arguments bundle for bitmap passing
            Bundle bundle = new Bundle();
            bundle.putByteArray("pickedImage", byteArray);
            analyseStaticFragment.setArguments(bundle);
            //Fragment transaction
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.relLayout4F,
                            analyseStaticFragment,
                            analyseStaticFragment.getTag())
                    //Avoiding error in stream
                    .commitAllowingStateLoss();
        }
        //Close drawer after bitmap has been loaded
        mDrawerLayout.closeDrawer(GravityCompat.START);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Navigation View Item Clicks
        int id = item.getItemId();

        if (id == R.id.nav_analyseLive) {

            //Analyse Live Activity
            Intent analyseOpenCVActivity = new Intent(MainActivity.this, AnalyseOpenCVActivity.class);
            startActivity(analyseOpenCVActivity);

        } else if (id == R.id.nav_analyseStatic) {
            //Analyse Static Fragment suppressed and moved into onActivityResult for bitmap passing

        } else if (id == R.id.nav_archive) {

            // Archive Fragment
            ArchiveFragment archiveFragment = new ArchiveFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.relLayout4F,
                            archiveFragment,
                            archiveFragment.getTag()
                    ).commit();

        } else if (id == R.id.nav_settings) {

            // Settings Fragment
            SettingsFragment settingsFragment = new SettingsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    //.setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left)
                    .replace(R.id.relLayout4F,
                            settingsFragment,
                            settingsFragment.getTag()
                    ).commit();

        } else if (id == R.id.nav_contact) {

            // Contact Fragment
            ContactFragment contactFragment = new ContactFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    //.setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left)
                    .replace(R.id.relLayout4F,
                            contactFragment,
                            contactFragment.getTag()
                    ).commit();

        } else if (id == R.id.nav_help) {

            // Help Fragment
            HelpFragment helpFragment = new HelpFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    //.setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left)
                    .replace(R.id.relLayout4F,
                            helpFragment,
                            helpFragment.getTag()
                    ).commit();

        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());


        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        Objects.requireNonNull(getActionBar()).setTitle(mTitle);
    }
}