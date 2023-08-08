package com.github.encryptsl.discord.framework.listener

import com.github.encryptsl.discord.framework.BaseDiscordCommand
import com.github.encryptsl.discord.framework.api.CommandContext
import com.github.encryptsl.discord.framework.api.CommandContextImpl
import com.github.encryptsl.discord.framework.api.insterfaces.Command
import com.github.encryptsl.discord.framework.exceptions.MissingCommandException
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DiscordCommandExecutor(private val commands: List<BaseDiscordCommand>) : ListenerAdapter()  {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        commands.forEach { a ->
            val method = a::class.java.getMethod("execute", CommandContext::class.java)
            val commandAnnotation = method.getAnnotation(Command::class.java)
            if (commandAnnotation.command.isNotEmpty() || commandAnnotation != null) {
                a.execute(CommandContext(CommandContextImpl(event, commandAnnotation.command)))
            } else {
                throw MissingCommandException("Missing command in class ${method.javaClass}")
            }
        }
    }

    fun registerListener(jda: JDA) {
        jda.addEventListener(this)
    }

}