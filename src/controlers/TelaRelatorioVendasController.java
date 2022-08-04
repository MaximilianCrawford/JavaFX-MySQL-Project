/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlers;

import dao.PassageirosVooDAO;
import dao.VooDAO;
import entidades.PassageirosVoo;
import entidades.Voo;
import java.net.URL;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class TelaRelatorioVendasController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Label txtEsquerdo;
    @FXML
    private GridPane gCalendario;
    @FXML
    private ComboBox<Voo> cbData;
    @FXML
    private Text txtMes;
    private PassageirosVooDAO banco;
    private VooDAO bancoVoo;
    private ObservableList<Voo> lstVoo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        banco = new PassageirosVooDAO();
        bancoVoo = new VooDAO();
        buscarPassageirosVoo();
    }

    /* percorre a matriz do calendario preenchendo-a com o valor de vendas em cada dia presente no mes do Voo selecionado.
    Após isso indica o valor total de vendas no mês, obtido através do enum Month. Também limpa o calendario antes de cada 
    execução para popula-lo com valores novos.
    */ 
    @FXML
    private void buscarVendas(ActionEvent event) {
        Voo alvo = cbData.getValue();
        List<PassageirosVoo> mesAlvo = new ArrayList<>();
        List<Integer> diasComVendas = new ArrayList<>();
        Double valorMes = 0.0;
        int dias = Month.of(Integer.parseInt(alvo.getDataPartida().substring(3, 5))).length(false), t = 0, c = 0;
        gCalendario.getChildren().clear();
        for(PassageirosVoo voo : FXCollections.observableArrayList(banco.consultar())) {
            if(voo.getVoo().getDataPartida().substring(3, 5).equals(alvo.getDataPartida().substring(3, 5))) {
                mesAlvo.add(voo);
                diasComVendas.add(Integer.parseInt(voo.getVoo().getDataPartida().substring(0, 2)));
            }
        }
        for(int i = 0; i < dias; i++) {
            if((i % 7 == 0) && (i != 0)) {
                t++;
                c = 0;
            }
            if(diasComVendas.contains(i + 1)) {
                Double soma = 0.0;
                for(PassageirosVoo p : mesAlvo) {
                    if(Integer.parseInt(p.getVoo().getDataPartida().substring(0, 2)) == i + 1) {
                        soma += p.getValorPago();
                    }
                }
                gCalendario.add(new Text(String.valueOf(i + 1) + ": R$: " + soma), c, t);
                valorMes += soma;
            } else {
                gCalendario.add(new Text(String.valueOf(i + 1) + ": Sem vendas neste dia"), c, t); 
            }
            c++;
        }
        txtMes.setText(Month.of(Integer.parseInt(alvo.getDataPartida().substring(3, 5))).getDisplayName(TextStyle.FULL, Locale.getDefault()) + ": R$" + valorMes);
        txtEsquerdo.setText("Relatório do mês " + Month.of(Integer.parseInt(alvo.getDataPartida().substring(3, 5))).getDisplayName(TextStyle.FULL, Locale.getDefault()));
        gCalendario.setGridLinesVisible(false);
        gCalendario.setGridLinesVisible(true);
        gCalendario.setVisible(true);
    }

    //busca entidades Voo para popular o combo box de datas com Converter especifico para o uso//
    private void buscarPassageirosVoo() {
        lstVoo = FXCollections.observableArrayList(bancoVoo.consultar());
        cbData.setItems(lstVoo);
        cbData.setConverter(new StringConverter<Voo>() {

            @Override
            public String toString(Voo object) {
                return object.getDataPartida().substring(3);
            }

            @Override
            public Voo fromString(String string) {
                return cbData.getItems().stream().filter(ap
                        -> ap.getCodigo().equals(string)).findFirst().orElse(null);
            }
        });
    }

}
