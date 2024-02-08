package kr.co.lion.homework_1

import MemoData
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.homework_1.databinding.ActivityShowMemoBinding

class ShowMemoActivity : AppCompatActivity() {

    companion object {
        const val CHANGE_REQUEST_CODE = 1001 // 임의의 정수 값
    }

    lateinit var activityShowMemoBinding: ActivityShowMemoBinding

    // Activity 런처
    lateinit var changeActivityLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityShowMemoBinding = ActivityShowMemoBinding.inflate(layoutInflater)
        setContentView(activityShowMemoBinding.root)

        // ChangeActivity 러처 초기화
        changeActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // 수정 화면에서 돌아온 경우 필요한 처리를 수행합니다.
            }
        }

        setToolbar()
        setView()
        initView()



}

    // 툴바 설정
    fun setToolbar() {
        activityShowMemoBinding.apply {
            toolbarShowMemo.apply {
                // 타이틀
                title = "메모 보기"
                // Back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }

                // 메뉴
                inflateMenu(R.menu.menu_show_memo)
                setOnMenuItemClickListener {
                    // 사용자가 선택한 메뉴 항목의 id로 분기한다.
                    when(it.itemId){
                        // 수정
                        R.id.menu_show_change -> {
                            val changeintent = Intent(this@ShowMemoActivity, ChangeMemoActivity::class.java)

                            // 동물 순서값을 저정한다.
                            val position = intent.getIntExtra("position", 0)
                            changeintent.putExtra("position", position)

                            changeActivityLauncher.launch(changeintent)
                        }

                        // 삭제
                        R.id.menu_show_delete -> {
                            // 항목 순서 값을 가져온다.
                            val position = intent.getIntExtra("position", 0)
                            // 항목 번째 객체를 리스트에서 제거한다.
                            Util.memoList.removeAt(position)

                            Snackbar.make(activityShowMemoBinding.root, "삭제가 완료되었습니다", Snackbar.LENGTH_SHORT).show()
                            // 이전으로 돌아간다.
                            val resultIntent = Intent()
                            resultIntent.putExtra("position", position)

                            setResult(RESULT_OK, resultIntent)
                            finish()
                        }
                    }

                    true
                }
            }
        }
    }

    //입력 불가
    fun initView() {

        activityShowMemoBinding.apply {
            // 입력을 불가능하게 설정
            textFieldIShowTitle.isEnabled = false
            textFieldIShowDate.isEnabled = false
            textFieldIShowContent.isEnabled = false



        }
    }

    // 뷰 설정
    fun setView() {
        activityShowMemoBinding.apply {
            // TextView
            textFieldIShowTitle.apply {
                // 항목 순서값을 가져온다.
                val position = intent.getIntExtra("position", 0)
                // 포지션 번째 객체를 추출한다.
                val memo = Util.memoList[position]

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
                val dateString = dateFormat.format(memo.date)
                val editableDate = Editable.Factory.getInstance().newEditable(dateString)
                textFieldIShowDate.text = editableDate

                // 공통
                text = Editable.Factory.getInstance().newEditable(memo.title)


            }
            textFieldIShowContent.apply {
                // 항목 순서값을 가져온다.
                val position = intent.getIntExtra("position", 0)
                // 포지션 번째 객체를 추출한다.
                val memo = Util.memoList[position]
                text = Editable.Factory.getInstance().newEditable(memo.content)
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHANGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // 수정된 메모를 처리하는 로직을 여기에 추가합니다.
            if (data != null) {
                val updatedMemo = data.getParcelableExtra<MemoData>("updatedMemo")
                // 수정된 메모 처리
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 다른곳 갔다 왔을 경우 출력 내용을 다시 구성해준다.
        setView()
    }
}


