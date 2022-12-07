package controller;

import java.awt.event.*;

import javax.swing.JButton;

public class ControlServer implements ActionListener{
    JButton b;

    
    public ControlServer(JButton b) {
        this.b = b;
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource()==getB()) {
            
        }
    }


    public JButton getB() {
        return b;
    }


    public void setB(JButton b) {
        this.b = b;
    }
    
}
