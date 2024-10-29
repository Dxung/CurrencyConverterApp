package com.example.currencyapp
import java.math.BigDecimal
import java.math.RoundingMode

class CurrencyExchanging(private val exchangeRateList: List<CurrencyRateStructure>) {



    //first Type: mã tiền đầu tiên
    //second Type: mã tiền thứ 2
    fun calculateMoney(firstType: String, secondType:String, inMoneyAmount: BigDecimal):BigDecimal {

        //setscale: làm tròn đến 7 chữ số thập phân sau dấu phẩy
        //stripTrailingZeros(): loại bỏ số 0 thừa
        return (getMoneyExchangeRate(firstType,secondType).toBigDecimal()*inMoneyAmount).setScale(7,RoundingMode.HALF_UP).stripTrailingZeros()
    }

    private fun getMoneyExchangeRate(firstType: String,secondType:String): Double {
        for (data in exchangeRateList){
            if (firstType == data.firstCurrencyType && secondType == data.secondCurrencyType){
                return data.exchangeRate
            }else if(firstType == data.secondCurrencyType && secondType == data.firstCurrencyType){
                return 1/data.exchangeRate
            }
        }
        return 0.0
    }

}