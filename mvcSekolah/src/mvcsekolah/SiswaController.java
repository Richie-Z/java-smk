/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvcsekolah;

/**
 *
 * @author Richie-PC
 */
public class SiswaController {
    private Siswa model;
    private SiswaView view;
    public SiswaController(Siswa model,SiswaView view){
        this.model = model;
        this.view = view;
    }
    public void setSiswaNama(String n){
        model.setNama(n);
    }
    public String getSiswaNama(){
        return model.getNama();
    }
     public void setSiswaNoDaftar(String n){
        model.setNoDaftar(n);
    }
    public String getSiswaNoDaftar(){
        return model.getNoDaftar();
    }
    public void updateView(){
        view.printSiswaDetails(model.getNama(),model.getNoDaftar());
    }
    
}
