package com.e.cryptocracy.home.spark

import com.robinhood.spark.SparkAdapter


class CustomSparkAdapter(private val yData: FloatArray) : SparkAdapter() {
    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }
}