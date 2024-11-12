package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContadorTiempo extends Application {
    private TextField inputField;
    private ProgressBar progressBar;
    private Label tiempoLabel;
    private Button iniciarButton;
    private Button cancelarButton;
    private int tiempoTotal;
    private int tiempoActual;
    private boolean contando;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Contador de Tiempo");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label instruccionLabel = new Label("Introduce el tiempo en segundos:");
        inputField = new TextField();
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);
        tiempoLabel = new Label("Tiempo: 0 segundos");

        iniciarButton = new Button("Iniciar");
        cancelarButton = new Button("Cancelar");
        cancelarButton.setDisable(true);

        root.getChildren().addAll(instruccionLabel, inputField, progressBar, tiempoLabel, iniciarButton, cancelarButton);

        iniciarButton.setOnAction(e -> iniciarContador());
        cancelarButton.setOnAction(e -> cancelarContador());

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void iniciarContador() {
        try {
            tiempoTotal = Integer.parseInt(inputField.getText());
            if (tiempoTotal <= 0) {
                mostrarAlerta("Error", "Por favor, introduce un número válido mayor que cero.");
                return;
            }
            tiempoActual = 0;
            contando = true;
            progressBar.setProgress(0);
            iniciarButton.setDisable(true);
            cancelarButton.setDisable(false);
            inputField.setDisable(true);

            new Thread(() -> {
                while (contando && tiempoActual < tiempoTotal) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        break;
                    }
                    tiempoActual++;
                    Platform.runLater(this::actualizarUI);
                }
                if (tiempoActual >= tiempoTotal && contando) {
                    Platform.runLater(() -> {
                        mostrarAlerta("Tiempo completado", "¡El tiempo ha finalizado!");
                        reiniciarUI();
                    });
                }
            }).start();
        } catch (NumberFormatException ex) {
            mostrarAlerta("Error", "Por favor, introduce un número válido mayor que cero.");
        }
    }

    private void cancelarContador() {
        contando = false;
        reiniciarUI();
    }

    private void actualizarUI() {
        double progreso = (double) tiempoActual / tiempoTotal;
        progressBar.setProgress(progreso);
        tiempoLabel.setText("Tiempo: " + tiempoActual + " segundos");
    }

    private void reiniciarUI() {
        iniciarButton.setDisable(false);
        cancelarButton.setDisable(true);
        inputField.setDisable(false);
        progressBar.setProgress(0);
        tiempoLabel.setText("Tiempo: 0 segundos");
        inputField.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

