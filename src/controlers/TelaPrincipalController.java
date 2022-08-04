/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlers;

import banco.CriaBancoDados;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.robot.Robot;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;

//classe que recebe a folha de estilos. Como todas as outras telas são//
//carregadas nela, seus estilos refletem os deta//
public class TelaPrincipalController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private MenuItem desfazer;
    @FXML
    private MenuItem refazer;
    @FXML
    private MenuItem cortar;
    @FXML
    private MenuItem copiar;
    @FXML
    private MenuItem colar;
    @FXML
    private MenuItem tudo;
    public Label txtEsquerdo;
    public Text txtEstrutura;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verificarBanco();
        loadUI("/telas/TelaVendas.fxml");
    }    

    @FXML
    private void sair(ActionEvent event) {
        Platform.exit();
        root.setVisible(false);
    }

    @FXML
    private void chamarAeronaves(ActionEvent event) {
        loadUI("/telas/TelaAeronaves.fxml");
    }

    @FXML
    private void chamarVoos(ActionEvent event) {
        loadUI("/telas/TelaVoos.fxml");
    }

    @FXML
    private void chamarOcupacao(ActionEvent event) {
        loadUI("/telas/TelaOcupacao.fxml");
    }

    //simula o precionar de teclas para usar atalhos do sistema//
    @FXML
    private void atalho(ActionEvent event) {
        Robot r = new Robot();
        r.keyPress(KeyCode.CONTROL);
        if (event.getSource() == desfazer) {
            r.keyType(KeyCode.Z);
        } else if (event.getSource() == refazer) {
            r.keyType(KeyCode.Y);
        } else if (event.getSource() == cortar) {
            r.keyType(KeyCode.X);
        } else if (event.getSource() == copiar) {
            r.keyType(KeyCode.C);
        } else if (event.getSource() == colar) {
            r.keyType(KeyCode.V);
        } else {
            r.keyType(KeyCode.A);
        }
        r.keyRelease(KeyCode.CONTROL);
    }

    //carrega o painel a ser exibido no centro da cena//
    public void loadUI(String nomeArq) {
        try {
            Pane novaTela = (Pane) new FXMLLoader().load(getClass().getResource(nomeArq));
            root.setCenter(novaTela);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void chamarVendas(ActionEvent event) {
        loadUI("/telas/TelaVendas.fxml");
    }

    @FXML
    private void chamarRelatorioOcupacao(ActionEvent event) {
        loadUI("/telas/TelaRelatorioOcupacao.fxml");
    }

    @FXML
    private void chamarRelatorioVendas(ActionEvent event) {
        loadUI("/telas/TelaRelatorioVendas.fxml");
    }

    //verifica a necessidadee de criação do banco ao iniciar a aplicação//
    private void verificarBanco() {
        CriaBancoDados banco = new CriaBancoDados();
        if(!banco.makeDatabase()) {
            try {
                banco.makeTables();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            }
        }
    }
    
}
