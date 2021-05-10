package com.app.spaceitemdeliveryapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.spaceitemdeliveryapp.R
import com.app.spaceitemdeliveryapp.ui.main.create_spacecraft.CreateSpaceCraftFragment
import com.app.spaceitemdeliveryapp.ui.main.space_travel.SpaceTravelFragment
import com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.planets.PlanetsFragment
import com.app.spaceitemdeliveryapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var isBackStackNull = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        if (Utils.getIsSpacecraftCreated(this))
            openTravelsPage()
        else
            openCreateSpacecraftPage()
    }

    private fun openCreateSpacecraftPage(){
        val createSpaceCraftFragment = CreateSpaceCraftFragment()
        replaceFragment(createSpaceCraftFragment, "CreateSpaceCraftFragment")

    }

    private fun openTravelsPage(){
        val spaceTravelFragment = SpaceTravelFragment()
        replaceFragment(spaceTravelFragment, "SpaceTravelFragment")

    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment, tag)
        }.commit()
    }


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Uygulama Kapatılacak")
            .setMessage("Uygulamayı kapatmak istiyor musunuz?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Kapat"){ dialogInterface, _ ->
                dialogInterface.cancel()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            .setNegativeButton("İptal Et"){ dialogInterface, _ ->
                dialogInterface.cancel()
            }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}