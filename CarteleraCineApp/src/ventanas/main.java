/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ventanas;

public class main {
    public static void main(String[] args) {
        // Recomendado por buenas prácticas de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new login().setVisible(true); // Asegúrate de tener una clase Login.java
        });
    }
}
