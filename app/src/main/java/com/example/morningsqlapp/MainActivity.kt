package com.example.morningsqlapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var edtName:EditText
    lateinit var edtEmail:EditText
    lateinit var edtIdNumber:EditText
    lateinit var btnsave:Button
    lateinit var btnview:Button
    lateinit var btndelete:Button
    lateinit var db:SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.Edttext)
        edtEmail = findViewById(R.id.Edtemail)
        edtIdNumber = findViewById(R.id.Edtnumber)
        btnsave = findViewById(R.id.btnsave)
        btnview = findViewById(R.id.btnview)
        btndelete = findViewById(R.id.btndelete)
        // create database called eMobilisDb
        db = openOrCreateDatabase("eMobiles", Context.MODE_PRIVATE,  null)
        // create a table called users inside the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")
        // set onclick listeners to the button
        btnsave.setOnClickListener {
            // Receive the data from the user
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber = edtIdNumber.text.toString().trim()
            // check if the user is submitting empty field
            if (name.isEmpty() || email.isEmpty() || idNumber.isEmpty()){
                // display an error using defined message function
                message("EMPTY FIELDS!!!","Please fill all inputs!")
            }else{
                //proceed to save the data
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+email+"','"+idNumber+"')")
                clear()
                message("success!!!","user saved successfully")
            }

        }
        btnview.setOnClickListener {
            var cursor =db.rawQuery("SELECT * FROM users",null)
            // check if there is any record in the db
            if (cursor.count == 0){
                message("no records!!!", "sorry no user were found!!!")
            }else{
                // user string to append all available records using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n\n")
                }
                message("users",buffer.toString())
            }
        }
        btndelete.setOnClickListener {
            val idNumber = edtIdNumber.text.toString().trim()
            if (idNumber.isEmpty()){
                message("empty fields","please enter the id number")
            }else{
                // user cursor to select the user with given id
                var cursor = db.rawQuery("SELECT * FROM * users WHERE kitambulisho='"+idNumber+"'",null)
                // check if the record with provided id exists
                if (cursor.count== 0){
                    message("no records","no user with id"+idNumber)
                }else{
                    //proceed to delete the user
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idNumber+"'")
                    clear()
                    message("SUCCESS!!!","User deleted successfully")
                }
            }
        }
    }
    fun message(title:String,message:String){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Close",null)
        alertDialog.create().show()

    }
    fun clear(){
        edtName.setText("")
        edtEmail.setText("")
        edtIdNumber.setText("")

    }
}