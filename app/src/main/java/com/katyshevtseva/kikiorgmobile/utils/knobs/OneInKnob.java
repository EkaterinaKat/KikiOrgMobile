package com.katyshevtseva.kikiorgmobile.utils.knobs;

@FunctionalInterface
public interface OneInKnob<T> {
    void execute(T t);
}
