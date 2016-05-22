package com.key.signature.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pyshankov on 24.04.2016.
 */
public class MathUtil {

    public static String textToVerify = "I certify this action as my own original act in accordance with site Honor Code.";

    //Mi
    public static double expectedValue(List<Double> list){
        return  list.stream().reduce(0d,(a,b)->a+b)/list.size();
    }

    //Si^2
    public static double dispersion(List<Double> list,double expVal){
        return list.stream().reduce(0d,(a,b)-> a + (b-expVal)*(b-expVal) )/(list.size()-1);
    }

    //sqrt(Si^2)
    public static double mediumArithmeticDeviation(double dispersion){
        return Math.sqrt(dispersion);
    }

    //Yi,Mi,Si
    public static double coefficientStudent(double val,double expVal,double dispersion){
        return Math.abs((val-expVal)/dispersion);
    }

    public static List<Double> removeElem(List<Double> list, int index){
        List<Double> res = new ArrayList<>();
        for (int i = 0 ; i < list.size()-1; i++){
            if(i!=index) res.add(list.get(i));
        }
        return  res;
    }

    //only for text length of 10
    public static List<Double> filterError(List<Double> list){
        List<Double> result = new ArrayList<>();

        for (int i = 0 ; i < list.size()-1 ; i++){

            List mediumRes = removeElem(list,i);
            double Mi = expectedValue(mediumRes);
            double dispersion = dispersion(mediumRes,Mi);
            double dis = mediumArithmeticDeviation(dispersion);
            double tp = coefficientStudent(list.get(i),Mi,dis);

            if(Mi - 3*mediumArithmeticDeviation(dispersion)<=list.get(i) ||
                    Mi + 3*mediumArithmeticDeviation(dispersion)>=list.get(i) ) result.add(list.get(i));
        }

        return result;
    }

    //D
    public static double sampleVariance(List<Double> list){
        double medium = expectedValue(list);
        return list.stream().reduce(0d,(a,b)->a + (b-medium)*(b-medium) )/(list.size()-1);
    }


    public static Double getTheoreticalFisher(int n){
        Map<Integer,Double> theorFisher = new HashMap<>();
        theorFisher.put(1,12.706);
        theorFisher.put(2,4.3027);
        theorFisher.put(3,3.1825);
        theorFisher.put(4,2.7764);
        theorFisher.put(5,2.5706);
        theorFisher.put(6,2.4469);
        theorFisher.put(7,2.3646);
        theorFisher.put(8,2.3060);
        theorFisher.put(9,2.2622);
        theorFisher.put(10,2.2281);
        theorFisher.put(11,2.2010);
        theorFisher.put(12,2.1788);
        theorFisher.put(13,2.1604);
        theorFisher.put(14,2.1448);
        theorFisher.put(15,2.1315);
        theorFisher.put(16,2.1199);
        theorFisher.put(17,2.1098);
        theorFisher.put(18,2.1009);
        theorFisher.put(19,2.0930);
        theorFisher.put(20,2.0860);
        theorFisher.put(21,2.0796);
        theorFisher.put(22,2.0739);
        theorFisher.put(23,2.0687);
        theorFisher.put(24,2.0639);
        theorFisher.put(25,2.0595);
        theorFisher.put(26,2.0555);
        theorFisher.put(27,2.0518);
        theorFisher.put(28,2.0484);
        theorFisher.put(29,2.0452);
        theorFisher.put(30,2.0423);
//40
        theorFisher.put(40,2.0211);
//60
        theorFisher.put(60,2.0003);
        if( n >= 60){
            return theorFisher.get(60);
        }else if(n>=40){
            return theorFisher.get(40);
        }else if(n>=30){
            return theorFisher.get(30);
        }else {
            return theorFisher.get(n);
        }
    }


    public static double getTheoreticalStudent(int n){
        Map<Integer,Double> theorStudent = new HashMap<>();
        theorStudent.put(10,2.2281389);
        theorStudent.put(20,2.0859634);
        theorStudent.put(30,2.0422725);
        theorStudent.put(40,2.0210754);
        theorStudent.put(50,2.0085591);
        theorStudent.put(60,2.0002978);
        theorStudent.put(70,1.9944371);
        if(n >=70) return theorStudent.get(70);
        else if(n >= 60) return theorStudent.get(60);
        else if(n >= 50) return theorStudent.get(50);
        else if(n >= 40) return theorStudent.get(40);
        else if(n >= 30) return theorStudent.get(30);
        else if(n >= 20) return theorStudent.get(20);
        else if(n >= 10) return theorStudent.get(10);
        else             return theorStudent.get(10);
    }

}
