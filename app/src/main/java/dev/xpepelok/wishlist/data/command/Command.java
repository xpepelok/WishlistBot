package dev.xpepelok.wishlist.data.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String command();

    String[] alias() default {}; // Aliases

    String usage() default "";

    String description() default "secret command";

    boolean hide() default false;
}