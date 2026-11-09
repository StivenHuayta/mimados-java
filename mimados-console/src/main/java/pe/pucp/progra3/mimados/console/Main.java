package pe.pucp.progra3.mimados.console;

import pe.pucp.progra3.mimados.bo.*;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.*;
import pe.pucp.progra3.mimados.model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args){
        UsuarioBO usuarioBO=new UsuarioBOImpl();
        ClienteBO clienteBO= new ClienteBOImpl();
        SedeBO sedeBO=new SedeBOImpl();
        EmpleadoBO empleadoBO=new EmpleadoBOImpl();
        MascotaBO mascotaBO=new MascotaBOImpl();
        // prueba de insertar
        Usuario us1=new Usuario("11111111","usuarioPrueba","apellidoPrueba",
                "email@rroba","p4ssw0rd","999999999");
        try{
            usuarioBO.insertar(us1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        // prueba listar todos
        List<Usuario> usuarios;
        try{
            usuarios =usuarioBO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Usuario us:usuarios){
            System.out.printf("   %d -  %s - %s - %s%n",
                    us.getId(), us.getDni(), us.getApellidos(),us.getTelefono());
        }
        // cambiar telefono de 6 prueba de update
        us1.setTelefono("999");

        try{
            usuarioBO.modificar(us1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }

        Usuario prueba=new Usuario();
        // prueba de mostrar por id
        try{
            prueba=usuarioBO.obtenerPorId(6);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("   %d -  %s - %s - %s  %n",
                prueba.getId(), prueba.getDni(), prueba.getApellidos(),prueba.getTelefono());

        Cliente cli1= new Cliente(us1,"calle el olimpo 123","955556333",true);
        //prueba insertar cliente
        try{
            clienteBO.insertar(cli1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }

        // prueba listar clientes
        List<Cliente> clientes ;
        try{
            clientes= clienteBO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Cliente cli:clientes){
            System.out.printf("   %d -  %s - %s - "+cli.getActivo()+"%n",
                    cli.getUsuario().getId(),cli.getDireccion(), cli.getTelefonoEmergencia() );
        }

        cli1.setTelefonoEmergencia("7777777");

        try{
            clienteBO.modificar(cli1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        // buscar por id
        Cliente clientePrueba;
        try{
            clientePrueba=clienteBO.obtenerPorId(cli1.getUsuario().getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("   %d -  %s - %s - "+clientePrueba.getActivo()+"%n",
                clientePrueba.getUsuario().getId(),clientePrueba.getDireccion(), clientePrueba.getTelefonoEmergencia() );

        // prueba insertar sede
        Sede sede1= new Sede("Huellitas - san miguel","direccion en sm","999999999",true);
        try{
            sedeBO.insertar(sede1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }

        // prueba listar sedes
        List<Sede>sedes;
        try{
            sedes=sedeBO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Sede sed:sedes){
            System.out.printf("   %d -  %s - %s - "+sed.getActivo()+"%n",
                    sed.getId(),sed.getNombre(), sed.getDireccion());
        }

        //actualizar sede
        sede1.setDireccion("nueva direccion");
        try{
            sedeBO.modificar(sede1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }

        //buscar por id y tmb comprobar modificaicon
        Sede sedePrueba;
        try{
            sedePrueba=sedeBO.obtenerPorId(sede1.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("   %d -  %s - %s - "+sedePrueba.getActivo()+"%n",
                sedePrueba.getId(),sedePrueba.getNombre(), sedePrueba.getDireccion());

        //eliminar sede
        try{
            sedeBO.eliminar(sede1.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        // usuario para planaer empleado
        Usuario us2=new Usuario("22222222","usEmpleado","apellidoP",
                "email@rroba2","p4ssw0rd2","999999989");
        try{
            usuarioBO.insertar(us2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        // insertar empleado
        Empleado emp1=new Empleado(sede1.getId(),us2.getId(),"cmvp-11",false,true,null,null, null,null);
        try{
            empleadoBO.insertar(emp1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        //lsitar empleados
        List<Empleado> empleados;
        try{
            empleados=empleadoBO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // imprime si es admin y luego si esta activo, en ese orden
        for (Empleado emp:empleados){
            System.out.printf("   %d -  %s -  %s - "+emp.getEsAdmin()+" - "+emp.getActivo()+"%n",
                    emp.getUsuario().getId(),emp.getUsuario().getApellidos(),emp.getCodigoCmvp());
        }
        emp1.setCodigoCmvp("nuCMVP");
//        try{
//            // falta actualizar en elpleado BO
////            empleadoBO.
//        }

        //eliminar empleado
        try{
            empleadoBO.eliminar(emp1.getUsuario().getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }

        //mostrar empleado por id
        Empleado empleadoPrueba;
        try{
            empleadoPrueba=empleadoBO.obtenerPorId(emp1.getUsuario().getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("   %d -  %s -  %s - "+empleadoPrueba.getEsAdmin()+" - "+empleadoPrueba.getActivo()+"%n",
                empleadoPrueba.getUsuario().getId(),empleadoPrueba.getUsuario().getApellidos(),empleadoPrueba.getCodigoCmvp());

        Mascota mas1= new Mascota(9,cli1, "chimenea",Especie.CANINO,"raza",Sexo.MACHO,
                                    LocalDate.of(2000, 5, 15),10.5,true);
        // insertar mascota
        try{
            mascotaBO.insertar(mas1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }


        //listar mascotas
        List<Mascota>mascotas;
        try{
            mascotas=mascotaBO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Mascota mas:mascotas){
            System.out.printf("   %d -  %s - %s - "+mas.getActivo()+"%n",
                    mas.getId(),mas.getNombre(), mas.getEspecie());
        }


        //modificar mascota
        mas1.setNombre("nuevoNombre");
        try{
            mascotaBO.modificar(mas1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }



        // matar mascota
        try{
            mascotaBO.eliminar(mas1.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }

        // mostar mascota por id del dueno
        Mascota mascotaPrueba;
        try{
            mascotaPrueba=mascotaBO.obtenerPorId(mas1.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NegocioException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("   %d -  %s - %s  - "+mascotaPrueba.getActivo()+"%n",
                mascotaPrueba.getId(),mascotaPrueba.getNombre(), mascotaPrueba.getEspecie());

    }
}

