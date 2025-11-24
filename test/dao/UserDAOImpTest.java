package dao;

import model.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpTest {

    private UserDAOImp userDAO;
    private EntityManager mockEm;
    private Query mockQuery;

    @Before
    public void setUp() {
        userDAO = new UserDAOImp();
        mockEm = mock(EntityManager.class);
        mockQuery = mock(Query.class);
        
        // Use reflection to set the EntityManager
        try {
            java.lang.reflect.Field field = UserDAOImp.class.getDeclaredField("em");
            field.setAccessible(true);
            field.set(userDAO, mockEm);
        } catch (Exception e) {
            fail("Failed to set EntityManager: " + e.getMessage());
        }
    }

    @Test
    public void testCreateUser() {
        User user = new User("test@test.com", "hash", "John", "Doe", "123", "client", 0.0);
        
        when(mockEm.find(User.class, "test@test.com")).thenReturn(null);
        
        User result = userDAO.createUser("test@test.com", "hash", "John", "Doe", "123", "client", 0.0);
        
        assertNotNull("Created user should not be null", result);
        verify(mockEm, times(1)).persist(any(User.class));
    }

    @Test
    public void testGetUser() {
        User expectedUser = new User("test@test.com", "hash", "John", "Doe", "123", "client", 0.0);
        when(mockEm.find(User.class, "test@test.com")).thenReturn(expectedUser);
        
        User result = userDAO.getUser("test@test.com");
        
        assertEquals(expectedUser, result);
        verify(mockEm, times(1)).find(User.class, "test@test.com");
    }

    @Test
    public void testDeleteUser() {
        User user = new User("test@test.com", "hash", "John", "Doe", "123", "client", 0.0);
        when(mockEm.find(User.class, "test@test.com")).thenReturn(user);
        
        userDAO.deleteUser("test@test.com");
        
        verify(mockEm, times(1)).find(User.class, "test@test.com");
        verify(mockEm, times(1)).remove(user);
    }

    @Test
    public void testGetAllUser() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("user1@test.com", "hash", "User1", "Name1", "123", "client", 0.0));
        userList.add(new User("user2@test.com", "hash", "User2", "Name2", "456", "host", 0.0));
        
        when(mockEm.createQuery(anyString(), eq(User.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(userList);
        
        List<User> result = userDAO.getAllUser();
        
        assertEquals(2, result.size());
        verify(mockEm, times(1)).createQuery(anyString(), eq(User.class));
    }

    @Test
    public void testCredit() {
        User user = new User("test@test.com", "hash", "John", "Doe", "123", "client", 100.0);
        when(mockEm.find(User.class, "test@test.com")).thenReturn(user);
        
        userDAO.credit("test@test.com", 50.0);
        
        assertEquals(150.0, user.getBalance(), 0.01);
    }

    @Test
    public void testDebit() {
        User user = new User("test@test.com", "hash", "John", "Doe", "123", "client", 100.0);
        when(mockEm.find(User.class, "test@test.com")).thenReturn(user);
        
        userDAO.debit("test@test.com", 30.0);
        
        assertEquals(70.0, user.getBalance(), 0.01);
    }
}
