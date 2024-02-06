package com.example.snake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*import com.example.snake.GameModal;
import com.example.snake.GameView;
import com.example.snake.R;*/

import java.util.ArrayList;

public class GameRvAdapter extends RecyclerView.Adapter<GameRvAdapter.ViewHolder> {

    private ArrayList<GameModal> gameModalArrayList;
    private Context context;

    public GameRvAdapter(ArrayList<GameModal> gameModalArrayList, Context context) {
        this.gameModalArrayList = gameModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gane_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameModal modal = gameModalArrayList.get(position);
        holder.playerNameText.setText(String.format(context.getResources().getString(R.string.player_name), modal.getPlayerName()));
        holder.pointText.setText(String.format(context.getResources().getString(R.string.points), Float.toString(modal.getPoints())));
        holder.startGameText.setText(String.format(context.getResources().getString(R.string.start_game), Long.toString(modal.getStartGameTime())));
    }

    @Override
    public int getItemCount() {
        return gameModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView playerNameText, pointText, startGameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameText = itemView.findViewById(R.id.player_name_text);
            pointText = itemView.findViewById(R.id.points_text);
            startGameText = itemView.findViewById(R.id.start_game_text);
        }
    }
}
