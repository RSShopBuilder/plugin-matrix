package org.bitbucket.shaigem.rssb.plugin.matrix

import javafx.beans.Observable
import javafx.beans.property.SimpleIntegerProperty
import org.bitbucket.shaigem.rssb.model.item.Item
import org.bitbucket.shaigem.rssb.model.shop.Shop
import org.bitbucket.shaigem.rssb.plugin.getValue
import org.bitbucket.shaigem.rssb.plugin.setValue

/**
 * Created on 21/09/16.
 */
class MatrixShop(key: Int, name: String, itemArray: List<Item>, currency: Int,
                 canSellTo: Boolean) : Shop(name, itemArray, canSellTo) {

    // Must import these:
    // import org.bitbucket.shaigem.rssb.plugin.getValue
    // import org.bitbucket.shaigem.rssb.plugin.setValue
    private val keyProperty = SimpleIntegerProperty(key)
    var key: Int by keyProperty

    private val currencyProperty = SimpleIntegerProperty(currency)
    var currency: Int by currencyProperty;

    override fun getCustomPropertiesToObserve(): Array<out Observable> {
        return arrayOf(keyProperty, currencyProperty)
    }

    override fun copy(): Shop {
        return MatrixShop(key, name, items, currency, generalStore)
    }

    override fun toString(): String {
        return "[$key] $name"
    }
}