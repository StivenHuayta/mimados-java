package pe.pucp.progra3.mimados.model.dto.response;

public class ReporteServiciosSolicitadosDTO {
    private int orden;
    private String nombre;
    private int cantidad;
    private double precio;
    private double total;
    private double proporcion;

    public ReporteServiciosSolicitadosDTO(int orden, String nombre, int cantidad, double precio,
                                          double total, double proporcion) {
        this.orden = orden;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
        this.proporcion = proporcion;

    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getProporcion() {
        return proporcion;
    }

    public void setProporcion(double proporcion) {
        this.proporcion = proporcion;
    }
}
