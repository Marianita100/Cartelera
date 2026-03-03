package ventanas;
import conexion.Conexion;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DetallesPeliculaFrame extends JFrame {
    private int idPelicula;
    private String usuarioActual; // Este es el usuario logueado correctamente
    private JTextArea txtResena;

    public DetallesPeliculaFrame(int idPelicula, String titulo, String clasificacion, String director,
                                  String actores, String duracion, String resumen, String usuarioActual) {
        this.idPelicula = idPelicula;
        this.usuarioActual = usuarioActual;

        setTitle(titulo);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("🎬 Título: " + titulo));
        panel.add(new JLabel("⭐ Clasificación: " + clasificacion));
        panel.add(new JLabel("🎥 Director: " + director));
        panel.add(new JLabel("🎭 Actores: " + actores));
        panel.add(new JLabel("⏱️ Duración: " + duracion));
        panel.add(new JLabel("📝 Resumen:"));

        JTextArea areaResumen = new JTextArea(resumen);
        areaResumen.setWrapStyleWord(true);
        areaResumen.setLineWrap(true);
        areaResumen.setEditable(false);
        panel.add(new JScrollPane(areaResumen));

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("✍️ Escribe tu reseña:"));
        txtResena = new JTextArea(5, 20);
        txtResena.setLineWrap(true);
        txtResena.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtResena));

        JButton btnEnviar = new JButton("Enviar Reseña");
        btnEnviar.addActionListener(e -> enviarResena());

        JButton btnVer = new JButton("Ver Reseñas");
        btnVer.addActionListener(e -> verResenas());

        panel.add(Box.createVerticalStrut(10));
        panel.add(btnEnviar);
        panel.add(Box.createVerticalStrut(5));
        panel.add(btnVer);

        add(panel);
    }

    private void enviarResena() {
        String contenido = txtResena.getText().trim();
        if (contenido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, escribe una reseña.");
            return;
        }

        if (usuarioActual == null || usuarioActual.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: usuario no válido.");
            return;
        }

        try (Connection con = Conexion.getConnection()) {
            String sql = "INSERT INTO resenas (id_pelicula, usuario, contenido, fecha) VALUES (?, ?, ?, NOW())";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idPelicula);
            ps.setString(2, usuarioActual); // 👈 Ahora correctamente se guarda el usuario
            ps.setString(3, contenido);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Reseña enviada correctamente.");
            txtResena.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al enviar reseña: " + e.getMessage());
        }
    }

    private void verResenas() {
        try (Connection con = Conexion.getConnection()) {
            String sql = "SELECT usuario, contenido, fecha FROM resenas WHERE id_pelicula = ? ORDER BY fecha DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idPelicula);
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("👤 ").append(rs.getString("usuario"))
                  .append(" (").append(rs.getString("fecha")).append("):\n")
                  .append(rs.getString("contenido")).append("\n\n");
            }

            if (sb.length() == 0) {
                sb.append("No hay reseñas para esta película.");
            }

            JOptionPane.showMessageDialog(this, sb.toString(), "Reseñas", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener reseñas: " + e.getMessage());
        }
    }
}