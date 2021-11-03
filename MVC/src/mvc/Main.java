package mvc;

import javax.swing.SwingUtilities;
import mvc.Model.*;
import mvc.View.*;
import mvc.Controller.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Model model = new Model(1);
                View view = new View("-");
                Controller controller = new Controller(model, view);
                controller.control();
            }
        });
    }
}
