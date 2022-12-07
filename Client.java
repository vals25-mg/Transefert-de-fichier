import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
   
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Client extends JPanel{

    public Client(int port) {
        
        try {
            Socket s=new Socket("localhost",port);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
           DataInputStream din=new DataInputStream(s.getInputStream());
            BufferedReader br= new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Send get to start...");
            String str="",filename="";
            while (!str.equals("start")) {
                str=br.readLine();
            }

            dout.writeUTF(str);
            dout.flush();

            filename=din.readUTF();
            System.out.println("Receiving file: "+filename);

            long sr=Long.parseLong(din.readUTF());
            System.out.println("File size: "+(sr/(1024*1024))+"KB");

            byte b[]=new byte[1024];
            System.out.println("REceiving file...");
            FileOutputStream fos=new FileOutputStream(new File(filename),true);
            long byteRead;
            do {
                byteRead=din.read(b,0,b.length);
                fos.write(b,0,b.length);
            }while (!(byteRead<1024));
            System.out.println("completed");
            fos.close();
            dout.close();
            s.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
    public static void main(String[] args) {
        Client client=new Client(6666);
    }
    
}
