package com.github.encryptsl.discord.framework.api

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping

class CommandContextImpl(private val interaction: SlashCommandInteractionEvent, private val command: String) {

    val slashEvent get() = interaction

    fun command(): Boolean {
        return interaction.name == command
    }

    fun subCommand(name: String): Boolean
    {
        return interaction.subcommandName == name
    }

    fun allowedTextChannel(requiredChannel: String): Boolean {
        return channel().id == requiredChannel
    }

    fun allowedVoiceChannel(requiredChannel: String): Boolean {
        return member()?.voiceState?.id == requiredChannel
    }

    fun inAudioChannel(): Boolean
    {
        return member()?.voiceState?.inAudioChannel() == true
    }

    fun hasRole(roleId: String): Boolean {
        val roles: MutableList<Role>? = member()?.roles

        return roles?.stream()?.map(Role::getId)?.anyMatch(roleId::equals) == true
    }

    fun option(name: String): OptionMapping? {
        return interaction.getOption(name)
    }

    fun commandGroup(name: String): Boolean
    {
        return interaction.subcommandGroup == name
    }

    fun member(): Member? {
        return interaction.member
    }

    fun user(): User {
        return interaction.user
    }

    fun channel(): Channel {
        return interaction.channel
    }

    fun deferReply()
    {
        interaction.deferReply().queue()
    }

    fun reply(string: String) {
        interaction.hook.sendMessage(string).queue()
    }

    fun reply(embed: MessageEmbed) {
        interaction.hook.sendMessageEmbeds(embed).queue()
    }

    fun replyPrivate(message: String, privateMessage: String) {
        interaction.hook.sendMessage(message).queue { r ->
            r.interaction?.user?.openPrivateChannel()?.flatMap { channel: PrivateChannel ->
                channel.sendMessage(privateMessage)
            }?.queue()
        }
    }

    fun replyPrivate(string: String, embed: MessageEmbed) {
        interaction.hook.sendMessage(string).queue { r ->
            r.interaction?.user?.openPrivateChannel()?.flatMap { channel: PrivateChannel ->
                channel.sendMessageEmbeds(embed)
            }?.queue()
        }
    }
}