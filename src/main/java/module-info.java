// Đặt file này vào thư mục src/main/java
module com.andy.screen.shoot {
    // Khai báo các module của JavaFX mà ứng dụng của bạn cần
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;

    // Khai báo các module của các thư viện khác
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // Mở gói chứa MainApplication để JavaFX FXML Loader có thể truy cập
    opens com.andy.screen.shoot to javafx.fxml;
    opens com.andy.screen.shoot.about to javafx.fxml;

    // Xuất gói để ứng dụng có thể chạy
    exports com.andy.screen.shoot;
}
