import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.web.Hub;
import org.json.JSONObject;

public class SeleniumGridServletTest 
{
	
    @BeforeClass
	public static void precondition() {
    	System.out.println("Start testing");
    }
    
    @AfterClass
	public static void postcondition() {
    	System.out.println("Stop testing");
    }

    @Test
    public void testConstructorWithoutParameters()
    {
        SeleniumGridServlet servlet = new SeleniumGridServlet();
        assertNotNull(servlet);
    }
    
    @Test
    public void testConstructorWithRegistry()
    {
        GridHubConfiguration config = new GridHubConfiguration();
        Hub hub = new Hub(config);
        Registry registry = Registry.newInstance(hub, config);
        SeleniumGridServlet servlet = new SeleniumGridServlet(registry);
        assertNotNull(servlet);
    }
    
    @Test
	public void testGetServletInfo() 
    {
        SeleniumGridServlet servlet = new SeleniumGridServlet();
        String info = servlet.getServletInfo();
        assertEquals("SeleniumGridServlet Servlet", info);
        System.out.println(info);
        System.out.println("Server release version: " + servlet.version);
    }

    @Test
    public void testStatus() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);       
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getPathInfo()).thenReturn("/status");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(resp.getWriter()).thenReturn(pw);
        
        GridHubConfiguration config = new GridHubConfiguration();
        Hub hub = new Hub(config);
        Registry registry = Registry.newInstance(hub, config);
        SeleniumGridServlet servlet = new SeleniumGridServlet(registry);
        
        servlet.doGet(req, resp);
        String result = sw.getBuffer().toString().trim();
        System.out.println("/status -> " + result.toString());
        JSONObject obj = new JSONObject(result);
        assertEquals(obj.getInt("sessionsCount"), 0);
    }
    
    @Test
    public void testSessions() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);       
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getPathInfo()).thenReturn("/sessions");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(resp.getWriter()).thenReturn(pw);
        
        GridHubConfiguration config = new GridHubConfiguration();
        Hub hub = new Hub(config);
        Registry registry = Registry.newInstance(hub, config);
        SeleniumGridServlet servlet = new SeleniumGridServlet(registry);
        
        servlet.doGet(req, resp);
        String result = sw.getBuffer().toString().trim();
        System.out.println("/sessions -> " + result.toString());
        JSONObject obj = new JSONObject(result);
        assertTrue(obj.getJSONArray("activeSessions").isEmpty());
    }

}
