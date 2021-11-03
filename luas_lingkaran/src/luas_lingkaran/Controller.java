/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luas_lingkaran;

/**
 *
 * @author Richie-PC
 */
public class Controller {
    private Model model;
    private View view;
    public Controller(Model model,View view){
        this.model = model;
        this.view =view;
    }
    public void setDiameter(int D){
        model.setDiameter(D);
    }
    public int getDiameter(){
        return model.getDiameter();
    }
    public void setNilaiLuas(Double L){
        model.setLuas(L);
    }
    public void updateView(){
        view.printLuasDetails(model.getDiameter(), model.getLuas());
    }
}
