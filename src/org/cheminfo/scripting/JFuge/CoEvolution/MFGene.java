/**
 * Name: MFGene
 * Goal: extends a Gene to represent the membership functions associated with
 *       each variable of a fuzzy system. Each line is a variable, and each
 *       column is a zero point of the MF. To ensure compatibility with the Gene
 *       abstract class, the thrid dimanesion of the DNA only contains one element
 * Methods:
 *      - mutate: changes a random cell of the DNA
 *      - crossover: copies and swaps DNA of two individuals from a random point
 *                   in the DNA.
 *      - makeRandom: attributes random (valid) values to the gene's DNA
 * @see: Gene
 * @author: Numa Trezzini
 */
package org.cheminfo.scripting.JFuge.CoEvolution;

import org.cheminfo.scripting.Utils.Tools;


public class MFGene extends Gene{
    
    private double[][] min_max;
    
    private int ante_count;
    
    //private double max;
    
    public MFGene(double[][] min_max, int ante_count){
        this.ante_count = ante_count;
        this.min_max = min_max;
    }/*end MFGene*/
    
    /**
     * Name: mutate
     * Goal: mutates a MF gene randomly. Only one cell is modified
     */
    void mutate(){
        int line_index = Tools.randomInt(0, dna.length-1);
        int column_index = Tools.randomInt(0, dna[0].length-1);
        if(line_index < ante_count)
            dna[line_index][column_index][0] = Tools.randomDouble(min_max[line_index][0], min_max[line_index][1]);
        else{
            int column = Math.random()<0.5?0:1;
            dna[line_index][column][0] = Tools.randomDouble(min_max[line_index][0], min_max[line_index][1]);
        }
    }/*end mutate*/
    
    /**
     * Name: crossover
     * Goal: allows crossing of two MF genes into two children. First, the genes
     *       are copied to prevent loss of parents in the original pool. A random
     *       line and column are selected. DNA is copied from the parents to their
     *       child until the randomly chosen point. From there on, DNA values are
     *       swapped between both individuals
     * @param companion: the companion of this gene for breeding
     * @return Gene[]: both children
     */
    Gene[] crossover(Gene companion){
        //create new genes to prevent messing with parents
        Gene first = new MFGene(min_max, ante_count);
        first.setDNA(new Double[dna.length][dna[0].length][1]);
        Gene second = new MFGene(min_max, ante_count);
        second.setDNA(new Double[dna.length][dna[0].length][1]);
        int line_index = Tools.randomInt(0, dna.length-1);
        int column_index = Tools.randomInt(0, dna[0].length-1);
        //copy first part of dna
        for(int i = 0; i<line_index; i++){
            for(int j = 0; j < dna[0].length; j++){
                first.dna[i][j][0] = this.dna[i][j][0];
                second.dna[i][j][0] = companion.dna[i][j][0];
            }
        }
        //complete cut dna line
        for(int i = 0; i < column_index; i++){
            first.dna[line_index][i][0] = this.dna[line_index][i][0];
            second.dna[line_index][i][0] = companion.dna[line_index][i][0];
        }
        for(int i = column_index; i < dna[line_index].length; i++){
            first.dna[line_index][i][0] = companion.dna[line_index][i][0];
            second.dna[line_index][i][0] = this.dna[line_index][i][0];
        }
        //swap latter part of dna
        for(int i = line_index+1; i<dna.length; i++){
            for(int j = 0; j < dna[0].length; j++){
                first.dna[i][j][0] = companion.dna[i][j][0];
                second.dna[i][j][0] = this.dna[i][j][0];
            }
        }
        Gene[] result = {first, second};
        return result;
    }/*end crossover*/
    
    /**
     * Name: makeRandom
     * Goal: gives the DNA random values. used as a base for evolution
     * @param line_number: number of lines in the DNA (the number of variables)
     * @param column_number: number of columns in the DNA (the number of terms a MF may take)
     */
    void makeRandom(int line_number, int column_number){
        dna = new Double[line_number][column_number][1];
        //antecedent random
        for(int i = 0; i < ante_count; i++){
            for(int j = 0; j < column_number; j++){
                dna[i][j][0] = Tools.randomDouble(min_max[i][0], min_max[i][1]);
            }
        }
        //consequent random
        for(int i = ante_count; i < line_number; i++){
            for(int j = 0; j < column_number; j++){
                if(j < 2)
                    dna[i][j][0] = Tools.randomDouble(min_max[i][0], min_max[i][1]);
                else
                    dna[i][j][0] = null;
            }
        }
    }/*end makeRandom*/
    
}/*end MFGene*/
