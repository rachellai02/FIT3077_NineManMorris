module com.example.ninemanmorris_team10 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ninemanmorris_team10 to javafx.fxml;
    exports com.example.ninemanmorris_team10;
    exports com.example.ninemanmorris_team10.Controller;
    opens com.example.ninemanmorris_team10.Controller to javafx.fxml;
    exports com.example.ninemanmorris_team10.BoardAction;
    opens com.example.ninemanmorris_team10.BoardAction to javafx.fxml;
    exports com.example.ninemanmorris_team10.Enum;
    opens com.example.ninemanmorris_team10.Enum to javafx.fxml;
}