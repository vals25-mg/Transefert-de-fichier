package fenetre;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import objets.MyFile;
 
public class WindowServer extends JFrame{

    DefaultTableModel dm= new DefaultTableModel(0,0);
    
    public DefaultTableModel getDm() {
        return dm;
    }
    public void setDm(DefaultTableModel dm) {
        this.dm = dm;
    }
    public void firstTable() throws Exception{

    }
    public WindowServer() throws Exception {

        
        /*  Property of the JFrame */
        this.setTitle("Server Principale");
            this.setLayout(null);
            // this.setLocationRelativeTo(null);
            this.setVisible(true);
            this.setSize(500, 500);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel pan=new JPanel(null);
            pan.setBounds(0, 0, this.getWidth(), this.getHeight());
            this.add(pan);

            /*JTable */
            Vector<MyFile> vFiles=new Vector<>();
            JTable tableUpload=new JTable();
            String header []= new String[] {"n°","FileName","FileSize"};
            getDm().setColumnIdentifiers(header);
            tableUpload.setModel(getDm());
            JScrollPane jPane=new JScrollPane(tableUpload);
            jPane.setBounds(50, 0, 400, 450);
            pan.add(jPane);
            getDm().setRowCount(0); 
            try {
                File f1=new File("Server Principal/memo.txt");
                if(!f1.exists()) f1.createNewFile();
                vFiles=getFromfile(f1);
                addRow(vFiles, getDm());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        
            /*Socket */
        ServerSocket serverSocket=new ServerSocket(1234);
        while(true){
            Socket socket=serverSocket.accept();
            DataInputStream dataInputStream= new DataInputStream(socket.getInputStream());

            int filenameLength= dataInputStream.readInt();

            if (filenameLength>0) {
                byte[] fileNameBytes=new byte[filenameLength];
                dataInputStream.readFully(fileNameBytes,0,fileNameBytes.length);
                String filename= new String(fileNameBytes);

                int fileContentLength= dataInputStream.readInt();

                if (fileContentLength>0) {
                    byte [] fileContentBytes=new byte[fileContentLength];
                    dataInputStream.readFully(fileContentBytes, 0, fileContentLength);

                    /*DIVIDE BYTE */
                    Vector <byte[]> vByte=divideByte(fileContentBytes, fileContentLength);
                    
                    /*Send byte one by one to the server1,2,3 */
                    sendDividedByte(vByte, filename);
                    
                    /* */
                    MyFile myFile=new MyFile(filename,fileContentLength);
                    File fileToDownload=new File("Server Principal/"+filename);
                    try {
                        writeFile(myFile);
                        vFiles=getFromfile(new File("Server Principal/memo.txt"));
                        addRow(vFiles, getDm());
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public  void writeFile(Object o) throws Exception{
        File file=new File("Server Principal/memo.txt");
         Vector v=new Vector<>();
        if (!file.exists()) {
         System.out.println("tsy misy atramzao");
         file.createNewFile();
         System.out.println("File Created");
        }
        
     //    System.out.println("Le vecteur inscrit: "+v.get(0));
        else{
         System.out.println("file already exists");
         v=getFromfile(file);
        }
        v.add(o);
       
     FileOutputStream fileOut=new FileOutputStream(file);
        ObjectOutputStream objectOut=new ObjectOutputStream(fileOut);
         objectOut.writeObject(v);
         fileOut.close();
         objectOut.close();
         System.out.println("Vector insered");
     }
     public byte[] unifyBite(Vector<byte[]> vector){
        byte[] result=new byte[vector.get(0).length+vector.get(1).length+vector.get(2).length];
        System.arraycopy(vector.get(0), 0, result, 0, vector.get(0).length);
        System.arraycopy(vector.get(1), 0, result, vector.get(0).length, vector.get(1).length);
        System.arraycopy(vector.get(2), 0, result, vector.get(0).length+vector.get(1).length, vector.get(2).length);
        return result;
     }
     public  Vector getFromfile(File file) throws IOException, ClassNotFoundException{
         
         Vector v=new Vector<>();
         FileInputStream fin=new FileInputStream(file);
         if(file.length()!=0)
         {
         ObjectInputStream in=new ObjectInputStream(fin);
         v=(Vector)in.readObject();
         fin.close();
         in.close();
         }
         return v;
     }
     public void addRow(Vector <MyFile> vFiles,DefaultTableModel dModel){
        dModel.setRowCount(0);    
        for (int i = 0; i < vFiles.size(); i++) {
            Vector <Object> data= new Vector<Object>();
            data.add(i);
            data.add(vFiles.get(i).getName());
            data.add(vFiles.get(i).getSize()+" octets");
            dModel.addRow(data);
        }
    }
    public int[] divideByteSize(int tailleTotale){
        System.out.println("taille Totale: "+tailleTotale);
        int[] t=new int[3];
        if (tailleTotale%3==0) {
            t[0]=t[1]=t[2]=tailleTotale/3;
            System.out.println("Anaty fonction t[0]: "+t[0]+" t[1]: "+t[1]+" t[2]: "+t[2]);
            return t;
        }
        t[0]=t[1]=tailleTotale/3;
        t[2]=tailleTotale-t[0]-t[1];
        System.out.println("Anaty fonction t[0]: "+t[0]+" t[1]: "+t[1]+" t[2]: "+t[2]);
        return t;        
    }
    public Vector <byte[]> divideByte(byte[] byteOriginal,int tailleTotale){
        Vector <byte[]> vByte=new Vector<>();
        int t[]=divideByteSize(tailleTotale);
        byte[] b1=new byte[t[0]],b2=new byte[t[1]],b3=new byte[t[2]];
        System.arraycopy(byteOriginal, 0, b1, 0, b1.length);
        System.arraycopy(byteOriginal, b1.length, b2, 0, b2.length);
        System.arraycopy(byteOriginal, b1.length+b2.length , b3, 0, b3.length);
        vByte.add(b1);
        vByte.add(b2);
        vByte.add(b3);
        return vByte;
    }
    public void sendDividedByte(Vector <byte[]> vByte,String filename) throws Exception{
        int[] ports={9991,9992,9993};
        File fileTosend=new File(filename);
        fileTosend.createNewFile();
        for (int i = 0,j=i+1 ; i < ports.length; i++,j++) {
            // Tokony miampy nom de fichier
            send("localhost", ports[i],fileTosend,".part"+j,vByte.get(i));
        }
        fileTosend.delete();
    }
    public void send(String host,int port,File fileTosend,String partName,byte[] part) throws Exception{
        
        FileInputStream fileInputStream= new FileInputStream(fileTosend);
                System.out.println(fileInputStream.getFD());
                    Socket socket= new Socket(host, port);
    
                    DataOutputStream dataOutputStream= new DataOutputStream(socket.getOutputStream());
    
                    String filename=fileTosend.getName()+partName;
                    byte[] fileNameBytes= filename.getBytes();
    
                    byte[] fileContentBytes=part;
                    
                    fileInputStream.read(fileContentBytes);
    
                    dataOutputStream.writeInt(fileNameBytes.length);
                    dataOutputStream.write(fileNameBytes);
    
                    dataOutputStream.writeInt(fileContentBytes.length);
                    dataOutputStream.write(fileContentBytes);
       }
    public static void main(String[] args) {
        try {
            new WindowServer();
            
            for (int i = 0,j=i+1 ; i < 3; i++,j++) {
                // Tokony miampy nom de fichier
                System.out.println("i: "+i+"// j: "+j);
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
  
}