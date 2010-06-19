/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jgap.ga.service;

import java.util.ArrayList;
import java.util.List;
import org.jgap.ga.constant.Constant;
import org.jgap.ga.exception.GAException;
import org.jgap.ga.constant.Parameter;
import org.jgap.ga.fitness.ContainerFitnessFunction;
import org.jgap.ga.model.Ship;
import org.jgap.ga.utility.Utility;
import org.apache.log4j.Logger;
import org.jgap.*;
import org.jgap.event.EventManager;
import org.jgap.impl.*;

/**
 *
 * @author Tabiul Mahmood
 */
public class ExecuteGA {

    private int evolutions;
    private int evolutionsCompleted=0;
    private Genotype genotype;
    private Ship ship;
    private static Logger logger=Logger.getLogger(ExecuteGA.class);
    private List<Container> containerList;
    private int fitnessfunction;
    public ExecuteGA(int evolutions, List<Container> containerList,int fitnessfunction) throws InvalidConfigurationException{
        this.evolutions = evolutions-1;
        this.containerList=containerList;  
        this.fitnessfunction=fitnessfunction;
        SetupGAEngine();
    }

    public int GetEvolutionCount(){
        return this.evolutions;
    }
    
    public int GetEvolutionCompleted(){
        return this.evolutionsCompleted;
    }
    
    public int GetEvolutionRemaining(){
        return this.evolutions-this.evolutionsCompleted;
    }
    
    public void RunEvolution() throws GAException{
        if(evolutionsCompleted<=evolutions){
            genotype.evolve();
            evolutionsCompleted++;
        }else{
            throw new GAException("The number of evolutions is completed");
        }
    }

    
    
    public double GetFitnessValue() {
        return genotype.getFittestChromosome().getFitnessValue();
    }

    public IChromosome GetFittestChromosome() {
        return genotype.getFittestChromosome();
    }

    private void SetupGAEngine() throws InvalidConfigurationException {
        ship = new Ship();
        
        Configuration gaConf = new DefaultConfiguration();
        gaConf.reset();
        gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());
        gaConf.reset();
        gaConf.setFitnessFunction(new ContainerFitnessFunction(this.containerList, ship,this.fitnessfunction));
        gaConf.reset();
        gaConf.setKeepPopulationSizeConstant(false);
        gaConf.reset();
        gaConf.setPreservFittestIndividual(true);
        gaConf.reset();
        gaConf.setPopulationSize(Parameter.POPULATION_SIZE);
        SwappingMutationOperator swapper = new SwappingMutationOperator(gaConf);
        gaConf.getGeneticOperators().clear();
        gaConf.addGeneticOperator(swapper);
        IChromosome sampleChromosome = new Chromosome(gaConf, new IntegerGene(gaConf, 0, Parameter.CONTAINER_NO - 1), Parameter.CONTAINER_NO);
        gaConf.reset();
        gaConf.setSampleChromosome(sampleChromosome);
        genotype = Genotype.randomInitialGenotype(gaConf);

        // Now ensure that each number from 1..64 (representing the
        // indices of the vents) is represented by exactly one gene.
        // --> Suboptimal here, as randomized initialization becomes
        //     obsolete (other solution would be more complicated).
        // ---------------------------------------------------------
        List chromosomes = genotype.getPopulation().getChromosomes();
        for (int i = 0; i < chromosomes.size(); i++) {
            IChromosome chrom = (IChromosome) chromosomes.get(i);
            for (int j = 0; j < chrom.size(); j++) {
                Gene gene = (Gene) chrom.getGene(j);
                 gene.setAllele(new Integer(j));
            }
        }

    }
    
    public double getAverageWeight(){
        return ship.GetAverageWeight();
    }
    
    public double getUnformWeight(){
        double ret=0.0d;
        switch(fitnessfunction){
            case Constant.FITNESS_FUNC_NORMAL:ret= ship.CalculateWeightFitness(); break;
            case Constant.FITNESS_FUNC_SQUARE:ret= ship.CalculateWeightFitnessSquare(); break;
            case Constant.FITNESS_FUNC_LOG:ret= ship.CalculateWeightFitnessLogarithm(); break;
            default:ret= ship.CalculateWeightFitnessSquare(); break;
        }
        return ret;
    }
    
    public List<Container> getContainerList(){
        return this.containerList;
    }
    
    public List<Container> getBestContainerArrangment(){
         IChromosome chromosome=GetFittestChromosome();
         List<Container> arrangedList=new ArrayList();
         for(int j=0; j<chromosome.size();j++){
             IntegerGene containerGene=(IntegerGene)chromosome.getGene(j);
             int containerId=containerGene.intValue();
             arrangedList.add(containerList.get(containerId));
    }
         return arrangedList;
    }
    
    public double GetAverageFittestValue(){
        Population population=genotype.getPopulation();
        double totalFitnessValue=0.0d;
        for(int i=0;i<population.size();i++){
            totalFitnessValue+=population.getChromosome(i).getFitnessValue();
        }
        return Utility.round(totalFitnessValue/population.size(),2);
    }
}
