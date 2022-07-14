//Jaideep Duggempudi
//Suneet Dravida
package chess;
/**
 * The Interface QRS is a functional interface that is used for lambda functions add1, minus1, nochange in the Chess class
 *
 * @author Jaideep Duggempudi
 * @author Suneet Dravida
 */
public interface QRS {
    /**
     *
     * @param x x is the original number before it has undegone any lamda manipulation
     * @return new x after it was incremented, decremented, or not changed by the lamda function
     */
    int QRSvalid(int x);
}
