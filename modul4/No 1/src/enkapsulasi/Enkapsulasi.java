/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enkapsulasi;

/**
 *
 * @author Richie
 */
public class Enkapsulasi {
private String nama;
private String nickname;
private String nis;
private String jurusan;
private String ttl;
private String email;
private String nomerhp;

public Enkapsulasi(String nama, String nickname, String nis, String jurusan, String ttl, String email, String nomerhp){
    this.nama=nama;
    this.nickname= nickname;
    this.nis= nis;
    this.jurusan= jurusan;
    this.ttl= ttl;
    this.email= email;
    this.nomerhp= nomerhp;
}
public String tampilkanNama(){
    return nama;
}
public String tampilkanNamaPangilan (){
    return nickname;
}
public String tampilkanNIS(){
    return nis;
}
public String tampilkanJurusan(){
    return jurusan;
}
public String tampilkanTTL(){
    return ttl;
}
public String tampilkanEmail(){
    return email;
}
public String tampilkanNomerHP(){
    return nomerhp;
}
}


