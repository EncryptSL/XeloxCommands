package com.github.encryptsl.discord.framework

import com.github.encryptsl.discord.framework.listener.DiscordCommandExecutor

class DiscordCommandBuilder {

    private var commands: MutableList<BaseDiscordCommand> = mutableListOf()
    fun addCommand(vararg commands: BaseDiscordCommand): DiscordCommandBuilder {
        this.commands.addAll(commands)

        return this
    }

    fun addCommand(commands: Collection<BaseDiscordCommand>): DiscordCommandBuilder {
        this.commands.addAll(commands)

        return this
    }

    fun addCommand(commands: BaseDiscordCommand): DiscordCommandBuilder {
        this.commands.add(commands)

        return this
    }

    fun build(): DiscordCommandExecutor {
        require(commands.isNotEmpty()) { "Commands must not be empty" }
        return DiscordCommandExecutor(commands)
    }

}