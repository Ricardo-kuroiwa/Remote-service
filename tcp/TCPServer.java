package tcp;
import java.net.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import client.Services;
import compute.Compute;
public class TCPServer {
	public static void main(String args[]) {
		try {
        int serverPort = 7896; // the server port
        ServerSocket listenSocket = new ServerSocket(serverPort);

        while(true) {
            Socket clientSocket = listenSocket.accept();
           
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
            Compute comp = null;
            try {
				//Fazendo o lookup
                comp = (Compute) registry.lookup(name);
				System.out.println("Conexao estabelecida com: " + clientSocket.getInetAddress().getHostAddress());
            } catch (NotBoundException e) {
                System.err.println("O serviço " + name + " não está registrado no registro RMI.");
                e.printStackTrace();
                continue; 
            }
            Connection c = new Connection(clientSocket, comp);
        }
    } catch (IOException e) {
        System.out.println("Listen socket:" + e.getMessage());
    }
	}
}
class Connection extends Thread {
	DataInputStream in ;
	DataOutputStream out; 
	Socket clientSocket;
	boolean sair = false;
	String mensagem = " ";
	Compute comp;
	Services task ;
	public Connection(Socket aClientSocket,Compute comp) {
		try {
			clientSocket = aClientSocket;
			this.comp=comp;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}
	
	public void run() {
		try { // an echo server
			String reponse = " ";
			//Enviando a lista de serviços
			out.writeUTF("Conexao estabelecida com sucesso...\nList Services\n1-Gerar Senha \n2-Vereficar se o texto é Palindromo \n3-Verificar se senha é valida(minimo de 8 caracteres,1 caracter maiusculo e 1 digito) \n4-Calcular Combinacao Simples\nEscolha um service (ex:3) :");
			do { //fica aqui ate' o cliente enviar a mensagem FIM
				try {
					//obtendo a mensagem enviada pelo cliente
					mensagem = in.readUTF();
					switch(Integer.parseInt(mensagem)) {
						case 1:
							System.out.println("Solicitado servico 1 - Gerar Senha");
							out.writeUTF("Argumento 1 (tamanho da senha):");
							String arg1 = in.readUTF();
							out.writeUTF("Argumento 2 (quer caracter especial , mande true ou false):");
							String arg2 = in.readUTF();
							task = new Services(Integer.parseInt(arg1),Boolean.parseBoolean(arg2),1);
            				String password = comp.executeTask(task);
							System.out.println("Recebido os argumentos 1:"+arg1+" e o 2:"+arg2);
							out.writeUTF("Resposta do service: "+password);
							System.out.println("Senha: " + password);
							break;
						case 2:
							System.out.println("Solicitado servico 2 - Vereficar se a palavra é um palindromo");
							out.writeUTF("Argumento 1 (palavra a ser validado):");
							String palindromo = in.readUTF();
							System.out.println("Recebido o argumento 1:"+palindromo);
							task = new Services(palindromo,2);
            				String isPalindrome = comp.executeTask(task);
							System.out.println("Resposta do service: " + isPalindrome);
							out.writeUTF("Palidromo: "+ isPalindrome);
							break;
						case 3:
							System.out.println("Solicitado servico 3 - Validar se a senha é valida");
							out.writeUTF("Argumento 1 (senha a ser validado):");
							String validInput = in.readUTF();
							System.out.println("Recebido o argumento 1:"+validInput);
							task = new Services(validInput,3);
            				String isValid = comp.executeTask(task);
							System.out.println("Resposta do service: " + isValid);
							out.writeUTF("Senha é valido: "+ isValid);
							break;
						case 4:
							System.out.println("Solicitado servico 4 - Calcular Combinacao Simples de n elementos e k elementos a agrupar");
							out.writeUTF("Argumento 1 (n elementos ):");
							String comb_arg_1 = in.readUTF();
							System.out.println("Recebido o argumento 1:"+ comb_arg_1);
							out.writeUTF("Argumento 2 (k elementos a agrupar):");
							String comb_arg_2 = in.readUTF();
							System.out.println("Recebido o argumento 2:"+ comb_arg_2);
							task = new Services(Integer.parseInt(comb_arg_1),Integer.parseInt(comb_arg_2),4);
            				String comb_result = comp.executeTask(task);
							System.out.println("Resposta do service: " + comb_result);
							out.writeUTF("Numero de possibilidades : "+comb_result);
							break;
						default:
							mensagem = "escolha invalida";
							break;
					}
				} catch (IOException iOException) {
					System.err.println("erro: " + iOException.toString());
				}
			} while(!mensagem.equals("FIM"));
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				/*close failed*/ }
		}
	}
}