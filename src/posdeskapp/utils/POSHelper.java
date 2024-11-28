/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.rmi.CORBA.Util;
import posdeskapp.controllers.MainController;
import posdeskapp.models.LineItem;
import posdeskapp.models.TaxBreakDown;

/**
 *
 * @author biphiri
 */
public class POSHelper {

    public static String encryptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }

    public static String decryptPassword(String cypherText) {
        return new String(Base64.getDecoder().decode(cypherText), StandardCharsets.UTF_8);
    }

    public void showUserStage(Node node, String fxmlUrl) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlUrl));
            Stage stage = (Stage) node.getScene().getWindow();
            stage.hide();
            Stage stage1 = new Stage(StageStyle.DECORATED);
            stage1.setScene(new Scene(root, 1812, 987));
            stage1.centerOnScreen();
            stage1.setTitle("POS");
            stage1.getIcons().add(new Image("/posdeskapp/images/point-of-sale-icon.png"));
            stage1.show();
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static double parseDecimal(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static String formatValue(double value) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(value);
    }

    public static double parseFormattedValue(String formattedValue) {
        try {
            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
            Number number = formatter.parse(formattedValue);
            return number.doubleValue();
        } catch (ParseException e) {
            System.err.println("Invalid formatted value: " + formattedValue);
            return 0.0;
        }
    }

    public static int generatePauseId() {
        List<Integer> usedIds = DbHelper.getUsedPauseIds();
        Random random = new Random();

        int newId;
        do {
            newId = random.nextInt(90) + 10;
        } while (usedIds.contains(newId));

        return newId;
    }

    public static HBox setActionButtons(String productCode, ObservableList<LineItem> data) {
        HBox hBox = new HBox(5);
        // Create a trash icon
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT);
        icon.setSize("20");
        icon.setFill(Color.RED);
        icon.setId(productCode);

        HBox.setMargin(icon, new Insets(0, 0, 0, 0));
        HBox.setHgrow(icon, Priority.ALWAYS);

        hBox.setAlignment(Pos.CENTER);

        icon.setOnMouseClicked(event -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove line item");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to remove this line item?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                for (LineItem lineItem : data) {
                    if (lineItem.getProductCode().equals(productCode)) {
                        data.remove(lineItem);
                        updateInvoiceSummary(data, MainController.invoiceTotalText, MainController.taxableAmountText, MainController.totalVAText, MainController.totalItemsText);
                        break;
                    }
                }
            }
        });

        // Add the icon to the HBox
        hBox.getChildren().add(icon);

        return hBox;
    }

    public static HBox suspendedSalesActions(int paudedId) {
        HBox hBox = new HBox(5);
        // Create a trash icon
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT);
        icon.setSize("20");
        icon.setFill(Color.RED);
        icon.setId(String.valueOf(paudedId));

        FontAwesomeIconView icon1 = new FontAwesomeIconView(FontAwesomeIcon.RECYCLE);
        icon1.setSize("17");
        icon1.setFill(Color.rgb(0, 128, 0, 1));
        icon1.setId(String.valueOf(paudedId));

        HBox.setMargin(icon, new Insets(0, 0, 0, 0));
        HBox.setHgrow(icon, Priority.ALWAYS);

        HBox.setMargin(icon1, new Insets(0, 0, 0, 0));
        HBox.setHgrow(icon1, Priority.ALWAYS);

        hBox.setAlignment(Pos.CENTER);

        icon.setOnMouseClicked(event -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Suspended Transaction");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this transaction?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
              //
            }
        });

        icon1.setOnMouseClicked(event -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Recall Transaction");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to recall this transaction?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
              //
            }
        });

        // Add the icons to the HBox
        hBox.getChildren().addAll(icon1, icon);

        return hBox;
    }

    public static List<TaxBreakDown> generateTaxSummary(List<LineItem> lineItems) {
        Map<String, TaxBreakDown> taxBreakdowns = new HashMap<>();

        lineItems.forEach((item) -> {
            double taxableAmount = item.getTotal() - item.getTotalVAT();
            double taxAmount = item.getTotalVAT();

            if (taxBreakdowns.containsKey(item.getTaxRateId())) {
                TaxBreakDown existing = taxBreakdowns.get(item.getTaxRateId());
                double updatedTaxableAmount = existing.getTaxableAmount() + taxableAmount;
                double updatedTaxAmount = existing.getTaxAmount() + taxAmount;

                taxBreakdowns.put(item.getTaxRateId(),
                        new TaxBreakDown(item.getTaxRateId(), updatedTaxableAmount, updatedTaxAmount));
            } else {
                taxBreakdowns.put(item.getTaxRateId(),
                        new TaxBreakDown(item.getTaxRateId(), taxableAmount, taxAmount));
            }
        });

        return new ArrayList<>(taxBreakdowns.values());
    }

    public static String getDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return currentDate.format(formatter);
    }

    public static void updateInvoiceSummary(ObservableList<LineItem> data, Text invoiceTotalText, Text taxableAmountText, Text totalVATText, Text itemsCount) {

        double invoiceTotal = data.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
        invoiceTotalText.setText(formatValue(invoiceTotal));

        double taxableAmount = data.stream()
                .mapToDouble(item -> item.getTotal() - item.getTotalVAT())
                .sum();
        taxableAmountText.setText(formatValue(taxableAmount));

        double totalVAT = data.stream()
                .mapToDouble(LineItem::getTotalVAT)
                .sum();
        totalVATText.setText(formatValue(totalVAT));

        double totalQuantity = data.stream()
                .mapToDouble(LineItem::getQuantity)
                .sum();
        itemsCount.setText(String.valueOf(totalQuantity));
    }

    public static double extractVATAmount(double sellingPrice, double quantity, double taxRate) {
        double totalPrice = sellingPrice * quantity;
        double vatAmount = (totalPrice * taxRate) / (100 + taxRate);
        return Math.round(vatAmount * 100.0) / 100.0; // Round to 2 decimal places
    }
}
