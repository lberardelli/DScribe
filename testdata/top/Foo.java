package top;

import ca.mcgill.cs.swevo.dscribe.annotations.DScribeAnnotations.AssertBools;

public class Foo {

    /**
     * @dscribe Returns true when isOdd, false when isEven
     */
    @AssertBools(factory = "Foo", falseParams = { "22" }, falseState = "isEven", trueParams = { "23" }, trueState = "isOdd")
    public static boolean isOdd(int n) {
        return n % 2 != 0;
    }

    public static void main(String[] args) {
        System.out.println(isOdd(23));
    }

    static class Bar {

        /**
         * @dscribe Returns true when isEven, false when Odd
         */
        @AssertBools(factory = "Foo.Bar", falseParams = { "23" }, falseState = "Odd", trueParams = { "22" }, trueState = "isEven")
        public static boolean isEven(int n) {
            return n % 2 == 0;
        }
    }
}
