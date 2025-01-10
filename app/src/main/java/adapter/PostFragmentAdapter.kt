package adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.talktrends.DashboardActivity
import fragment.ForyouFragment
import fragment.PopularFragment
import fragment.RecentFragment

class PostFragmentAdapter(activity: DashboardActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PopularFragment() // Popular Fragment
            1 -> ForyouFragment() // For You Fragment
            2 -> RecentFragment() // Recent Fragment
            else -> PopularFragment()
        }
    }
}

