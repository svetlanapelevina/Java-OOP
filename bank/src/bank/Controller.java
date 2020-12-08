package bank;

import javafx.collections.FXCollections;
import javafx.scene.control.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class Controller {
    public TextField clientsSearchTextField;
    public ListView clientsListView;
    public Button addClientButton;

    public Label clientNameLabel;
    public Label clientTotalBalanceLabel;
    public ListView accountsListView;
    public Button addAccountButton;

    public Label accountCreatedDateLabel;
    public Label accountBalanceLabel;
    public Label accountNameLabel;
    public ListView accountOperationsListView;
    public Button closeAccountButton;
    public Button accountDepositButton;
    public Button accountWithdrawButton;
    public Button accountTransferButton;

    public Button showReportButton;

    private final Bank bankInstance = Bank.getInstance();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private Client selectedClient;
    private BankAccount selectedAccount;

    private ArrayList<Client> clientsList;

    public void initialize() {
        this.clientsListView.setCellFactory(param -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + ": passport: " + item.getPassportNumber() + " ( " + item.getTotalBalance() + " )");
                }
            }
        });

        this.accountsListView.setCellFactory(param -> new ListCell<BankAccount>() {
            @Override
            protected void updateItem(BankAccount item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " ( " + item.getBalance() + " )");
                }
            }
        });

        this.accountOperationsListView.setCellFactory(param -> new ListCell<BankOperation>() {
            @Override
            protected void updateItem(BankOperation item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    double amount = item.getAmount();
                    String prefix = amount > 0 ? "+" : " ";

                    setText(prefix + amount + " (" + dateFormat.format(item.getDate()) + ")");
                }
            }
        });

        this.clientsSearchTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue.equals("")) {
                this.clientsList = this.bankInstance.getAllClients();
            } else {
                this.clientsList = this.bankInstance.searchClientsByName(newValue);
            }

            this.updateClientsList();
        }));

        this.clientsList = this.bankInstance.getAllClients();
        updateClientsList();
    }

    private Boolean checkAccountIsNotNull() {
        if (this.selectedAccount == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You need to select account first!");
            alert.show();
        }
        return this.selectedAccount == null;
    }

    private Boolean checkClientIsNotNull() {
        if (this.selectedClient == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You need to select client first!");
            alert.show();
        }
        return this.selectedClient == null;
    }

    private void updateAccountsList() {
        if (this.selectedClient != null){
            this.accountsListView.setItems(FXCollections.observableArrayList(this.selectedClient.getAccounts()));
        }
    }

    private void updateClientsList() {
        this.clientsListView.setItems(FXCollections.observableArrayList(this.clientsList));
    }

    private void updateClientInfo() {
        if (this.selectedClient != null) {
            this.clientNameLabel.setText(this.selectedClient.getName());
            this.clientTotalBalanceLabel.setText(String.valueOf(this.selectedClient.getTotalBalance()));
            this.accountsListView.setItems(FXCollections.observableArrayList(this.selectedClient.getAccounts()));
        }
    }

    private void updateView() {
        this.updateClientInfo();
        this.updateAccountInfo();
        this.updateAccountsList();
        this.updateClientsList();
    }

    private void updateAccountInfo() {
        if (this.selectedAccount != null) {
            this.accountNameLabel.setText(this.selectedAccount.getName());
            this.accountCreatedDateLabel.setText(dateFormat.format(this.selectedAccount.getCreationDate()));
            this.accountBalanceLabel.setText(String.valueOf(this.selectedAccount.getBalance()));
            this.accountOperationsListView.setItems(FXCollections.observableArrayList(this.selectedAccount.getOperations()));
        }
    }

    public void clientsListViewClicked() {
        this.selectedClient = (Client) clientsListView.getSelectionModel().getSelectedItem();
        this.selectedAccount = null;
        this.updateClientInfo();
    }

    public void accountsListViewClicked() {
        this.selectedAccount = (BankAccount) accountsListView.getSelectionModel().getSelectedItem();
        this.updateAccountInfo();
    }

    public void addClientButtonClicked() {
        TextInputDialog nameInput = new TextInputDialog("");
        nameInput.setTitle("New Client");
        nameInput.setHeaderText(null);
        nameInput.setGraphic(null);
        nameInput.setContentText("Enter client name:");

        Optional<String> nameResult = nameInput.showAndWait();
        nameResult.ifPresent((name) -> {
            TextInputDialog passportInput = new TextInputDialog("");
            passportInput.setTitle("New Client");
            passportInput.setHeaderText(null);
            passportInput.setGraphic(null);
            passportInput.setContentText("Enter client passport:");

            Optional<String> passportResult = passportInput.showAndWait();
            passportResult.ifPresent((passport) -> {
                this.bankInstance.createClient(name, passport);
                this.clientsList = this.bankInstance.getAllClients();
                this.updateClientsList();
            });
        });
    }

    public void addAccountButtonClicked() {
        if (checkClientIsNotNull()) {
            return;
        }

        TextInputDialog depositDialog = new TextInputDialog("");
        depositDialog.setTitle("Create account");
        depositDialog.setContentText("Enter account name:");
        Optional<String> result = depositDialog.showAndWait();

        result.ifPresent(inputName -> {
            this.selectedClient.openAccount(inputName);
            updateView();
        });
    }

    public void closeAccountButtonClicked() {
        if (checkAccountIsNotNull()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close Account");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("Close bank account " + this.selectedAccount.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK){
                this.selectedClient.closeAccount(selectedAccount);
                updateView();
            }
        });
    }

    public void accountDepositButtonClicked() {
        if (checkAccountIsNotNull()) {
            return;
        }

        TextInputDialog depositDialog = new TextInputDialog("");
        depositDialog.setTitle("Deposit");
        depositDialog.setContentText("Enter amount:");
        depositDialog.setHeaderText("Deposit to " + selectedAccount.getName());

        Optional<String> result = depositDialog.showAndWait();

        result.ifPresent(amount -> {
            selectedAccount.createOperation(Double.parseDouble(amount));
            updateView();
        });
    }

    public void accountWithdrawButtonClicked() {
        if (checkAccountIsNotNull()) {
            return;
        }

        TextInputDialog withdrawalDialog = new TextInputDialog("");
        withdrawalDialog.setTitle("Withdrawal");
        withdrawalDialog.setHeaderText("Withdraw from " + selectedAccount.getName());
        withdrawalDialog.setContentText("Enter amount:");
        Optional<String> result = withdrawalDialog.showAndWait();
        result.ifPresent(amount -> {
            selectedAccount.createOperation(-1*Double.parseDouble(amount));
            updateView();
        });
    }

    public void accountTransferButtonClicked() {
        if (checkAccountIsNotNull()) {
            return;
        }

        if (bankInstance.getAllAccounts().size() < 2){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("There're only one account");
            alert.show();
            return;
        }
        ArrayList<String> choices = bankInstance.getAllAccounts().stream()
                .filter(item -> !item.getId().equals(this.selectedAccount.getId()))
                .map(item -> item.getName())
                .collect(Collectors.toCollection(ArrayList::new));

        ChoiceDialog<String> accountDialog = new ChoiceDialog<>(choices.get(0), choices);
        accountDialog.setTitle("Transfer");
        accountDialog.setHeaderText("Transfer from " + this.selectedAccount.getName());
        accountDialog.setGraphic(null);
        accountDialog.setContentText("Choose account to transfer:");

        Optional<String> destinationAccountResult = accountDialog.showAndWait();
        destinationAccountResult.ifPresent(destination -> {
            BankAccount destinationAccount = bankInstance.getAllAccountsMap().get(destination);

            TextInputDialog amountDialog = new TextInputDialog("");
            amountDialog.setTitle("Transfer");
            amountDialog.setHeaderText("Transfer to " + destinationAccount.getName());
            amountDialog.setContentText("Enter amount:");

            Optional<String> amountResult = amountDialog.showAndWait();
            amountResult.ifPresent(amount -> {
                try {
                    bankInstance.transferMoney(selectedAccount, destinationAccount, Double.parseDouble(amount));
                    updateView();
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        });
    }

    public void showReportButtonClicked() {
        if (checkClientIsNotNull()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report for " + this.selectedClient.getName());
        alert.setHeaderText(this.selectedClient.getReport());
        alert.show();
    }

    public void onSaveStateButtonClicked() {
        try {
            this.bankInstance.saveDataToFile();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Data saved");
            alert.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
