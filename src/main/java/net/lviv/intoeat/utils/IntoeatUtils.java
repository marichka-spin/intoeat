package net.lviv.intoeat.utils;

import org.springframework.data.domain.Sort;

import java.lang.reflect.ParameterizedType;

public class IntoeatUtils {

    public static final Sort SORT_BY_NAME_ASC = new Sort(Sort.Direction.ASC, "name");

    public static Class getGenericType(Class clazz) {
        return (Class) ((ParameterizedType)clazz.
                getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }

}
