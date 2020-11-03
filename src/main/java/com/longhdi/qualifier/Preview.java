package com.longhdi.qualifier;

import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Preview {
}
