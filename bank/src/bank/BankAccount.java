package bank;

import java.io.Serializable;
import java.util.*;

public class BankAccount implements Serializable {
    private final String accountId;
    private final String name;
    private final Date creationDate;
    private final Client owner;

    private final ArrayList<BankOperation> operations = new ArrayList<>();

    public BankAccount(Client owner, String name) {
        this.name = name + " (" + Math.random() + ")";
        this.accountId = Long.toHexString(Double.doubleToLongBits(Math.random()));
        this.creationDate = new Date(System.currentTimeMillis());
        this.owner = owner;
    }

    public String getId() {
        return this.accountId;
    }

    public String getName() {
        return this.name;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public Client getOwner() {
        return this.owner;
    }

    public ArrayList<BankOperation> getOperations() {
        this.operations.sort(Comparator.comparing(BankOperation::getDate));

        return this.operations;
    }

    public double getBalance() {
        return this.operations.stream().mapToDouble(BankOperation::getAmount).sum();
    }

    public void createOperation(double amount) {
        this.operations.add(new BankOperation(amount));
    }
}
