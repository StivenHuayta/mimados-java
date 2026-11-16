package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.dao.ReporteServicioDAO;
import pe.pucp.progra3.mimados.model.dto.response.ReporteServiciosSolicitadosDTO;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteServicioDAOimp implements ReporteServicioDAO {
    @Override
    public List<ReporteServiciosSolicitadosDTO> obtenerServiciosMasSolicitados(Date fechaInicio, Date fechaFin)
            throws SQLException {
        List<ReporteServiciosSolicitadosDTO> servicios = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall("{CALL sp_obtener_reporte_servicios_por_fechas(?,?)}")
            ){
            cs.setDate(1 , fechaInicio);//hola eeee
            cs.setDate(2 , fechaFin);
            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){

                    ReporteServiciosSolicitadosDTO serv = new ReporteServiciosSolicitadosDTO(
                            rs.getInt("orden"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio"),
                            rs.getDouble("total"),
                            rs.getDouble("proporcion")

                    );
                    servicios.add(serv);
                }
            }

        }
        return servicios;
    }

}
