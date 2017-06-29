package telegramplus.Uplist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ir.mmnotimm.telegramplus.R;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.tgnet.TLRPC;

import java.util.List;



public class lastChatAdapter  extends RecyclerView.Adapter<lastChatAdapter.MyViewHolder> {

    private List<TLRPC.TL_dialog> dialogs;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name;
        public ImageView user_image;

        public MyViewHolder(View view) {
            super(view);
            user_image = (ImageView) view.findViewById(R.id.user_image);
            user_name = (TextView) view.findViewById(R.id.user_name);
        }
    }


    public lastChatAdapter(List<TLRPC.TL_dialog> dialogs) {
        this.dialogs = dialogs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_user_item, parent, false);

        return new MyViewHolder(itemView);
    }


    public void onBindViewHolder(MyViewHolder holder, int position) {
        TLRPC.TL_dialog d = dialogs.get(position);


        holder.user_name.setText(String.valueOf(d.last_message_date));

    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

}