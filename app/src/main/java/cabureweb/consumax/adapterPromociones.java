package cabureweb.consumax;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xelere-lenovo on 7/11/2017.
 */

public class adapterPromociones extends ArrayAdapter {


    public List<Promociones>promociones;
    public LayoutInflater inflater;

    public adapterPromociones(Activity activity, List<Promociones> promociones){
        super(activity, R.layout.activity_adapter_promociones, promociones);
        this.promociones=promociones;
        inflater=activity.getWindow().getLayoutInflater();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView= inflater.inflate(R.layout.activity_adapter_promociones, null, true);
        Promociones promo = promociones.get(position);


        ImageView imgPromo = (ImageView)convertView.findViewById(R.id.imgPromocion);
        ImageView imgLocal = (ImageView)convertView.findViewById(R.id.imgLocal);
        TextView txtPromo = (TextView) convertView.findViewById(R.id.txtDetallePromocion);
        TextView txtLocal = (TextView) convertView.findViewById(R.id.txtDetalleLocal);

        if(promo.getDescuento().equals("15.00%")){
            imgPromo.setImageResource(R.drawable.promo15);
        }

        if(promo.getDescuento().equals("20.00%")){
            imgPromo.setImageResource(R.drawable.promo20);
        }

        if(promo.getDescuento().equals("30.00%")){
            imgPromo.setImageResource(R.drawable.promo30);
        }

        //txtPromo.setText(promo.getPromocion());
        txtLocal.setText(promo.getDireccion());

        return convertView;
    }


}
