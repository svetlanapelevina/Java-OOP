package bank;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Bank {
    private static Bank instance;
    private HashMap<String, Client> clients;

    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    private Bank() {
        try {
            this.loadDataFromFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            clients = new HashMap<>();
        }
    }

    public void saveDataToFile() throws IOException {
        ObjectOutputStream objectOutputStreamForClients = new ObjectOutputStream(new FileOutputStream("bank_clients"));
        objectOutputStreamForClients.writeObject(this.clients);
        objectOutputStreamForClients.close();

        ObjectOutputStream objectOutputStreamForAccounts = new ObjectOutputStream(new FileOutputStream("bank_accounts"));
        objectOutputStreamForAccounts.writeObject(this.getAllAccounts());
        objectOutputStreamForAccounts.close();
    }

    public void loadDataFromFile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectOutputStreamForClients = new ObjectInputStream(new FileInputStream("bank_clients"));
        this.clients = (HashMap<String, Client>) objectOutputStreamForClients.readObject();
        objectOutputStreamForClients.close();

        ObjectInputStream objectInputStreamForAccounts = new ObjectInputStream(new FileInputStream("bank_accounts"));
        ArrayList<BankAccount> accounts = (ArrayList<BankAccount>) objectInputStreamForAccounts.readObject();
        for (BankAccount account : accounts) {
            account.getOwner().openAccount(account.getName());
        }
        objectInputStreamForAccounts.close();
    }

    public void createClient(String clientName, String passportNumber) {
        Client client = new Client(clientName, passportNumber);
        clients.put(client.getId(), client);
    }

    public Client getClientByName(String name) {
        return clients.get(name);
    }

    public ArrayList<Client> searchClientsByName(String searchString) {
        return this.getAllClients().stream()
                .filter(client -> client.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Client> getAllClients() {
        return new ArrayList<>(clients.values());
    }

    public ArrayList<BankAccount> getAllAccounts() {
        return this.getAllClients().stream()
                .map(Client::getAccounts)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public HashMap<String, BankAccount> getAllAccountsMap() {
        return (HashMap<String, BankAccount>) this.getAllAccounts().stream()
                .collect(Collectors.toMap(BankAccount::getName, Function.identity()));
    }


    public void transferMoney(BankAccount senderAccount, BankAccount recipientAccount, double amount) {
        senderAccount.createOperation(-1 * amount);
        recipientAccount.createOperation(amount);
    }
}
