package cabureweb.consumax;

/**
 * Created by nicolas.a.simeone on 11/24/2017.
 */

public class Comercios {
    private String categoriaprincipal;
    private int codigopostal;
    private String direccion;
    private String estado;
    private int horariodedomingo;
    private int horariodejueves;
    private int horariodelunes;
    private int horariodemartes;
    private int horariodemiercoles;
    private int horariodesabado;
    private int horariodeviernes;
    private Double latitud;
    private Double longitud;
    private String municipio;
    private String nombredelaempresa;
    private String pais;
    private String sitioweb;
    private String areaadministrativa;
    private int telefonoprincipal;


    public String getCategoriaprincipal(){
        return categoriaprincipal;
    }

    public void setCategoriaprincipal(String categoriaprincipal){
        this.categoriaprincipal = categoriaprincipal;
    }

    public int getCodigopostal(){
        return codigopostal;
    }

    public void setCodigopostal(int codigopostal){
        this.codigopostal = codigopostal;
    }

    public String getDireccion(){
        return direccion;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public int getHorariodedomingo(){
        return horariodedomingo;
    }

    public void setHorariodedomingo(int horariodedomingo){
        this.horariodedomingo = horariodedomingo;
    }

    public int getHorariodejueves(){
        return horariodejueves;
    }

    public void setHorariodejueves(int horariodejueves){
        this.horariodejueves = horariodejueves;
    }

    public int getHorariodelunes(){
        return horariodelunes;
    }

    public void setHorariodelunes(int horariodelunes){
        this.horariodelunes = horariodelunes;
    }

    public int getHorariodemartes(){
        return horariodemartes;
    }

    public void setHorariodemartes(int horariodemartes){
        this.horariodemartes = horariodemartes;
    }

    public int getHorariodemiercoles(){
        return horariodemiercoles;
    }

    public void setHorariodemiercoles(int horariodemiercoles){
        this.horariodemiercoles = horariodemiercoles;
    }

    public int getHorariodesabado(){
        return horariodesabado;
    }

    public void setHorariodesabado(int horariodesabado){
        this.horariodesabado = horariodesabado;
    }

    public int getHorariodeviernes(){
        return horariodeviernes;
    }

    public void setHorariodeviernes(int horariodeviernes){
        this.horariodeviernes = horariodeviernes;
    }

    public int getTelefonoprincipal(){
        return telefonoprincipal;
    }

    public void setTelefonoprincipal(int telefonoprincipal){
        this.telefonoprincipal = telefonoprincipal;
    }

    public String getMunicipio(){
        return municipio;
    }

    public void setMunicipio(String municipio){
        this.municipio = municipio;
    }

    public String getNombredelaempresa(){
        return nombredelaempresa;
    }

    public void setNombredelaempresa(String nombredelaempresa){
        this.nombredelaempresa = nombredelaempresa;
    }

    public String getPais(){
        return pais;
    }

    public void setPais(String pais){
        this.pais = pais;
    }

    public String getSitioweb(){
        return sitioweb;
    }

    public void setSitioweb(String sitioweb){
        this.sitioweb = sitioweb;
    }

    public String getAreaadministrativa(){
        return areaadministrativa;
    }

    public void setAreaadministrativa(String areaadministrativa){
        this.areaadministrativa = areaadministrativa;
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


    public Comercios(Double pLatiud, Double pLongitud, String pMunicipio){
        latitud=pLatiud;
        longitud=pLongitud;
        municipio = pMunicipio;
    }

    public Comercios( Double pLatiud, Double pLongitud, String pCategoriaprincipal, int pCodigopostal, String pDireccion, String pEstado,
                      int pHorariodedomingo, int pHorariodejueves, int pHorariodelunes, int pHorariodemartes, int pHorariodemiercoles,
                      int pHorariodesabado, int pHorariodeviernes, String pMunicipio, String pNombredelaempresa, String pPais, String pSitioweb, String pAreaadministrativa, int pTelefonoprincipal){

        categoriaprincipal = pCategoriaprincipal;
        codigopostal = pCodigopostal;
        direccion = pDireccion;
        estado = pEstado;
        horariodedomingo = pHorariodedomingo;
        horariodejueves = pHorariodejueves;
        horariodelunes = pHorariodelunes;
        horariodemartes = pHorariodemartes;
        horariodemiercoles = pHorariodemiercoles;
        horariodesabado = pHorariodesabado;
        horariodeviernes = pHorariodeviernes;
        municipio = pMunicipio;
        nombredelaempresa = pNombredelaempresa;
        pais = pPais;
        sitioweb = pSitioweb;
        areaadministrativa = pAreaadministrativa;
        telefonoprincipal = pTelefonoprincipal;
        latitud=pLatiud;
        longitud=pLongitud;


    }
}
