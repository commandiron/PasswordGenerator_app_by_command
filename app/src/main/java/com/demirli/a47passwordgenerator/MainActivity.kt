package com.demirli.a47passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.function.Predicate
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private var password: StringBuilder? = null
    private var passwordLength: Int? = null

    private var numberArray: CharArray? = null
    private var uppercaseLetterArray: CharArray? = null
    private var lowercaseLetterArray: CharArray? = null
    private var symbolArray: CharArray? = null

    private var passwordArrayList: ArrayList<CharArray>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberArray = "0123456789".toCharArray()
        uppercaseLetterArray = "ABCDEFGHIJKLMNOPQRSTUVWXWZ".toCharArray();
        lowercaseLetterArray = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        symbolArray = "\\.[]{}()<>*+-=!?^\$|".toCharArray();

        passwordArrayList = arrayListOf()

        generate_btn.setOnClickListener {

            passwordArrayList!!.clear()

            passwordLength = lenght_password_edt.text.toString().toInt()

            if(numbers_cb.isChecked){
                passwordArrayList!!.add(numberArray!!)
            }
            if(uppercase_letter_cb.isChecked){
                passwordArrayList!!.add(uppercaseLetterArray!!)
            }
            if(lowercase_letter_cb.isChecked){
                passwordArrayList!!.add(lowercaseLetterArray!!)
            }
            if(symbols_cb.isChecked){
                passwordArrayList!!.add(symbolArray!!)
            }

            generatePassword(passwordLength!!,passwordArrayList!!)

        }

        password_tv.setOnClickListener {
            if(password_tv.text.toString() != ""){
                copyToBoard()
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generatePassword(passwordLength: Int, passwordArrayList: List<CharArray>){

        password = StringBuilder()

        if(passwordLength >= passwordArrayList.size){

            val listOfIntForPreventNonUsage = arrayListOf<Int>()

            for(i in 0 until passwordLength){

                val randomIntForWhichArray = (0..passwordArrayList.size-1).random()

                listOfIntForPreventNonUsage.add(randomIntForWhichArray)

                val randomCharArray = passwordArrayList[randomIntForWhichArray]

                val randomIntForWhichChar = (0..randomCharArray.size-1).random()

                val randomChar = randomCharArray[randomIntForWhichChar]

                password!!.append(randomChar)

            }

            val range = passwordArrayList.indices

            if(controlPassword(range,  listOfIntForPreventNonUsage) != null){

                generatePassword(passwordLength, passwordArrayList)

            }else{
                password_tv.setText(password)
            }

        }else{
            Toast.makeText(this, "Password length must be bigger", Toast.LENGTH_SHORT).show()
        }
    }

    private fun controlPassword(range: IntRange, listOfIntForPreventNonUsage: ArrayList<Int>): Int?{
        range.forEach{
            if(!listOfIntForPreventNonUsage.contains(it)){
                return it
            }
        }
        return null
    }

    private fun copyToBoard(){
        val clipBoard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", password_tv.text.toString())
        clipBoard.setPrimaryClip(clip)
    }
}
