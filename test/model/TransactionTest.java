package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTest {

    private Transaction transaction;
    private User sender;
    private User receiver;

    @BeforeEach
    public void setUp() {
        sender = new User("sender@example.com", "pass1", "John", "Doe", "1234567890", "customer", 500.0);
        receiver = new User("receiver@example.com", "pass2", "Jane", "Smith", "0987654321", "host", 100.0);
        transaction = new Transaction(sender, receiver, 250.0);
    }

    @Test
    public void testTransactionCreation() {
        assertNotNull(transaction);
        assertEquals(sender, transaction.getSender());
        assertEquals(receiver, transaction.getReceiver());
        assertEquals(250.0, transaction.getAmount());
        assertNotNull(transaction.getDate());
    }

    @Test
    public void testSetters() {
        User newSender = new User();
        User newReceiver = new User();
        
        transaction.setSender(newSender);
        transaction.setReceiver(newReceiver);
        transaction.setAmount(300.0);

        assertEquals(newSender, transaction.getSender());
        assertEquals(newReceiver, transaction.getReceiver());
        assertEquals(300.0, transaction.getAmount());
    }

    @Test
    public void testNoArgsConstructor() {
        Transaction t = new Transaction();
        assertNotNull(t);
    }

    @Test
    public void testTransactionWithDifferentAmounts() {
        transaction.setAmount(100.0);
        assertEquals(100.0, transaction.getAmount());
        
        transaction.setAmount(1000.0);
        assertEquals(1000.0, transaction.getAmount());
    }

    @Test
    public void testTransactionBetweenSameUser() {
        User sameUser = new User("same@example.com", "pass", "Same", "User", "1111111111", "customer", 500.0);
        Transaction sameUserTransaction = new Transaction(sameUser, sameUser, 50.0);
        
        assertEquals(sameUser, sameUserTransaction.getSender());
        assertEquals(sameUser, sameUserTransaction.getReceiver());
        assertEquals(50.0, sameUserTransaction.getAmount());
    }

    @Test
    public void testTransactionWithZeroAmount() {
        transaction.setAmount(0.0);
        assertEquals(0.0, transaction.getAmount());
    }
}
