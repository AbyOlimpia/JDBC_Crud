package com.example.jdbcjavafxcrud2022;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.random;

public class HelloController implements Initializable {
    @FXML
    private Label lblReloj;
    @FXML
    private Label lblInfo;
    @FXML
    private TableView<Trabajador> tblTrabajadores;
    @FXML
    private TableColumn<Trabajador, Integer> colIdTrabajador;
    @FXML
    private TableColumn<Trabajador, String> colNombre;
    @FXML
    private TextField txtIdTrabajador;
    @FXML
    private TextField txtNombre;
    @FXML
    private Button btnFichar;
    @FXML
    private ComboBox comboTrabajadores;

    Repositorio miRepositorio;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Para poner un temporizador con una función lambda. Sin JavaFX, usaríamos Timer y TimerTask
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            lblReloj.setText(String.valueOf(LocalTime.now()).substring(0,8));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        miRepositorio=new Repositorio();
        miRepositorio.setConexion();
        mostrarTrabajadoresTabla();
        ObservableList<Trabajador> lista=miRepositorio.getTodosLosTrabajadores();
        for(Trabajador t:lista){
            comboTrabajadores.getItems().add(t.getNombre());
        }

    }
    public void mostrarTrabajadoresTabla() {
        ObservableList<Trabajador> list = miRepositorio.getTodosLosTrabajadores();

        colIdTrabajador.setCellValueFactory(new PropertyValueFactory<Trabajador,Integer>("idTrabajador"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Trabajador,String>("nombre"));

        tblTrabajadores.setItems(list);
    }

    public void callbackClicTabla(javafx.scene.input.MouseEvent mouseEvent) {
        //Leo los datos del trabajador que han seleccionado al hacer clic en la tabla
        Trabajador trabajador = (Trabajador)  tblTrabajadores.getSelectionModel().getSelectedItem();

        //Guardo los datos de ese trabajador en los TextField para poder editar sus valores
        txtIdTrabajador.setText(String.valueOf(trabajador.getIdTrabajador()));
        txtNombre.setText(String.valueOf(trabajador.getNombre()));

    }
    public void callbackClicNuevo(){
        txtIdTrabajador.setText("");
        txtNombre.setText("");
    }
    public void callbackClicInsertar(){
        //Hay que advertir cuando se escriba un id de que no se va a usar o si el nombre está en blanco
        //También se debe comprobar si el nombre ya existe en la bd para no repetirlo
        Trabajador t=new Trabajador();
        t.setNombre(txtNombre.getText());
        miRepositorio.insertarTrabajador(t);
        mostrarTrabajadoresTabla();
    }

    public void callbackClicFichar(){
        Movimiento m=new Movimiento();
        m.setTrabajador(comboTrabajadores.getValue().toString());
        Movimiento definitivo=miRepositorio.existeEntrada(m);
        if(definitivo==null){
            definitivo= new  Movimiento();
            definitivo.setTrabajador(comboTrabajadores.getValue().toString());
            definitivo.setFechaEntrada(Date.valueOf(LocalDate.now()));
            definitivo.setHoraEntrada(Time.valueOf(LocalTime.now()));
            m.setFechaEntrada(Date.valueOf(LocalDate.now()));
            m.setHoraEntrada(Time.valueOf(LocalTime.now()));
            System.out.println("entrada");
            lblInfo.setText("Ha fichado " + m.trabajador+", a la hora "+String.valueOf(LocalTime.now()).substring(0,8));
            miRepositorio.insertarFichaje(definitivo);
        }else{
            definitivo.setFechaSalida(Date.valueOf(LocalDate.now()));
            definitivo.setHoraSalida(Time.valueOf(LocalTime.now()));
            System.out.println("salida");
            lblInfo.setText("Ha salido " + definitivo.trabajador+", a la hora "+String.valueOf(LocalTime.now()).substring(0,8));
            miRepositorio.modificarMovimiento(definitivo);
        }
        lblInfo.setText("Ha fichado " + definitivo.trabajador+", a la hora "+String.valueOf(LocalTime.now()).substring(0,8));
    }


    public void callbackClicModificar(){
        //No se debe dejar modificar con un nombre vacío o con un id que no exista
        Trabajador t=new Trabajador(Integer.valueOf(txtIdTrabajador.getText()), txtNombre.getText());
        miRepositorio.modificarTrabajador(t);
        mostrarTrabajadoresTabla();
    }
    public void callbackClicBorrar(){
        //Se debe pedir confirmación antes de borrar y comprobar si existe el id
        miRepositorio.borrarTrabajador(Integer.valueOf(txtIdTrabajador.getText()));
        mostrarTrabajadoresTabla();
    }


}