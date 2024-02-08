package kr.co.lion.homework_1

import MemoData
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.homework_1.databinding.ActivityInputMemoBinding
import java.text.DateFormat
import kotlin.concurrent.thread
import java.util.Date

class InputMemoActivity : AppCompatActivity() {

    lateinit var activityInputMemoBiding : ActivityInputMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityInputMemoBiding = ActivityInputMemoBinding.inflate(layoutInflater)
        setContentView(activityInputMemoBiding.root)

        setToolbar()
        setView()
    }

    // 툴바
    fun setToolbar(){
        activityInputMemoBiding.apply {
            toolbarInput.apply {
                // 타이틀
                title = "메모 작성"
                // Back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_input_memo)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_input_memo_done -> {
                            processInputDone()
                        }
                    }
                    true
                }
            }
        }
    }
    // View 설정
    fun setView(){
        // 뷰에 포커스를 준다.
        activityInputMemoBiding.apply {
            textFieldInputTitle.requestFocus()
            // 키보드를 올린다.
            // 이 때, View를 지정해야한다.

            showSoftInput(textFieldInputTitle)

            // 엔터키를 누르면 입력 완료 처리를 한다.
            textFieldInputContent.setOnEditorActionListener { v, actionId, event ->
                processInputDone()
                true
            }
        }
    }

    // 입력 완료 처리
    fun processInputDone(){

        activityInputMemoBiding.apply {
            // 사용자가 입력한 내용을 가져온다
            val title = textFieldInputTitle.text.toString()
            val content = textFieldInputContent.text.toString()

            // 입력 검사
            if(title.isEmpty()){
                showDialog("제목 입력 오류", "제목을 입력해주세요", textFieldInputTitle)
                return
            }
            if(content.isEmpty()){
                showDialog("내용 입력 오류", "내용을 입력해주세요", textFieldInputContent)
                return
            }
            // 현재 시간을 가져와서 Date 객체로 생성
            val currentDate = Date(System.currentTimeMillis())

            // 입력받은 정보와 현재 시간을 객체에 담아 준다.
            val memoData = MemoData(title, content, currentDate)



            Snackbar.make(activityInputMemoBiding.root, "등록이 완료되었습니다", Snackbar.LENGTH_SHORT).show()
            // 이전으로 돌아간다.
            val resultIntent = Intent()
            resultIntent.putExtra("memoData", memoData)


            setResult(RESULT_OK, resultIntent)
            finish()

        }
    }

    // 다이얼로그를 보여주는 메서드
    fun showDialog(title:String, message:String, focusView: TextInputEditText){
        // 다이얼로그를 보여준다.
        val builder = MaterialAlertDialogBuilder(this@InputMemoActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                //마우스 올려서 ramda로 refactor해서 매개변수 세팅//
                focusView.setText("")
                focusView.requestFocus()
                showSoftInput(focusView)
            }
        }
        builder.show()
    }

    // 포커스를 주고 키보드를 올려주는 메서드
    fun showSoftInput(focusView: TextInputEditText){
        thread {
            SystemClock.sleep(1000)
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(focusView, 0)
        }
    }
}