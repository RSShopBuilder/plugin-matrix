package org.bitbucket.shaigem.rssb.plugin.matrix.unpacked

import javafx.collections.ObservableList
import javafx.stage.FileChooser
import org.bitbucket.shaigem.rssb.model.item.Item
import org.bitbucket.shaigem.rssb.plugin.ShopFormat
import org.bitbucket.shaigem.rssb.plugin.ShopFormatDescriptor
import org.bitbucket.shaigem.rssb.plugin.ShopLoadException
import org.bitbucket.shaigem.rssb.plugin.extension
import org.bitbucket.shaigem.rssb.plugin.matrix.MatrixShop
import java.io.File
import java.util.*

/**
 * Defines the unpacked shop format for Matrix servers.
 */
class MatrixUnpackedShopFormat : ShopFormat<MatrixShop> {

    override val defaultFileName: String = "unpackedShops.txt"

    override val extensions: List<FileChooser.ExtensionFilter>
            = arrayListOf(extension("Text Files", "*.txt"))

    override val defaultShop: MatrixShop = MatrixShop.getDefault()

    override fun descriptor(): ShopFormatDescriptor {
        return ShopFormatDescriptor(
                name = "Matrix Unpacked",
                description = "Edit unpacked Matrix shops ($defaultFileName)")
    }

    override fun load(selectedFile: File): ArrayList<MatrixShop> {
        val shops = arrayListOf<MatrixShop>()

        fun parseShop(splitStringList: List<String>) {
            if (splitStringList.size != 3) {
                throw ShopLoadException("Invalid line at: " + splitStringList.toString())
            }

            val splitShopPropertiesList: List<String> =
                    splitStringList[0].split(" ", limit = 3)

            if (splitShopPropertiesList.size != 3) {
                throw ShopLoadException("Invalid line at: " + splitStringList.toString())
            }

            val key: Int = splitShopPropertiesList[0].toInt()
            val currency: Int = splitShopPropertiesList[1].toInt()
            val isGeneralStore: Boolean = splitShopPropertiesList[2].toBoolean()
            val shopName = splitStringList[1]

            fun readItems(): List<Item> {
                val shopItemsStringList: List<String> = splitStringList[2].split(" ").filterNot(String::isNullOrEmpty)
                // Get every element with a even (0, 2, 4...) index for the item id
                val shopItemIdentifiers = shopItemsStringList.filterIndexed { i, s -> (i % 2) == 0 }.map(String::toInt)
                // Get every element with a odd (1, 3, 5...) index for the amount
                val shopItemAmounts = shopItemsStringList.filterIndexed { i, s -> (i % 2) != 0 }.map(String::toInt)
                return shopItemIdentifiers.zip(shopItemAmounts, ::Item)
            }

            val shopItems = readItems()

            shops.add(MatrixShop(key, shopName, shopItems, currency, isGeneralStore))
        }

        selectedFile.bufferedReader().readLines().filterNot { it.isNullOrEmpty() || it.startsWith("//") }.
                map { it.split(" - ", limit = 3) }.forEach(::parseShop)

        return shops
    }

    override fun export(selectedFile: File, shopsToExport: ObservableList<MatrixShop>) {
        selectedFile.bufferedWriter().use { writer ->
            writer.write("//shopId money generalstore - name - item quantity item quantity etc.")
            writer.newLine()
            shopsToExport.forEach { shop ->
                // write the properties
                writer.write("${shop.key} ${shop.currency} ${shop.generalStore} - ${shop.name} - ")
                // write the items
                shop.items.forEach { item -> writer.write("${item.id} ${item.amount} ") }
                writer.newLine()
            }
        }
    }
}
