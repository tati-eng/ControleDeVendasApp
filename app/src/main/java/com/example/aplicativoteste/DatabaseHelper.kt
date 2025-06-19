package com.example.aplicativoteste

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "LojaDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PRODUTOS = "produtos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOME = "nome"
        private const val COLUMN_QUANTIDADE = "quantidade"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_PRODUTOS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NOME TEXT," +
                "$COLUMN_QUANTIDADE INTEGER)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUTOS")
        onCreate(db)
    }

    // Adicionar produto
    fun addProduto(nome: String, quantidade: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOME, nome)
        values.put(COLUMN_QUANTIDADE, quantidade)

        val result = db.insert(TABLE_PRODUTOS, null, values)
        db.close()
        return result != -1L
    }

    // Buscar todos os produtos (com ID)
    fun getTodosProdutos(): List<Produto> {
        val lista = mutableListOf<Produto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUTOS", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME))
                val quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTIDADE))
                lista.add(Produto(id, nome, quantidade))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    // Atualizar produto
    fun atualizarProduto(id: Int, novoNome: String, novaQtd: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOME, novoNome)
            put(COLUMN_QUANTIDADE, novaQtd)
        }
        val result = db.update(TABLE_PRODUTOS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    // Excluir produto
    fun excluirProduto(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_PRODUTOS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }
}


