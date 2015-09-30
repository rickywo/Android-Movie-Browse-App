package edu.ricky.mada2.utility;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ricky Wu on 2015/9/30.
 */
public class BoundedLruCache<KEY, TYPE> extends LinkedHashMap<KEY, TYPE> {

    private static  final int DEFAULT_INITIAL_CAPACITY = 100;

    private static final float DEFAULT_LOAD_FACTOR = (float) 0.75;

    private final int bound;

    public BoundedLruCache(final int bound) {
        super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
        this.bound = bound;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<KEY, TYPE> eldest) {
        return size() > bound;
    }
}
