package com.example.pepperapp28aprile.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.example.pepperapp28aprile.R;
import com.example.pepperapp28aprile.card.CategoryCardView;
import com.example.pepperapp28aprile.models.Categoria;

public class CardPresenter extends Presenter {

  private Context mContext;

  public CardPresenter(Context c) {
    this.mContext = c;
  }

  static class ViewHolder extends Presenter.ViewHolder {

    public ViewHolder(View view) {
      super(view);
      CategoryCardView mCardView = (CategoryCardView) view;
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent) {

    mContext = parent.getContext();
    CategoryCardView cardView = new CategoryCardView(mContext);
    cardView.setFocusable(true);
    cardView.setFocusableInTouchMode(true);

    return new ViewHolder(cardView);
  }

  @Override
  public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {

    Categoria card = (Categoria) item;

    TextView primaryText = viewHolder.view.findViewById(R.id.txt_category);
    TextView textgame = viewHolder.view.findViewById(R.id.txt_game);

    final ImageView imageView = viewHolder.view.findViewById(R.id.img_category);

    primaryText.setText(card.getNomeCategoia());
    textgame.setText(card.getNomeGioco());
    imageView.setImageDrawable(mContext.getDrawable(card.getImg()));

  }

  @Override
  public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

  }

}