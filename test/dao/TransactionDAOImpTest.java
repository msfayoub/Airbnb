package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import model.Offer;
import model.Transaction;
import model.User;

public class TransactionDAOImpTest {

    private TransactionDAOImp transactionDAO;
    private EntityManager mockEm;

    @BeforeEach
    public void setUp() throws Exception {
        transactionDAO = new TransactionDAOImp();
        mockEm = mock(EntityManager.class);

        Field emField = TransactionDAOImp.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(transactionDAO, mockEm);
    }

    @Test
    public void testCreateTransaction() {
        User sender = new User();
        User receiver = new User();
        Offer offer = new Offer();
        double amount = 250.0;

        transactionDAO.createTransaction(sender, receiver, offer, amount);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(mockEm).persist(transactionCaptor.capture());
        
        Transaction persistedTransaction = transactionCaptor.getValue();
        assertEquals(sender, persistedTransaction.getSender());
        assertEquals(receiver, persistedTransaction.getReceiver());
        assertEquals(amount, persistedTransaction.getAmount());
    }
}
