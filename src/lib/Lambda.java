package lib;

import java.lang.Object;;

/**
 * Interface to be able to do a lambda calls
 */
public interface Lambda {
    /**
     * General callback
     * 
     * @param any
     * @return any object
     */
    Object callback(Object any);
}