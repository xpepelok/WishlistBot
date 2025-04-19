package dev.xpepelok.wishlist.data.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommandHandler {
    String[] alias(); // Aliases

    String usage() default "";

    String description() default "";

    boolean adminOnly() default true;

    boolean hide() default false;
}
