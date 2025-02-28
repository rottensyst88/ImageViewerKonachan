package vista;

import controlador.ControladorSistema;
import excepciones.SistemaExcepcionesAPP;
import modelo.Imagen;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;


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
    private JLabel fotoVistaPrevia;
    private JLabel formatoImagen;
    private JLabel tagsImagen;

    private final String[] cabeceras = {"ID", "URL de la imagen", "Etiquetas", "Calificación", "Fuente"};
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
                if (!barraBusquedaField.getText().isEmpty()){
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
                if (!barraBusquedaField.getText().isEmpty()){
                    NUMERO_PAG++;
                    etiquetaNum.setText("Página: " + NUMERO_PAG);
                    buscar();
                }
            }
        });
        tablaDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Resultado clickeado!");
                int fila = tablaDatos.getSelectedRow();
                String url = (String) tablaDatos.getValueAt(fila, 1);
                System.out.println("URL: " + url);

                Imagen imagenVistaPrevia = new Imagen(Integer.parseInt((String) tablaDatos.getValueAt(fila,0)),
                        (String) tablaDatos.getValueAt(fila, 1),
                        (String) tablaDatos.getValueAt(fila, 2),
                        (String) tablaDatos.getValueAt(fila, 3),
                        (String) tablaDatos.getValueAt(fila, 4));

                setFotoVistaPrevia(imagenVistaPrevia);
            }
        });
    }

    private void cargarImagen(){
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

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws SistemaExcepcionesAPP {
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
                    etiquetaNum.setText("Página: " + NUMERO_PAG);

                } else {
                    tablaDatos.setModel(new DefaultTableModel(datos, cabeceras));
                }
            } catch (SistemaExcepcionesAPP e) {
                JOptionPane.showMessageDialog(null, "Error al obtener resultados. " + e.getMessage());
            }
        }
    }

    private void setFotoVistaPrevia(Imagen imgVista){
        Image img = imgVista.getImage();

        /*
        try{
            URL urlImagen = new URL(url);
            img = ImageIO.read(img.getImage());
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error al cargar la imagen.");
        }*/

        if (img != null) {
            int originalWidth = img.getWidth(null);
            int originalHeight = img.getHeight(null);
            double aspectRatio = (double) originalWidth / originalHeight;

            int newWidth = 350;
            int newHeight = (int) (newWidth / aspectRatio);

            if (newHeight > 350) {
                newHeight = 350;
                newWidth = (int) (newHeight * aspectRatio);
            }

            Image newImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(newImg);
            fotoVistaPrevia.setIcon(newIcon);

            formatoImagen.setText("Formato: " + imgVista.getFormato());

            String etiquetas = imgVista.getEtiquetas();
            if (etiquetas.length() > 50) {
                etiquetas = etiquetas.substring(0, 50) + "...";
            }

            tagsImagen.setText("Tags: " + etiquetas);
        }
    }

    private void onCancel() {
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        GUIPrincipal dialog = new GUIPrincipal();
        dialog.setResizable(false);
        dialog.setTitle("Visor de imagenes para Konachan.com");
        dialog.setPreferredSize(new Dimension(1366, 600));
        dialog.pack();
        dialog.setVisible(true);
    }
}
