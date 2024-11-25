module comp3111.examsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jconsole;
    requires java.logging;
    requires javafx.base;
    exports comp3111.examsystem.entity;


    opens comp3111.examsystem to javafx.fxml;
    exports comp3111.examsystem;
    opens comp3111.examsystem.controller to javafx.fxml;
    exports comp3111.examsystem.controller;
    opens comp3111.examsystem.entity to javafx.base;
}