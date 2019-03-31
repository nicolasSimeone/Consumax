package cabureweb.consumax;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,NavigationView.OnNavigationItemSelectedListener{

    private Button buttonSucursales;
    private Button buttonComercios;
    private Button buttonPromociones;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double latitud;
    private double longitud;
    private NavigationView navigationView;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSucursales = (Button)findViewById(R.id.button3);
        buttonSucursales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("lat",latitud);
                intent.putExtra("lon",longitud);
                startActivity(intent);
            }
        });

        buttonComercios = (Button)findViewById(R.id.button5);
        buttonComercios.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent2 = new Intent(getApplicationContext(), comerciosActivity.class);
                intent2.putExtra("lat",latitud);
                intent2.putExtra("lon",longitud);
                startActivity(intent2);
            }
        });

        buttonPromociones = (Button)findViewById(R.id.button4);
        buttonPromociones.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent3 = new Intent(getApplicationContext(), promocionesActivity.class);
                intent3.putExtra("lat",latitud);
                intent3.putExtra("lon",longitud);
                startActivity(intent3);
            }
        });

        if (mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mGoogleApiClient.connect();
        }
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Los servicios de localización no están encendidos");
            builder.setMessage("Por favor active el GPS");
            builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        if(mLastLocation!=null){
            //latLng=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            latitud=mLastLocation.getLatitude();
            longitud=mLastLocation.getLongitude();
            //Toast.makeText(getApplicationContext(), "latitud: " +latitud, Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(), "longitud: " +longitud, Toast.LENGTH_LONG).show();
        } else{
            // Toast.makeText(getApplicationContext(), "mLastLocation NULL " , Toast.LENGTH_LONG).show();
        }



    }


    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.Sucursales){
            Intent sucursales = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(sucursales);

        } else if(id == R.id.Comercios){
            Intent comercios = new Intent(getApplicationContext(),comerciosActivity.class);
            startActivity(comercios);
        }
        else if(id == R.id.Promociones){
            Intent promociones = new Intent(getApplicationContext(), promocionesActivity.class);
            startActivity(promociones);
        }
        else if(id == R.id.Promociones){
            Intent promociones = new Intent(getApplicationContext(), promocionesActivity.class);
            startActivity(promociones);
        }
        else if(id == R.id.LogOut){
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();

            startActivity((new Intent(getBaseContext(), loginActivity.class))); //Logout
            finish();
        }
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
