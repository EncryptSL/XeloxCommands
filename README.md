# XeloxCommands
Simple JDA Discord Command Framework.

Example Registering of Commands
```KOTLIN
        val commandBuilder = DiscordCommandBuilder()
        commandBuilder
            .addCommand(
                AnimalCmd(echelon),
                MagicBallCmd(echelon),
                DiscordCmd(echelon),
                MinecraftCmd(echelon)
            ).build()
            .registerListener(jda-instance) // Require JDA
```

Example Usage of Commands Framework
All functions are in class CommandContextImpl
```KOTLIN
class ExampleCmd : BaseDiscordCommand() {
    @Command("example")
    override fun execute(event: CommandContext) {
        if (event.ctx.command()) {
            event.ctx.deferReply()
            event.ctx.reply("Simple example")
        }
    }
}

class ExampleRequiredTextChannel : BaseDiscordCommand() {
    @Command("example")
    override fun execute(event: CommandContext) {
        if (event.ctx.command()) {
            event.ctx.deferReply()
            if (!event.ctx.allowedTextChannel("YOUR_TEXT_CHANNEL_ID")) {
                event.ctx.reply("This command can't be executed in this channel !")
                return
            }

            event.ctx.reply("Simple example")
        }
    }
}

class ExampleRequiredVoiceChannel : BaseDiscordCommand() {
    @Command("example")
    override fun execute(event: CommandContext) {
        if (event.ctx.command()) {
            event.ctx.deferReply()
            if (!event.ctx.allowedVoiceChannel("YOUR_TEXT_CHANNEL_ID") && !event.ctx.inAudioChannel()) {
                event.ctx.reply("You are not in same channel where is bot")
                return
            }

            event.ctx.reply("Simple example")
        }
    }
}

class ExampleRoleRequired : BaseDiscordCommand() {
    @Command("example")
    override fun execute(event: CommandContext) {
        if (event.ctx.command()) {
            event.ctx.deferReply()
            if (!event.ctx.hasRole("REQUIRED_ROLE_ID")) {
                event.ctx.reply("You don't have required role for this command !")
                return
            }

            event.ctx.reply("Hi hi i have required role !")
        }
    }
}

class ExampleSubCommand: BaseDiscordCommand() {
    @Command("example")
    override fun execute(event: CommandContext) {
        if (event.ctx.command()) {
            event.ctx.deferReply()
            if (event.ctx.subCommand("world")) {
                event.ctx.reply("Hello world")
            }

            if (event.ctx.subCommand("john")) {
                event.ctx.reply("Hello John")
            }
        }
    }
}

class ExampleSubGroupCommand: BaseDiscordCommand() {
    @Command("permissions")
    override fun execute(event: CommandContext) {
        if (event.ctx.command()) {
            event.ctx.deferReply()
            if (event.ctx.commandGroup("add")) {
                if (event.ctx.subCommand("role")) {

                    val mentionedUser: IMentionable = event.ctx.option("user")?.asMentionable ?: return
                    val role: Role = event.ctx.option("role")?.asRole ?: return

                    event.ctx.slashEvent.guild?.addRoleToMember(UserSnowflake.fromId(mentionedUser.id), role)
                    event.ctx.reply("Hello world ${mentionedUser.asMention} now have role ${role.name}")
                }
            }
            
            if (event.ctx.commandGroup("remove")) {
                if (event.ctx.subCommand("role")) {

                    val mentionedUser: IMentionable = event.ctx.option("user")?.asMentionable ?: return
                    val role: Role = event.ctx.option("role")?.asRole ?: return

                    event.ctx.slashEvent.guild?.removeRoleFromMember(UserSnowflake.fromId(mentionedUser.id), role)
                    event.ctx.reply("Hello world ${mentionedUser.asMention} now don't role ${role.name}")
                }
            }
        }
    }
}
```