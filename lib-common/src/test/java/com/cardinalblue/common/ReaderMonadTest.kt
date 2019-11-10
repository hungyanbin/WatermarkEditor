package com.cardinalblue.common

import org.junit.Assert.assertEquals
import org.junit.Test

class ReaderMonadTest {

    @Test
    fun `use map to convert data`() {
        val getAllReader = Reader { repo: DataRepository -> repo.getAll() }

        val dataRepository = DataRepository()
        dataRepository.addDataList(listOf("a", "b", "c"))
        val result = getAllReader.map { dataList -> dataList.map { "$it - 1" } }
            .runBy(dataRepository)

        assertEquals(listOf("a - 1", "b - 1", "c - 1"), result)
    }

    @Test
    fun `use flatMap to convert reader`() {
        val getAllReader = Reader { repo: DataRepository -> repo.getAll() }
        val addDataListReader = Reader { repo: DataRepository -> repo.addDataList(listOf("a", "b", "c")) }

        val dataRepository = DataRepository()
        val result = addDataListReader.flatMap { getAllReader }
            .map { dataList -> dataList.map { "$it - 1" } }
            .runBy(dataRepository)

        assertEquals(listOf("a - 1", "b - 1", "c - 1"), result)
    }

    private class DataRepository {

        private var dataList: MutableList<String> = mutableListOf()

        fun addDataList(dataList: List<String>) {
            this.dataList.addAll(dataList)
        }

        fun getAll(): List<String> {
            return dataList
        }
    }

}