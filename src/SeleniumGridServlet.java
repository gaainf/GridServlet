import java.io.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.ProxySet;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;

import org.openqa.grid.internal.TestSlot;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.common.SeleniumProtocol;
import org.openqa.grid.web.Hub;
import org.openqa.grid.web.servlet.RegistryBasedServlet;
import org.openqa.selenium.internal.BuildInfo;
import org.openqa.selenium.remote.CapabilityType;

public class SeleniumGridServlet extends RegistryBasedServlet 
{
    private static final long serialVersionUID = 1L;
    
    private ServletConfig config;
    private final Logger log = Logger.getLogger(getClass().getName());
    public String version;

    public SeleniumGridServlet() 
    {
        this(null);
    }

    public SeleniumGridServlet(Registry registry) 
    {
        super(registry);
        version = new BuildInfo().getReleaseLabel();
    }
    
    public void init(ServletConfig config) 
        throws ServletException 
    {
        this.config = config;
    }
    
    public void destroy()
    {
        // do nothing.
    }

    public ServletConfig getServletConfig()
    {   
        return config;
    }

    public String getServletInfo()
    {
        return this.getClass().getCanonicalName() + " Servlet";
    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException 
    {
        try {
            if ("/status".equals(req.getPathInfo())) {
                sendJson(status(), req, resp);
            } else if("/sessions".equals(req.getPathInfo())) {
                JSONArray activeSessions = getActiveSessions();
                JSONObject jsonActiveSessions = new JSONObject();
                jsonActiveSessions.put("activeSessions",activeSessions);
                sendJson(jsonActiveSessions, req, resp);
            }
        } catch (JSONException je) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(500);
            JSONObject error = new JSONObject();

            try {
                error.put("message", je.getMessage());
                error.put("location", je.getStackTrace());
                error.write(resp.getWriter());
            } catch (JSONException e1) {
                log.log(Level.WARNING, "Failed to write error response", e1);
            }
        }
    }

    protected void sendJson(JSONObject json, HttpServletRequest req, HttpServletResponse resp)
    {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        Writer w = null;
        try {
            w = resp.getWriter();
            json.write(w);
        } catch (IOException e) {
            log.log(Level.WARNING, "Error writing response", e);
        } catch (JSONException e) {
            log.log(Level.WARNING, "Failed to serialize JSON response", e);
        }
    }

    protected JSONObject status()
        throws JSONException {
        JSONObject status = new JSONObject();
        Hub h = getRegistry().getHub();
        ProxySet proxies = this.getRegistry().getAllProxies();
        Iterator<RemoteProxy> iterator = proxies.iterator();
        JSONArray jsonProxies = new JSONArray();
        while (iterator.hasNext()) {
            RemoteProxy eachProxy = iterator.next();
            JSONObject jsonProxy = new JSONObject();
            jsonProxy.put("id", eachProxy.getId());
            if (eachProxy.isBusy()) {
                jsonProxy.put("busy", true);
            } else {
                jsonProxy.put("busy", false);
            }
            JSONArray jsonSlots = new JSONArray();
            for (TestSlot slot : eachProxy.getTestSlots()) {
                JSONObject jsonSlot = new JSONObject();
                jsonSlot.put("url", slot.getRemoteURL());
                jsonSlot.put("lastSession", slot.getLastSessionStart());
                String browserName = (String) slot.getCapabilities().get(CapabilityType.BROWSER_NAME).toString().replaceFirst("\\*", "");
                jsonSlot.put("browser", browserName);
                TestSession session = slot.getSession();
                if (session != null) {
                    jsonSlot.put("session",render(session));
                } else {
                    //log.log(Level.WARNING, "No sessions");
                }
                jsonSlots.put(jsonSlot);
            }
            jsonProxy.put("slots", jsonSlots);
            jsonProxies.put(jsonProxy);
        }
        
        status.put("proxies", jsonProxies);
        int sessionsCount = getRegistry().getNewSessionRequestCount();
        status.put("sessionsCount", sessionsCount);
        status.put("version", version);
        status.put("configuration", getRegistry().getConfiguration().toJson().getAsJsonObject().entrySet());
        status.put("host", h.getConfiguration().host);
        status.put("port", h.getConfiguration().port);
        status.put("registration_url", h.getRegistrationURL());
        
        return status;
    }

    protected JSONArray getActiveSessions()
        throws JSONException 
    {
        JSONArray jsonActiveSessions = new JSONArray();
        ProxySet proxies = this.getRegistry().getAllProxies();
        Iterator<RemoteProxy> iterator = proxies.iterator();
        while (iterator.hasNext()) {
            RemoteProxy eachProxy = iterator.next();
            for (TestSlot slot : eachProxy.getTestSlots()) {
                JSONObject jsonActiveSession = new JSONObject();
                TestSession session = slot.getSession();
                if (session != null) {
                    jsonActiveSession.put("key", session.getExternalKey().getKey());
                    jsonActiveSession.put("node", slot.getRemoteURL());
                    String browserName = (String) slot.getCapabilities().get(CapabilityType.BROWSER_NAME).toString().replaceFirst("\\*", "");
                    jsonActiveSession.put("browser", browserName);
                    jsonActiveSessions.put(jsonActiveSession);
                }
            }
        }
        return jsonActiveSessions;
    }
    
    protected String getNodeHostBySessionId(String sessionId)
    {
        //TODO: add function to get node host by session ID
        // The function should create List <sessionID, nodeHost>
        return "node_host";
    }

    private JSONObject render(TestSession session) 
        throws JSONException 
    {
        JSONObject json = new JSONObject();
        json.put("key", session.getExternalKey().getKey());
        json.put("inactivity_time", session.getInactivityTime());
        json.put("requested_capabilities", session.getRequestedCapabilities());
        json.put("forwarding_request", session.isForwardingRequest());
        json.put("orphaned", session.isOrphaned());
        return json;
    }
}

