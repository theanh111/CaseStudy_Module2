package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Deck;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class MainController implements Initializable {
    private final NumberFormat longFormat = new DecimalFormat("#,###");

    private ObservableList<Deck> deckList;

    private ObservableList<Float> sizeChoiceBoxList = FXCollections.observableArrayList();

    @FXML
    private ImageView imagePreView;

    @FXML
    private TableView<Deck> table;

    @FXML
    private TableColumn<Deck, String> imageColumn;

    @FXML
    private TableColumn<Deck, Integer> idColumn;

    @FXML
    private TableColumn<Deck, Double> sizeColumn;

    @FXML
    private TableColumn<Deck, String> nameColumn;

    @FXML
    private TableColumn<Deck, String> brandColumn;

    @FXML
    private TableColumn<Deck, String> colorColumn;

    @FXML
    private TableColumn<Deck, Long> priceColumn;

    @FXML
    private TextField imageText;

    @FXML
    private TextField idText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField brandText;

    @FXML
    private TextField colorText;

    @FXML
    private TextField priceText;

    @FXML
    private TextField searchText;

    @FXML
    private ChoiceBox sizeBox;

    @FXML
    private Text searchWarning;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSizeChoiceBox();
        deckList = FXCollections.observableArrayList(readFile());
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(tc -> new TableCell<Deck, Long>() {
            @Override
            protected void updateItem(Long price, boolean empty) {
                super.updateItem(price, empty);
                if (price == null || empty) {
                    setText(null);
                } else {
                    setText(longFormat.format(price));
                }
            }
        });
        table.setItems(deckList);
        search();
    }

    public void loadSizeChoiceBox() {
        sizeChoiceBoxList.removeAll();
        float small = 7.5f;
        float smaller = 7.75f;
        float medium = 8.0f;
        float large = 8.25f;
        float larger = 8.5f;
        float extraLarge = 8.75f;
        sizeChoiceBoxList.addAll(small, smaller, medium, large, larger, extraLarge);
        sizeBox.getItems().addAll(sizeChoiceBoxList);
    }

    public void add() {
        try {
            Deck newDeck = new Deck();
            newDeck.setImage(imageText.getText());
            newDeck.setId(Integer.parseInt(idText.getText()));
            newDeck.setSize((Float) sizeBox.getValue());
            newDeck.setName(nameText.getText());
            newDeck.setBrand(brandText.getText());
            newDeck.setColor(colorText.getText());
            newDeck.setPrice(Long.parseLong(priceText.getText()));
            boolean checkId = true;
            for (Deck deck : deckList) {
                if (Integer.parseInt(idText.getText()) == deck.getId()) {
                    checkId = false;
                }
            }

            if (checkId && Integer.parseInt(idText.getText()) > 0 &&
                    Long.parseLong(priceText.getText()) > 0 &&
                    sizeBox.getValue() != null &&
                    !nameText.getText().equals("") &&
                    !brandText.getText().equals("") &&
                    !colorText.getText().equals("") &&
                    !imageText.getText().equals("")) {
                deckList.add(newDeck);
                writeFile();
                reset();
            } else if (idText.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Wrong Input!");
                alert.setContentText("ID is Empty!");
                alert.showAndWait();
            } else if (nameText.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Wrong Input!");
                alert.setContentText("Name is Empty!");
                alert.showAndWait();
            } else if (brandText.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Wrong Input!");
                alert.setContentText("Brand is Empty!");
                alert.showAndWait();
            } else if (colorText.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Wrong Input!");
                alert.setContentText("Color is Empty!");
                alert.showAndWait();
            } else if (priceText.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Wrong Input!");
                alert.setContentText("Price is Empty!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Wrong Input!");
                alert.setContentText("Invalid input! Check information");
                alert.showAndWait();
            }

        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong Input!");
            alert.setContentText("Please Input Full Information!");
            alert.showAndWait();
        }
    }

    public void delete() {
        Deck deckSelected = table.getSelectionModel().getSelectedItem();
        if (deckSelected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING!");
            alert.setContentText("Are you sure to delete?");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                deckList.remove(deckSelected);
            }
            reset();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Can't Delete!");
            alert.setContentText("Please Choose One To Delete!");
            alert.showAndWait();
        }
    }

    public void select(MouseEvent click) {
        if (click.getClickCount() == 2) {
            Deck deckSelected = table.getSelectionModel().getSelectedItem();
            imagePreView.setImage(new Image(deckSelected.getImage()));
            imageText.setText(deckSelected.getImage());
            idText.setText(String.valueOf(deckSelected.getId()));
            nameText.setText(deckSelected.getName());
            brandText.setText(deckSelected.getBrand());
            sizeBox.setValue(deckSelected.getSize());
            colorText.setText(deckSelected.getColor());
            priceText.setText(String.valueOf(deckSelected.getPrice()));
        }
    }

    public void update() {
        Deck deckSelected = table.getSelectionModel().getSelectedItem();
        if (deckSelected != null) {
            for (int i = 0; i < deckList.size(); i++) {
                if (Integer.parseInt(idText.getText()) == deckList.get(i).getId()) {
                    deckList.get(i).setImage(imageText.getText());
                    deckList.get(i).setId(Integer.parseInt(idText.getText()));
                    deckList.get(i).setName(nameText.getText());
                    deckList.get(i).setBrand(brandText.getText());
                    deckList.get(i).setSize((Float) sizeBox.getValue());
                    deckList.get(i).setColor(colorText.getText());
                    deckList.get(i).setPrice(Long.parseLong(priceText.getText()));
                    deckList.set(i, deckList.get(i));
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("File was updated!");
            alert.setContentText("Update Successfully!");
            alert.showAndWait();
            reset();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Can't Update!");
            alert.setContentText("Please Choose One To Update!");
            alert.showAndWait();
        }
    }

    public void search() {
        FilteredList<Deck> searchList = new FilteredList<>(deckList, b -> true);
        searchText.textProperty().addListener(((observable, oldValue, newValue) -> {
            searchList.setPredicate(deck -> {
                if (newValue == null || newValue.isEmpty()) {
                    searchWarning.setText("");
                    return true;
                } else {
                    searchWarning.setText("Searching...");
                }
                String lowercaseValue = newValue.toLowerCase();
                if (String.valueOf(deck.getId()).contains(lowercaseValue)) return true;
                else if (deck.getBrand().toLowerCase().contains(lowercaseValue)) return true;
                else if (deck.getName().toLowerCase().contains(lowercaseValue)) return true;
                else if (String.valueOf(deck.getSize()).contains(lowercaseValue)) return true;
                else if (String.valueOf(deck.getPrice()).contains(lowercaseValue)) return true;
                else if (deck.getColor().contains(lowercaseValue)) return true;
                else return false;
            });
        }));
        table.setItems(searchList);
    }

    public void reset() {
        imagePreView.setImage(null);
        imageText.clear();
        idText.clear();
        sizeBox.setValue(null);
        nameText.clear();
        brandText.clear();
        colorText.clear();
        priceText.clear();
    }

    public void imageButton() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        File fileSelected = fileChooser.showOpenDialog(null);
        if (fileSelected != null) {
            String imageFile = fileSelected.toURI().toURL().toString();
            Image image = new Image(imageFile);
            imagePreView.setImage(image);
            imageText.setText(fileSelected.toURI().toURL().toString());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong File!");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a file!");
            alert.showAndWait();
        }
    }

    public void logout(ActionEvent event) throws IOException {
       if (event != null) {
           writeFile();
           Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
           alert.setTitle("WARNING!");
           alert.setContentText("Are you sure to log out and Save file?");
           Optional<ButtonType> action = alert.showAndWait();
           if (action.get() == ButtonType.OK) {
               Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
               Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
               primaryStage.setTitle("Skateshop Management Application");
               primaryStage.setScene(new Scene(root, 500, 500));
               primaryStage.centerOnScreen();
               primaryStage.show();
           }
       }
    }

    public void writeFile() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("D:\\CodeGym\\Module_02\\CaseStudy_Module2\\src\\file\\SkateshopManagement.dat"));
            for (Deck deck : deckList) {
                outputStream.writeObject(deck);
            }
            outputStream.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NotSerializableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Deck> readFile() {
        List<Deck> newDeckList = new ArrayList<>();
        try {
            ObjectInputStream readFile = new ObjectInputStream(new FileInputStream("D:\\CodeGym\\Module_02\\CaseStudy_Module2\\src\\file\\SkateshopManagement.dat"));
            while (true) {
                newDeckList.add((Deck) readFile.readObject());
            }
        } catch (EOFException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newDeckList;
    }
}

