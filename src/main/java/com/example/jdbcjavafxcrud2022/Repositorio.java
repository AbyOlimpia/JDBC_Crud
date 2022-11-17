package com.example.jdbcjavafxcrud2022;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class Repositorio {
    Connection conexion;
    public void setConexion() {
        try {
            //Conecto con MySQL
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/empresa","root","");
            Statement st=conexion.createStatement();

            //Si no existe la tabla de trabajadores, la creo
            st.execute("CREATE TABLE IF NOT EXISTS trabajadores(id_trabajador INT NOT NULL AUTO_INCREMENT, nombre VARCHAR(255), PRIMARY KEY (id_trabajador));");

            //Si no hay ningún trabajador, añado uno
            st.execute("INSERT INTO trabajadores (nombre) SELECT 'Algernon Flores' FROM DUAL WHERE NOT EXISTS (SELECT * FROM trabajadores);");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ObservableList<Trabajador> getTodosLosTrabajadores(){
        ObservableList<Trabajador> trabajadores= FXCollections.observableArrayList();
        try {

            PreparedStatement st=conexion.prepareStatement("SELECT * FROM trabajadores");
            ResultSet rs=st.executeQuery();

            while(rs.next()){
                //Convertimos los registros de la BD con los datos de trabajadores en objetos Trabajador
                trabajadores.add(new Trabajador(rs.getInt("id_trabajador"), rs.getString("nombre")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trabajadores;
    }

    public void insertarTrabajador(Trabajador t){
        try {
            PreparedStatement ps=conexion.prepareStatement("INSERT INTO trabajadores values(NULL, ?)");
            ps.setString(1, t.getNombre());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Movimiento existeEntrada(Movimiento m){
        Movimiento aux=null;
        try {
            PreparedStatement st=conexion.prepareStatement("SELECT * FROM movimientos WHERE trabajador=? AND fecha_salida IS NULL;");
            st.setString(1, m.getTrabajador());
            ResultSet rs=st.executeQuery();

            while(rs.next()){
                aux = new Movimiento();
                //Convertimos los registros de la BD con los datos de trabajadores en objetos Trabajador
                aux.setTrabajador(rs.getString("trabajador"));
                aux.setHoraEntrada(rs.getTime("hora_entrada"));
                aux.setFechaEntrada(rs.getDate("fecha_entrada"));
                aux.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return aux;
    }

    public void insertarFichaje(Movimiento m){
        try {
            PreparedStatement ps=conexion.prepareStatement("INSERT INTO movimientos " +
                    "(trabajador,fecha_entrada,hora_entrada,fecha_salida,hora_salida) values(?, ?, ?, ?, ?)");
            ps.setString(1, m.getTrabajador());
            ps.setDate(2, m.getFechaEntrada());
            ps.setTime(3, m.getHoraEntrada());
            ps.setDate(4, m.getFechaSalida());
            ps.setTime(5, m.getHoraSalida());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificarTrabajador(Trabajador t){
        try{
            PreparedStatement ps=conexion.prepareStatement("UPDATE trabajadores SET nombre=? WHERE id_trabajador=?");
            ps.setString(1, t.getNombre());
            ps.setInt(2, t.getIdTrabajador());
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void modificarMovimiento(Movimiento m){
        try{
            PreparedStatement ps=conexion.prepareStatement("UPDATE movimientos SET trabajador=?, fecha_entrada=?," +
                    "hora_entrada=?, fecha_salida=?, hora_salida=?  WHERE id=?");
            ps.setString(1, m.getTrabajador());
            ps.setDate(2, m.getFechaEntrada());
            ps.setTime(3, m.getHoraEntrada());
            ps.setDate(4, m.getFechaSalida());
            ps.setTime(5, m.getHoraSalida());
            ps.setInt(6, m.getId());
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void borrarTrabajador(int idTrabajador){

        try{
            PreparedStatement ps=conexion.prepareStatement("DELETE FROM trabajadores WHERE id_trabajador=?");
            ps.setInt(1, idTrabajador);
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

//st.execute("CREATE TABLE IF NOT EXISTS books( id INT NOT NULL AUTO_INCREMENT, id_editorial INT NOT NULL, title varchar(255), author varchar(255), year INT, pages INT, PRIMARY KEY(id), INDEX (id_editorial), FOREIGN KEY (id_editorial) REFERENCES editorial(id_editorial))");
//st.execute("INSERT INTO editorial (editorial, pais) SELECT 'Planeta', 'España' FROM DUAL WHERE NOT EXISTS (SELECT * FROM editorial);");