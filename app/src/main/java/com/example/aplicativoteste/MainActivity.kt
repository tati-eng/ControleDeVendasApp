package com.example.aplicativoteste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val etNomeProduto = findViewById<EditText>(R.id.etNomeProduto)
        val etQuantidade = findViewById<EditText>(R.id.etQuantidade)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnVerProdutos = findViewById<Button>(R.id.btnVerProdutos)

        // Botão para salvar produto no banco
        btnSalvar.setOnClickListener {
            val nomeProduto = etNomeProduto.text.toString()
            val quantidadeStr = etQuantidade.text.toString()

            if (nomeProduto.isEmpty() || quantidadeStr.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                val quantidade = quantidadeStr.toInt()
                val sucesso = dbHelper.addProduto(nomeProduto, quantidade)

                if (sucesso) {
                    Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    etNomeProduto.text.clear()
                    etQuantidade.text.clear()
                } else {
                    Toast.makeText(this, "Erro ao salvar produto!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botão para abrir tela que lista produtos
        btnVerProdutos.setOnClickListener {
            val intent = Intent(this, ListaProdutosActivity::class.java)
            startActivity(intent)
        }
    }
}



