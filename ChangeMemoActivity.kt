package kr.co.lion.homework_1


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import kr.co.lion.homework_1.databinding.ActivityChangeMemoBinding


class ChangeMemoActivity : AppCompatActivity() {

    lateinit var activityChangeMemoBinding: ActivityChangeMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityChangeMemoBinding = ActivityChangeMemoBinding.inflate(layoutInflater)
        setContentView(activityChangeMemoBinding.root)

        setToolbar()
        setView()
        changeData()
    }


    // 툴바 설정
    fun setToolbar() {
        activityChangeMemoBinding.apply {
            toolbarChangeMemo.apply {
                // 타이틀
                title = "메모 수정"
                // Back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }

                // 메뉴
                inflateMenu(R.menu.menu_change_memo)
                setOnMenuItemClickListener {
                    changeData()
                    finish()
                    true
                }
            }
        }
    }

    // 뷰 설정
    fun setView() {
        activityChangeMemoBinding.apply {
            // TextView
            textFieldChangeTitle.apply {
                // 항목 순서값을 가져온다.
                val position = intent.getIntExtra("position", 0)
                // 포지션 번째 객체를 추출한다.
                val memo = Util.memoList[position]

                // 공통
                text = Editable.Factory.getInstance().newEditable(memo.title)


            }
            textFieldChangeContent.apply {
                // 항목 순서값을 가져온다.
                val position = intent.getIntExtra("position", 0)
                // 포지션 번째 객체를 추출한다.
                val memo = Util.memoList[position]
                text = Editable.Factory.getInstance().newEditable(memo.content)
            }
        }
    }

    // 수정 처리
    fun changeData(){
        // 위치값을 가져온다.
        val position = intent.getIntExtra("position", 0)
        // position 번째 객체를 가져온다.
        val memo = Util.memoList[position]

        activityChangeMemoBinding.apply {
            // 공통
            memo.title = textFieldChangeTitle.text.toString()
            memo.content = textFieldChangeContent.text.toString()

        }
    }
}