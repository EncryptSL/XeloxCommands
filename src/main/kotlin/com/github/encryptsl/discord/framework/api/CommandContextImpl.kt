package com.github.encryptsl.discord.framework.api

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction
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

    fun deferReplyQueue(): ReplyCallbackAction {
        return interaction.deferReply()
    }

    fun reply(content: String, ephemeral: Boolean = false) {
        if (!interaction.isAcknowledged) {
            interaction.reply(content).setEphemeral(ephemeral).queue()
        } else {
            interaction.hook.sendMessage(content).queue()
        }
    }

    fun reply(embed: MessageEmbed, ephemeral: Boolean = false) {
        if (!interaction.isAcknowledged) {
            interaction.replyEmbeds(embed).setEphemeral(ephemeral).queue()
        } else {
            interaction.hook.sendMessageEmbeds(embed).queue()
        }
    }

    fun replyPrivate(message: String, privateMessage: String) {
        if (!interaction.isAcknowledged) {
            interaction.reply(message).queue { r ->
                r.interaction.user.openPrivateChannel().flatMap { channel: PrivateChannel ->
                    channel.sendMessage(privateMessage)
                }.queue()
            }
        } else {
            interaction.hook.sendMessage(message).queue { r ->
                r.author.openPrivateChannel().flatMap { channel: PrivateChannel ->
                    channel.sendMessage(privateMessage)
                }.queue()
            }
        }
    }

    fun replyPrivate(target: User, message: String, privateMessage: String) {
        if (!interaction.isAcknowledged) {
            interaction.reply(message).queue { r ->
                target.openPrivateChannel().flatMap { channel: PrivateChannel ->
                    channel.sendMessage(privateMessage)
                }.queue()
            }
        } else {
            interaction.hook.sendMessage(message).queue { _ ->
                target.openPrivateChannel().flatMap { channel: PrivateChannel ->
                    channel.sendMessage(privateMessage)
                }.queue()
            }
        }
    }

    fun replyPrivate(string: String, embed: MessageEmbed) {
        if (!interaction.isAcknowledged) {
            interaction.reply(string).queue { r ->
                r.interaction.user.openPrivateChannel().flatMap { channel: PrivateChannel ->
                    channel.sendMessageEmbeds(embed)
                }.queue()
            }
        } else {
            interaction.hook.sendMessage(string).queue { r ->
                r.author.openPrivateChannel().flatMap { channel: PrivateChannel ->
                    channel.sendMessageEmbeds(embed)
                }.queue()
            }
        }
    }

    fun replyPrivate(target: User, string: String, embed: MessageEmbed) {
        if (!interaction.isAcknowledged) {
            interaction.reply(string).queue { _ ->
                target.openPrivateChannel().flatMap { channel: PrivateChannel ->
                    channel.sendMessageEmbeds(embed)
                }.queue()
            }
        } else {
            interaction.hook.sendMessage(string).queue { _ ->
                target.openPrivateChannel().flatMap { channel: PrivateChannel ->
                    channel.sendMessageEmbeds(embed)
                }.queue()
            }
        }
    }
}