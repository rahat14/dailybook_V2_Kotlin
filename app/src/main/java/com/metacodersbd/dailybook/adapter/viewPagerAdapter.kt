package com.metacodersbd.dailybook.adapter


import android.icu.text.CaseMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class viewPagerAdapter (manger : FragmentManager) : FragmentPagerAdapter (manger, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    private  val mfragment: ArrayList<Fragment> = ArrayList()
    private  val mFragmentTitle : ArrayList <String> = ArrayList()


    fun addFrament(fragment: Fragment  , title: String)
    {
        mfragment.add(fragment)
        mFragmentTitle.add(title)
    }

    override fun getItem(position: Int): Fragment {

        return  mfragment.get(position)
         }

    override fun getCount(): Int {

        return mfragment.size
        }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitle.get(position)
    }
}