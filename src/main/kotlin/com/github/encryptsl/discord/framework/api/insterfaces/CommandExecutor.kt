package com.github.encryptsl.discord.framework.api.insterfaces

import com.github.encryptsl.discord.framework.api.CommandContext

interface CommandExecutor  {
    fun execute(event: CommandContext)
}