package com.katyshevtseva.kikiorgmobile.utils;

@FunctionalInterface
public interface TwoInKnob<P1, P2> {
    void execute(P1 p1, P2 p2);
}
