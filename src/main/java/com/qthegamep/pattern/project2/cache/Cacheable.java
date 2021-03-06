package com.qthegamep.pattern.project2.cache;

import com.qthegamep.pattern.project2.model.container.HashAlgorithm;
import com.qthegamep.pattern.project2.model.container.KeyAlgorithm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Cacheable {

    KeyAlgorithm keyAlgorithm() default KeyAlgorithm.FULL_SIGNATURE_WITH_FULL_ARGUMENTS_KEY_ALGORITHM;

    HashAlgorithm hashAlgorithm() default HashAlgorithm.MD5_HASH_ALGORITHM;
}
