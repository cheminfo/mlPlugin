/**
 * Name: Gene
 * Goal: abstract class to represent a gene (individual) in a population. This
 *       class ensures compatibility with the genetic algorithm
 * Attributes:
 *      - dna: tridimensional matrix of doubles to represent  genome
 *      - fitness: indicates the gene's fitness value
 * Methods:
 *      - mutate: enables a gene to mutate
 *      - crossover: enables a gene to cross its DNA with another
 *      - makeRandom: generates a random dna
 * @author Numa Trezzini
 */

package org.cheminfo.scripting.JFuge.CoEvolution;

public abstract class Gene {
    
    protected Double[][][] dna;
    
    protected double fitness;
    
    //prototype for mutation method
    abstract void mutate();
    
    //prototype for crossover method
    abstract Gene[] crossover(Gene companion);
    
    //prototype for random construction method
    abstract void makeRandom(int line_length, int column_length);
    
    public void setFitness(double fitness){
        this.fitness = fitness;
    }/*end setFitness*/
    
    public double getFitness(){return fitness;}/*end getFitness*/

    public Double[][][] getDNA() {
        return dna;
    }/*end getDNA*/

    public void setDNA(Double[][][] dna) {
        this.dna = dna;
    }/*end setDNA*/
    
    public String toString(){
        String str = "DNA:\n[";
        for(int i = 0; i < dna.length; i++){
            str += "[";
            for(int j = 0; j < dna[i].length; j++){
                str += "[";
                for(int k = 0; k < dna[i][j].length; k++){
                    str += dna[i][j][k]+", ";
                }
                str += "],";
            }
            str += "]\n";
        }
        return str+"];";
    }/*end toString*/
}/*end Gene*/
