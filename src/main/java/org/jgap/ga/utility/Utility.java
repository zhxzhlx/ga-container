/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jgap.ga.utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jgap.ga.constant.Parameter;
import org.jgap.ga.service.Container;
import org.apache.log4j.Logger;
/**
 *
 * @author Tabiul Mahmood
 */
public class Utility {
        static Logger logger=Logger.getLogger(Utility.class);
    
    public static List<Container>CreateContainerList(){
        Random random=new Random();
        List<Container> list=new ArrayList();
        logger.info("Default gene type");
        for(int i=0;i<Parameter.CONTAINER_NO;i++){
            Container container=new Container();
            container.setContainerId(i);
            container.setWeight(round(random.nextDouble()*40,2));
            container.setPort(random.nextInt(3)+1);
            container.setFlammableindex(round(random.nextDouble(),1));
            logger.info("Gene=>"+i);
            logger.info("Weight=>"+container.getWeight());
            list.add(container);
        }
        logger.info("End Initial Config");
        return list;
    }
    
    public static double round(double d,int decimalPlace){
        BigDecimal bd=new BigDecimal(Double.toString(d));
        bd=bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
}
