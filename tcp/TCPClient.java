import java.net.*;
import java.io.*;
import java.util.Scanner;
public class TCPClient {
	public static void main (String args[]) {
		// arguments supply message and hostname
		Socket s = null;
		Scanner ler = new Scanner(System.in);
        String mensagem = "";
		try{
			int serverPort = 7896;
			s = new Socket(args[0], serverPort);    
			DataInputStream in = new DataInputStream( s.getInputStream());
			DataOutputStream out =new DataOutputStream( s.getOutputStream());
			String data = in.readUTF();	    // read a line of data from the stream
			System.out.println("Received Server: "+ data) ; 
			do {
                System.out.print("..: ");
                mensagem = ler.nextLine();
				//Enviando a mensagem enviada pelo servidor
                out.writeUTF(mensagem);
                out.flush();
                //lendo a mensagem enviada pelo servidor
                mensagem = in.readUTF();
                System.out.println("Servidor>> "+mensagem);
            } while (!mensagem.equals("FIM"));
		}catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		}catch (IOException e){System.out.println("readline:"+e.getMessage());
		}finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
     }
}
