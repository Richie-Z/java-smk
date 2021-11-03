/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgbreak;

/**
 *
 * @author Richie
 */
public class Break {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int i,j, All;
        for(i=1; i<=3;i++){
            for(j=1; j<=3;j++){
             All= i *j;
            if( i==2) break;
            System.out.println(i+"*"+j+ "="+All );
        }
        }
    }
    
}
