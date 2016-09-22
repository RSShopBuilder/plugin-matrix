package org.bitbucket.shaigem.rssb.plugin.matrix

import javafx.beans.property.SimpleIntegerProperty
import net.xeoh.plugins.base.annotations.PluginImplementation
import net.xeoh.plugins.base.annotations.meta.Author
import org.bitbucket.shaigem.rssb.plugin.BaseShopFormatPlugin
import org.bitbucket.shaigem.rssb.plugin.ShopFormat
import org.bitbucket.shaigem.rssb.plugin.matrix.packed.MatrixPackedShopFormat
import org.bitbucket.shaigem.rssb.plugin.matrix.unpacked.MatrixUnpackedShopFormat

@PluginImplementation
@Author(name = "AbyssPartyy")
class MatrixUnpackedShopPlugin : BaseShopFormatPlugin() {

    override val format: ShopFormat<MatrixShop> = MatrixUnpackedShopFormat()

    override fun addColumnToExplorer() {
        column<MatrixShop, Int>(title = "Key") {
            SimpleIntegerProperty(it.value.key).asObject()
        }
    }
}

@PluginImplementation
@Author(name = "AbyssPartyy")
class MatrixPackedShopPlugin : BaseShopFormatPlugin() {

    override val format: ShopFormat<MatrixShop> = MatrixPackedShopFormat()

    override fun addColumnToExplorer() {
        column<MatrixShop, Int>(title = "Key") {
            SimpleIntegerProperty(it.value.key).asObject()
        }
    }
}