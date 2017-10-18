package vn.tiki.sample.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class CartItem(
    @field:PrimaryKey
    val id: String,
    val productId: String,
    val size: String,
    var quantity: Int)
