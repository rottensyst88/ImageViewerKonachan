package vista;

import controlador.ControladorSistema;
import excepciones.SistemaExcepcionesAPP;
import modelo.Imagen;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GUIVisor extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JLabel fotoLabel;
    private JButton descargarImagenButton;

    private Imagen imagen = ControladorSistema.getInstance().loadImagen();

    public GUIVisor() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);
        setSize(1366, 768);
        setResizable(true);
        setTitle(imagen.getId() + " - " + imagen.getUrlImagen());

        fotoLabel.setIcon(new ImageIcon(imagen.getImage()));

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        descargarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                descargarImagen();
            }
        });
    }

    private void descargarImagen() {
        try{
            ControladorSistema.getInstance().descargarImagen();

            JOptionPane.showMessageDialog(this, "Imagen descargada con éxito",
                    "Descarga", JOptionPane.INFORMATION_MESSAGE);
        } catch (SistemaExcepcionesAPP e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        GUIVisor dialog = new GUIVisor();
        dialog.pack();
        dialog.setVisible(true);
    }
}
