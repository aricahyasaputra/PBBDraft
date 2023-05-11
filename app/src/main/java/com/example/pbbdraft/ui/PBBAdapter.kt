package com.example.pbbdraft.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pbbdraft.databinding.ActivityMainBinding
import com.example.pbbdraft.databinding.AdapterPbbBinding
import com.example.pbbdraft.room.PBB


class PBBAdapter (private val pajaks: ArrayList<PBB>, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<PBBAdapter.pajakViewHolder>(){

    private lateinit var binding: AdapterPbbBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pajakViewHolder {
        val binding = AdapterPbbBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return pajakViewHolder(binding)
    }

    override fun getItemCount() = pajaks.size
    override fun onBindViewHolder(holder: pajakViewHolder, position: Int) {
        with(holder){
           val pajak = pajaks[position]

            holder.binding.namaPemilik.text = pajak.namaWajibPajak
            holder.binding.NOP.text = pajak.NOP
            holder.binding.Alamat.text = pajak.alamatObjekPajak
            holder.binding.luasTanah.text = pajak.luasObjekPajak.toString()
            holder.binding.pajakDitetapkan.text = pajak.pajakDitetapkan.toString()
            holder.binding.namaPemilik.setOnClickListener{
                listener.onClik( pajak )
            }
            holder.binding.iconEdit.setOnClickListener{
                listener.onUpdate( pajak )
            }
            holder.binding.iconDelete.setOnClickListener{
                listener.onDelete( pajak )
            }
        }

    }
    class pajakViewHolder(val binding: AdapterPbbBinding) : RecyclerView.ViewHolder(binding.root)
    fun setData(list: List<PBB>){
        pajaks.clear()
        pajaks.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener{
        fun onClik(pajak : PBB)
        fun onUpdate(pajak : PBB)
        fun onDelete(pajak : PBB)
    }
}