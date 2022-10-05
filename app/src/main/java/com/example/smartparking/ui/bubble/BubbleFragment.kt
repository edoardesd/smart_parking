package com.example.smartparking.ui.bubble

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartparking.R
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalUserType
import com.example.smartparking.internal.UserType
import com.example.smartparking.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_bubble.*

class BubbleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bubble, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initTitle()
        initUsers()
        initProfilePic()
    }

    private fun initProfilePic() {
        when(globalUserType){
            UserType.STUDENT_ARCHITECTURE -> iv_profile_pic.setImageResource(R.drawable.student_architecture)
            UserType.STUDENT_ENGINEERING -> iv_profile_pic.setImageResource(R.drawable.student_engineering)
            UserType.PROF_ARCHITECTURE -> iv_profile_pic.setImageResource(R.drawable.prof_architecture)
            UserType.PROF_ENGINEERING -> iv_profile_pic.setImageResource(R.drawable.prof_engineering)
            UserType.GUEST -> iv_profile_pic.setImageResource(R.drawable.guest)
        }    }

    private fun initUsers() {
        when(globalUserType){
            UserType.STUDENT_ARCHITECTURE -> setName("Eleonora", "Cantaluppoli", "10537326")
            UserType.STUDENT_ENGINEERING -> setName("Riccardo", "Pongo", "10410666")
            UserType.PROF_ARCHITECTURE -> setName("Daniel", "Reggia", "10338299")
            UserType.PROF_ENGINEERING -> setName("Matthew", "Chesane", "1032092")
            UserType.GUEST -> setName("Patricia", "Boltsun", "10638287")
        }
    }

    private fun setName(name: String, surname: String, code: String) {
        tv_name_surname.text = "$name $surname"
        tv_email_bubble.text = "$name.$surname@polimi.it".lowercase()
        tv_person_code.text = "$code"
    }

    private fun initTitle() {
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = "Bubble"
        }

        if(globalUserType == UserType.GUEST){
            ll_career.visibility = View.INVISIBLE;
        }
    }
}