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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.rmi.CORBA.Util;
import posdeskapp.controllers.MainController;
import posdeskapp.models.InvoiceGenerationRequest;
import posdeskapp.models.InvoiceHeader;
import posdeskapp.models.InvoiceLineItem;
import posdeskapp.models.InvoiceSummary;
import posdeskapp.models.LineItem;
import posdeskapp.models.Product;
import posdeskapp.models.SalesInvoice;
import posdeskapp.models.TaxBreakDown;

/**
 *
 * @author biphiri
 */
public class POSHelper {

    private Product selectedProduct;
    private MainController mainController;

    public POSHelper(MainController mainController) {
        this.mainController = mainController;
    }

    public POSHelper() {
    }

    public static String formatDate(LocalDateTime date) {
        ZonedDateTime utcDateTime = date.atZone(ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDateTime = utcDateTime.format(formatter);
        return formattedDateTime;
    }

    public static String computeXSignature(String activationCode, String secretKey) {
        try {

            Mac hmacSha512 = Mac.getInstance("HmacSHA512");

            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmacSha512.init(keySpec);

            byte[] hash = hmacSha512.doFinal(activationCode.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error computing X-Signature", e);
        }
    }

    public static int convertSequentialToBase10(String sequentialString) {
        final String base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

        // Split and get the last part
        String[] parts = sequentialString.split("-");
        String base64Value = parts[parts.length - 1];

        // Handle special case for zero
        if ("A".equals(base64Value)) {
            return 0;
        }

        int result = 0;
        for (int i = 0; i < base64Value.length(); i++) {
            char c = base64Value.charAt(i);
            int digitValue = base64Chars.indexOf(c);

            if (digitValue == -1) {
                throw new IllegalArgumentException("Invalid Base64 character: " + c);
            }

            result = result * 64 + digitValue;
        }

        return result;
    }

    public static String base10ToBase64(long number) {
        if (number == 0) {
            return "A";
        }

        final String base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        StringBuilder result = new StringBuilder();

        while (number > 0) {
            int remainder = (int) (number % 64);
            result.append(base64Chars.charAt(remainder));
            number /= 64;
        }

        return result.reverse().toString();
    }

    public static String generateInvoiceNumber(InvoiceGenerationRequest invoiceGenerationRequest) {

        long julianDate = convertToJulianDate(invoiceGenerationRequest.getTransactionDate());

        String combinedString = generateCombinedString(invoiceGenerationRequest.getBusinessId(),
                invoiceGenerationRequest.getTerminalPosition(),
                julianDate,
                invoiceGenerationRequest.getTransactionCount());
        return combinedString;
    }

    public static String generateCombinedString(long taxpayerId, int position, long julianDate, long transactionCount) {
        String base64TaxpayerNumber = base10ToBase64(taxpayerId);
        String base64Position = base10ToBase64(position);
        String julianDateBase64 = base10ToBase64(julianDate);
        String serialNumberBase64 = base10ToBase64(transactionCount);

        return base64TaxpayerNumber + "-" + base64Position + "-" + julianDateBase64 + "-" + serialNumberBase64;
    }

    public static int convertToJulianDate(LocalDateTime dateTime) {
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        int day = dateTime.getDayOfMonth();

        if (month <= 2) {
            year -= 1;
            month += 12;
        }

        int A = year / 100;
        int B = 2 - A + (A / 4);

        int JD = (int) (Math.floor(365.25 * (year + 4716))
                + Math.floor(30.6001 * (month + 1))
                + day + B - 1524);

        return JD;
    }

    public static long convertInvoiceNumberToBase10(String invoiceNumber) {
        String[] parts = invoiceNumber.split("-");
        long result = 0;

        for (String part : parts) {
            try {
                byte[] bytes = java.util.Base64.getDecoder().decode(part);
                // Ensure the byte array is exactly 8 bytes for conversion
                byte[] paddedBytes = new byte[8];
                System.arraycopy(bytes, 0, paddedBytes, 0, Math.min(bytes.length, 8));
                long partValue = java.nio.ByteBuffer.wrap(paddedBytes).getLong();

                result = (result << 32) | (partValue & 0xFFFFFFFFL);
            } catch (IllegalArgumentException ex) {
                System.err.println("Error processing part '" + part + "': " + ex.getMessage());
            }
        }

        return result;
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

    public static SalesInvoice createInvoiceRequest(InvoiceHeader invoice, List<LineItem> lineItems, double total, double totalVAT) {
        SalesInvoice invoiceRequest = new SalesInvoice();

        InvoiceHeader invoiceHeader = new InvoiceHeader(invoice.getInvoiceNumber(), invoice.getInvoiceDateTime(), invoice.getSellerTIN(), invoice.getBuyerTIN(), invoice.getBuyerAuthorizationCode(), invoice.getSiteId(), invoice.getGlobalConfigVersion(), invoice.getTaxpayerConfigVersion(), invoice.getTerminalConfigVersion());
        invoiceRequest.InvoiceHeader = invoiceHeader;
        List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();

        lineItems.stream().map((invoiceLineItem) -> {
            double lineItemTotal = invoiceLineItem.getQuantity() * invoiceLineItem.getUnitPrice();
            // Create an InvoiceLineItem object
            InvoiceLineItem lineItem = new InvoiceLineItem(
                    0,
                    invoiceLineItem.getProductCode(),
                    invoiceLineItem.getDescription(),
                    invoiceLineItem.getUnitPrice(),
                    invoiceLineItem.getQuantity(),
                    invoiceLineItem.getDiscount(),
                    lineItemTotal,
                    invoiceLineItem.getTotalVAT(),
                    invoiceLineItem.getTaxRateId()
            );
            return lineItem;
        }).forEachOrdered((lineItem) -> {
            // Add to the list
            invoiceLineItems.add(lineItem);
        });

        invoiceRequest.InvoiceLineItems = invoiceLineItems;

        InvoiceSummary invoiceSummary = new InvoiceSummary(generateTaxSummary(lineItems), totalVAT, "", total);

        invoiceRequest.InvoiceSummary = invoiceSummary;

        return invoiceRequest;
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
