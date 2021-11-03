/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kode;

/**
 *
 * @author Richie-PC
 */
public class Insert implements Interfacenya{
    private String nama;
    private String npm;
    Koneksi kd= new Koneksi();
    
    public void setNama(String nama){
        this.nama= nama;
    }
    public void setNpm(String npm){
        this.npm = npm;
    }
    public String getNama(){
        return nama;
    }
    public String getNpm(){
        return npm;
    }
    public void setData(){
        String data = "INSERT into tbook(npm,nama) values('"+this.npm+"','"+this.nama+"')";
        kd.insertData(data);
    }
}
