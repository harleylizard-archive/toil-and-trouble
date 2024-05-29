package com.harleylizard.trouble.common;

@FunctionalInterface
public interface FloatComparable<T> {

    float compareTo(T t);
}
