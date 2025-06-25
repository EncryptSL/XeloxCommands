package com.github.encryptsl.discord.framework.api

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import java.util.concurrent.TimeUnit

class CommandContextImpl(private val interaction: SlashCommandInteractionEvent, private val command: String) {

    val slashEvent get() = interaction

    fun command(): Boolean {
        return interaction.name == command
    }

    fun subCommand(name: String): Boolean
    {
        return interaction.subcommandName == name
    }

    fun inAllowedTextChannel(requiredChannel: String): Boolean {
        return channel().id == requiredChannel
    }

    fun isMemberInAudioChannel(requiredChannel: String): Boolean {
        return member()?.voiceState?.id == requiredChannel
    }

    fun inAudioChannel(): Boolean
    {
        return member()?.voiceState?.inAudioChannel() == true
    }

    fun ban(member: Member, reason: String, timeOut: Int, timeUnit: TimeUnit) {
        member.ban(timeOut, timeUnit).reason(reason).queue()
    }

    fun kick(member: Member, reason: String) {
        member.kick().reason(reason).queue()
    }

    fun addRoleToMember(member: Member, role: Role) {
        interaction.guild?.addRoleToMember(UserSnowflake.fromId(member.id), role)?.queue()
    }

    fun removeRoleFromMember(member: Member, role: Role) {
        interaction.guild?.removeRoleFromMember(UserSnowflake.fromId(member.id), role)?.queue()
    }

    fun hasRole(roleId: String): Boolean {
        val roles: MutableList<Role>? = member()?.roles

        return roles?.stream()?.map(Role::getId)?.anyMatch(roleId::equals) == true
    }

    fun hasPermission(permission: Permission): Boolean {
        return member()?.hasPermission(permission) == true
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

    fun reply(string: String, deferReply: Boolean = false) {
        if (deferReply) {
            interaction.hook.setEphemeral(true)
            interaction.hook.sendMessage(string).queue()
        } else {
            interaction.reply(string)
        }
    }

    fun reply(embed: MessageEmbed, deferReply: Boolean = false) {
        if (deferReply) {
            interaction.hook.setEphemeral(true)
            interaction.hook.sendMessageEmbeds(embed).queue()
        } else {
            interaction.replyEmbeds(embed)
        }
    }

    fun replyPrivate(message: String, privateMessage: String) {
        interaction.hook.sendMessage(message).queue { r ->
            r.author.openPrivateChannel().flatMap { channel: PrivateChannel ->
                channel.sendMessage(privateMessage)
            }.queue()
        }
    }

    fun replyPrivate(target: User, message: String, privateMessage: String) {
        interaction.hook.sendMessage(message).queue { _ ->
            target.openPrivateChannel().flatMap { channel: PrivateChannel ->
                channel.sendMessage(privateMessage)
            }.queue()
        }
    }

    fun replyPrivate(string: String, embed: MessageEmbed) {
        interaction.hook.sendMessage(string).queue { r ->
            r.author.openPrivateChannel().flatMap { channel: PrivateChannel ->
                channel.sendMessageEmbeds(embed)
            }.queue()
        }
    }

    fun replyPrivate(target: User, string: String, embed: MessageEmbed) {
        interaction.hook.sendMessage(string).queue { _ ->
            target.openPrivateChannel().flatMap { channel: PrivateChannel ->
                channel.sendMessageEmbeds(embed)
            }.queue()
        }
    }
}