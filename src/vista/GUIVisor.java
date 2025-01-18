package vista;

import controlador.ControladorSistema;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUIVisor extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel fotoLabel;

    public GUIVisor() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);

        setSize(800, 600);
        setResizable(false);

        Image image = null;
        try {
            URL url = new URL(ControladorSistema.getInstance().getUrlImagenActual());
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null) {
            // Obtener las dimensiones originales de la imagen
            int originalWidth = image.getWidth(null);
            int originalHeight = image.getHeight(null);

            // Calcular las nuevas dimensiones para mantener la relación de aspecto
            double aspectRatio = (double) originalWidth / originalHeight;
            int newWidth = 800; // Ancho máximo
            int newHeight = (int) (newWidth / aspectRatio); // Alto proporcional

            // Si el alto calculado es mayor que el máximo (600), ajustamos el alto
            if (newHeight > 600) {
                newHeight = 600; // Alto máximo
                newWidth = (int) (newHeight * aspectRatio); // Ancho proporcional
            }

            // Escalar la imagen manteniendo la relación de aspecto
            Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            fotoLabel.setIcon(new ImageIcon(scaledImage));
        }

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GUIVisor dialog = new GUIVisor();
        dialog.pack();
        dialog.setVisible(true);
    }
}
