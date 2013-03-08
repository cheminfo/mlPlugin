/**
 * Name: MembershipFunction
 * Goal: represents the membership function of a variable. It is used for attributing
 *       quantities of membership to linguistic variables. It is composed of a variable,
 *       a minimum and maximum value this variable may take, and the points where the
 *       individual membership functions start.
 * Methods:
 *      - eval: computes, for each individual function, the quantity of membership
 *        the data posesses. Because of the restrictions specified in the conception,
 *        a data instance may only be a member two functions at a time
 *      - getters and setters: all above specified elements have a get and set method
 * @author Numa Trezzini
 */
package org.cheminfo.scripting.JFuge.FuzzyLogic;

import java.util.Arrays;


public class MembershipFunction {
    
    //Variable used for MF
    private Variable var;
    
    //minimum value for membership function
    private double min_val;
    
    //maximum value for membership function
    private double max_val;
    
    //beginnings of MFs
    private double[] zero_points;
    
    /**
     * Name: MembershipFunction
     * Goal: creates a membership function associated with given variable.
     *       MFs are caracterized by min and max value, as well as the points where the functions are 0
     * @param var: variable attached to this MF
     * @param min: minimum value the MF may take
     * @param max: maximum value the MF may take
     * @param zero_points: the points where the functions are 0
     */
    public MembershipFunction(Variable var, double min, double max, double[] zero_points){
        this.var = var;
        this.min_val = min;
        this.max_val = max;
        this.zero_points = zero_points;
        Arrays.sort(this.zero_points);
    }/*end MembershipFunction*/
    
    /**
     * Name: MembershipFunction
     * Goal: creates a default binary or triple MF
     * @param is_binary: indicates if MF is binary or triple
     */
    public MembershipFunction(boolean is_binary){
        this.var = null;
        this.min_val = Double.MIN_VALUE;
        this.max_val = Double.MAX_VALUE;
        if(is_binary){
            zero_points = new double[2];
            zero_points[0] = 0.3;
            zero_points[1] = 0.7;
        }
        else{
            zero_points = new double[3];
            zero_points[0] = 0.3;
            zero_points[1] = 0.5;
            zero_points[2] = 0.7;
        }
    }
    
    /**
     * Name: eval
     * Goal: evaluates the functions of the MF according to given value
     * @param z: the value of the data instance
     * @return double[]: the activation of each function. same length as number
     *                   of terms for associated variable
     */
    public double[] eval(double z){
        
        //creation of array, same length as number of MFs
        double[] eval = new double[zero_points.length];
        double value;
        //evaluation of MF according to input data (z)
        //if data is before first triangular MF, inverse sigma equals 1, others equal 0
        if(z < zero_points[0]){
            eval[0] = 1;
            return eval;
        }
        //if data is after last triangular MF, sigma equals 1, others equal 0
        else if(z >= zero_points[zero_points.length-1]){
            eval[eval.length-1] = 1;
            return eval;
        }
        //data is in one of the triangular functions
        else{
            //test each triangular function
            for(int i = 0; i < zero_points.length-1; i++){
                //test if data is in current MF range
                if(z >= zero_points[i] && z < zero_points[i+1]){
                    value = triangleEval(z, zero_points[i], zero_points[i+1]);
                    //we're in the descent of inverse sigma and ascent of first triangular function
                    if(i == 0){
                        //inverse sigma value
                        eval[0] = 1-value;
                        //triangular function
                        eval[1] = value;
                    }
                    //we're in the descent of last triangluar function and ascent of sigma function
                    else if(i == zero_points.length-2){
                        //triangular function
                        eval[i] = 1-value;
                        //sigma function
                        eval[i+1] = value;
                    }
                    //we're in two of the triangular functions
                    else{
                        //since triangleEval always computes an ascent, function i+1 will be computed
                        //therefore, eval[i] is the complement to one
                        eval[i] = 1-value;
                        eval[i+1] = value;
                    }
                    //since only two functions can be activated at a point, return values once 
                    return eval;
                }
            }
        }
        //if code reaches this point, there is an error
        return null;
    }/*end eval*/
    
    /**
     * Name: triangleEval
     * Goal: computes the value of a rising slope for a triangular (or sigma) functions
     * @param data: the value of the data instance for the variable
     * @param start: start point of slope
     * @param end: end point of slope
     * @return double: the y-value of the function
     */
    private double triangleEval(double data, double start, double end){
        return (data-start)/(end-start);
    }

    /**
     * Name: getMaxVal
     * @return double: indicates the maximum x-value the data may take
     */
    public double getMaxVal() {
        return max_val;
    }/*end getMaxVal*/

    /**
     * Name: setMaxVal
     * @param max_val: the new maximum x-value the data may take
     */
    public void setMaxVal(double max_val) {
        this.max_val = max_val;
    }/*end setMaxVal*/

    /**
     * Name: getMinVal
     * @return double: indicates the minimum x-value the data may take
     */
    public double getMinVal() {
        return min_val;
    }/*end getMinVal*/

    /**
     * Name: setMinVal
     * @param min_val: the new minimum x-value the data may take
     */
    public void setMinVal(double min_val) {
        this.min_val = min_val;
    }/*end setMinVal*/

    /**
     * Name: getVar
     * @return Variable: indicates the variable this MF is associated to
     */
    public Variable getVar() {
        return var;
    }/*end getVar*/

    /**
     * Name: setVar
     * @param var: the new variable associated to this MF
     */
    public void setVar(Variable var) {
        this.var = var;
    }/*end setVar*/

    /**
     * Name: getZeroPoints
     * @return double[]: indicates the points where the functions are 0
     */
    public double[] getZeroPoints() {
        return zero_points;
    }/*end getZeroPoints*/

    /**
     * Name: setZeroPoints
     * @param zero_points: the new points where the functions are 0
     */
    public void setZeroPoints(double[] zero_points) {
        this.zero_points = zero_points;
        Arrays.sort(this.zero_points);
    }/*end setZeroPoints*/
    
}/*end MembershipFunction*/
