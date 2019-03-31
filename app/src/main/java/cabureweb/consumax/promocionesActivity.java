package cabureweb.consumax;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class promocionesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public ListView listView; //Lista de promociones
    private Spinner spinnerLocalidades;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ArrayList<String>localidadesUnicas = new ArrayList<>(); //Array para obtener los valores unicos de las localidades

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promociones);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.promocionesdrawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Localización harcodeada
        final Location locationA = new Location ("puntoA");
        locationA.setLatitude(-31.3785101);
        locationA.setLongitude(-58.076641);

        final Location locationB = new Location("puntoB");

        final List<String> localidades=new ArrayList<String>(); //array de localidades de las promociones

        Integer [] imagenPromoArray={R.drawable.com_facebook_favicon_blue,R.drawable.com_facebook_favicon_blue,R.drawable.com_facebook_button_icon_white,R.drawable.com_facebook_button_like_icon_selected};
        String [] textPromoArray = { "Promo 1","Promo 2","Promo 3","Promo 4"};
        Integer []imagenLocalArray={R.drawable.com_facebook_favicon_blue,R.drawable.com_facebook_favicon_blue,R.drawable.com_facebook_button_icon_white,R.drawable.com_facebook_button_like_icon_selected};
        String [] textInfoLocal = { "Local 1","Local 2","Local 3","Local 4"};;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Promociones");
        final ArrayList<Promociones> promociones = new ArrayList<Promociones>(); //promociones en general
        final ArrayList<Promociones> promocionesFiltradas = new ArrayList<Promociones>(); //Array para guardar las promociones filtradas
        final ArrayList<Promociones> promocionesAux = new ArrayList<Promociones>(); //Auxiliar para no poerder los datos en el filtro
        final adapterPromociones adapterPromociones  = new adapterPromociones(this,promociones);
        final List<String> output = new ArrayList<String>();
        //LLamada a Firebase para traer el nodo promociones
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listView= (ListView)findViewById(R.id.listaPromociones);
                listView.setAdapter(adapterPromociones);





                //loop por los nodos de las promociones
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()){

                    final String promoid = (String) messageSnapshot.child("ID").getValue();
                    String descuento = (String) messageSnapshot.child("Descuento").getValue();
                    String desde = (String) messageSnapshot.child("Desde").getValue();
                    String diasPuntuales= (String) messageSnapshot.child("DiasPuntuales").getValue();
                    final String direccion = (String)messageSnapshot.child("Dirección").getValue();
                    String hasta = (String) messageSnapshot.child("Hasta").getValue();
                    final Double latitud = (Double) messageSnapshot.child("Latitud").getValue();
                    final Double longitud = (Double) messageSnapshot.child("Longitud").getValue();
                    final String nombreFantasia = (String) messageSnapshot.child("NombreDeFantasia").getValue();
                    String promocion = (String) messageSnapshot.child("Promocion").getValue();
                    String rubro = (String) messageSnapshot.child("Rubro").getValue();
                    String sucursal = (String) messageSnapshot.child("Sucursal").getValue();
                   final String tope = (String) messageSnapshot.child("Tope").getValue();

                    locationB.setLatitude(latitud);
                    locationB.setLongitude(longitud);


                    double distancia = locationA.distanceTo(locationB);
                    double distanciaKm = distancia * 0.001;

                    Log.d("Distancia:",  String.valueOf(distanciaKm));
                    Promociones promo = new Promociones(descuento,desde,diasPuntuales,direccion,hasta,latitud,longitud,nombreFantasia,promocion,rubro,sucursal,tope,promoid);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(promocionesActivity.this, PromocionesMapaActivity.class);
                            intent.putExtra("lat", latitud);
                            intent.putExtra("lon", longitud);
                            intent.putExtra("dir", direccion);
                            intent.putExtra("nom",nombreFantasia);
                            intent.putExtra("id", promoid);
                            intent.putExtra("top", tope);

                            startActivity(intent);
                        }
                    });

                    if(distanciaKm<11){
                        promociones.add(promo);
                        adapterPromociones.notifyDataSetChanged();
                        localidades.add(sucursal);
                        promocionesAux.add(promo);

                    }
                    // promociones.add(promo);
                    //adapterPromociones.notifyDataSetChanged();
                    //localidades.add(sucursal);
                    //promocionesAux.add(promo);

                    //Comparo las localidades para solo dejar las unicas, se se hace fuera del metodo no funciona
                    for(String s: localidades){
                        if(!localidadesUnicas.contains(s))
                            localidadesUnicas.add(s);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                //Filtro los eventos por localidades
                String localidad= spinnerLocalidades.getSelectedItem().toString();
                for(int a=0; a<promocionesAux.size();a++){
                    if(promocionesAux.get(a).getSucursal().equals(localidad)){
                        promocionesFiltradas.add(promocionesAux.get(a));
                    }
                }

                spinnerLocalidades.setSelection(i);
                promociones.clear();
                promociones.addAll(promocionesFiltradas);
                adapterPromociones.notifyDataSetChanged();
                promocionesFiltradas.clear();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



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
