
package org.cheminfo.scripting.Utils;

import java.util.Random;

public class Tools {
    
   public static double min(double[] array){
        double min = array[0];
        for(int i = 1; i < array.length; i++){
            if(array[i] < min)
                min = array[i];
        }
        return min;
    }
    
    public static double max(double[] array){
        double max = array[0];
        for(int i = 1; i < array.length; i++){
            if(array[i] > max)
                max = array[i];
        }
        return max;
    }
    
    /**
     * Nom: randomDouble
     * @param min valeur minimum du generateur
     * @param max valeur maximum du generateur
     * @return int: un nombre aleatoire entre min et max
     */
    public static double randomDouble(double min, double max){
        return (Math.random()*(max-min))+min;
    }/*end randomDouble*/
    
    /**
     * Nom: randomInt
     * @param min valeur minimum du generateur
     * @param max valeur maximum du generateur
     * @return int: un nombre aleatoire entre min et max
     */
    public static int randomInt(int min, int max){
        //return (int)((Math.random()*((max+1)-min))+min);
        Random rand = new Random();
        return Math.min(min, max+1) + rand.nextInt(Math.abs((max+1) - min));
    }/*end randomInt*/
    
    
}
