package com.it_finne.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /* 実際はコマンドライン引数や、標準入力、またはファイルなどから検索文字列などの情報を読み込むのが普通だと思います */
        String filePath = "text.txt";
        String[] searchWord = { "専門学校", "東京", "大学", "大学院" };

        String selectedWord = Selector.select(searchWord);

        TextSearcher searcher = new TextSearcher(filePath);
        List<String> result = searcher.search(selectedWord);

        if (result.size() > 0) {
            for (String s : result) {
                System.out.println(s);
            }
        }
        else {
            System.out.println("該当なし");
        }
    }
}

class Selector {
    /* インスタンス化させなくする */
    private Selector() {
    }

    /**
     * 引数で与えられた配列から要素を1つ選択し、選択された要素を返します。
     *
     * 具体的な例として、5つの文字列が格納された配列を渡した場合、その中から1つを選び、
     * 選ばれた文字列が結果としてリターンされます。
     *
     * 引数の配列が要素数0の場合、例外がスローされます。
     *
     * @param elements - 要素を選択する配列
     * @throws IllegalArgumentException
     *                 - 配列の長さが0のとき
     *
     * @return         - 選択された要素
     */
    static <E> E select(E[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("no elements in the array");
        }

        Scanner sc = new Scanner(System.in);

        String input;
        do {
            /* 引数で与えられた配列の要素をインデックスと共に表示 */
            for (int i = 1; i <= elements.length; i++ ) {
                System.out.println("[" + i + "]" + elements[i - 1]);
            }

            /* 入力を受け付ける */
            System.out.print("input number > ");
            input = sc.nextLine();
        }
        /* 入力された値が正しい値でないなら再処理する */
        while (!inputCheck(input, elements.length));

        int no = Integer.parseInt(input);
        return elements[no - 1];
    }

    private static boolean inputCheck(String input, int range) {
        boolean flag = true;

        /* 引数の文字列が正の整数以外であればエラーメッセージを表示し、結果をfalseに */
        if (!input.matches("\\d")) {
            System.err.println("error : input value is not integer.");
            flag = false;
        }
        /* 正の整数を表現する文字列がrangeより大きい場合エラーメッセージを表示し、結果をfalseに */
        else if (Integer.parseInt(input) > range) {
            System.err.println("error : input value is oug of range.");
            flag = false;
        }
        return flag;
    }
}

class TextSearcher {
    private File file;

    TextSearcher(String path) {
        file = new File(path);
    }

    /**
     * 引数で与えられた文字列を含むテキスト行を検索し、検索結果をリストで返します。
     *
     * 検索した結果、該当するテキスト行が存在しなかった場合や処理中に入出力エラーが発生した場合は
     * 空のリストを返します。
     *
     * @param text - 検索するテキストワード
     * @return     - 一致したテキスト行を保持したリスト
     */
    List<String> search(String text) {
        /* 引数で与えられた文字列を含む文字列行を格納しておくリスト */
        List<String> list = new ArrayList<>();

        /* BufferedReaderをインスタンス化、try文の終了時に自動的にcloseされる */
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            /* Readerが次の行を読み込める場合 */
            while (reader.ready()) {
                String s = reader.readLine();

                /* Readerから読み込んだ文字列に引数の文字列が含まれていた場合 */
                if (s.contains(text)) {
                    list.add(s);
                }
            }
        }
        /* 何らかの入出力エラーが発生した場合 */
        catch (IOException err) {
            err.printStackTrace();
        }
        /* 検索結果を保存したリストをリターンする。
           検索した結果、どれにもヒットしなかった場合、入出力エラーが発生した場合は空のリストを返す。
         */
        return list;
    }
}
