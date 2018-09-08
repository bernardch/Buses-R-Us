package ca.ubc.cs.cpsc210.translink.tests.model;

        import ca.ubc.cs.cpsc210.translink.model.Arrival;
        import ca.ubc.cs.cpsc210.translink.model.Route;
        import ca.ubc.cs.cpsc210.translink.model.RouteManager;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;

        import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test Arrival
 */
public class ArrivalTest {
    Route r;
    Arrival a;
    Arrival early;
    Arrival late;

    @BeforeEach
    public void setup() {
        r = RouteManager.getInstance().getRouteWithNumber("43");
        a = new Arrival(23, "Home", r);
        early = new Arrival(24, "Home", r);
        late = new Arrival(22, "Home", r);
    }

    @Test
    public void testConstructor() {
        assertEquals(23, a.getTimeToStopInMins());
        assertEquals(r, a.getRoute());
        assertEquals(" ", a.getStatus());
        a.setStatus("early");
        assertEquals("early", a.getStatus());
    }

    @Test
    public void testcompareTo() {
        assertEquals(-1, a.compareTo(early));
        assertEquals(1, a.compareTo(late));
        assertEquals(0, a.compareTo(a));
    }
}
