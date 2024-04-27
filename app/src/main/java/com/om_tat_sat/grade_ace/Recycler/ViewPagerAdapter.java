package com.om_tat_sat.grade_ace.Recycler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.om_tat_sat.grade_ace.Bsc_Agriculture_tab;
import com.om_tat_sat.grade_ace.frags.OGPA;
import com.om_tat_sat.grade_ace.frags.Graph_OGPA;
import com.om_tat_sat.grade_ace.frags.Overall_OGPA;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull Bsc_Agriculture_tab fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:return new Overall_OGPA();
            case 2:return new Graph_OGPA();
            case 0:
            default:return new OGPA();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
