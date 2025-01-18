package vista;

import controlador.ControladorSistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;


public class GUIPrincipal extends JDialog {
    private JPanel contentPane;
    private JTextField barraBusquedaField;
    private JButton busquedaButton;
    private JTable tablaDatos;
    private JButton salirButton;
    private JButton cargarImagenButton;
    private JComboBox selectorComboBox;
    private JButton buttonOK;
    private JButton buttonCancel;
    private String[] cabeceras = {"ID", "URL de la imagen", "Etiquetas", "Calificación", "Fuente"};

    public GUIPrincipal() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(busquedaButton);

        tablaDatos.setModel(new DefaultTableModel(null,cabeceras));


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

        salirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        busquedaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });
        cargarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarImagen();
            }
        });
    }

    private void cargarImagen() {
        int fila = tablaDatos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado una imagen.");
        } else {
            ControladorSistema.getInstance().setUrlImagenActual((String) tablaDatos.getValueAt(fila, 1));

            GUIVisor.main(null);
        }
    }

    private void buscar() {
        String[][] datos = new String[0][0];
        String busq = barraBusquedaField.getText();

        if (busq.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se ha ingresado una búsqueda.");
        } else {
            try{
                datos = ControladorSistema.getInstance().obtenerResultados(busq, selectorComboBox.getSelectedIndex());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al obtener resultados.");
            }
        }

        tablaDatos.setModel(new DefaultTableModel(datos,cabeceras));
    }

    private void onCancel() {
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        GUIPrincipal dialog = new GUIPrincipal();
        dialog.pack();
        dialog.setVisible(true);
    }
}
