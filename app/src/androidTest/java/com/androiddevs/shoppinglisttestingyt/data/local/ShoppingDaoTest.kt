package com.androiddevs.shoppinglisttestingyt.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class) // we write this for Application to Know that Test need Android Component and context and it will not run on JVM
@SmallTest // Unit test
class ShoppingDaoTest {
    /**
     * @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
     ///////////
     * to tell Junit to work one task after another so runBlocking test can work correctly
     * as when we use runblockingtest it run immediatlly and remove any delay that may happen so we
     * dont use runbloking in test because it may make delay
     */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao


    @Before
    fun setUp() {
        /**
         * this run before every Test Fun SO Every fun will have it's own database */
        database= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java).build()
        dao = database.shoppingDao()
    }
    @After
    fun tearDown(){
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insert_shopping_items() = runBlockingTest{
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        val data = dao.observeAllShoppingItems()
        assertTrue(data.contains(shoppingItem))
    }
    @Test
    @Throws(Exception::class)
    fun delete_shopping_items() = runBlockingTest{
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)
        val data = dao.observeAllShoppingItems()
        assertFalse(data.contains(shoppingItem))
    }
    @Test
    @Throws(Exception::class)
    fun ammount_sum_shopping_items() = runBlockingTest{
        val shoppingItem1 = ShoppingItem("name", 1, 10f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 2, 20f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 3, 30f, "url", id = 3)
        val shoppingItem4 = ShoppingItem("name", 0, 1f, "url", id = 4)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)
        dao.insertShoppingItem(shoppingItem4)
        val price = dao.observeTotalPrice()
        assertTrue(price == 1*10f + 2*20f +3*30f )
    }

}