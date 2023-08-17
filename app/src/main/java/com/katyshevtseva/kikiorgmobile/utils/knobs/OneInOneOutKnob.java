package com.katyshevtseva.kikiorgmobile.utils.knobs;

@FunctionalInterface
public interface OneInOneOutKnob<P, R> {
    R execute(P p);
}
