package top;

import ca.mcgill.cs.swevo.dscribe.annotations.DScribeAnnotations.AssertBools;

public class MinimalUsage {

    @AssertBools(factory = "MinimalUsage", falseParams = { "22", "0" }, falseState = "IsEven", trueParams = { "23", "0" }, trueState = "IsOdd")
    public static boolean isOdd(int n, int n1) {
        return n % 2 != 0;
    }
}
