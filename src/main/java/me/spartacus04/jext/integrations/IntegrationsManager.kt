package me.spartacus04.jext.integrations

import me.spartacus04.jext.integrations.unique.GeyserIntegration
import me.spartacus04.jext.language.LanguageManager.Companion.GEYSER_RELOAD
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.entity.Player
import javax.annotation.Nullable

class IntegrationsManager {
    private val integrations = ArrayList<Integration>()

    private fun registerIntegration(integration: Integration) {
        if(integrations.any { it.id == integration.id }) throw IllegalArgumentException("Integration with id ${integration.id} is already registered!")

        integrations.add(integration)
    }

    fun registerIntegrations(vararg integrations: Integration) {
        integrations.forEach { registerIntegration(it) }
    }

    fun hasJukeboxAccess(player: Player, block: Block): Boolean? {
        for(integration in integrations){
            val state = integration.hasJukeboxAccess(player, block)
            return when(state){
                true->true
                false->continue
                null->null
            }
        }
        return false
    }

    fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean? {
        for(integration in integrations){
            val state = integration.hasJukeboxGuiAccess(player, block)
            return when(state){
                true->true
                false->continue
                null->null
            }
        }
        return false
    }

    fun reloadDefaultIntegrations() {
        integrations.removeIf { it.id == "worldguard" || it.id == "griefprevention" }

        try {
            registerIntegration(WorldGuardIntegration())
        } catch(_: NoClassDefFoundError) { }

        try {
            registerIntegration(GriefPreventionIntegration())
        } catch (_: NoClassDefFoundError) { }

        try {
            if(GeyserIntegration.GEYSER == null)
                GeyserIntegration.GEYSER = GeyserIntegration()
            else
                Bukkit.getConsoleSender().sendMessage(GEYSER_RELOAD)

        } catch (_ : NoClassDefFoundError) { }
    }
}