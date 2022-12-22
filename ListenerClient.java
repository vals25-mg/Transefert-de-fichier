package listener;

import java.awt.Color;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

import javax.swing.JFileChooser;

import fenetre.WindowClient;

public class ListenerClient implements ActionListener{
  
    WindowClient windowClient;

    public WindowClient getWindowClient() {
        return windowClient;
    }


    public void setWindowClient(WindowClient windowClient) {
        this.windowClient = windowClient;
    }
    
    
    public ListenerClient(WindowClient windowClient) {
        this.windowClient = windowClient;
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub 
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a file to send");
        if (arg0.getSource()==getWindowClient().getChoose()) {
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                    getWindowClient().getShowFile().setForeground(Color.BLACK);
                    getWindowClient().getShowFile().setText(chooser.getSelectedFile().getName());
                    getWindowClient().setFichier(chooser.getSelectedFile());
            }
        }
        if (arg0.getSource()==getWindowClient().getSend()) {
            if (getWindowClient().getFichier()==null) {
                getWindowClient().getShowFile().setText("Select File first");
                getWindowClient().getShowFile().setBackground(Color.RED);
            }
            else{
                try {
                getWindowClient().send();
                getWindowClient().getShowFile().setForeground(Color.green);
                getWindowClient().getShowFile().setText("File Sent");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            }
        }
        
    }


    
}
