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

		String negotiateURL = null;
		String negotiateSPN = null;
		if (args.length > 3) {
			negotiateURL = args[3];
			negotiateSPN = args[4];
		}

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		ServletHolder holder = new ServletHolder(new ProxyServlet());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("targetUri", args[1]);
		map.put("proxyURL", "http://localhost:" + args[0]);
		map.put("log", "false");
		map.put("negotiate.refreshtime", "100000");
		if (negotiateURL != null) {
			map.put("negotiate.url", negotiateURL);
			map.put("negotiate.spn", negotiateSPN);
		}
		holder.setInitParameters(map);
		context.addServlet(holder, args[2]);

		server.start();
		server.join();
	}

}
