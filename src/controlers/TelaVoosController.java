/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlers;

import dao.AeronaveDAO;
import dao.DestinoDAO;
import dao.VooDAO;
import entidades.Aeronave;
import entidades.Destino;
import entidades.Voo;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
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
import jpaControles.exceptions.IllegalOrphanException;
import operadoresNumericos.OperadorNumerico;

public class TelaVoosController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Text txtInf;
    @FXML
    private Text txtAviso;
    @FXML
    private TableColumn<Integer, Voo> rCodAviao;
    @FXML
    private Label txtEsquerdo;
    @FXML
    private AnchorPane aPrinc;
    @FXML
    private TextField txtCodVoo;
    @FXML
    private TextField txtCodDestino;
    @FXML
    private AnchorPane pAtributosVoo;
    @FXML
    private TableColumn<Integer, Voo> rCodVoo;
    @FXML
    private TableColumn<String, Destino> rCodigoDestino;
    @FXML
    private TableColumn<String, Voo> rDataPartida;
    @FXML
    private TableColumn<Double, Voo> rValor;
    @FXML
    private AnchorPane pAtributosDestino;
    @FXML
    private TableView<Destino> tDestinos;
    @FXML
    private TableColumn<String, Destino> rCodDestino;
    @FXML
    private TableColumn<String, Destino> rNomeDestino;
    @FXML
    private TableColumn<Double, Destino> rTaxaDestino;
    @FXML
    private TableView<Voo> tVoos;
    private VooDAO bancoVoo;
    private DestinoDAO bancoDestino;
    private AeronaveDAO bancoAeronave;
    private ObservableList<Voo> lstVoos;
    private ObservableList<Destino> lstDestinos;
    private ObservableList<Aeronave> lstAvioes;
    @FXML
    private Button bRemVoo;
    @FXML
    private Button bEdiVoo;
    @FXML
    private Button bInsVoo;
    @FXML
    private TextField txtValor;
    @FXML
    private DatePicker datPartida;
    @FXML
    private ComboBox<Aeronave> cbAviao;
    @FXML
    private ComboBox<Destino> cbDestino;
    @FXML
    private TextField txtTaxa;
    @FXML
    private TextField txtAeroporto;
    @FXML
    private Button bRemDest;
    @FXML
    private Button bEdiDest;
    @FXML
    private Button bInsDest;
    @FXML
    private Text txtInfoVoo;
    @FXML
    private Text txtInfoDestino;
    @FXML
    private Text txtInfDes;
    @FXML
    private TextField txtCodAeroporto;
    private OperadorNumerico campoNumerico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoNumerico = new OperadorNumerico();
        txtTaxa.setTextFormatter(new TextFormatter<>(campoNumerico.fazerCampoReal()));
        txtValor.setTextFormatter(new TextFormatter<>(campoNumerico.fazerCampoReal()));
        txtCodVoo.setTextFormatter(new TextFormatter<>(campoNumerico.fazerCampoNumerico()));
        bancoVoo = new VooDAO();
        bancoDestino = new DestinoDAO();
        bancoAeronave = new AeronaveDAO();
        rCodAviao.setCellValueFactory(new PropertyValueFactory<>("codigoAeronave"));
        rCodDestino.setCellValueFactory(new PropertyValueFactory<>("codAeroporto"));
        rCodVoo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        rCodigoDestino.setCellValueFactory(new PropertyValueFactory<>("codAeroportoDestino"));
        rDataPartida.setCellValueFactory(new PropertyValueFactory<>("dataPartida"));
        rNomeDestino.setCellValueFactory(new PropertyValueFactory<>("nomeAeroporto"));
        rTaxaDestino.setCellValueFactory(new PropertyValueFactory<>("taxaEmbarque"));
        rValor.setCellValueFactory(new PropertyValueFactory<>("valorPassagem"));
        buscarDestinos();
        buscarVoos();
    }

    private Boolean consultar(Boolean fonteVoo) {
        if ((fonteVoo) && ("".equals(txtCodVoo.getText()))) {
            JOptionPane.showMessageDialog(null,
                    "Informe o código do voo", "Aviso:",
                    JOptionPane.WARNING_MESSAGE);
            txtCodVoo.requestFocus();
        } else if ((!fonteVoo) && ("".equals(txtCodDestino.getText()))) {
            JOptionPane.showMessageDialog(null,
                    "Informe o código do destino", "Aviso:",
                    JOptionPane.WARNING_MESSAGE);
            txtCodDestino.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    //pergunta ao usuário se deseja excluir o Voo ou Destino selecionado//
    //dependendo do botão apertado. Impede a exclusão caso existam dependencias//
    //para manter a integridade do banco//
    @FXML
    private void remover(ActionEvent event) {
        String[] escolhas = {"Sim", "Não"};
        if (event.getSource() == bRemVoo) {
            try {
                int result = JOptionPane.showOptionDialog(
                        null,
                        "Tem certeza que deseja excluir este voo?", "Aviso:",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        escolhas,
                        "Não"
                );
                if (result == JOptionPane.YES_OPTION) {
                    bancoVoo.excluir(Integer.parseInt(txtCodVoo.getText()));
                    buscarVoos();
                    JOptionPane.showMessageDialog(null,
                            "Voo " + txtCodVoo.getText() + " excluido com sucesso.", "",
                            JOptionPane.PLAIN_MESSAGE);
                    txtEsquerdo.setText("Voo " + txtCodVoo.getText() + " excluido com sucesso.");
                    pAtributosVoo.setVisible(false);
                    txtInfoVoo.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else {
            try {
                int result = JOptionPane.showOptionDialog(
                        null,
                        "Tem certeza que deseja excluir este destino?", "Aviso:",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        escolhas,
                        "Não"
                );
                if (result == JOptionPane.YES_OPTION) {
                    bancoDestino.excluir(txtCodDestino.getText());
                    buscarDestinos();
                    buscarVoos();
                    JOptionPane.showMessageDialog(null,
                            "Destino " + txtCodVoo.getText() + " excluido com sucesso.", "",
                            JOptionPane.PLAIN_MESSAGE);
                    txtEsquerdo.setText("Destino " + txtCodVoo.getText() + " excluido com sucesso.");
                    pAtributosDestino.setVisible(false);
                    txtInfoDestino.setText("");
                }
            } catch (IllegalOrphanException ex) {
                JOptionPane.showMessageDialog(null,
                        "Destino " + txtCodDestino.getText() + " não pode ser removido pois há voos destinados a ele. Por favor remova os voos dependentes para remove-lo.", "",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                System.out.println(ex);
            }
        }
    }
    
    //funções abaixo seguem o padrão estabelecido na classe TelaAeronaves: edição, criação, busca, verificação de integridade//
    //e seleção de objetos relevantes para manutenção do banco//

    @FXML
    private void editar(ActionEvent event) {
        if (event.getSource() == bEdiVoo) {
            try {
                if (verificarCamposVoo()) {
                    Voo novo = bancoVoo.consultar(Integer.parseInt(txtCodVoo.getText()));
                    Date data = Date.from(datPartida.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    novo.setCodAeroportoDestino(cbDestino.getValue());
                    novo.setCodigoAeronave(cbAviao.getValue());
                    novo.setDataPartida(data);
                    novo.setValorPassagem(Double.parseDouble(txtValor.getText()));
                    bancoVoo.editar(novo);
                    txtEsquerdo.setText("Voo " + txtCodVoo.getText() + " editado com sucesso.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Campo(s) com valor(es) muito grande. Por favor insira um valor menor.", "Erro:",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                buscarVoos();
            }
        } else {
            try {
                if (verificarCamposDestino()) {
                    Destino novo = bancoDestino.consultar(txtCodDestino.getText());
                    novo.setCodAeroporto(txtCodAeroporto.getText());
                    novo.setNomeAeroporto(txtAeroporto.getText());
                    novo.setTaxaEmbarque(Double.parseDouble(txtTaxa.getText()));
                    bancoDestino.editar(novo);
                    txtEsquerdo.setText("Destino " + txtCodVoo.getText() + " editado com sucesso.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Campo(s) com valor(es) muito grande. Por favor insira um valor menor.", "Erro:",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                buscarDestinos();
                buscarVoos();
            }
        }
    }

    @FXML
    private void inserir(ActionEvent event) {
        if (event.getSource() == bInsVoo) {
            try {
                if (verificarCamposVoo()) {
                    Voo novo = new Voo();
                    Date data = Date.from(datPartida.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    novo.setCodAeroportoDestino(cbDestino.getValue());
                    novo.setCodigoAeronave(cbAviao.getValue());
                    novo.setDataPartida(data);
                    novo.setValorPassagem(Double.parseDouble(txtValor.getText()));
                    bancoVoo.inserir(novo);
                    txtEsquerdo.setText("Voo inserido com sucesso.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Campo(s) com valor(es) muito grande. Por favor insira um valor menor.", "Erro:",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                buscarVoos();
            }
        } else {
            try {
                if (verificarCamposDestino()) {
                    if (bancoDestino.consultar(txtCodAeroporto.getText()) == null) {
                        Destino novo = new Destino();
                        novo.setCodAeroporto(txtCodAeroporto.getText());
                        novo.setNomeAeroporto(txtAeroporto.getText());
                        novo.setTaxaEmbarque(Double.parseDouble(txtTaxa.getText()));
                        bancoDestino.inserir(novo);
                        txtEsquerdo.setText("Destino inserido com sucesso.");
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Destino com código " + txtCodAeroporto.getText() + " já existe. Por favor insira um valor novo.", "Aviso:",
                                JOptionPane.WARNING_MESSAGE);

                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Campo(s) com valor(es) muito grande. Por favor insira um valor menor.", "Erro:",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                buscarDestinos();
                buscarVoos();
            }
        }
    }

    private Boolean verificarCamposVoo() {
        if ((cbAviao.getValue() != null) && (cbDestino.getValue() != null) && !"".equals(datPartida.getValue().toString())) {
            if (!"0".equals(txtValor.getText())) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Passagems não podem ser de graça.", "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Todos os campos devem ser preenchidos.", "Aviso:",
                    JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    private Boolean verificarCamposDestino() {
        if (!"".equals(txtAeroporto.getText()) && !"".equals(txtCodAeroporto.getText()) && !"".equals(txtTaxa.getText())) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "Todos os campos devem ser preenchidos.", "Aviso:",
                    JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    @FXML
    private void procurarVoo(ActionEvent event) {
        if (consultar(true)) {
            try {
                Voo alvo = bancoVoo.consultar(Integer.parseInt(txtCodVoo.getText()));
                if (alvo != null) {
                    txtInfoVoo.setText("Voo com código " + txtCodVoo.getText() + " selecionado.");
                    txtInf.setText("Dados do voo:");
                    cbAviao.setValue(alvo.getCodigoAeronave());
                    cbDestino.setValue(alvo.getCodAeroportoDestino());
                    datPartida.setValue(LocalDate.parse(alvo.getDataPartida().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    txtValor.setText(alvo.getValorPassagem().toString());
                    abrirPainelVoo(false, true, true);
                } else {
                    txtInfoVoo.setText("Voo com código " + txtCodVoo.getText() + " não encontrado.");
                    pAtributosVoo.setVisible(false);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Código com valor muito grande. Por favor insira um valor menor.", "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void abrirPainelVoo(Boolean inserir, Boolean remover, Boolean editar) {
        pAtributosVoo.setVisible(true);
        bInsVoo.setVisible(inserir);
        bRemVoo.setVisible(remover);
        bEdiVoo.setVisible(editar);
    }

    @FXML
    private void novoVoo(ActionEvent event) {
        try {
            txtInfoVoo.setText("Inserindo novo voo.");
            txtInf.setText("Informe os dados do voo:");
            cbAviao.setValue(null);
            cbDestino.setValue(null);
            datPartida.setValue(null);
            txtValor.clear();
            abrirPainelVoo(true, false, false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(), "Erro:",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void procurarDestino(ActionEvent event) {
        if (consultar(false)) {
            try {
                Destino alvo = bancoDestino.consultar(txtCodDestino.getText());
                if (alvo != null) {
                    txtInfoDestino.setText("Destino com código " + txtCodDestino.getText() + " selecionado.");
                    txtInfDes.setText("Dados do destino:");
                    txtCodAeroporto.setText(alvo.getCodAeroporto());
                    txtAeroporto.setText(alvo.getNomeAeroporto());
                    txtTaxa.setText(alvo.getTaxaEmbarque().toString());
                    txtCodAeroporto.setDisable(true);
                    abrirPainelDestino(false, true, true);
                } else {
                    txtInfoDestino.setText("Destino com código " + txtCodDestino.getText() + " não encontrado.");
                    pAtributosDestino.setVisible(false);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Código com valor muito grande. Por favor insira um valor menor." + ex.getMessage(), "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @FXML
    private void novoDestino(ActionEvent event) {
        try {
            txtInfoDestino.setText("Inserindo novo destino.");
            txtInfDes.setText("Informe os dados do destino:");
            txtCodAeroporto.clear();
            txtAeroporto.clear();
            txtTaxa.clear();
            txtCodAeroporto.setDisable(false);
            abrirPainelDestino(true, false, false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(), "Erro:",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirPainelDestino(Boolean inserir, Boolean remover, Boolean editar) {
        pAtributosDestino.setVisible(true);
        bInsDest.setVisible(inserir);
        bRemDest.setVisible(remover);
        bEdiDest.setVisible(editar);
    }

    private void buscarVoos() {
        try {
            lstVoos = FXCollections.observableArrayList(bancoVoo.consultar());
            lstAvioes = FXCollections.observableArrayList(bancoAeronave.consultar());
            tVoos.setItems(lstVoos);
            tVoos.refresh();
            cbAviao.setItems(lstAvioes);
            cbDestino.setItems(lstDestinos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void buscarDestinos() {
        try {
            lstDestinos = FXCollections.observableArrayList(bancoDestino.consultar());
            tDestinos.setItems(lstDestinos);
            tDestinos.refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @FXML
    private void selecionarVoo(MouseEvent event) {
        txtCodVoo.setText(tVoos.getSelectionModel().getSelectedItem().getCodigo().toString());
        procurarVoo(null);
    }

    @FXML
    private void selecionarDestino(MouseEvent event) {
        txtCodDestino.setText(tDestinos.getSelectionModel().getSelectedItem().getCodAeroporto().toString());
        procurarDestino(null);
    }

}
