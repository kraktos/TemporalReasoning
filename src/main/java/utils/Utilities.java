/**
 * 
 */

package utils;

/**
 * All different kinds of utility methods are placed here
 * 
 * @author Arnab Dutta
 */
public class Utilities
{

    /**
     * coverts an aargument string to a surface form by cleaning out any OIS specific formats
     * 
     * @param oieInstance
     * @return
     */

    public static String clean(String oieInstance)
    {

        if (oieInstance.indexOf(":") != -1)
            oieInstance = oieInstance.substring(oieInstance.indexOf(":") + 1, oieInstance.length());

        return oieInstance.replaceAll("\\_+", " ").toLowerCase();
    }

}
