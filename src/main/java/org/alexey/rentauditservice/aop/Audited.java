package org.alexey.rentauditservice.aop;


import org.alexey.rentauditservice.core.entity.AuditedAction;
import org.alexey.rentauditservice.core.entity.EssenceType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audited {

    AuditedAction auditedAction();

    EssenceType essenceType();
}
