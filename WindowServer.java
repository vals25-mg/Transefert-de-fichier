package fenetre;
   
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowServer extends JFrame{

    public WindowServer(String title) throws HeadlessException {
        this.setTitle(title);
        this.setVisible(true);
        this.setSize(600, 400);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
