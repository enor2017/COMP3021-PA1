package hk.ust.cse.comp3021.pa1.util;

import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Constructor<?>[] getPublicConstructors(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(ModifierSupport::isPublic)
                .toArray(Constructor[]::new);
    }

    public static Method[] getPublicInstanceMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(ModifierSupport::isNotStatic)
                .filter(ModifierSupport::isPublic)
                .toArray(Method[]::new);
    }

    public static Method[] getPublicStaticMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(ModifierSupport::isStatic)
                .filter(ModifierSupport::isPublic)
                .toArray(Method[]::new);
    }

    public static Field[] getPublicInstanceFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(ModifierSupport::isNotStatic)
                .filter(ModifierSupport::isPublic)
                .toArray(Field[]::new);
    }

    public static Field[] getPublicStaticFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(ModifierSupport::isStatic)
                .filter(ModifierSupport::isPublic)
                .toArray(Field[]::new);
    }
}
