import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;
import java.io.Console;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

public class SeleniumGridServletTest 
{
    private SeleniumGridServlet servlet;
	
    @Before
	public void setUp() {
        servlet = new SeleniumGridServlet();
        //request = new MockHttpServletRequest();
        //response = new MockHttpServletResponse();
    }

    @Test
    public void testConstructorWithRegistry()
    {
        registry = new Registry();
        new_servlet = new SeleniumGridServlet(Registry);
    }
    
    @Test
	public void testGetServletInfo() 
    {
        String info = servlet.getServletInfo();
        assertEquals("SeleniumGridServlet Servlet", info);
        //debug(info);
    }

    @Test
    public void testStatus() {
        HttpServletRequest req = mock(HttpServletRequest.class);       
        HttpServletResponse resp = mock(HttpServletResponse.class);
        try {
            servlet.doGet(req, resp);
        } catch (IOException ie) {
            System.exit(1);
        } catch (ServletException se) {
            System.exit(1);
        }
    }

    private void debug(String output)
    {
        Console console = System.console();
        console.writer().println(output);
    }

}
