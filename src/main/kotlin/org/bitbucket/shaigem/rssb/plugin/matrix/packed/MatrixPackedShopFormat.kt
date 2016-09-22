package org.bitbucket.shaigem.rssb.plugin.matrix.packed

import javafx.collections.ObservableList
import javafx.stage.FileChooser
import org.bitbucket.shaigem.rssb.plugin.ShopFormat
import org.bitbucket.shaigem.rssb.plugin.ShopFormatDescriptor
import org.bitbucket.shaigem.rssb.plugin.ext
import org.bitbucket.shaigem.rssb.plugin.matrix.MatrixShop
import java.io.File
import java.util.*

/**
 * Defines the packed shop format for Matrix servers.
 */
class MatrixPackedShopFormat : ShopFormat<MatrixShop> {
    override val defaultFileName: String = "packedShops.s"

    override val extensions: List<FileChooser.ExtensionFilter>
            = arrayListOf(ext(description = "Packed Files", extension = "*.s"))

    override val defaultShop: MatrixShop = MatrixShop.getDefault()

    override fun load(selectedFile: File): ArrayList<MatrixShop> {
        //TODO
        throw UnsupportedOperationException("not implemented")
    }

    override fun export(selectedFile: File, shopsToExport: ObservableList<MatrixShop>) {
        //TODO
        throw UnsupportedOperationException("not implemented")
    }

    override fun descriptor(): ShopFormatDescriptor {
        return ShopFormatDescriptor(
                name = "Matrix Packed",
                description = "Edit packed Matrix shops (packedShops.s).")
    }

}