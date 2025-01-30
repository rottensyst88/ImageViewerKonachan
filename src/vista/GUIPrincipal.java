package vista;

import controlador.ControladorSistema;
import excepciones.SistemaExcepcionesAPP;
import modelo.Imagen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;


public class GUIPrincipal extends JDialog {
    private JPanel contentPane;
    private JTextField barraBusquedaField;
    private JButton busquedaButton;
    private JTable tablaDatos;
    private JButton salirButton;
    private JButton cargarImagenButton;
    private JComboBox selectorComboBox;
    private JButton pSiguienteButton;
    private JButton pAnteriorButton;
    private JLabel etiquetaNum;

    private String[] cabeceras = {"ID", "URL de la imagen", "Etiquetas", "Calificación", "Fuente"};
    private int NUMERO_PAG = 1;

    public GUIPrincipal() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(busquedaButton);

        tablaDatos.setModel(new DefaultTableModel(null,cabeceras));
        etiquetaNum.setText("Página: " + NUMERO_PAG);

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
        pAnteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(NUMERO_PAG == 1) {
                    JOptionPane.showMessageDialog(null, "No hay más páginas anteriores.");
                }else{
                    NUMERO_PAG--;
                }

                etiquetaNum.setText("Página: " + NUMERO_PAG);
            }
        });
        pSiguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NUMERO_PAG++;
                etiquetaNum.setText("Página: " + NUMERO_PAG);
            }
        });
    }

    private void cargarImagen() {
        int fila = tablaDatos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado una imagen.");
        } else {
            ControladorSistema.getInstance().saveImagen(new Imagen(Integer.parseInt(
                    (String) tablaDatos.getValueAt(fila, 0)),
                    (String) tablaDatos.getValueAt(fila, 1),
                    (String) tablaDatos.getValueAt(fila, 2),
                    (String) tablaDatos.getValueAt(fila, 3),
                    (String) tablaDatos.getValueAt(fila, 4)));
            GUIVisor.main(null);
        }
    }

    private void buscar() {
        String[][] datos;

        String busq = barraBusquedaField.getText();

        if(barraBusquedaField.getText().contains(" ")){
            busq = barraBusquedaField.getText().replace(" ", "+");
        }

        if (busq.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se ha ingresado una búsqueda.");
        } else {
            try{
                datos = ControladorSistema.getInstance().obtenerResultados(busq, selectorComboBox.getSelectedIndex(), NUMERO_PAG);

                if(datos.length == 0){
                    JOptionPane.showMessageDialog(null, "No se encontraron resultados.");
                }else{
                    tablaDatos.setModel(new DefaultTableModel(datos,cabeceras));
                }
            } catch (SistemaExcepcionesAPP e) {
                JOptionPane.showMessageDialog(null, "Error al obtener resultados. " + e.getMessage());
            }
        }
    }

    private void onCancel() {
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        GUIPrincipal dialog = new GUIPrincipal();
        dialog.setTitle("Visor de imagenes para Konachan.com");
        dialog.setPreferredSize(new Dimension(1366, 768));
        dialog.pack();
        dialog.setVisible(true);
    }
}
