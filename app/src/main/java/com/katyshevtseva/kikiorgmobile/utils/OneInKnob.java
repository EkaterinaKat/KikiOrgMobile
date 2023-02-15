package com.katyshevtseva.kikiorgmobile.utils;

@FunctionalInterface
public interface OneInKnob<T> {
    void execute(T t);
}
