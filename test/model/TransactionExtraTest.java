package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;

public class TransactionExtraTest {

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
    }

    @Test
    public void testSetId() {
        transaction.setId(1L);
        assertEquals(1L, transaction.getId());
    }

    @Test
    public void testSetAmount() {
        transaction.setAmount(100.0);
        assertEquals(100.0, transaction.getAmount());
    }

    @Test
    public void testZeroAmount() {
        transaction.setAmount(0.0);
        assertEquals(0.0, transaction.getAmount());
    }

    @Test
    public void testVeryLargeAmount() {
        transaction.setAmount(999999999.99);
        assertEquals(999999999.99, transaction.getAmount());
    }

    @Test
    public void testDecimalPrecision() {
        transaction.setAmount(12.34);
        assertEquals(12.34, transaction.getAmount());
    }

    @Test
    public void testSetDate() {
        Date date = new Date();
        transaction.setDate(date);
        assertEquals(date, transaction.getDate());
    }

    @Test
    public void testSetNullDate() {
        transaction.setDate(null);
        assertNull(transaction.getDate());
    }

    @Test
    public void testSetSender() {
        User sender = new User();
        transaction.setSender(sender);
        assertEquals(sender, transaction.getSender());
    }

    @Test
    public void testSetReceiver() {
        User receiver = new User();
        transaction.setReceiver(receiver);
        assertEquals(receiver, transaction.getReceiver());
    }

    @Test
    public void testConstructorWithParameters() {
        User sender = new User();
        User receiver = new User();
        Transaction newTransaction = new Transaction(sender, receiver, 50.0);
        
        assertEquals(sender, newTransaction.getSender());
        assertEquals(receiver, newTransaction.getReceiver());
        assertEquals(50.0, newTransaction.getAmount());
        assertNotNull(newTransaction.getDate());
    }
}
