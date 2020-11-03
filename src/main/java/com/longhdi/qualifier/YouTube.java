package com.longhdi.qualifier;

import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface YouTube {

}
