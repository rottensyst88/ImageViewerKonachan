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

        tablaDatos.setModel(new DefaultTableModel(null, cabeceras));
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
                if (!barraBusquedaField.getText().equals("")){
                    if (NUMERO_PAG == 1) {
                        JOptionPane.showMessageDialog(null, "No hay más páginas anteriores.");
                    } else {
                        NUMERO_PAG--;
                    }

                    etiquetaNum.setText("Página: " + NUMERO_PAG);

                    buscar();
                }
            }
        });
        pSiguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!barraBusquedaField.getText().equals("")){
                    NUMERO_PAG++;
                    etiquetaNum.setText("Página: " + NUMERO_PAG);
                    buscar();
                }
            }
        });
    }

    private void cargarImagen() {
        int fila = tablaDatos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado una imagen.");
        } else {

            System.out.println("Cargando imagen...");

            final JDialog loadingDialog = new JDialog();
            loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            loadingDialog.setModal(true);
            loadingDialog.setUndecorated(true);
            loadingDialog.add(new JLabel("Espere mientras se carga la imagen...", SwingConstants.CENTER));
            loadingDialog.setSize(300, 100);
            loadingDialog.setLocationRelativeTo(this);

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    ControladorSistema.getInstance().saveImagen(new Imagen(Integer.parseInt(
                            (String) tablaDatos.getValueAt(fila, 0)),
                            (String) tablaDatos.getValueAt(fila, 1),
                            (String) tablaDatos.getValueAt(fila, 2),
                            (String) tablaDatos.getValueAt(fila, 3),
                            (String) tablaDatos.getValueAt(fila, 4)));

                    System.out.println("Foto cargada exitosamente.");

                    GUIVisor.main(null);
                    return null;
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();

                }
            };

            worker.execute();
            loadingDialog.setVisible(true);
        }
    }

    private void buscar() {
        String[][] datos;

        String busq = barraBusquedaField.getText();

        if (barraBusquedaField.getText().contains(" ")) {
            busq = barraBusquedaField.getText().replace(" ", "+");
        }

        if (busq.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se ha ingresado una búsqueda.");
        } else {
            try {
                datos = ControladorSistema.getInstance().obtenerResultados(busq, selectorComboBox.getSelectedIndex(), NUMERO_PAG);

                if (datos.length == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontraron resultados.");
                    NUMERO_PAG--;
                } else {
                    tablaDatos.setModel(new DefaultTableModel(datos, cabeceras));
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
