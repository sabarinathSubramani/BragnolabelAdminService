package org.LPIntegrator.utils;

import java.util.function.Function;

import javax.ws.rs.WebApplicationException;

@FunctionalInterface
public interface ThrowingFunction<T,R> extends Function<T,R> {

	
    @Override
    default R apply(T t){
        try{
            return applyThrows(t);
        }catch (Exception e){
            throw new WebApplicationException(e);
        }
    }

    R applyThrows(T t) throws Exception;
}

