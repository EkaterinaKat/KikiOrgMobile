package com.katyshevtseva.kikiorgmobile.utils.knobs;

@FunctionalInterface
public interface OneOutKnob<T> {
    T execute();
}
