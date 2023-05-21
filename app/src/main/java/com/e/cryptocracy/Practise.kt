package com.e.cryptocracy

class Practise {


    fun findOddNumberInString(number: Int): String {
        val map = HashMap<Int, Int>()
        var temp = number
        while (temp > 0) {
            val num = temp % 10
            if (num % 2 != 0) {
                map[num] = num
            }
            temp /= 10
        }
        return map.keys.toString()

    }

    fun findOddNumberInList(number: List<Int>): String {
        val oddNumbers = HashMap<Int, Int>()
        number.map {
            if (it % 2 != 0) {
                oddNumbers[it] = it
            }
        }
        return oddNumbers.keys.toString()
    }


    fun sortArrayInAssending(numbers: MutableList<Int>): List<Int> {
        for (i in numbers.indices) {
            for (j in i until numbers.size) {
                var temp: Int
                if (numbers[i] > numbers[j]) {
                    temp = numbers[i]
                    numbers[i] = numbers[j]
                    numbers[j] = temp
                }
            }
        }
        return numbers
    }

    fun palindromeNumber(num: Int): Boolean {
        var reverseOfNumber = 0
        var temp = num
        while (temp > 0) {
            reverseOfNumber = (reverseOfNumber * 10) + (temp % 10)
            temp /= 10
        }
        return num == reverseOfNumber

    }
}