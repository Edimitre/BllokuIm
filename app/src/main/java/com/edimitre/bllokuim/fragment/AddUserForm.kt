package com.edimitre.bllokuim.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.model.MainUser
import com.edimitre.bllokuim.systemservices.SystemService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_user_form.*
import javax.inject.Inject

@AndroidEntryPoint
class AddUserForm : AppCompatDialogFragment() {

    @Inject
    lateinit var systemService: SystemService

    private var listener: AddUserListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_user_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        if (systemService.dbExist()) {
            btn_reload_user_db.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {

        btn_save_user.setOnClickListener {

            if (inputIsOk()) {

                val name = input_name.text.toString()
                val totalAmountOfMoney = input_total_existing_value.text.toString().toDouble()
                val user = MainUser(0, name, totalAmountOfMoney)
                listener!!.addUser(user)

                dismiss()
            } else {

                Toast.makeText(
                    activity,
                    "Dicka nuk shkoi sic duhet....! ju lutem kontrolloni te dhenat !",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

        btn_reload_user_db.setOnClickListener {

            opendReloadDbDialog()
        }
    }

    private fun opendReloadDbDialog() {
        val dialog = AlertDialog.Builder(requireActivity())

        dialog.setTitle("BllokuIm")
        dialog.setMessage("U gjenden te dhena te ruajtura !!\nDeshironi ti ngarkoni ?")

        dialog.setPositiveButton("Ngarko", DialogInterface.OnClickListener { _, _ ->
            systemService.importDatabase()
            systemService.restartApp()
        })

        dialog.setNegativeButton("Mbyll", DialogInterface.OnClickListener { _, _ ->
            dismiss()
        })

        dialog.show()

    }

    private fun inputIsOk(): Boolean {

        return when {
            input_name.text.toString() == "" -> {

                Toast.makeText(activity, "Emri s'mund te jete bosh", Toast.LENGTH_SHORT).show()

                false

            }
            input_total_existing_value.text.toString() == "" -> {
                Toast.makeText(
                    activity,
                    "Vlera s'mund te jete bosh...vendos 0 nqs skeni gjendje",
                    Toast.LENGTH_SHORT
                ).show()

                false

            }
            else -> {
                true
            }
        }

    }

    interface AddUserListener {
        fun addUser(mainUser: MainUser)

    }

    override fun onAttach(context: Context) {
        listener = try {
            context as AddUserListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "shto listener")
        }
        super.onAttach(context)
    }

}