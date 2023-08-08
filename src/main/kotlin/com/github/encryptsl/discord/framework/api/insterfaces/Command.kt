package com.github.encryptsl.discord.framework.api.insterfaces

import java.lang.annotation.Inherited

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Inherited
annotation class Command(val command: String)
