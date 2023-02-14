package com.katyshevtseva.kikiorgmobile.utils;

@FunctionalInterface
public interface OneInOneOutKnob<P, R> {
    R execute(P p);
}
