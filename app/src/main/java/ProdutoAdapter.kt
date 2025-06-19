package com.example.aplicativoteste

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdutoAdapter(
    private var lista: MutableList<Produto>,
    private val onItemClick: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNome)
        val tvQuantidade: TextView = itemView.findViewById(R.id.tvQuantidade)

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onItemClick(lista[pos])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = lista[position]
        holder.tvNome.text = produto.nome
        holder.tvQuantidade.text = "Qtd: ${produto.quantidade}"
    }

    override fun getItemCount(): Int = lista.size

    fun atualizarLista(novaLista: List<Produto>) {
        lista = novaLista.toMutableList()
        notifyDataSetChanged()
    }
}
