/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operadoresNumericos;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;

public final class OperadorNumerico {
    
    //classe para instanciação de operadores numéricos relevantes//
    //presentes em toda a aplicação//

    private UnaryOperator<TextFormatter.Change> operador;

    public UnaryOperator<TextFormatter.Change> fazerCampoNumerico() {
        operador = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        return operador;
    }

    public UnaryOperator<TextFormatter.Change> fazerCampoReal() {
        Pattern decimalPattern = Pattern.compile("-?\\d*(\\.\\d{0,2})?");
        operador = change -> {
            if ((decimalPattern.matcher(change.getControlNewText()).matches()) && (change.getControlNewText().length() < 8)) {
                return change ;
            } else {
                return null ;
            }
        };
        return operador;
    }

    public UnaryOperator<TextFormatter.Change> fazerCampoNumericoCpf() {
        operador = change -> {
            String text = change.getText();
            int newLength = change.getControlNewText().length();
            if (newLength > 11) {
                change.setText("");
                return change;
            }
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        return operador;
    }
}
