package kr.co.lion.homework_1

import MemoData
import Util
import Util.Companion.memoList
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.homework_1.databinding.ActivityMainBinding
import kr.co.lion.homework_1.databinding.RowMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // InputActivity의 런처
    //반드시 oncreate안에서 구현해야한다.//
    lateinit var inputMemoActivityLauncher: ActivityResultLauncher<Intent>
    // ShowActivity의 런처
    lateinit var showMemoActivityLauncher: ActivityResultLauncher<Intent>
    // ChangeActivity의 런처
    lateinit var changeMemoActivityLauncher: ActivityResultLauncher<Intent>
    private val DELETE_REQUEST_CODE = 1


    // RecycerView를 구성하기 위한 리스트
    val recyclerViewList = mutableListOf<MemoData>()
    // 현재 항목을 구성하기 위해 사용한 객체가 Util.animalList의 몇번째 객체인지를 담을 리스트
    val recyclerViewIndexList = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setToolBar()
        setView()
        initData()
    }


    // 기본 데이터 및 객체 셋팅
    fun initData() {
        // InputActivity 런처
        //미래는 어떻게 될지 모르니 유지보수를 위해 런쳐 무조건 쓰는것을 권장//
        val contract1 = ActivityResultContracts.StartActivityForResult()
        inputMemoActivityLauncher = registerForActivityResult(contract1){
            // 작업 결과가 OK 라면
            if(it.resultCode == RESULT_OK){
                // 전달된 Intent객체가 있다면
                if(it.data != null){
                    //객체를 추출한다.
                    if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                        val memoData = it.data?.getParcelableExtra("memoData", MemoData::class.java)
                        Util.memoList.add(memoData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    } else {
                        val memoData = it.data?.getParcelableExtra<MemoData>("memoData")
                        Util.memoList.add(memoData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }



        // ShowInfoActivity 런처
        val contract2 = ActivityResultContracts.StartActivityForResult()
        showMemoActivityLauncher = registerForActivityResult(contract2) {

        }

        // ChangeInfoActivity 런처
        val contract3 = ActivityResultContracts.StartActivityForResult()
        changeMemoActivityLauncher = registerForActivityResult(contract3) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 삭제 후에 MainActivity로 돌아왔을 때 갱신
        if (requestCode == DELETE_REQUEST_CODE && resultCode == RESULT_OK) {
            // 새로운 정보를 RecyclerView에 추가
            val memoData = data?.getParcelableExtra<MemoData>("memoData")
            memoData?.let {
                Util.memoList.add(it)
                activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activityMainBinding.apply {
            recyclerViewList.clear()
            recyclerViewIndexList.clear()

            // 여기에 새로운 정보를 RecyclerView에 추가하는 로직을 넣어줍니다.
            for (memo in Util.memoList) {
                recyclerViewList.add(memo)
                recyclerViewIndexList.add(Util.memoList.indexOf(memo))
            }

            // 리사이클러뷰 갱신
            recyclerViewMain.adapter?.notifyDataSetChanged()
        }
    }

    //툴바 구성
    fun setToolBar(){
        activityMainBinding.apply {
            toolbarMain.apply {
                // 타이틀
                title = "메모 관리"
                // 메뉴
                inflateMenu(R.menu.menu_main)
                // 메뉴의 리스너
                setOnMenuItemClickListener {
                    // 메뉴의 id로 분기한다
                    when(it.itemId){
                        // 추가 메뉴
                        R.id.menu_main_add -> {
                            // InputActivity를 실행한다.
                            val inputIntent = Intent(this@MainActivity, InputMemoActivity::class.java)
                            inputMemoActivityLauncher.launch(inputIntent)
                        }
//                        else -> {
//                        //오류가 난다면 넣어준다. 비워놔도 됌//
//                        }
                    }
                    true
                }
            }
        }
    }

    // View 구성
    fun setView(){
        activityMainBinding.apply {
            // RecyclerView
            recyclerViewMain.apply {
                // 어뎁터 설정
                adapter = RecyclerViewMainAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(this@MainActivity)
                // 구분선
                val deco = MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>(){
        // ViewHolder
        inner class ViewHolderMain(rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root) {
            val rowMainBinding: RowMainBinding

            init {
                this.rowMainBinding = rowMainBinding

                this.rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                // 아이템 클릭 리스너 설정
                this.rowMainBinding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        // 클릭된 아이템의 포지션을 ShowMemoActivity로 전달
                        val showMenuIntent = Intent(this@MainActivity, ShowMemoActivity::class.java)
                        showMenuIntent.putExtra("position", position)
                        showMemoActivityLauncher.launch(showMenuIntent)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolderMain = ViewHolderMain(rowMainBinding)

            return viewHolderMain
        }

        override fun getItemCount(): Int {
//            return 100
            return memoList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
//             holder.rowMainBinding.textViewRowMainTitle.text = "메모 $position"
//             holder.rowMainBinding.textViewRowMainDate.text = "날짜 $position"
            holder.rowMainBinding.textViewRowMainTitle.text = "${memoList[position].title}"
            holder.rowMainBinding.textViewRowMainDate.text =  "${memoList[position].date}"
        }
    }
}







