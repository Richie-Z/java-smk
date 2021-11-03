package mvc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mvc.Model.*;
import mvc.View.*;

public class Controller {
    private Model model;
    private View view;
    private ActionListener actionListener;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void control() {
        actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                linkBtnAndLabel();
            }
        };
        view.getButton().addActionListener(actionListener);
    }

    private void linkBtnAndLabel() {
        model.incX();
        view.setText(Integer.toString(model.getX()));
    }
}
