package com.github.encryptsl.discord.framework.api

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