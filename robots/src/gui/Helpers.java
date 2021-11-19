package gui;

public class Helpers
{
    public static boolean areEqual(Object o1, Object o2)
    {
        if (o1 == null)
            return o2 == null;
        return o1.equals(o2);
    }
}