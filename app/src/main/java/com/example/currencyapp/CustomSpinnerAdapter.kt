package com.example.currencyapp

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.currencyapp.databinding.CustomSpinnerBinding


class CustomSpinnerAdapter(private val activity:Activity,
                           private val itemList:List<SpinnerItemStructure>,
                            ):ArrayAdapter<SpinnerItemStructure>(activity,R.layout.custom_spinner ) {
    private lateinit var spinnerBinding: CustomSpinnerBinding


    override fun getCount(): Int {
        return itemList.size        //so luong item se co tren spinner
    }

    override fun getItem(position: Int): SpinnerItemStructure {
        return itemList[position]
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //position: vi tri cua item trong itemList
        //convertView: cau truc hien thi cua cac item trong spinner (o day la "custom_spinner.xml")
        //parent: cha cua spinner
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }


    private fun initView(position: Int, convertView: View?, parent: ViewGroup):View{
        val context = activity.layoutInflater
        spinnerBinding = CustomSpinnerBinding.inflate(context,parent,false)

        spinnerBinding.itemImageView.setImageResource(itemList[position].itemImage)
        spinnerBinding.itemNameView.text=itemList[position].itemName

        return spinnerBinding.root

    }
}
