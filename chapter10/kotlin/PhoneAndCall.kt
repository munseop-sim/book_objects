import java.time.Duration
import java.time.LocalDateTime
import java.util.DoubleSummaryStatistics

/**
 * 개별 통화 기간을 저장하는 Call 클래스
 */
class Call(private val from: LocalDateTime,
           private val to:LocalDateTime){
    fun getDuration(): Duration = Duration.between(from, to)
    fun getFrom():LocalDateTime = this.from
}

/**
 * 통화요금 계산을 위한 Phone 클래스 (최초 요구사항)
 * (요구사항 추가) 통화요금에 대한 세금부과
 */
open class Phone{
    protected var amount:Double? = null
    protected var seconds:Duration? = null
    protected val calls:ArrayList<Call> = ArrayList()

    constructor(){}

    constructor(amount:Double,
                seconds: Duration
    ){
        this.amount = amount
        this.seconds = seconds
    }


    fun getCalls():List<Call> = this.calls
    fun call(call:Call){
        this.calls.add(call)
    }

    open fun calculateFee():Double {
        var result:Double = 0.0
        this.calls.forEach{
            var calculatedValue = amount!!.times(other = it.getDuration().seconds / this.seconds!!.seconds)
            result += calculatedValue
         }
        return result
    }
}

class NightlyDiscountPhone(private val nightlyAmount: Double, val regularAmount: Double, val c_seconds: Duration)
    :Phone(regularAmount, c_seconds){
    private val LATE_NIGHT_HOUR:Int = 22

    override fun calculateFee(): Double {
        var result = super.calculateFee()
        this.calls.forEach{
            if(it.getFrom().hour >= this.LATE_NIGHT_HOUR){
                result = result.plus(
                    amount!!.minus(this.nightlyAmount).times(
                        it.getDuration().seconds / this.seconds!!.seconds
                    )
                )
            }
        }

        return result
    }
}



/**
 * 10초당 5원씩 부과되는 요금제에 가입한 사용자가 각각 1분동안 두번 통화를 한 경우
 *
 * (요구사항 추가) 통화요금에 대한 세금부과 - 코드 중복 발생()
 * (코드개선) 중복코드를 피하기 위해 타입코드를 사용하여 클래스를 합침. 하지만 타입코드를 사용함으로 인해 낮은 응집도와 높은 결합또를 가지는 구조가 되었음
 */
fun 요구사항() {
    val phone: Phone = Phone(5.0, Duration.ofSeconds(10))
    val nightlyPhone = NightlyDiscountPhone(3.0,5.0, Duration.ofSeconds(10))
    nightlyPhone.call(
        Call(
            LocalDateTime.of(2018, 1, 1, 22, 10, 0),
            LocalDateTime.of(2018, 1, 1, 22, 11, 0)
        )
    )

    nightlyPhone.call(
        Call(
            LocalDateTime.of(2018, 1, 2, 12, 10, 0),
            LocalDateTime.of(2018, 1, 2, 12, 11, 0)
        )
    )
    nightlyPhone.getCalls().forEach{
        phone.call(it)
    }

    println("일반 요금 : ${phone.calculateFee()}")
    println("심야할인 적용 요금 : ${nightlyPhone.calculateFee()}")

}


fun main(){
    요구사항()
}