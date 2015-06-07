package com.github.sorhus.hmmongo.viterbi.result;

public class NoPath<I,O> implements Result<I,O> {
    private NoPath() {}
    private static NoPath<Object,Object> instance;
    public static Result getInstance() {
        if(instance == null) {
            instance = new NoPath<>();
        }
        return instance;
    }
}
