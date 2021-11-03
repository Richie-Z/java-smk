/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author Richie
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       int[] data={21,13,36,12,18,9,59,24};
       int temp;
       for (int i=1;i<data.length;i++){
        for (int j=data.length-1;j>=i;j–){
         if (data[j]<data[j-1]){
            temp=data[j];
            data[j]=data[j-1];
            data[j-1]=temp;
            }
        }
       }
       for (int i=0;i<data.length;i++)
        System.out.print(data[i]+” “);
        }
    }
    

