package com.e.cryptocracy.coinDetail.coinUtil

import com.google.android.material.tabs.TabLayout

class TabSelection(
    val tabSelection: (key: String) -> Unit,
) : TabLayout.OnTabSelectedListener {

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.apply {
            tabSelection(this.text.toString())
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {


    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

}