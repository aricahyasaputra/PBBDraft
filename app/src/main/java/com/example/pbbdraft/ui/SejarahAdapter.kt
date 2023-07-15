package com.example.pbbdraft.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pbbdraft.databinding.SejarahAdapterBinding
import com.example.pbbdraft.room.Constant
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.room.Sejarah

class SejarahAdapter(private val sejarah: ArrayList<Sejarah>, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<SejarahAdapter.sejarahViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SejarahAdapter.sejarahViewHolder {
        val binding = SejarahAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return sejarahViewHolder(binding)
    }

    override fun getItemCount() = sejarah.size
    override fun onBindViewHolder(holder: sejarahViewHolder, position: Int) {
        val seja = sejarah[position]

        holder.binding.namaPemilik.text = seja.namaObjekPajak
        holder.binding.NOC.text = "No. C: " + seja.noC
        holder.binding.nomorSeri.text = "Seri : " + seja.noSeri
        holder.binding.nomorUrut.text = seja.noUrut

        holder.binding.namaPemilik.setOnClickListener{
            listener.onClik( seja )
        }

        holder.binding.iconEdit.setOnClickListener{
            listener.onUpdate( seja )
        }
        holder.binding.iconDelete.setOnClickListener{
            listener.onDelete( seja )
        }
    }

    class sejarahViewHolder(val binding: SejarahAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(list: List<Sejarah>){
        sejarah.clear()
        sejarah.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClik(sejarah: Sejarah)
        fun onUpdate(sejarah: Sejarah)
        fun onDelete(sejarah: Sejarah)
    }
}