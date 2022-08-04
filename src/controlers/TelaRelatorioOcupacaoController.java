/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlers;

import dao.PassageirosVooDAO;
import dao.VooDAO;
import entidades.PassageirosVoo;
import entidades.PassageirosVooImpl;
import entidades.Voo;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class TelaRelatorioOcupacaoController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Label txtEsquerdo;
    @FXML
    private Text txtEstrutura;
    @FXML
    private TableView<PassageirosVooImpl> tRelatorio;
    @FXML
    private TableColumn<String, PassageirosVooImpl> rCodigoVoo;
    @FXML
    private TableColumn<Integer, PassageirosVooImpl> rCapacidade;
    @FXML
    private TableColumn<Integer, PassageirosVooImpl> rOcupados;
    @FXML
    private TableColumn<String, PassageirosVooImpl> rData;
    private List<PassageirosVoo> lstPassageirosVoo;
    private List<Voo> lstVoos;
    private ObservableList<PassageirosVooImpl> lstOcupacao;
    private List<Integer[]> ocupados;
    private PassageirosVooDAO banco;
    private VooDAO bancoVoos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        banco = new PassageirosVooDAO();
        bancoVoos = new VooDAO();
        ocupados = new ArrayList<>();
        rCapacidade.setCellValueFactory(new PropertyValueFactory<>("numAssento"));
        rCodigoVoo.setCellValueFactory(new PropertyValueFactory<>("passageiro"));
        rOcupados.setCellValueFactory(new PropertyValueFactory<>("codigoAeronave"));
        rData.setCellValueFactory(new PropertyValueFactory<>("dataPartida"));
        popularTabela();
    }

    //popula a tabela de ocupações a partir de uma classe especifica para população de tabelas de fonte composta e conta a quantia de passageiros por voo//
    private void popularTabela() {
        lstPassageirosVoo = banco.consultar();
        lstOcupacao = FXCollections.observableArrayList(new ArrayList<>());
        lstVoos = bancoVoos.consultar();
        Integer ocupacoes = 0;
        for (Voo v : lstVoos) {
            for (PassageirosVoo p : v.getPassageirosVooCollection()) {
                ocupacoes++;
            }
            Integer[] mapa = new Integer[2];
            mapa[0] = v.getCodigo();
            mapa[1] = ocupacoes;
            ocupados.add(mapa);
            ocupacoes = 0;
        }

        for (Voo o : lstVoos) {
            for (Integer[] mapa : ocupados) {
                if (mapa[0] == o.getCodigo()) {
                    PassageirosVooImpl ocupacao = new PassageirosVooImpl(o.getDataPartida(), (o.getCodigoAeronave().getQtdeAssentos() + o.getCodigoAeronave().getQtdeAssEspeciais()), mapa[1], o.getCodigo().toString());
                    lstOcupacao.add(ocupacao);
                }
            }
        }

        tRelatorio.setItems(lstOcupacao);
    }
}
