package org.bitbucket.shaigem.rssb.plugin.matrix.packed

import javafx.collections.ObservableList
import javafx.stage.FileChooser
import org.bitbucket.shaigem.rssb.model.item.Item
import org.bitbucket.shaigem.rssb.plugin.ShopFormat
import org.bitbucket.shaigem.rssb.plugin.ShopFormatDescriptor
import org.bitbucket.shaigem.rssb.plugin.extension
import org.bitbucket.shaigem.rssb.plugin.matrix.MatrixShop
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.util.*

/**
 * Defines the packed shop format for Matrix servers.
 */
class MatrixPackedShopFormat : ShopFormat<MatrixShop> {

    override val defaultFileName: String = "packedShops.s"

    override val extensions: List<FileChooser.ExtensionFilter>
            = arrayListOf(extension(description = "Packed Files", extension = "*.s"))

    override val defaultShop: MatrixShop = MatrixShop.getDefault()

    override fun descriptor(): ShopFormatDescriptor {
        return ShopFormatDescriptor(
                name = "Matrix Packed",
                description = "Edit packed Matrix shops ($defaultFileName)")
    }

    override fun load(selectedFile: File): ArrayList<MatrixShop> {
        val shops = arrayListOf<MatrixShop>()
        val dataInputStream = DataInputStream(selectedFile.inputStream())
        dataInputStream.use { stream ->
            while (stream.available() > 0) {
                val key = stream.readInt()
                val name = stream.readAlexString()
                val currency = stream.readUnsignedShort()
                val generalStore = stream.readUnsignedByte() === 1
                val itemSize = stream.readUnsignedByte()
                val items = Array(itemSize, { Item(stream.readUnsignedShort(), stream.readInt()) })
                shops.add(MatrixShop(key, name, items.toList(), currency, generalStore))
            }
        }
        return shops
    }

    override fun export(selectedFile: File, shopsToExport: ObservableList<MatrixShop>) {
        val dataOutputStream = DataOutputStream(selectedFile.outputStream())
        dataOutputStream.use { stream ->
            shopsToExport.forEach { shop ->
                // write the shop's properties
                stream.writeInt(shop.key)
                stream.writeAlexString(shop.name)
                stream.writeShort(shop.currency)
                stream.writeByte(if (shop.generalStore) 1 else 0)
                // write the shop's items
                val items = shop.items
                stream.writeByte(items.size)
                items.forEach { item ->
                    stream.writeShort(item.id)
                    stream.writeInt(item.amount)
                }
            }
        }
    }

    private fun DataInputStream.readAlexString(): String {
        val count = readUnsignedByte()
        val bytes = ByteArray(count)
        read(bytes, 0, count)
        return String(bytes)
    }

    private fun DataOutputStream.writeAlexString(string: String) {
        val bytes = string.toByteArray()
        writeByte(bytes.size)
        write(bytes)
    }
}