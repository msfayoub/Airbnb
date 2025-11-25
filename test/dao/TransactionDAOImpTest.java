package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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
        double amount = 250.0;

        Transaction createdTransaction = transactionDAO.createTransaction(sender, receiver, amount);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(mockEm).persist(transactionCaptor.capture());
        
        Transaction persistedTransaction = transactionCaptor.getValue();
        assertEquals(sender, persistedTransaction.getSender());
        assertEquals(receiver, persistedTransaction.getReceiver());
        assertEquals(amount, persistedTransaction.getAmount());
    }

    @Test
    public void testGetTransaction() {
        long transactionId = 1L;
        Transaction transaction = new Transaction();
        when(mockEm.find(Transaction.class, transactionId)).thenReturn(transaction);

        Transaction foundTransaction = transactionDAO.getTransaction(transactionId);

        assertNotNull(foundTransaction);
        verify(mockEm).find(Transaction.class, transactionId);
    }
}
