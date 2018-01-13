package work.nityc_nyuta.sirasunakondate

import android.app.VoiceInteractor
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    //onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        setTitle("白砂寮献立")
    }

    //画面表示時
    override fun onResume() {
        super.onResume()

        //API接続 (GetAPI -> KondateShow)
        GetAPI("all",DatePlusToString(0));
    }

    //戻るボタン
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

    //ナビゲーションドロワー
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_search -> {

            }
            R.id.nav_calender_open -> {

            }
            R.id.nav_credit -> {

            }
            R.id.nav_setting -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    //日時を指定日加算してStr型で返す
    fun DatePlusToString(delay: Int): String{
        val calender = Calendar.getInstance()
        calender.add(Calendar.DAY_OF_MONTH,delay)
        val date = listOf<Int>(calender.get(Calendar.YEAR),
                                     calender.get(Calendar.MONTH)+1,
                                     calender.get(Calendar.DAY_OF_MONTH))
        return date[0].toString() + "," + date[1].toString() + "," + date[2].toString()
    }

    //献立表示
    fun KondateShow(response_json: JSONObject){
        val menu = response_json.getJSONObject("menu")
        val breakfast = menu.getJSONArray("breakfast")
        val lunch = menu.getJSONArray("lunch")
        val dinner = menu.getJSONArray("dinner")
        Toast.makeText(this,dinner.toString(), LENGTH_LONG).show()
    }

    //API接続
    fun GetAPI(isbn: String, keys: String){
        //URL設定
        var API_URL = "http://nityc-nyuta.work/sirasuna_kondateAPI_prototype/"
        val key = keys.split(",")
        if(isbn == "all"){
            API_URL += "all?year=" + key[0] + "&month=" + key[1] + "&day=" + key[2]
        }

        //接続
        val queue = Volley.newRequestQueue(this)
        val params: JSONObject = JSONObject()
        val request = JsonObjectRequest(Request.Method.GET, API_URL, params,
                Response.Listener<JSONObject> { response ->
                    KondateShow(response)
                },
                Response.ErrorListener { volleyError ->
                    Toast.makeText(this, volleyError.toString(), LENGTH_SHORT).show()
                }
        )
        queue.add(request)
        queue.start()
    }
}