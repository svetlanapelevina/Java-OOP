package bank;

import java.io.Serializable;
import java.util.Date;

public class BankOperation implements Serializable {
    private final double amount;
    private final Date date;

    public BankOperation(double amount) {
        this.amount = amount;
        this.date = new Date(System.currentTimeMillis());
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }
}
