package org.pms.sammenu.utils;

import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumericUtils {

    private static final BigDecimal THOUSAND = new BigDecimal(1000);
    private static final BigDecimal MILLION = new BigDecimal(1000000);
    private static final BigDecimal BILLION = new BigDecimal(1000000000);

    //add null check for object "sales" when using buildSalesPreview
    public static String buildSalesPreview(BigDecimal sales) {

        String salesString = sales.toString();

        if (salesString.contains(".")) {
            salesString = salesString.substring(0, salesString.indexOf("."));
        }

        if (salesString.length() > 3 && salesString.length() <= 6) {
            return sales.divide(THOUSAND).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "k";
        } else if (salesString.length() > 6 && salesString.length() <= 9) {
            return sales.divide(MILLION).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "m";
        } else if (salesString.length() > 9) {
            return sales.divide(BILLION).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "bn";
        } else return sales.toString();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            if (strNum.contains(".")) {
                strNum = strNum.substring(0, strNum.indexOf("."));
            }
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static int numberGen(int digits) {

        long timeSeed = System.nanoTime(); // to get the current date time value

        double randSeed = Math.random() * 1000; // random number generation

        long midSeed = (long) (timeSeed * randSeed); // mixing up the time and rand number.

        // variable timeSeed
        // will be unique


        // variable rand will
        // ensure no relation
        // between the numbers

        String s = midSeed + "";
        String subStr = s.substring(0, digits);

        int finalSeed = Integer.parseInt(subStr); // integer value

        return finalSeed;
    }

    /*
    *
    * p  = πιθανότητα
    * am = αριθμητικός μέσος
    * s  = τυπική απόκλιση
    *
    * */
    public static double computeNormalDistributionFunction(double p, double am, double s) {
        if(p < 0 || p > 1)
            throw new RuntimeException("The probality p must be bigger than 0 and smaller than 1");
        if(s < 0)
            throw new RuntimeException("The standard deviation s must be positive");
        if(p == 0)
            return Double.NEGATIVE_INFINITY;
        if(p == 1)
            return Double.POSITIVE_INFINITY;
        if(s == 0)
            return am;
        double  q, r, val;

        q = p - 0.5;

        /* 0.075 <= p <= 0.925 */
        if(Math.abs(q) <= .425) {
            r = .180625 - q * q;
            val =
                    q * (((((((r * 2509.0809287301226727 +
                            33430.575583588128105) * r + 67265.770927008700853) * r +
                            45921.953931549871457) * r + 13731.693765509461125) * r +
                            1971.5909503065514427) * r + 133.14166789178437745) * r +
                            3.387132872796366608)
                            / (((((((r * 5226.495278852854561 +
                            28729.085735721942674) * r + 39307.89580009271061) * r +
                            21213.794301586595867) * r + 5394.1960214247511077) * r +
                            687.1870074920579083) * r + 42.313330701600911252) * r + 1);
        }
        /* closer than 0.075 from {0,1} boundary */
        else {
            /* r = min(p, 1-p) < 0.075 */
            if (q > 0) {
                r = 1 - p;
            } else {
                r = p;
            }

            r = Math.sqrt(-Math.log(r));
            /* r = sqrt(-log(r))  < ==>  min(p, 1-p) = exp( - r^2 ) */

            if (r <= 5) { /* <==> min(p,1-p) >= exp(-25) ~= 1.3888e-11 */
                r += -1.6;
                val = (((((((r * 7.7454501427834140764e-4 +
                        .0227238449892691845833) * r + .24178072517745061177) *
                        r + 1.27045825245236838258) * r +
                        3.64784832476320460504) * r + 5.7694972214606914055) *
                        r + 4.6303378461565452959) * r +
                        1.42343711074968357734)
                        / (((((((r *
                        1.05075007164441684324e-9 + 5.475938084995344946e-4) *
                        r + .0151986665636164571966) * r +
                        .14810397642748007459) * r + .68976733498510000455) *
                        r + 1.6763848301838038494) * r +
                        2.05319162663775882187) * r + 1);
            } else { /* very close to  0 or 1 */
                r += -5;
                val = (((((((r * 2.01033439929228813265e-7 +
                        2.71155556874348757815e-5) * r +
                        .0012426609473880784386) * r + .026532189526576123093) *
                        r + .29656057182850489123) * r +
                        1.7848265399172913358) * r + 5.4637849111641143699) *
                        r + 6.6579046435011037772)
                        / (((((((r *
                        2.04426310338993978564e-15 + 1.4215117583164458887e-7) *
                        r + 1.8463183175100546818e-5) * r +
                        7.868691311456132591e-4) * r + .0148753612908506148525)
                        * r + .13692988092273580531) * r +
                        .59983220655588793769) * r + 1);
            }

            if (q < 0.0) {
                val = -val;
            }
        }

        return am + s * val;
    }

    public static double square(double x){
        return x * x;
    }

    public static double roundNumberToCentimeters(double x){

        DecimalFormat df = new DecimalFormat("#.##");

        double parsedNumber = Double.parseDouble(df.format(x).replace(",", "."));

        if (parsedNumber == -0.0)
            return 0.0;

        return parsedNumber;
    }

    public static double getDouble(Double number){

        return !ObjectUtils.isEmpty(number) ? number : 0.0;
    }
}
