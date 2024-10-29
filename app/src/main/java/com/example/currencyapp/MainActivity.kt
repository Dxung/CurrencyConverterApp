package com.example.currencyapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.currencyapp.databinding.ActivityMainBinding
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currencyName:CurrencyName
    private lateinit var exchangeRateList: MutableList<CurrencyRateStructure>
    private lateinit var currencyExchanging: CurrencyExchanging
    private lateinit var editTextInput :EditText
    private lateinit var editTextOutput :EditText
    private lateinit var spinnerInput : Spinner
    private lateinit var spinnerOutput: Spinner
    private var isUpdateing: Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initProperty()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setupCustomSpinnerAdaptor(currencyName)         //tạo custom adaptor và gắn với Spinner


    }


    /*---------------------- Khởi tạo ban đầu ----------------------------*/

    /*--- khởi tạo tất cả thuộc tính ---*/
    private fun initProperty(){
        binding =ActivityMainBinding.inflate(layoutInflater)    // chuyển xml thành view và khởi tạo giá trị cho tham chiếu ViewBinding
        currencyName = CurrencyName()   //khởi tạo tên của các currency, bắt buộc phải khởi tạo sớm nhất có thể để dùng được các giá trị tên trong các hàm sau
        initFirstInOut() //khởi tạo giá trị ban đầu cho các input và output
        setupCurrencyExchanging() //truyền giá trị vào cho list tỷ giá bằng hàm ở phía dưới
        currencyExchanging = CurrencyExchanging(exchangeRateList)   //khởi tạo đối tượng giúp tính toán tiền trả về theo tỷ giá quy đổi

        addChangingListenerAndUpdate(editTextInput)     //thêm listener (cơ chế tự cập nhật ở bên trong)
        addChangingListenerAndUpdate(editTextOutput)    //thêm listener (cơ chế tự cập nhật ở bên trong)
    }


    /*--- khởi tạo giá trị ban đầu cho input và output ---*/
    private fun initFirstInOut(){
        editTextInput = binding.moneyInput1
        editTextOutput = binding.moneyInput2
        spinnerInput = binding.currencyDropList1
        spinnerOutput = binding.currencyDropList2


        binding.moneyInput1.setText("")
        binding.moneyInput2.setText("")
    }


    /*--- Gắn Adaptor cho các spinner ---*/
    private fun setupCustomSpinnerAdaptor(currencyName: CurrencyName) {
        val itemList = mutableListOf<SpinnerItemStructure>()
        itemList.add(SpinnerItemStructure(R.drawable.vietnam,currencyName.vnd))
        itemList.add(SpinnerItemStructure(R.drawable.usa,currencyName.usd))
        itemList.add(SpinnerItemStructure(R.drawable.china,currencyName.cny)) //CNY for international transaction whereas RMB for domestic transaction
        itemList.add(SpinnerItemStructure(R.drawable.japan,currencyName.jpy))
        itemList.add(SpinnerItemStructure(R.drawable.euro_union,currencyName.eur))
        itemList.add(SpinnerItemStructure(R.drawable.united_kingdom,currencyName.gbp))


        //gắn adaptor cho các spinner
        val customSpinnerAdapter = CustomSpinnerAdapter(this, itemList)
        binding.currencyDropList1.adapter=customSpinnerAdapter
        binding.currencyDropList2.adapter=customSpinnerAdapter


        //gắn hàm lắng nghe thay đổi cho các spinner
        addSelectedItemListener(binding.currencyDropList1)
        addSelectedItemListener(binding.currencyDropList2)



    }


    /*--- truyền giá trị vào cho list tỷ giá ---*/
    private fun setupCurrencyExchanging(){
        exchangeRateList = mutableListOf()      //khởi tạo đã, chưa khởi tạo sao truyền
        exchangeRateList.add(CurrencyRateStructure(currencyName.usd,currencyName.vnd,25399.0))
        exchangeRateList.add(CurrencyRateStructure(currencyName.cny,currencyName.vnd,3561.71))
        exchangeRateList.add(CurrencyRateStructure(currencyName.jpy,currencyName.vnd,161.12))
        exchangeRateList.add(CurrencyRateStructure(currencyName.eur,currencyName.vnd,27461.0))
        exchangeRateList.add(CurrencyRateStructure(currencyName.gbp,currencyName.vnd,32868.38))
        exchangeRateList.add(CurrencyRateStructure(currencyName.eur,currencyName.usd,1.08))
        exchangeRateList.add(CurrencyRateStructure(currencyName.gbp,currencyName.usd,1.29))
        exchangeRateList.add(CurrencyRateStructure(currencyName.usd,currencyName.cny,7.13))
        exchangeRateList.add(CurrencyRateStructure(currencyName.eur,currencyName.cny,7.69))
        exchangeRateList.add(CurrencyRateStructure(currencyName.gbp,currencyName.cny,9.23))
        exchangeRateList.add(CurrencyRateStructure(currencyName.usd,currencyName.jpy,153.44))
        exchangeRateList.add(CurrencyRateStructure(currencyName.cny,currencyName.jpy,21.51))
        exchangeRateList.add(CurrencyRateStructure(currencyName.eur,currencyName.jpy,165.48))
        exchangeRateList.add(CurrencyRateStructure(currencyName.gbp,currencyName.jpy,198.60))
        exchangeRateList.add(CurrencyRateStructure(currencyName.gbp,currencyName.eur,1.2))

        exchangeRateList.add(CurrencyRateStructure(currencyName.vnd,currencyName.vnd,1.0))
        exchangeRateList.add(CurrencyRateStructure(currencyName.usd,currencyName.usd,1.0))
        exchangeRateList.add(CurrencyRateStructure(currencyName.cny,currencyName.cny,1.0))
        exchangeRateList.add(CurrencyRateStructure(currencyName.jpy,currencyName.jpy,1.0))
        exchangeRateList.add(CurrencyRateStructure(currencyName.eur,currencyName.eur,1.0))
        exchangeRateList.add(CurrencyRateStructure(currencyName.gbp,currencyName.gbp,1.0))

    }



    /*--------------------------- Tính tiền và hiển thị ---------------------------*/

    /*--- thêm listener thay đổi cho edittext ---*/
    private fun addChangingListenerAndUpdate(thisEditText: EditText) {
        thisEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            //sau khi có thay đổi tại edittext này
            override fun afterTextChanged(s: Editable?) {
                ///Bắt buộc phải đang là đối tượng được focus, nếu không chỉ cần thay đổi một
                /// cái sẽ lập tức thay đổi cả 2 và gây ra lỗi do thay đổi 2 chiều liên tục:
                ///1 đổi -> 2 đổi (do 2 vừa được viết lại) -> 1 đổi (do 2 đổi :]]] )
                if(!isUpdateing && thisEditText.hasFocus()){    //isupdating = false để chắc rằng đây là thay đổi trên edittext, chứ không phải trên spinner nhưng focus vẫn ở trên edittext

                    //update lại input và output mới
                    updateInOutWhenNewEditText(thisEditText)

                    //dựa vào input và output mới để tính tiền
                    showMoneyAfterExchange()

                }

            }
        })
    }

    /*--- thêm listener thay đổi cho Spinner ---*/
    private fun addSelectedItemListener(thisSpinner: Spinner){
        thisSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                //điều kiện !IsUpdating để chắc rằng edittext không chen vào khi spinner đang là đối tượng thay đổi
                if(!isUpdateing){
                    isUpdateing = true



                    //tính tiền và hiển thị
                    showMoneyAfterExchange()

                    isUpdateing=false
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }



    /*--- func tính tiền và đẩy lên đầu ra ---*/
    fun showMoneyAfterExchange(){
        // Lấy lượng tiền input từ inputText
        val inputNumberText: String = editTextInput.text.toString().trim()

        // Kiểm tra nếu inputNumberText rỗng
        if (inputNumberText=="") {
            editTextOutput.setText("")
            return
        }

        // Chuyển đổi inputNumberText sang BigDecimal
        val inputMoney: BigDecimal = try {
            inputNumberText.toBigDecimal()
        } catch (e: NumberFormatException) {
            editTextOutput.setText("0") // Đặt đầu ra về 0 nếu có lỗi
            return
        }

        //tính tiền output thông qua currencyExchanging
        val outputMoney :BigDecimal = currencyExchanging.calculateMoney(getSelectedItemOfASpinner(spinnerInput).itemName,getSelectedItemOfASpinner(spinnerOutput).itemName,inputMoney)

        //đẩy text lên output
        editTextOutput.setText(outputMoney.toPlainString())



    }

    /*--- func update Spinner Input, Spinner output và editText output khi editText/spinner input thay đổi ---*/
    fun updateInOutWhenNewEditText(inputEditText: EditText){
        //đặt editText hiện tại làm editText input
        editTextInput = inputEditText

        //nếu edittext input là edittext1
        if(editTextInput == binding.moneyInput1){
            //edittext output là edittext2
            editTextOutput = binding.moneyInput2

            //spinner input là Spinner1
            spinnerInput = binding.currencyDropList1

            //spinner output là Spinner2
            spinnerOutput = binding.currencyDropList2

        }else if(editTextInput == binding.moneyInput2){
            //edittext output là edittext1
            editTextOutput = binding.moneyInput1

            //spinner input là Spinner2
            spinnerInput = binding.currencyDropList2

            //spinner output là Spinner1
            spinnerOutput = binding.currencyDropList1
        }
    }


    /*------------------- Tool khác -------------------*/
    /*--- func để lấy được item đang được chọn trên 1 spinner ---*/
    private fun getSelectedItemOfASpinner(whichSpinner: Spinner):SpinnerItemStructure{
        val itemPos: Int = whichSpinner.selectedItemPosition
        val item:SpinnerItemStructure = whichSpinner.adapter.getItem(itemPos) as SpinnerItemStructure
        return item
    }

}