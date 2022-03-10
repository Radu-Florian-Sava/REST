module ro.ubbcluj.mpp.proiectproblema1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;

    opens ro.ubbcluj.mpp.proiectproblema1 to javafx.fxml;
    exports ro.ubbcluj.mpp.proiectproblema1;
    exports ro.ubbcluj.mpp.proiectproblema1.model;
    opens ro.ubbcluj.mpp.proiectproblema1.model to javafx.fxml;
}