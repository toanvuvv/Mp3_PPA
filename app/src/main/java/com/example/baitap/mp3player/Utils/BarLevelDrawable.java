// Copyright 2011 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package com.example.baitap.mp3player.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;



public final class BarLevelDrawable extends View {
  private ShapeDrawable mDrawable;
  private double mLevel = 0.1;

  final int[] segmentColors = {
      0xff5555ff,
      0xff5555ff,

      0xff00ff00,
      0xff00ff00,
      0xff00ff00,
      0xff00ff00,
      0xff00ff00,

      0xffffff00,
      0xffffff00,
      0xffffff00,
      0xffffff00,
      0xffffff00,

      0xffff0000,
      0xffff0000,
      0xffff0000};
  final int segmentOffColor = 0xff555555;

  public BarLevelDrawable(Context context, AttributeSet attrs) {
    super(context, attrs);
    initBarLevelDrawable();
  }

  public BarLevelDrawable(Context context) {
    super(context);
    initBarLevelDrawable();
  }

//  Thiết lập mức thanh. Mức độ phải ở trong phạm vi [0.0; 1.0], nghĩa là
//    0.0 không cho đèn LED sáng và 1.0 cho phép đo đầy đủ.
  public void setLevel(double level) {
    mLevel = level;
    invalidate();
  }

  public double getLevel() {
    return mLevel;
  }

  private void initBarLevelDrawable() {
    mLevel = 0.1;
  }


  //vẽ bar hình chữ nhật
  private void drawBar(Canvas canvas) {
    int padding = 1;
    int x = 0;
    int y = 15;

    int width = (int) (Math.floor(getWidth() / segmentColors.length))
        - (2 * padding);
    int height = 50;

    mDrawable = new ShapeDrawable(new RectShape());
    for (int i = 0; i < segmentColors.length; i++) {
      x = x + padding;
      if ((mLevel * segmentColors.length) > (i + 0.5)) {
        mDrawable.getPaint().setColor(segmentColors[i]);
      } else {
        mDrawable.getPaint().setColor(segmentOffColor);
      }
      mDrawable.setBounds(x, y, x + width, y + height);
      mDrawable.draw(canvas);
      x = x + width + padding;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    drawBar(canvas);
  }
}
