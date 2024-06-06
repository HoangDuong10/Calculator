package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainActivityBing: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainActivityBing = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainActivityBing.root)
        onClick()
    }

    private fun onClick(){
        activityMainActivityBing.btnNumber0.setOnClickListener{addNumber("0")}
        activityMainActivityBing.btnNumber1.setOnClickListener{addNumber("1")}
        activityMainActivityBing.btnNumber2.setOnClickListener{addNumber("2")}
        activityMainActivityBing.btnNumber3.setOnClickListener{addNumber("3")}
        activityMainActivityBing.btnNumber4.setOnClickListener{addNumber("4")}
        activityMainActivityBing.btnNumber5.setOnClickListener{addNumber("5")}
        activityMainActivityBing.btnNumber6.setOnClickListener{addNumber("6")}
        activityMainActivityBing.btnNumber7.setOnClickListener{addNumber("7")}
        activityMainActivityBing.btnNumber8.setOnClickListener{addNumber("8")}
        activityMainActivityBing.btnAddition.setOnClickListener{addNumber("+")}
        activityMainActivityBing.btnSubtraction.setOnClickListener{addNumber("-")}
        activityMainActivityBing.btnMultiplication.setOnClickListener{addNumber("x")}
        activityMainActivityBing.btnDivision.setOnClickListener{addNumber("/")}
        activityMainActivityBing.btnNumber9.setOnClickListener{addNumber("9")}
        activityMainActivityBing.btnClear.setOnClickListener{allClearAction()}
        activityMainActivityBing.ibBackSpace.setOnClickListener{ backSpaceAction()}
        activityMainActivityBing.btnDot.setOnClickListener{addNumber(".")}
    }

    private fun addNumber(number: String) {
        activityMainActivityBing.tvWorking.text = activityMainActivityBing.tvWorking.text.toString() + number
    }

    fun allClearAction(){
        activityMainActivityBing.tvWorking.text = ""
        activityMainActivityBing.tvResult.text = ""
    }

    fun backSpaceAction() {
        val length = activityMainActivityBing.tvWorking.length()
        if(length > 0)
            activityMainActivityBing.tvWorking.text = activityMainActivityBing.tvWorking.text.subSequence(0, length - 1)
    }

    fun equalsAction(view: View)
    {
        activityMainActivityBing.tvResult.text = calculateResults()
    }

    private fun calculateResults(): String
    {
        val digitsOperators = cutSubstring()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = mulDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        if (result % 1 == 0f) {
            return String.format("%.0f", result)
        }

        return result.toString()
    }

    private fun mulDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = calcMulDiv(list)
        }
        return list
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices) {
            if(passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun calcMulDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices) {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun cutSubstring(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in activityMainActivityBing.tvWorking.text) {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if(currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }
}