package com.example.colze.examsdk.core;

/**
 * Created by tong on 15/11/18.
 * 难度
 */
public enum Difficulty {
   easy(1),middle(2),hard(3);

   Difficulty(int code) {
      this.code = code;
   }

   private int code;

   public int getCode() {
      return code;
   }

   public static Difficulty valueOf(int code) {
      for (Difficulty difficulty : Difficulty.values()) {
         if (difficulty.getCode() == code) {
            return difficulty;
         }
      }
      return null;
   }
}
