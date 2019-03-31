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
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PromocionesMapaActivity extends FragmentActivity implements OnMapReadyCallback {
    public ArrayList<Promociones> promocioneslista= new ArrayList<Promociones>();
    private GoogleMap mMap;
    private double latitud;
    private double longitud;
    private String promoid;
    private String direccion;
    private String tope;
    private String nombreFantasia;
    private View popup=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promociones_mapa);

        Intent intent=getIntent();
        latitud= intent.getDoubleExtra("lat", 0);
        longitud= intent.getDoubleExtra("lon", 0);
        tope= intent.getStringExtra("top");
        direccion= intent.getStringExtra("dir");
        nombreFantasia = intent.getStringExtra("nom");
        promoid = intent.getStringExtra("id");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                render(marker, popup);
                return popup;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (popup == null) {
                    popup = getLayoutInflater().inflate(R.layout.popupmaps, null);
                }
                TextView tv = (TextView) popup.findViewById(R.id.title);
                ImageView iv = (ImageView) popup.findViewById(R.id.icon);
                iv.setImageResource(R.drawable.raffe);
                tv.setText(marker.getTitle());
                tv = (TextView) popup.findViewById(R.id.snippet);
                ;
                tv.setText(marker.getSnippet());


                return (popup);
            }

            public void render(Marker marker, View view) {

            }
        });

        final ArrayList<Promociones> output = new ArrayList<Promociones>();




        if (latitud==0 || longitud ==0){
            //seteo ubicacion fija porque me fallo la geolocalizacion
            //ConsumaxEsquina
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-31.396618,-58.0212747), 12));
        } else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud), 15));
        }

        LatLng LL = new LatLng(latitud, longitud);


        Marker myLocMarker = mMap.addMarker(new MarkerOptions()
                .snippet(direccion + "\n" + tope)
                .title(nombreFantasia)
                .position(LL)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markercomercios)));

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
}
