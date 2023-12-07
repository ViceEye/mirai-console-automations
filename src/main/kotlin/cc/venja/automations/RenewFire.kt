package cc.venja.automations

import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.utils.info
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate


object RenewFire : KotlinPlugin(
        JvmPluginDescription(
                id = "cc.venja.automations",
                name = "自动续火QwQ",
                version = "0.1.0",
        ) {

            author("Kazi")
            info("该插件的目的是登陆双方的账户并互相向对方发消息实现续火")
        }
) {

    @OptIn(ConsoleExperimentalApi::class)
    override fun onEnable() {
        MainSetting.reload()
        logger.info { "Plugin & Settings loaded" }
        logger.debug(MainSetting.settings.toString())
        val eventChannel = GlobalEventChannel.parentScope(this)
        logger.info { "Event channel registered" }

        with(MainSetting) {
            if (local) {
                // When locally running

                val totalActions = settings.size
                var proceedActions = 0
                eventChannel.subscribeAlways<BotOnlineEvent> {
                    if (settings[bot.id.toString()] !== null) {
                        settings[bot.id.toString()]?.forEach { (key, value) ->
                            bot.getFriend(key.toLong())?.sendMessage(value)
                        }
                        proceedActions += 1
                    }
                    if (totalActions == proceedActions) {
                        MiraiConsole.shutdown()
                    }
                }
            } else {
                // When server running

                val timer = Timer("FireRenewTimer", true)
                timer.scheduleAtFixedRate(0, (0.5 * 60 * 60 * 1000).toLong()) { // 每0.5小时执行一次
                    val currentTime = Calendar.getInstance()
                    if (currentTime.get(Calendar.HOUR_OF_DAY) == 12) {
                        RenewFire.launch {
                            Bot.instances.forEach { bot ->
                                if (settings[bot.id.toString()] !== null) {
                                    settings[bot.id.toString()]?.forEach { (key, value) ->
                                        bot.getFriend(key.toLong())?.sendMessage(value)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}