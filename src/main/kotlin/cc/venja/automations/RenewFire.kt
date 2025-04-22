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


object RenewFire : KotlinPlugin(
        JvmPluginDescription(
                id = "cc.venja.automations",
                name = "自动续火QwQ",
                version = "0.3.0",
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
                val now = Calendar.getInstance()
                val firstTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 18)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val interval = 24 * 60 * 60 * 1000L // 每 24 小时

                if (now.after(firstTime)) {
                    // 已过今天18点：先立即执行一次
                    RenewFire.launch {
                        Bot.instances.forEach { bot ->
                            settings[bot.id.toString()]?.forEach { (key, value) ->
                                bot.getFriend(key.toLong())?.sendMessage(value)
                            }
                        }
                    }
                    // 下一次定时是明天 18:00
                    firstTime.add(Calendar.DAY_OF_YEAR, 1)
                }

                // 启动定时器，从 firstTime 开始，每 24 小时执行一次
                timer.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        RenewFire.launch {
                            Bot.instances.forEach { bot ->
                                settings[bot.id.toString()]?.forEach { (key, value) ->
                                    bot.getFriend(key.toLong())?.sendMessage(value)
                                }
                            }
                        }
                    }
                }, firstTime.time, interval)
            }
        }
    }
}