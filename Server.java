import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
  
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import fenetre.WindowServer;

public class Server extends JPanel{

    public Server(int port) {
        init();
        JButton b=new JButton("Start Server");
        b.setBounds(400, 300, 150,50);
        this.add(b);
        try {
            ServerSocket ss=new ServerSocket(port);
            System.out.println(ss.getLocalPort());
            Socket s=ss.accept(); //etablissement de la connection
            DataInputStream dis=new DataInputStream(s.getInputStream());
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

            // Conversation
            String str="",str2=""; 
            
            while (!str.equals("stop")) {
                str=dis.readUTF();
                System.out.println("Client : "+str);
                str2=br.readLine();
                dout.writeUTF(str2);
                dout.flush();
            }
            dis.close();
            ss.close();
            s.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }  
    }
    public void init(){
        this.setLayout(null);
        // this.setBackground(Color.BLACK);
        WindowServer frame=new WindowServer("title");
        
        this.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(this);
    }
    public static void main(String[] args) {
        Server server=new Server(6666);
    }
    
}