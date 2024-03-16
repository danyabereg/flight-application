package org.example.mapper;

public interface Mapper <T, F> {
    T mapFrom(F f);
}
