import java.io.*;
import java.net.*;
import java.security.KeyStore;
import javax.net.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;

public class server implements Runnable {
	private ServerSocket serverSocket = null;
	private static int numConnectedClients = 0;
	private DataBase database;

	public server(ServerSocket ss) throws IOException {
		serverSocket = ss;
		newListener();
		database = new DataBase();
	}

	public void run() {
		try {
			SSLSocket socket = (SSLSocket) serverSocket.accept();
			newListener();
			SSLSession session = socket.getSession();
			X509Certificate cert = (X509Certificate) session.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();
			System.out.println("Subject = " + subject);
			String issuer = cert.getIssuerDN().getName();
			System.out.println("Issuer = " + issuer);
			String serial = cert.getSerialNumber().toString();
			String name = null;
			String division = null;
			if(subject.contains(":")){
				String[] array = subject.split(":");
				name = array[0];
				division = array[1];
			}
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
				String rev = new StringBuilder(clientMsg).reverse().toString();
				System.out.println("received '" + clientMsg + "' from client");
				System.out.println("Current Certificate user: " + subject + "\n");
				System.out.print("sending '" + rev + "' to client...");

				User user = null;
				switch (issuer.toLowerCase()) {
				case "doctor":
					user = new Doctor(database, name, division);
					break;

				case "nurse":
					user = new Nurse(database, name, division);
					break;

				case "patient":
					user = new Patient(database, subject);
					break;

				}
			
				//Possible commands: getList, getRecord, createRecord
				//command syntax
				//getList: getList
				//getRecord Doctor && Nurse: getRecord 'patient'
				//getRecord Patient: getRecord 'patient':'Hospital Division'
				//createRecord: createRecord "'patient':'nurse':'data'"
				//modifyRecord Doctor:  modifyRecord 'patient':'nurse':'data' om ingen skillnad 'patient':-:'data'
				//modifyRecord Nurse:  modifyRecord 'patient':'data' 
				if (user != null) {
					if (clientMsg.equals("getList")) {
						out.println(user.getRecordListInfo());
						System.out.println("List info sent to client");
					} else if (clientMsg.contains("getRecord")) {
						out.println(user.getRecord(clientMsg));
						System.out.println("Patient record sent to client");
					}else if(clientMsg.contains("createRecord")){
						if(user instanceof Doctor){
							user.createRecord(clientMsg);
							System.out.println("New record created");
							out.println("New record created");
						}else{
							out.println("Only Doctors can create Records!!");
						}
					}else if(clientMsg.contains("modifyRecord")){
						if(user instanceof Doctor || user instanceof Nurse){
							user.modifyRecord(clientMsg);
						}
					}
				}
				out.println("Something went wrong! Command sent: " + clientMsg);
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

				ks.load(new FileInputStream("serverkeystore"), password); // keystore
																			// password
																			// (storepass)
				ts.load(new FileInputStream("servertruststore"), password); // truststore
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
