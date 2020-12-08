package bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Client implements Serializable {
    private final String clientId;
    private final String name;
    private final String passportNumber;

    private final HashMap<String, BankAccount> accounts = new HashMap<>();

    public Client(String name, String passportNumber) {
        this.clientId = Long.toHexString(Double.doubleToLongBits(Math.random()));
        this.name = name;
        this.passportNumber = passportNumber;
    }

    public String getId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public ArrayList<BankAccount> getAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public BankAccount openAccount(String name) {
        BankAccount account = new BankAccount(this, name);
        this.accounts.put(account.getId(), account);
        return account;
    }

    public void closeAccount(BankAccount account) {
        this.accounts.remove(account.getId());
    }

    public double getTotalBalance() {
        return this.getAccounts().stream().mapToDouble(BankAccount::getBalance).sum();
    }

    public String getReport() {
        String result =  "Number of accounts: " + this.getAccounts().size() + "\n" +
                "Total amount: " + this.getTotalBalance() + "\n\n";
        Integer i = 1;
        for (BankAccount account : this.getAccounts()) {
            result += Integer.toString(i) + ") " + account.getName() + " - " + account.getBalance() + "\n";
        }

        return result;
    }
}
