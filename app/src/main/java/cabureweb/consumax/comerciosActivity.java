package cabureweb.consumax;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class comerciosActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private double latitud;
    private double longitud;
    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private View popup=null;
    private Spinner spinnerLocalidades;
    private ArrayList<String> localidadesUnicas = new ArrayList<>(); //Array para obtener los valores unicos de las localidades


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comercios);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.comerciosdrawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        latitud= intent.getDoubleExtra("lat", 0);
        longitud= intent.getDoubleExtra("lon", 0);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final List<String> localidades=new ArrayList<String>(); //array de localidades de las sucursales



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Comercios");
        final ArrayList<Comercios> comercios = new ArrayList<Comercios>(); //promociones en general
        final ArrayList<Comercios> comerciosFiltradas = new ArrayList<Comercios>(); //Array para guardar las promociones filtradas
        final ArrayList<Comercios> comerciosAux = new ArrayList<Comercios>(); //Auxiliar para no poerder los datos en el filtro






// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String Latitud = (String) messageSnapshot.child("Latitud").getValue();
                    String Nombredelaempresa = (String) messageSnapshot.child("Nombredelaempresa").getValue();
                    String Longitud = (String) messageSnapshot.child("Longitud").getValue();
                    String Municipio =(String) messageSnapshot.child("Municipio").getValue();
                    String CategoriaPrincipal = (String) messageSnapshot.child("Categoríaprincipal").getValue();
                    String TelefonoPrincipal = (String) messageSnapshot.child("Teléfonoprincipal").getValue();
                    String Direccion = (String) messageSnapshot.child("Dirección").getValue();

                    LatLng LL = new LatLng (Double.parseDouble(Latitud)	,Double.parseDouble(Longitud));

                    Comercios comercio = new Comercios(Double.parseDouble(Latitud),Double.parseDouble(Longitud),Municipio);
                    comercios.add(comercio);

                    //Probando los MARKERS con Imagenes
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.markercomercios);

                    Marker myLocMarker = mMap.addMarker(new MarkerOptions()
                            .snippet(Direccion + "\n" + CategoriaPrincipal + "\n" + TelefonoPrincipal)
                            .title(Nombredelaempresa)
                            .position(LL)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.markercomercios)));

                    // mMap.addMarker(new MarkerOptions().position(LL).title(LocalId).snippet("Exclusivo con tu tarjeta ConsuMax").icon(icon));
                    localidades.add(Municipio);

                    for(String s: localidades){
                        if(!localidadesUnicas.contains(s))
                            localidadesUnicas.add(s);
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        //Manejo del spinner de localidades
        spinnerLocalidades = (Spinner)findViewById(R.id.spnLocalidades);
        ArrayAdapter<String> adapterSpinnerLocalidades = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,localidadesUnicas);
        adapterSpinnerLocalidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSpinnerLocalidades.insert("",0); //Work arround para que tome el evento de OnItemSelected
        spinnerLocalidades.setAdapter(adapterSpinnerLocalidades);

        spinnerLocalidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String localidad= spinnerLocalidades.getSelectedItem().toString();
                Double latitudLocalidad=0.00001;
                Double longitudLocalidad=0.0001;

                for(int a=0; a<comercios.size();a++){
                    if(comercios.get(a).getMunicipio().equals(localidad)){
                        latitudLocalidad=comercios.get(a).getLatitud();
                        longitudLocalidad=comercios.get(a).getLongitud();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudLocalidad,longitudLocalidad), 15));
                    }
                }

                spinnerLocalidades.setSelection(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

            @Override
            public View getInfoWindow(Marker marker) {
                render(marker, popup);
                return popup;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if(popup == null ){
                    popup=getLayoutInflater().inflate(R.layout.popupmaps, null);
                }
                TextView tv = (TextView)popup.findViewById(R.id.title);
                ImageView iv = (ImageView)popup.findViewById(R.id.icon);
                iv.setImageResource(R.drawable.raffe);
                tv.setText(marker.getTitle());
                tv=(TextView)popup.findViewById(R.id.snippet);;
                tv.setText(marker.getSnippet());



                return(popup);
            }

            public void render(Marker marker, View view){

            }
        });

        if (latitud==0 || longitud ==0){
            //seteo ubicacion fija porque me fallo la geolocalizacion
            //ConsumaxEsquina
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-31.3906131,-58.0032715), 12));
        } else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud), 15));
        }
    }


    public int setIcon(String resultado){

        int iconMap = 0;

        switch (resultado) {
            case "marker":
                iconMap = R.drawable.marker;
                break;

        }

        return iconMap;
    }
    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(getBaseContext(), 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(getBaseContext(), 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }



    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

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

        }
        else if(id == R.id.Comercios){
            Intent comercios = new Intent(getApplicationContext(),comerciosActivity.class);
            startActivity(comercios);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
