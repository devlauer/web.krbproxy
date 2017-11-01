package de.elnarion.web.krbproxy;

import java.util.HashMap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.mitre.dsmiley.httpproxy.ProxyServlet;

/**
 * Starter class for the kerberos/NTLM proxy, which uses the local kerberos TGT
 * or windows NTLM to access a remote address. This is useful if a program can
 * not access this remote address because it is not aware of the kerberos/NTLM
 * negotiate.
 * 
 * @author dev.lauer@elnarion.de
 *
 */
public class Appstarter {

	/**
	 * Default constructor
	 */
	public Appstarter() {
	}

	/**
	 * Starts up the kerberos proxy.
	 * 
	 * @param args
	 *            the local port for the proxy (0), the remote URL for the proxy
	 *            (1), the local context root (2), the negotiate URL of the remote
	 *            server (3), the negotiate SPN of the remote server (4)
	 * @throws Exception
	 */
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
