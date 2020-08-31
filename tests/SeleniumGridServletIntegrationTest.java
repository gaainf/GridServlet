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

public class SeleniumGridServletIntegrationTest 
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
    public void testRun()
    {
        assertTrue(true);
    }

}
