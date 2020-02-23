package com.example.finalproject2.ui.main.main_tab_fragment.personal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalproject.personalPresenter
import com.example.finalproject2.R
import kotlinx.android.synthetic.main.fragment_personal.*

class PersonalFragment : Fragment() {

    private val mPresenter by lazy { personalPresenter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personalFragmentLogOutButton.setOnClickListener {
            mPresenter.logOut()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }
}
