package utils.jhy.ledger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import utils.jhy.ledger.data.FirestoreQuery;
import utils.jhy.ledger.data.MainData;

public class ListActivity extends BaseActivity {

    RecyclerView LIST_RV;
    RecyclerView.LayoutManager manager;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        LIST_RV = findViewById(R.id.LIST_RECYC);
        // use a linear layout manager
        manager = new LinearLayoutManager(this);
        LIST_RV.setLayoutManager(manager);

        FirestoreQuery.getData(this);
//        FirestoreQuery.setCate();
    }

    public void dataUpdate(ArrayList<MainData> data) {
        Log.i("jhy","!!!!!!!!data");
        adapter = new ListAdapter(data);
        LIST_RV.setAdapter(adapter);
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private ArrayList<MainData> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mTextView = view.findViewById(R.id.LIST_TITLE);
            }
        }

        public ListAdapter(ArrayList<MainData> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.raw_list, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mDataset.get(position).getComment());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

}
