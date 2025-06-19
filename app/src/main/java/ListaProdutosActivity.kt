package com.example.aplicativoteste

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaProdutosActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ProdutoAdapter
    private lateinit var rvProdutos: RecyclerView
    private lateinit var etBuscar: EditText
    private lateinit var tvRelatorio: TextView
    private var listaProdutos = listOf<Produto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produtos)

        dbHelper = DatabaseHelper(this)
        rvProdutos = findViewById(R.id.rvProdutos)
        etBuscar = findViewById(R.id.etBuscar)
        tvRelatorio = findViewById(R.id.tvRelatorio)

        listaProdutos = dbHelper.getTodosProdutos()
        exibirRelatorio(listaProdutos)

        adapter = ProdutoAdapter(listaProdutos.toMutableList(), onItemClick = { produto ->
            mostrarDialogoEditarExcluir(produto)
        })

        rvProdutos.layoutManager = LinearLayoutManager(this)
        rvProdutos.adapter = adapter

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().lowercase()
                val filtrados = listaProdutos.filter {
                    it.nome.lowercase().contains(texto)
                }
                adapter.atualizarLista(filtrados)
                exibirRelatorio(filtrados)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun mostrarDialogoEditarExcluir(produto: Produto) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar ou Excluir")

        val layout = layoutInflater.inflate(R.layout.dialog_editar_produto, null)
        val etNome = layout.findViewById<EditText>(R.id.etEditNome)
        val etQtd = layout.findViewById<EditText>(R.id.etEditQuantidade)

        etNome.setText(produto.nome)
        etQtd.setText(produto.quantidade.toString())

        builder.setView(layout)
        builder.setPositiveButton("Salvar") { _: DialogInterface, _: Int ->
            val novoNome = etNome.text.toString()
            val novaQtd = etQtd.text.toString().toIntOrNull() ?: 0
            dbHelper.atualizarProduto(produto.id, novoNome, novaQtd)
            atualizarLista()
        }

        builder.setNegativeButton("Excluir") { _: DialogInterface, _: Int ->
            dbHelper.excluirProduto(produto.id)
            atualizarLista()
        }

        builder.setNeutralButton("Cancelar", null)
        builder.show()
    }

    private fun atualizarLista() {
        listaProdutos = dbHelper.getTodosProdutos()
        adapter.atualizarLista(listaProdutos)
        exibirRelatorio(listaProdutos)
    }

    private fun exibirRelatorio(lista: List<Produto>) {
        val totalItens = lista.size
        val somaQtd = lista.sumOf { it.quantidade }
        tvRelatorio.text = "Total de produtos: $totalItens | Total em estoque: $somaQtd"
    }
}









