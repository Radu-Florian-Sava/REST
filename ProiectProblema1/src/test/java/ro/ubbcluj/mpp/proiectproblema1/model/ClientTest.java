package ro.ubbcluj.mpp.proiectproblema1.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientTest {
    Client client = null;

    @BeforeEach
    void setClient() {
        client = new Client(1, "Ioan Popa", "Str. Lucian Blaga nr. 10");
    }

    @Test
    void getName() {
        assertEquals(client.getName(), "Ioan Popa");
    }

    @Test
    void setName() {
        client.setName("Marian Florescu");
        assertEquals(client.getName(), "Marian Florescu");
    }

    @Test
    void getAddress() {
        assertEquals(client.getAddress(), "Str. Lucian Blaga nr. 10");
    }

    @Test
    void setAddress() {
        client.setName("Str. Plevnei nr. 20");
        assertEquals(client.getName(), "Str. Plevnei nr. 20");
    }

    @Test
    void getID() {
        assertEquals(client.getID(), 1);
    }

    @Test
    void setID() {
        client.setID(20);
        assertEquals(client.getID(), 20);
    }

    @Test
    void testToString() {
        assertEquals(client.toString(), "Client{" +
                "ID=" + 1 +
                ", name='" + "Ioan Popa" + '\'' +
                ", address='" + "Str. Lucian Blaga nr. 10" + '\'' +
                '}');
    }
}