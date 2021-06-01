import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.events_and_reminders.*

class AddOptionBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var toggle: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.events_and_reminders, container, false)

        materialButton_toDo.isSelected = true
        materialButton_toDo.setOnClickListener {

        }
        materialButton_today.setOnClickListener {

        }

        return view;
    }
}