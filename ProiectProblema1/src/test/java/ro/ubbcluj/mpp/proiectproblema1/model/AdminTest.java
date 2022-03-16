package ro.ubbcluj.mpp.proiectproblema1.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminTest {

    Admin admin = null;

    @BeforeEach
    void setUp() {
        admin = new Admin(1, "redragon", "1234");
    }

    @Test
    void getId() {
        assertEquals(admin.getID(), 1);
    }

    @Test
    void setId() {
        admin.setID(-4);
        assertEquals(admin.getID(), -4);
    }

    @Test
    void getUsername() {
        assertEquals(admin.getUsername(), "redragon");
    }

    @Test
    void getPassword() {
        assertEquals(admin.getPassword(), "1234");
    }


    @Test
    void testToString() {
        assertEquals(admin.toString(), "Admin{" +
                "id=" + 1 +
                ", username=" + "redragon" +
                ", password=" + "1234" +
                '}');
    }
}