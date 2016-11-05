package de.elnarion.web.krbproxy;

import java.util.HashMap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.mitre.dsmiley.httpproxy.ProxyServlet;

public class Appstarter {


	public Appstarter() {
	}

	public static void main(String[] args) throws Exception {
		
		Server server = new Server(Integer.parseInt(args[0]));
		
	      
	      String negotiateURL = args[3];
	      String negotiateSPN = args[4];
		

//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.print("Enter username");
//        String username = br.readLine();
//        System.out.print("Enter password");
//        String password = br.readLine();
//        System.out.print("Enter domain");
//        String domain = br.readLine();
//        System.out.print("Enter workstation");
//        String workstation = br.readLine();
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
 
        ServletHolder holder = new ServletHolder(new ProxyServlet());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("targetUri", args[1]);
        map.put("proxyURL", "http://localhost:"+args[0]);
        map.put("log", "true");
//        map.put("user", username);
//        map.put("password", password);
//        map.put("domain", domain);
//        map.put("host", workstation);
        map.put("negotiate.refreshtime", "100000");
        map.put("negotiate.url", negotiateURL);
        map.put("negotiate.spn", negotiateSPN);

        holder.setInitParameters(map);
        context.addServlet(holder,args[2]);
 
        server.start();
        server.join();
	}

}
