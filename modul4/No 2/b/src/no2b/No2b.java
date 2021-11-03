/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no2b;

/**
 *
 * @author Richie
 */
public class No2b {
 public String nama;
    public No2b(String n){
        this.nama = n;
    }
  public String tampilkanNama(){
      return nama;
  }
  protected void makan (){
      System.out.println("Nyam...nyam...nyam...");
  }
  private void bunuhDiri (){
      System.out.println("Dor...bruk....");
  }
    }
