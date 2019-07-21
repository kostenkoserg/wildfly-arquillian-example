package com.fasto.admin.security;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * @author kostenko
 */
@Inherited
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Secured {

}
