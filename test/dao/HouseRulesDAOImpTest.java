package dao;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.time.LocalTime;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import model.HouseRules;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HouseRulesDAOImpTest {

    private HouseRulesDAOImp houseRulesDAO;
    private EntityManager mockEm;

    @BeforeEach
    public void setUp() throws Exception {
        houseRulesDAO = new HouseRulesDAOImp();
        mockEm = mock(EntityManager.class);

        Field emField = HouseRulesDAOImp.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(houseRulesDAO, mockEm);
    }

    @Test
    public void testCreateHouseRules() {
        HouseRules houseRules = new HouseRules();
        
        houseRulesDAO.createHouseRules(houseRules);
        
        verify(mockEm).persist(houseRules);
    }

    @Test
    public void testCreateHouseRulesWithParameters() {
        LocalTime arrival = LocalTime.of(14, 0);
        LocalTime departure = LocalTime.of(11, 0);
        
        HouseRules createdHouseRules = houseRulesDAO.createHouseRules(arrival, departure, true, false, false);
        
        ArgumentCaptor<HouseRules> houseRulesCaptor = ArgumentCaptor.forClass(HouseRules.class);
        verify(mockEm).persist(houseRulesCaptor.capture());
        
        HouseRules persistedHouseRules = houseRulesCaptor.getValue();
        assertEquals(arrival, persistedHouseRules.getArrivalHour());
        assertEquals(departure, persistedHouseRules.getDepartureHour());
    }

    @Test
    public void testUpdateHouseRules() {
        HouseRules houseRules = new HouseRules();
        
        houseRulesDAO.updateHouseRules(houseRules);
        
        verify(mockEm).merge(houseRules);
    }
}
