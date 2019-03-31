package cabureweb.consumax;



public class Promociones {
    private String descuento;
    private String desde;
    private String diasPuntuales;
    private String direccion;
    private String hasta;
    private Double latitud;
    private Double longitud;
    private String nombreDeFantasia;
    private String promocion;
    private String rubro;
    private String sucursal;
    private String tope;
    private String id;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getDiasPuntuales() {
        return diasPuntuales;
    }

    public void setDiasPuntuales(String diasPuntuales) {
        this.diasPuntuales = diasPuntuales;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
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

    public String getNombreDeFantasia() {
        return nombreDeFantasia;
    }

    public void setNombreDeFantasia(String nombreDeFantasia) {
        this.nombreDeFantasia = nombreDeFantasia;
    }

    public String getPromocion() {
        return promocion;
    }

    public void setPromocion(String promocion) {
        this.promocion = promocion;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getTope() {
        return tope;
    }

    public void setTope(String tope) {
        this.tope = tope;
    }

    public Promociones(Double pLatiud, Double pLongitud, String pId){
        latitud=pLatiud;
        longitud=pLongitud;
        id = pId;
    }

    public Promociones(String pDescuento, String pDesde, String pDiasPuntuales, String pDireccion, String pHasta, Double pLatiud,
                       Double pLongitud, String pNombreDeFantasia, String pPromocion, String pRubro, String pSucursal, String pTope, String pId){

        id = pId;
        descuento = pDescuento;
        desde=pDesde;
        diasPuntuales=pDiasPuntuales;
        direccion=pDireccion;
        hasta=pHasta;
        latitud=pLatiud;
        longitud=pLongitud;
        nombreDeFantasia=pNombreDeFantasia;
        promocion=pPromocion;
        rubro=pRubro;
        sucursal=pSucursal;
        tope=pTope;
    }
    public Promociones(){}

}
