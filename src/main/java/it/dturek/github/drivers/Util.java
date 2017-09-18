package it.dturek.github.drivers;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static <T> List<T> iteratorToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

}
