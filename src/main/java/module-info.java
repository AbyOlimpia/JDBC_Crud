module com.example.jdbcjavafxcrud2022 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.jdbcjavafxcrud2022 to javafx.fxml;
    exports com.example.jdbcjavafxcrud2022;


}