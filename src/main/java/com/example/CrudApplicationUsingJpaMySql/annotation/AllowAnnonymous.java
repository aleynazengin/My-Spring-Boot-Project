package com.example.CrudApplicationUsingJpaMySql.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.METHOD ) //Tanımlanmış annotationın nereye uygulanacağını belirlemek için kullanılır.
// @Target({ElementType.METHOD}) olarak işaretlenmiş bir annotation yalnızca metodlar için kullanılabilir
@Retention( RetentionPolicy.RUNTIME) //@Retention : Specifies whether the annotation metadata can be accessed at runtime by the application
public @interface AllowAnnonymous {

    ValidationType validationType() default ValidationType.ANNONYMOUS;
}
