/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package cc.venja.automations

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.value

/**
 * Mirai Console Automations 的配置文件类，它应该是单例，并且在 [RenewFire.onEnable] 时被初始化
 */
object MainSetting : ReadOnlyPluginConfig("setting") {

    /**
     * local 模式
     * 为 true 时在启动后完成续火直接关闭
     */
    val local: Boolean by value(true)

    /**
     * RenewTarget 配置
     * 123456:
     *   654321: message
     */
    val settings: Map<String, Map<String, String>> by value()
}