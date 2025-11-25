package dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.HouseRules;

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
    public void testGetHouseRules() {
        int houseRulesId = 1;
        HouseRules houseRules = new HouseRules();
        when(mockEm.find(HouseRules.class, houseRulesId)).thenReturn(houseRules);

        HouseRules foundHouseRules = houseRulesDAO.getHouseRules(houseRulesId);

        assertNotNull(foundHouseRules);
        verify(mockEm).find(HouseRules.class, houseRulesId);
    }
}
