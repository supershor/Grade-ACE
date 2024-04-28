package com.om_tat_sat.grade_ace.Recycler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.om_tat_sat.grade_ace.Bsc_Agriculture_tab;
import com.om_tat_sat.grade_ace.Bsc_Horticulture_tab;
import com.om_tat_sat.grade_ace.Btech_Agriculture_Engineering_tab;
import com.om_tat_sat.grade_ace.frags.Graph_OGPA;
import com.om_tat_sat.grade_ace.frags.Graph_OGPA_Horticulture_Btech_Agriculture;
import com.om_tat_sat.grade_ace.frags.OGPA;
import com.om_tat_sat.grade_ace.frags.OGPA_Horticulture_Btech_Agriculture;
import com.om_tat_sat.grade_ace.frags.Overall_OGPA;
import com.om_tat_sat.grade_ace.frags.Overall_OGPA_Horticulture_Btech_Agriculture;

public class ViewPagerAdapter_horticulture_btech_agriculture extends FragmentStateAdapter {
    public ViewPagerAdapter_horticulture_btech_agriculture(@NonNull Bsc_Horticulture_tab fragment) {
        super(fragment);
    }
    public ViewPagerAdapter_horticulture_btech_agriculture(@NonNull Btech_Agriculture_Engineering_tab fragment) {
        super(fragment);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:return new Overall_OGPA_Horticulture_Btech_Agriculture();
            case 2:return new Graph_OGPA_Horticulture_Btech_Agriculture();
            case 0:
            default:return new OGPA_Horticulture_Btech_Agriculture();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
