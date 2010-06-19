/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jgap.ga.fitness;

import java.util.ArrayList;
import java.util.List;
import org.jgap.ga.constant.Constant;
import org.jgap.ga.service.Container;
import org.jgap.ga.model.Ship;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.impl.IntegerGene;
import org.jgap.ga.constant.Parameter;
import org.apache.log4j.Logger;
/**
 *
 * @author Tabiul Mahmood
 */
public class ContainerFitnessFunction extends FitnessFunction{
    private static Logger logger=Logger.getLogger(ContainerFitnessFunction.class);
    private List<Container>containerList;
    private Ship ship;
    private int fitnessfunction;
    public ContainerFitnessFunction(List<Container> containerList,Ship ship,int fitnessfunction){
        this.ship=ship;
        this.containerList=containerList;
        this.fitnessfunction=fitnessfunction;
    }
    /**
     * <p> Constraints <p>
     * <b>Soft Contraint</b>
     * </br>
     * 
     * @param arg0
     * @return
     */
    @Override
    protected double evaluate(IChromosome chromosome) {
        List<Container> list=new ArrayList();
        //build up the container arrangment based upon the Chromosome
        for(int i=0;i<chromosome.size();i++){
            IntegerGene containerGene=(IntegerGene)chromosome.getGene(i);
            int containerId=containerGene.intValue();
            list.add(this.containerList.get(containerId));
        }
        
        
        //load the ship with the new container arragnment
        ship.LoadShip(list);
        
        // get fitness based on uniform weight distribution
        double fitnessvalue=0.0d;

        switch(this.fitnessfunction){
            case Constant.FITNESS_FUNC_NORMAL: fitnessvalue=ship.CalculateWeightFitness(); break;
            case Constant.FITNESS_FUNC_SQUARE: fitnessvalue=ship.CalculateWeightFitnessSquare(); break;
            case Constant.FITNESS_FUNC_LOG: fitnessvalue=ship.CalculateWeightFitnessLogarithm(); break;
            default:ship.CalculateWeightFitnessSquare(); break;
        }
            
        
        
        
        // add penalty for having heavier Container on top of lighter ones
        // multiple the number of violations with a large number to make it look
        // like hard constraints
        fitnessvalue+=ship.GetNoOfHeavierViolations()*ship.GetAverageWeight()*5000;
        //add penalty for unloading violations
        
        fitnessvalue+=(ship.GetNoOfUnloadingViolations()*ship.GetAverageWeight())/5;

        //add penalty for flammable index
        fitnessvalue+=ship.CalculateFlammableIndex();
        
        return fitnessvalue;
        
    }

    
}
