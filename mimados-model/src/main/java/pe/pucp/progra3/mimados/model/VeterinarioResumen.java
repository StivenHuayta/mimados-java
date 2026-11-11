
package pe.pucp.progra3.mimados.model;

public class VeterinarioResumen {

    private int empleadoId;
    private String nombre;
    private int sedeId;
    private String sede;
    private String puesto;

    public VeterinarioResumen() {
    }

    public VeterinarioResumen(int empleadoId, String nombre,
                              int sedeId, String sede, String puesto) {
        this.empleadoId = empleadoId;
        this.nombre = nombre;
        this.sedeId = sedeId;
        this.sede = sede;
        this.puesto = puesto;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSedeId() {
        return sedeId;
    }

    public void setSedeId(int sedeId) {
        this.sedeId = sedeId;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
}
