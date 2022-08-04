/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlers;

import dao.AeronaveDAO;
import dao.PassageiroDAO;
import dao.PassageirosVooDAO;
import dao.VooDAO;
import entidades.Aeronave;
import entidades.Destino;
import entidades.Passageiro;
import entidades.PassageirosVoo;
import entidades.PassageirosVooImpl;
import entidades.Voo;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import operadoresNumericos.OperadorNumerico;

public class TelaOcupacaoController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane aPrinc;
    @FXML
    private Text txtAviso;
    @FXML
    private TableView<Aeronave> tAvioes;
    @FXML
    private TableColumn<Aeronave, Integer> rCodAviao;
    @FXML
    private TableColumn<Aeronave, String> rModeloAviao;
    @FXML
    private TableColumn<Aeronave, Integer> rAssentosAviao;
    @FXML
    private Label txtEsquerdo;
    @FXML
    private AnchorPane pAtributosOcupacao;
    @FXML
    private Button bRemOcupacao;
    @FXML
    private Button bEdiOcupacao;
    @FXML
    private DatePicker datVoo;
    @FXML
    private ComboBox<PassageirosVoo> cbVoo;
    @FXML
    private TableView<PassageirosVooImpl> tOcupacao;
    @FXML
    private TableColumn<Integer, PassageirosVooImpl> rCodVooOcupacao;
    @FXML
    private TableColumn<String, PassageirosVooImpl> rDataOcupacao;
    @FXML
    private TableColumn<Integer, PassageirosVooImpl> rAssentoAviaoOcupacao;
    @FXML
    private TableColumn<Integer, PassageirosVooImpl> rAviaoOcupacao;
    @FXML
    private TableColumn<String, PassageirosVooImpl> rCpfPassageiroOcupacao;
    @FXML
    private TableColumn<String, Passageiro> rCpf;
    @FXML
    private TableColumn<String, Passageiro> rNome;
    @FXML
    private TableColumn<String, Passageiro> rNasc;
    @FXML
    private TableView<Voo> tVoos;
    @FXML
    private TableColumn<Integer, Aeronave> rCodVoo;
    @FXML
    private TableColumn<String, Destino> rCodigoDestino;
    @FXML
    private TableColumn<String, Voo> rDataPartida;
    @FXML
    private TableColumn<Double, Voo> rValor;
    @FXML
    private TableColumn<Integer, Voo> rCodAviaoVoo;
    private ObservableList<Aeronave> lstAeronaves;
    private AeronaveDAO bancoAeronave;
    private PassageirosVooDAO bancoPassageirosVoo;
    @FXML
    private TableColumn<Integer, Aeronave> rAssentosEsAviao;
    private ObservableList<Voo> lstVoos;
    private ObservableList<Passageiro> lstPassageiros;
    private ObservableList<PassageirosVoo> lstpassageirosVoo;
    private ObservableList<PassageirosVooImpl> lstOcupacoes;
    private VooDAO bancoVoo;
    private PassageiroDAO bancoPassageiro;
    @FXML
    private TableView<Passageiro> tPassageiros;
    @FXML
    private TextField txtAssento;
    @FXML
    private Text txtAssentosLm;

    private OperadorNumerico campoNumerico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoNumerico = new OperadorNumerico();
        bancoAeronave = new AeronaveDAO();
        bancoVoo = new VooDAO();
        bancoPassageiro = new PassageiroDAO();
        bancoPassageirosVoo = new PassageirosVooDAO();
        rCodAviao.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        rModeloAviao.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        rAssentosAviao.setCellValueFactory(new PropertyValueFactory<>("qtdeAssentos"));
        rAssentosEsAviao.setCellValueFactory(new PropertyValueFactory<>("qtdeAssEspeciais"));
        rDataPartida.setCellValueFactory(new PropertyValueFactory<>("dataPartida"));
        rValor.setCellValueFactory(new PropertyValueFactory<>("valorPassagem"));
        rCodAviaoVoo.setCellValueFactory(new PropertyValueFactory<>("codigoAeronave"));
        rCodigoDestino.setCellValueFactory(new PropertyValueFactory<>("codAeroportoDestino"));
        rCodVoo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        rCpf.setCellValueFactory(new PropertyValueFactory<>("numDocumento"));
        rNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        rNasc.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        rCodVooOcupacao.setCellValueFactory(new PropertyValueFactory<>("codigoVoo"));
        rDataOcupacao.setCellValueFactory(new PropertyValueFactory<>("dataPartida"));
        rAssentoAviaoOcupacao.setCellValueFactory(new PropertyValueFactory<>("numAssento"));
        rAviaoOcupacao.setCellValueFactory(new PropertyValueFactory<>("codigoAeronave"));
        rCpfPassageiroOcupacao.setCellValueFactory(new PropertyValueFactory<>("passageiro"));
        txtAssento.setTextFormatter(new TextFormatter<>(campoNumerico.fazerCampoNumerico()));
        buscarAeronaves();
        buscarVoos();
        buscarPassageiros();
        buscarOcupacoes();
    }

    //pergunta ao usuário se deseja remover a ocupação selecionada e efetiva a ação se sim//
    @FXML
    private void remover(ActionEvent event) {
        String[] escolhas = {"Sim", "Não"};
        try {
            int result = JOptionPane.showOptionDialog(
                    null,
                    "Tem certeza que deseja excluir esta ocupação?", "Aviso:",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    escolhas,
                    "Nenhum"
            );
            if (result == JOptionPane.YES_OPTION) {
                bancoPassageirosVoo.excluir(cbVoo.getValue());
                buscarVoos();
                JOptionPane.showMessageDialog(null,
                        "Ocupação " + cbVoo.getValue() + " excluida com sucesso.", "",
                        JOptionPane.PLAIN_MESSAGE);
                txtEsquerdo.setText("Ocupação " + cbVoo.getValue() + " excluida com sucesso.");
                limparCampos();
                buscarOcupacoes();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void limparCampos() {
        cbVoo.setValue(null);
        datVoo.setValue(null);
        txtAssento.setText("");
        txtAssentosLm.setText("");
        bEdiOcupacao.setDisable(true);
        bRemOcupacao.setDisable(true);
    }

    //edita o banco de forma a refletir os dados desejados pelo usuário//
    @FXML
    private void editar(ActionEvent event) {
        try {
            if (!"".equals(txtAssento.getText()) && ((cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssentos() + cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssEspeciais()) > (Integer.parseInt(txtAssento.getText())))) {

                String[] escolhas = {"Sim", "Não"};
                int result = JOptionPane.showOptionDialog(
                        null,
                        "Editar a data de uma ocupação alterara todas as outras presentes no mesmo voo. Tme certeza que deseja continuar?", "Aviso:",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        escolhas,
                        "Nenhum"
                );
                if (result == JOptionPane.YES_OPTION) {
                    System.out.println(Integer.parseInt(txtAssento.getText()));
                    Voo voo = cbVoo.getValue().getVoo();
                    PassageirosVoo ocupacao = cbVoo.getValue();
                    voo.setDataPartida(Date.from(datVoo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    bancoVoo.editar(voo);
                    bancoPassageirosVoo.excluir(ocupacao);
                    ocupacao.getPassageirosVooPK().setNumAssento(Integer.parseInt(txtAssento.getText()));
                    bancoPassageirosVoo.inserir(ocupacao);
                    buscarVoos();
                    buscarOcupacoes();
                    txtEsquerdo.setText("Ocupação " + cbVoo.getValue() + " editada com sucesso.");
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Por favor informe um assento entre 0 e " + (cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssentos() + cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssEspeciais()), "",
                        JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Por favor informe um assento entre 0 e " + (cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssentos() + cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssEspeciais()), "",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void buscarAeronaves() {
        try {
            lstAeronaves = FXCollections.observableArrayList(bancoAeronave.consultar());
            tAvioes.setItems(lstAeronaves);
            tAvioes.refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void buscarVoos() {
        try {
            lstVoos = FXCollections.observableArrayList(bancoVoo.consultar());
            tVoos.setItems(lstVoos);
            tVoos.refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void buscarPassageiros() {
        lstPassageiros = FXCollections.observableArrayList(bancoPassageiro.consultar());
        tPassageiros.setItems(lstPassageiros);
        tPassageiros.refresh();
    }

    private void buscarOcupacoes() {
        lstpassageirosVoo = FXCollections.observableArrayList(bancoPassageirosVoo.consultar());
        cbVoo.setItems(lstpassageirosVoo);
        List<PassageirosVooImpl> imp = new ArrayList();
        for (PassageirosVoo i : lstpassageirosVoo) {
            PassageirosVooImpl novo = new PassageirosVooImpl(i.getPassageirosVooPK(), i.getVoo().getDataPartida(), i.getPassageirosVooPK().getNumAssento(), i.getVoo().getCodigoAeronave().getCodigo(), i.getPassageiro().getNome() + " -CPF: " + i.getPassageiro().getNumDocumento());
            imp.add(novo);
        }
        lstOcupacoes = FXCollections.observableArrayList(imp);
        tOcupacao.setItems(lstOcupacoes);
        tOcupacao.refresh();
    }

    //seleciona a ocupação a partir do item selecionado na tabela//
    @FXML
    private void selecionarOcupacao(MouseEvent event) {
        try {
            cbVoo.setValue(bancoPassageirosVoo.consultar(tOcupacao.getSelectionModel().getSelectedItem().getCodigoVoo()));
            txtAssento.setText(String.valueOf(tOcupacao.getSelectionModel().getSelectedItem().getNumAssento()));
        } catch (Exception ex) {
            Logger.getLogger(TelaOcupacaoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //filtra as datas posiveis em formato dia/mes/ano e permite a alteração dos campos//
    @FXML
    private void filtrarData(ActionEvent event) {
        if (cbVoo.getValue() != null) {
            datVoo.setValue(LocalDate.parse(cbVoo.getValue().getVoo().getDataPartida(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            datVoo.setDisable(false);
            txtAssento.setDisable(false);
            bEdiOcupacao.setDisable(false);
            bRemOcupacao.setDisable(false);
            txtAssentosLm.setText("Assentos: 0 - " + cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssentos() + " | Assentos Especiais: " + (cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssentos() + 1) + " - " + (cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssEspeciais() + cbVoo.getValue().getVoo().getCodigoAeronave().getQtdeAssentos()));
        }
    }
}
