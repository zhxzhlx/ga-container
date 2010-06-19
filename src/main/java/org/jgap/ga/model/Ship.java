/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jgap.ga.model;

import java.util.ArrayList;
import java.util.List;
import org.jgap.ga.constant.Parameter;
import org.jgap.ga.service.Container;
import org.jgap.ga.utility.Utility;
import org.apache.log4j.Logger;
import org.jgap.ga.service.Container;
/**
 *
 * @author Tabiul Mahmood
 */
public class Ship {
    static Logger logger=Logger.getLogger(Ship.class);
    private List<Container> containerList;
    public final int NO_COLUMNS=Parameter.CONTAINER_NO/Parameter.LEVEL_NO;    
    public Ship(){
        containerList=new ArrayList();
    }
    /**
     * Load the Ship with Container. The List is HashMap of Container
     * where the key is the location of the container
     * @param listContainer
     */
    public void LoadShip(List<Container> listContainer) {
        containerList.clear();
        containerList.addAll(listContainer);
    }

    public List<Container>GetAllContainers(){
        return this.containerList;
    }
    
    private List<Container>GetContainerPerColumn(int col){
        List<Container> list=new ArrayList();
        int index=col;
        for(int i=0;i<Parameter.LEVEL_NO;i++){
          list.add(containerList.get(index));
          index+=NO_COLUMNS;
        }
        return list;
    }
    
    public double GetAverageWeight(){
        double weight=0.0d;
        for(Container container:containerList){
            weight+=container.getWeight();
        }
        return Utility.round(weight/containerList.size(),2);
    }
    
    public double GetColumnAverageWeight(int col){
        List<Container> list=GetContainerPerColumn(col);
        double weight=0.0d;
        
        for(Container container:list){
            weight+=container.getWeight();
        }
        return weight/list.size();
            
    }
    
    public int GetNoOfHeavierViolations(){
        int violations=0;
        for(int i=0;i<NO_COLUMNS;i++){
            List<Container> list=GetContainerPerColumn(i);
            for(int j=0;j<Parameter.LEVEL_NO;j++){
                if(j!=Parameter.LEVEL_NO-1){
                    if(list.get(j+1).getWeight()>list.get(j).getWeight()){
                        violations++;
                    }
                }
            }
            
        }
        return violations;
    }
    
    public int GetNoOfUnloadingViolations(){
        int violations=0;
        for(int i=0;i<NO_COLUMNS;i++){
            List<Container> list=GetContainerPerColumn(i);
            for(int j=0;j<Parameter.LEVEL_NO;j++){
                if(j!=Parameter.LEVEL_NO-1){
                    if(list.get(j+1).getPort()>list.get(j).getPort()){
                        violations++;
                    }
                }
            }
            
        }
        return violations;
    }
    
    public double CalculateWeightFitnessSquare(){
        double averageWeight=GetAverageWeight();
        double diff=0.0d;
        double averageColumWeight=0.0d;
        for (int i=0;i<NO_COLUMNS;i++)
      	{
            averageColumWeight=GetColumnAverageWeight(i);
            diff+=Math.pow(averageColumWeight-averageWeight,2);
      	}    	
      	return diff;
    }
    
    public double CalculateWeightFitnessLogarithm(){
        double averageWeight=GetAverageWeight();
        double diff=0.0d;
        double averageColumWeight=0.0d;
        for (int i=0;i<NO_COLUMNS;i++)
      	{
            averageColumWeight=GetColumnAverageWeight(i);
            diff+=Math.abs(averageColumWeight-averageWeight);
      	}
      	return diff==0?0:Math.log(diff);
    }
    
    public double CalculateWeightFitness(){
        double averageWeight=GetAverageWeight();
        double diff=0.0d;
        double averageColumWeight=0.0d;
        for (int i=0;i<NO_COLUMNS;i++)
      	{
            averageColumWeight=GetColumnAverageWeight(i);
            diff+=averageColumWeight-averageWeight;
      	}    	
      	return diff;
    }
    
    public double CalculateFlammableIndex(){
        double flammableValue=0.0d;
        for (int i=0;i<NO_COLUMNS;i++){
            List<Container> list=GetContainerPerColumn(i);
            for(Container container:list){
                flammableValue+=(1-container.getFlammableindex())* (Parameter.DISTANCE+(2*(i+1)));
            }
        }
        return flammableValue;
    }
}
