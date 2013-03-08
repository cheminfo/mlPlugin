/**
 * Name: GeneEvolver
 * Goal: creates a class that may be used by a FutureTask as a thread. The
 *       advantage of implementing Callable is that the thread returns a value
 *       upon joining
 * Methods:
 *      - call: the method called upon starting the FutureTask using it. runs an
 *              iteration of the genetic algorithm
 * @author: Numa Trezzini
 */
package org.cheminfo.scripting.JFuge.CoEvolution;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import org.cheminfo.scripting.Utils.Tools;


public class GeneEvolver implements Callable<LinkedList<Gene>>{
    
    private GeneticEngine ge;
    
    public GeneEvolver(GeneticEngine ge){
        this.ge = ge;
    }/*end GeneEvolver*/
    
    public void setGeneticEngine(GeneticEngine ge){
        this.ge = ge;
    }/*end setGeneticEngine*/

    /**
     * Name: call
     * Goal: runs an iteration of a genetic algorithm
     * @return LinkedList<Gene>: the new generation of a population
     * @throws Exception 
     */
    @Override
    public LinkedList<Gene> call() throws Exception{

        //selects parents for the new generation
        ge.selection();

        LinkedList<Gene> children = new LinkedList<Gene>();
        //adds the elite the the next generation if there is any
        children.addAll(ge.getElite());
        
        //crosses parents until population is complete again
        Gene[] offspring;
        while(children.size() < ge.getPopSize()-1){
            offspring = ge.crossover(ge.getPopulation().remove(Tools.randomInt(0, ge.getPopulation().size()-1)), ge.getPopulation().remove(Tools.randomInt(0, ge.getPopulation().size()-1)));
            children.add(offspring[0]);
            children.add(offspring[1]);
        }
        //adds the fittest individual to the next generation if only one
        //individual misses to complete next generation
        if(children.size() == ge.getPopSize()-1){
            children.add(ge.getElite().getFirst());
        }
        //replaces the population by the children
        ge.setPopulation(children);
        

        //mutates the next generation
        for(int i = 0; i < ge.getPopSize(); i++)
            if(Tools.randomDouble(0, 1) < ge.getMutationRate())
                ge.mutate(i);
        return ge.getPopulation();
    }/*end call*/

}/*end GeneEvolver*/