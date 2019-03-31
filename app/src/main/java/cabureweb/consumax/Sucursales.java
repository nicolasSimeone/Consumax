package cabureweb.consumax;

/**
 * Created by nicolas.a.simeone on 11/24/2017.
 */

public class Sucursales {
    private String localId;
    private Double latitud;
    private Double longitud;
    private String localidad;


    public String getLocalId(){
        return localId;
    }

    public void setLocalId(String localId){
        this.localId = localId;
    }
    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String sucursal) {
        this.localidad = localidad;
    }


    public Sucursales(){}

    public Sucursales( Double pLatiud, Double pLongitud,String pLocalidad, String pLocalId){

        localId = pLocalId;
        latitud=pLatiud;
        longitud=pLongitud;
        localidad=pLocalidad;

    }
}
