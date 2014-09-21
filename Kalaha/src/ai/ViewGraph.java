/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Torkelz
 */
public class ViewGraph extends JFrame{
    
    private Panel pan;
    
    public ViewGraph() {
        initComponents();
    }
    
    public void setRoot(Node _root){
        pan.setRoot(_root);
    }

    private void initComponents() {
        // we want a custom Panel2, not a generic JPanel!
        pan = new Panel();

        pan.setBackground(new java.awt.Color(255, 255, 255));
        pan.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//        pan.addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent evt) {
//                jPanel2MousePressed(evt);
//            }
//            public void mouseReleased(MouseEvent evt) {
//                jPanel2MouseReleased(evt);
//            }
//        });
//        jPanel2.addMouseMotionListener(new MouseMotionAdapter() {
//            public void mouseDragged(MouseEvent evt) {
//                jPanel2MouseDragged(evt);
//            }
//        });

        // add the component to the frame to see it!
        this.setContentPane(pan);
        // be nice to testers..
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }// </editor-fold>    
}
