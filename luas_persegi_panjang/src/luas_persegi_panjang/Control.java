/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luas_persegi_panjang;

/**
 *
 * @author Richie-PC
 */
public class Control {
    private Model model;
    private View view;
    
    public Control(Model model, View view){
        this.model = model;
        this.view = view;
    }
    public void setNilaiPanjang(int panjang){
        model.setPanjang(panjang);
    }
    public int getNilaiPanjang(){
        return model.getPanjang();
    }
    public void setNilaiLebar(int lebar){
        model.setLebar(lebar);
    }
    public int getNilaiLebar(){
        return model.getLebar();
    }
    public void setNilaiLuas(int luas){
        model.setLuas(luas);
    }
    public void updateView(){
        view.printLuasDetails(model.getPanjang(), model.getLebar(), model.getLuas());
    }
}
