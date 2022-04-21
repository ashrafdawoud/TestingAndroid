package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem) :Long

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM shopping_items")
    suspend fun observeAllShoppingItems(): List<ShoppingItem>

    @Query("SELECT SUM(price * amount) FROM shopping_items")
    suspend fun observeTotalPrice(): Float
}