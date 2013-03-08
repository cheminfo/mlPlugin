/**
 * Name: RuleGene
 * Goal: extends a Gene to represent the rules associated with of a fuzzy system
 *       Each line is a rule, and each column is a term of the rule. The last
 *       column is always the consequent of the rule. The last line is always the
 *       default rule
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

public class RuleGene extends Gene{
    
    private int max_var_index;
    
    private int max_term_index;
    
    private int max_class_index;
    
    public RuleGene(int max_var_index, int max_term_index, int max_class_index){
        this.max_term_index = max_term_index;
        this.max_var_index = max_var_index;
        this.max_class_index = max_class_index;
    }/*end RuleGene*/
    
    /**
     * Name: mutate
     * Goal: randomly replaces a rule term (variable+term) in the DNA
     */
    void mutate(){
        //TODO remettre -2 si jamais
        int line_index = Tools.randomInt(0, dna.length-1);
        int column_index = Tools.randomInt(0, dna[0].length-1);
        //don't mutate default rule except for consequent
        if(line_index == dna.length-1)
            dna[line_index][dna[0].length-1][1] = (double)Tools.randomInt(0, 1);
        else if(column_index >= max_var_index-1)
            dna[line_index][column_index][1] = (double)Tools.randomInt(0, 1); 
        else
            dna[line_index][column_index][1] = (double)Tools.randomInt(-1, max_term_index-1);
    }/*end mutate*/
    
    /**
     * Name: crossover
     * Goal: allows crossing of two rule genes into two children. First, the genes
     *       are copied to prevent loss of parents in the original pool. A random
     *       line and column are selected. DNA is copied from the parents to their
     *       child until the randomly chosen point. From there on, DNA values are
     *       swapped between both individuals
     * @param companion: the individual to cross this gene with
     * @return Gene[]: both children resulting from crossover
     */
    Gene[] crossover(Gene companion){
        Gene first = new RuleGene(max_var_index, max_term_index, max_class_index);
        first.setDNA(new Double[dna.length][dna[0].length][2]);
        Gene second = new RuleGene(max_var_index, max_term_index, max_class_index);
        second.setDNA(new Double[dna.length][dna[0].length][2]);
        int line_index = Tools.randomInt(0, dna.length-1);
        int column_index = Tools.randomInt(0, max_var_index-1);
        //copy dna to prevent messing with parents
        for(int i = 0; i<line_index; i++){
            for(int j = 0; j < dna[0].length; j++){
                first.dna[i][j][0] = this.dna[i][j][0];
                first.dna[i][j][1] = this.dna[i][j][1];
                second.dna[i][j][0] = companion.dna[i][j][0];
                second.dna[i][j][1] = companion.dna[i][j][1];
            }
        }
        //complete cut line
        for(int i = 0; i < column_index; i++){
            first.dna[line_index][i][0] = this.dna[line_index][i][0];
            first.dna[line_index][i][1] = this.dna[line_index][i][1];
            second.dna[line_index][i][0] = companion.dna[line_index][i][0];
            second.dna[line_index][i][1] = companion.dna[line_index][i][1];
        }
        for(int i = column_index; i < dna[line_index].length; i++){
            first.dna[line_index][i][0] = companion.dna[line_index][i][0];
            first.dna[line_index][i][1] = companion.dna[line_index][i][1];
            second.dna[line_index][i][0] = this.dna[line_index][i][0];
            second.dna[line_index][i][1] = this.dna[line_index][i][1];
        }
        //swap remaining dna
        for(int i = line_index+1; i<dna.length; i++){
            for(int j = 0; j < dna[0].length; j++){
                first.dna[i][j][0] = companion.dna[i][j][0];
                first.dna[i][j][1] = companion.dna[i][j][1];
                second.dna[i][j][0] = this.dna[i][j][0];
                second.dna[i][j][1] = this.dna[i][j][1];
            }
        }
        Gene[] result = {first, second};
        return result;
    }/*end crossover*/
    
    /**
     * Name: makeRandom
     * Goal: gives random values to the gene, to work as a basis for evolution
     * @param line_number: number of lines in the DNA (number of rules)
     * @param column_number: number of columns in the DNA (number of variables in a rule)
     */
    void makeRandom(int line_number, int column_number){
        dna = new Double[line_number][column_number][2];
        for(int i = 0; i < line_number; i++){
            if(i != line_number-1){
                //input values
                for(int j = 0; j < max_var_index-1; j++){
                    dna[i][j][0] = (double)j;
                    dna[i][j][1] = (double)Tools.randomInt(-1, max_term_index-1);
                }
                //output calues
                for(int j = max_var_index-1; j < column_number; j++){
                    dna[i][j][0] = (double)max_var_index-1;
                    dna[i][j][1] = (double)Tools.randomInt(0, 1);
                }
            }
            //default rule has don't care values everyehre
            else{
                for(int j = 0; j < column_number-1; j++){
                    dna[i][j][0] = -1.0;
                    dna[i][j][1] = -1.0;
                }
                for(int j = max_var_index-1; j < column_number; j++){
                    dna[i][j][0] = (double)max_var_index-1;
                    dna[i][j][1] = (double)Tools.randomInt(0, 1);
                }
            }
        }
    }/*end makeRandom*/
    
}/*end RuleGene*/
