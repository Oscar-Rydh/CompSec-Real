import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.KeyStore;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

public class server implements Runnable {
	private ServerSocket serverSocket = null;
	private static int numConnectedClients = 0;
	private DataBase database;
	private Log log;
	private boolean firstTimeCreateLog = true;

	public server(ServerSocket ss) throws IOException {
		serverSocket = ss;
		newListener();
		database = new DataBase();
		log = new Log();
	}

	public void run() {
		try {
			SSLSocket socket = (SSLSocket) serverSocket.accept();
			newListener();
			SSLSession session = socket.getSession();
			X509Certificate cert = (X509Certificate) session.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();
			System.out.println("Subject = " + subject);

			String name = subject.substring(3, subject.indexOf(","));
			subject = subject.substring(subject.indexOf("OU=") + 3);
			String hospitalDivision = subject.substring(0, subject.indexOf(","));
			subject = subject.substring(subject.indexOf("O=") + 2);
			String occupation = subject.substring(0, subject.indexOf(","));

			System.out.println("name: " + name);
			System.out.println("div: " + hospitalDivision);
			System.out.println("occ: " + occupation);

			String issuer = cert.getIssuerDN().getName();
			System.out.println("Issuer = " + issuer);
			String serial = cert.getSerialNumber().toString();
			numConnectedClients++;
			System.out.println("client connected");
			System.out.println("client name (cert subject DN field): " + subject);
			System.out.println("Issuer name: " + issuer);
			System.out.println("Serial number: " + serial);
			System.out.println(numConnectedClients + " concurrent connection(s)\n");

			PrintWriter out = null;
			BufferedReader in = null;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String clientMsg = null;
			while ((clientMsg = in.readLine()) != null) {
				System.out.println("CLIENTMESSAGE IS: " + clientMsg);
				User user = null;
				switch (occupation.toLowerCase()) {
				case "doctor":
					user = new Doctor(database, name, hospitalDivision, log, firstTimeCreateLog);
					break;

				case "nurse":
					user = new Nurse(database, name, hospitalDivision, log, firstTimeCreateLog);
					break;

				case "patient":
					user = new Patient(database, name, log, firstTimeCreateLog);
					break;

				case "government":
					user = new Government(database, name, log, firstTimeCreateLog);
					break;
				}
				firstTimeCreateLog = false;

				// Possible commands: getList, getRecord, createRecord
				// command syntax
				// getList: getList
				// getRecord Doctor && Nurse: getRecord 'patient'
				// getRecord Patient && Government: getRecord 'patient':'Hospital Division'
				// createRecord: createRecord "'patient':'nurse':'data'"
				// modifyRecord Doctor: modifyRecord 'patient':'nurse':'data' om
				// ingen skillnad 'patient':-:'data'
				// modifyRecord Nurse: modifyRecord 'patient':'data'
				// deleteRecord Gov: deleteRecord 'patient':'Hospital Division'
				int counter = 0;
				for (int i = 0; i < clientMsg.length(); i++) {
					if (clientMsg.charAt(i) == ':') {
						counter++;
					}
				}

				if (user != null) {
					if(clientMsg.equals("help")){
						out.println("Possible commands: " + user.getPossibleCommands());
					}else if (clientMsg.equals("getList")) {
						out.println(user.getRecordListInfo());
						System.out.println("List info sent to client");
					} else if (clientMsg.contains("getRecord")) {
						if((user instanceof Patient || user instanceof Government) && counter != 1){
							user.unavailibleCommand(clientMsg);
							out.println("Invalid argument");
						}else{
							out.println(user.getRecord(clientMsg));
							System.out.println("Patient record sent to client");	
						}
					} else if (clientMsg.contains("createRecord")) {
						if (user instanceof Doctor) {
							if(counter == 2){
								user.createRecord(clientMsg);
								System.out.println("New record created");
								out.println("New record created");
							}else {
								user.unavailibleCommand(clientMsg);
								out.println("Invalid argument");
							}
						} else {
							user.unavailibleCommand(clientMsg);
							out.println("Only Doctors can create Records!!");
						}
					} else if (clientMsg.contains("modifyRecord")) {
						if ((user instanceof Doctor || user instanceof Nurse) && counter == 2) {
							out.println(user.modifyRecord(clientMsg));
						} else {
							user.unavailibleCommand(clientMsg);
							out.println("Invalid argument");
						}
					} else if (clientMsg.contains("deleteRecord")) {
						if (user instanceof Government && counter == 1) {
							out.println(user.deleteRecord(clientMsg));
						}else {
							user.unavailibleCommand(clientMsg);
							out.println("Invalid argument");
						}
					} else {
						out.println("Something went wrong! Command sent: " + clientMsg);
						user.unavailibleCommand(clientMsg);
						System.out.println("Något gick fel");
					}
				}
				out.flush();
				System.out.println("done\n");
			}
			in.close();
			out.close();
			socket.close();
			numConnectedClients--;
			System.out.println("client disconnected");
			System.out.println(numConnectedClients + " concurrent connection(s)\n");
		} catch (IOException e) {
			System.out.println("Client died: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private void newListener() {
		(new Thread(this)).start();
	} // calls run()

	public static void main(String args[]) {
		System.out.println("\nServer Started\n");
		int port = -1;
		if (args.length >= 1) {
			port = Integer.parseInt(args[0]);
		}
		String type = "TLS";
		try {
			ServerSocketFactory ssf = getServerSocketFactory(type);
			ServerSocket ss = ssf.createServerSocket(port);
			((SSLServerSocket) ss).setNeedClientAuth(true); // enables client
															// authentication
			new server(ss);
		} catch (IOException e) {
			System.out.println("Unable to start Server: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static ServerSocketFactory getServerSocketFactory(String type) {
		if (type.equals("TLS")) {
			SSLServerSocketFactory ssf = null;
			try { // set up key manager to perform server authentication
				SSLContext ctx = SSLContext.getInstance("TLS");
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
				KeyStore ks = KeyStore.getInstance("JKS");
				KeyStore ts = KeyStore.getInstance("JKS");
				char[] password = "password".toCharArray();

				ks.load(new FileInputStream("../certs_stores/serverkeystore"), password); // keystore
																			// password
																			// (storepass)
				ts.load(new FileInputStream("../certs_stores/servertruststore"), password); // truststore
																			// password
																			// (storepass)
				kmf.init(ks, password); // certificate password (keypass)
				tmf.init(ts); // possible to use keystore as truststore here
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
				ssf = ctx.getServerSocketFactory();
				return ssf;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return ServerSocketFactory.getDefault();
		}
		return null;
	}
}
