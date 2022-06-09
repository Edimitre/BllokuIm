package com.edimitre.bllokuim.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.adapter.DescriptionAdapter
import com.edimitre.bllokuim.data.model.Description
import com.edimitre.bllokuim.data.model.Expense
import com.edimitre.bllokuim.data.viewModel.DescriptionViewModel
import com.edimitre.bllokuim.data.viewModel.MainUserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_expense.*
import java.util.*

@AndroidEntryPoint
class DescriptionActivity : AppCompatActivity() ,DescriptionAdapter.OnDescriptionClickListener{

    private lateinit var adapter: DescriptionAdapter

    private lateinit var descriptionViewModel: DescriptionViewModel

    private var TAG = "BllokuIm => "

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        initViewModel()

        setToolbar()

        initRecyclerView()

        setListeners()

        showDescriptions()
    }

    private fun initViewModel(){
        descriptionViewModel = ViewModelProvider(this)[DescriptionViewModel::class.java]

    }

    private fun setToolbar(){
        setSupportActionBar(description_toolbar)

        supportActionBar?.title = "Pershkrimet"

        description_toolbar.setTitleTextColor(Color.WHITE)
    }

    private fun initRecyclerView(){

        adapter = DescriptionAdapter(this)

        descriptions_recycler_view.setHasFixedSize(true)
        descriptions_recycler_view.layoutManager = LinearLayoutManager(this)
        descriptions_recycler_view.adapter = adapter


    }

    private fun setListeners(){
        btn_add_description.setOnClickListener{
            openAddDescriptionDialog()
        }
    }

    private fun openAddDescriptionDialog() {
        val inputName = EditText(this)
        inputName.hint = "Emri Pershkrimit"
        inputName.inputType = InputType.TYPE_CLASS_TEXT

        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        )
        dialog.setView(inputName)
        dialog.setTitle("Shto Pershkrim")
        dialog.setMessage(
            "Vendosni nje pershkrim\n" +
                    "si psh :PAZAR_SHPORTE, KARBURANT,KESTI_KREDISE, etj"
        )
        dialog.setPositiveButton("Shto") { _, _ ->

            val name = inputName.text.toString().trim().uppercase(Locale.getDefault())
            if (name.isNotEmpty()) {
                val description = Description(0,name)
                descriptionViewModel.saveDescription(description)
                Toast.makeText(this, "pershkrimi : $name u shtua me sukses", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Emri nuk mund te jete bosh", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.setNegativeButton("Mbyll") { _, _ ->

        }
        dialog.show()
    }

    private fun showDescriptions(){

        descriptionViewModel.descriptionList.observe(this){adapter.putDescriptions(it)}

        enableTouchFunctions()

    }





    // method inherited from adapter
    override fun onDescriptionClicked(description: Description) {

        Log.e(TAG, "description clicked => " + description.name )
    }

    private fun enableTouchFunctions() {
        itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val description:Description? =
                        adapter.getDescriptionByPos(viewHolder.adapterPosition)

                    if (description != null) {

                        val alertDialog = MaterialAlertDialogBuilder(
                            this@DescriptionActivity,
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
                        )

                        alertDialog.setTitle("Pershkrimi : ${description.name} " )

                        alertDialog.setMessage(
                            "Deshironi Ta Fshini ?\n" +
                                    "KUJDES! ..ky veperim nuk mund te rikthehet!"
                        )
                        alertDialog.setPositiveButton("Fshij") { _, _ ->

                            descriptionViewModel.deleteDescription(description)


                            Toast.makeText(
                                applicationContext,
                                "pershkrimi ${description.name} u fshi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        alertDialog.setNegativeButton("mbyll") { _, _ ->


                        }

                        alertDialog.setOnDismissListener {
                            showDescriptions()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(descriptions_recycler_view)
    }


}