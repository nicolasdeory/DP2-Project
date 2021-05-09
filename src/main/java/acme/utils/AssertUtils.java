package acme.utils;

public final class AssertUtils {

    private AssertUtils() { }

    private static void assertGenericNotNull(Object object, String name)
    {
        if (object == null)
            throw new IllegalArgumentException(name + " cannot be null");
    }


    public static void assertRequestNotNull(Object request)
    {
        assertGenericNotNull(request, "request");
    }

    public static void assertEntityNotNull(Object entity)
    {
        assertGenericNotNull(entity, "entity");
    }

    public static void assertErrorsNotNull(Object errors)
    {
        assertGenericNotNull(errors, "errors");
    }

    public static void assertModelNotNull(Object model)
    {
        assertGenericNotNull(model, "model");
    }

}