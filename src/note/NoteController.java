/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package note;

import filesystem.FileSystem;
import filesystem.directories.File;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

/**
 * This class manages the logic behind the "The Note Editor"
 * @author Michelle Alvarado
 */
public final class NoteController implements KeyListener {

    private NoteEditor noteEditorView;
    private String textAreaBuffer;
    private File file;
    private FileSystem fs;

    public NoteController(String fileText) {
        this.textAreaBuffer = fileText;
    }

    public NoteController(String contents, File file, FileSystem fs) {
        this.textAreaBuffer = contents;
        this.file = file;
        this.fs = fs;
    }

    /**
     * This method initialize the NoteController Editor window frame
     */
    public void showNote() {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NoteEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NoteEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NoteEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NoteEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        noteEditorView = new NoteEditor();
        noteEditorView.noteTextArea.addKeyListener(this);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                noteEditorView.setVisible(true);
                noteEditorView.setResizable(false);
                noteEditorView.setLocationRelativeTo(null);
                noteEditorView.noteTextArea.setText(textAreaBuffer);
            }
        });
    }

    /**
     * this method captures a key event and validates if the key combination "CTRL + X" was performed, in that case
     * the system will show a message dialog that ask to the user if he wants to save the changes done in the text.
     * @param e: an event that contains the information about which keys were pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyChar() != 'x' && e.getKeyCode() == 88) {
            
            int selectedOption = JOptionPane.showConfirmDialog(noteEditorView, "Do you want to save the changes?");
            
            if (JOptionPane.CANCEL_OPTION != selectedOption) { //if the option was not "cancel" then the window has to be closed
                if (JOptionPane.OK_OPTION == selectedOption) {
                    this.textAreaBuffer = noteEditorView.noteTextArea.getText(); //if the user wants to save the changes then the modified
                    file.setContents(textAreaBuffer);
                    fs.saveFile(file);
                }                                                                //text should be saved in the textAreaBuffer
                noteEditorView.dispose();
            }
        }
    }

    /**
     * 
     * @return textAreaBuffer: the string variable where the text file is saved
     */
    public String getModifiedText() {
        return textAreaBuffer;
    }

    /**
     * Not implemented
     * @param e 
     */
    @Override
    public void keyReleased(KeyEvent e) {}
    
    /**
     * not implemented
     * @param e 
     */
    @Override
    public void keyTyped(KeyEvent e) {}
}
