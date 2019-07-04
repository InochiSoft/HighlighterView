/*
* 07/04/2019
* Agung Novian
* INOCHI Software
* inochisoftware@gmail.com; pujanggabageur@gmail.com
* https://github.com/InochiSoft
*/

package com.inochi.highlighter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeView extends ScrollView {
    private String mPlainText = "";
    private String mCodeText = "";
    private String mLanguage = "";
    private float mTextSize = 14;

    private float mNumberPaddingLeft = 6;
    private float mNumberPaddingTop = 4;
    private float mNumberPaddingRight = 10;
    private float mNumberPaddingBottom = 4;

    private float mCodePaddingLeft = 4;
    private float mCodePaddingTop = 4;
    private float mCodePaddingRight = 4;
    private float mCodePaddingBottom = 4;

    private int mCodeLines = 0;
    private int mKeywordColor = ContextCompat.getColor(getContext(), R.color.red_400);
    private int mDataTypeColor = ContextCompat.getColor(getContext(), R.color.orange_600);
    private int mNumericColor = ContextCompat.getColor(getContext(), R.color.cyan_400);
    private int mMethodColor = ContextCompat.getColor(getContext(), R.color.amber_600);
    private int mNotationColor = ContextCompat.getColor(getContext(), R.color.yellow_600);
    private int mAttributeColor = ContextCompat.getColor(getContext(), R.color.blue_600);
    private int mCommentColor = ContextCompat.getColor(getContext(), R.color.grey_600);
    private int mStringColor = ContextCompat.getColor(getContext(), R.color.green_600);
    private int mCodeColor = ContextCompat.getColor(getContext(), R.color.white);
    private int mNumberColor = ContextCompat.getColor(getContext(), R.color.yellow_50);
    private int mNumberBackground = ContextCompat.getColor(getContext(), R.color.grey_800);
    private int mCodeBackground = ContextCompat.getColor(getContext(), R.color.grey_900);

    private String colorKeyword = "";
    private String colorDataType = "";
    private String colorNotation = "";
    private String colorMethod = "";
    private String colorComment = "";
    private String colorAttribute = "";
    private String colorString = "";
    private String colorNumeric = "";

    private TextView textViewNumber;
    private TextView textViewCode;

    private String colKeywords = "";
    private String colDataTypes = "";
    private String colNotations = "";
    private String colMethods = "";

    public CodeView(Context context) {
        super(context);
        init(null, 0);
    }

    public CodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CodeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private int convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /*
    private float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }
    */

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CodeView, defStyle, 0);

        mPlainText = a.getString(R.styleable.CodeView_text);
        mLanguage = a.getString(R.styleable.CodeView_language);

        mTextSize = a.getDimensionPixelSize(R.styleable.CodeView_text_size, convertDpToPixel(mTextSize));

        mNumberPaddingLeft = a.getDimension(R.styleable.CodeView_number_padding_left, (mNumberPaddingLeft));
        mNumberPaddingTop = a.getDimension(R.styleable.CodeView_number_padding_top, (mNumberPaddingTop));
        mNumberPaddingRight = a.getDimension(R.styleable.CodeView_number_padding_right, (mNumberPaddingRight));
        mNumberPaddingBottom = a.getDimension(R.styleable.CodeView_number_padding_bottom, (mNumberPaddingBottom));

        mCodePaddingLeft = a.getDimension(R.styleable.CodeView_code_padding_left, (mCodePaddingLeft));
        mCodePaddingTop = a.getDimension(R.styleable.CodeView_code_padding_top, (mCodePaddingTop));
        mCodePaddingRight = a.getDimension(R.styleable.CodeView_code_padding_right, (mCodePaddingRight));
        mCodePaddingBottom = a.getDimension(R.styleable.CodeView_code_padding_bottom, (mCodePaddingBottom));

        mKeywordColor = a.getInt(R.styleable.CodeView_keyword_color, mKeywordColor);
        mDataTypeColor = a.getInt(R.styleable.CodeView_datatype_color, mDataTypeColor);
        mNumericColor = a.getInt(R.styleable.CodeView_numeric_color, mNumericColor);
        mMethodColor = a.getInt(R.styleable.CodeView_method_color, mMethodColor);
        mNotationColor = a.getInt(R.styleable.CodeView_notation_color, mNotationColor);
        mAttributeColor = a.getInt(R.styleable.CodeView_attribute_color, mAttributeColor);
        mCommentColor = a.getInt(R.styleable.CodeView_comment_color, mCommentColor);
        mStringColor = a.getInt(R.styleable.CodeView_string_color, mStringColor);

        mCodeColor = a.getColor(R.styleable.CodeView_code_color, mCodeColor);
        mNumberColor = a.getColor(R.styleable.CodeView_number_color, mNumberColor);
        mNumberBackground = a.getColor(R.styleable.CodeView_number_background, mNumberBackground);
        mCodeBackground = a.getColor(R.styleable.CodeView_code_background, mCodeBackground);

        colorKeyword = String.format("#%06x", mKeywordColor & 0xffffff);
        colorDataType = String.format("#%06x", mDataTypeColor & 0xffffff);
        colorNotation = String.format("#%06x", mNotationColor & 0xffffff);
        colorMethod = String.format("#%06x", mMethodColor & 0xffffff);
        colorComment = String.format("#%06x", mCommentColor & 0xffffff);
        colorAttribute = String.format("#%06x", mAttributeColor & 0xffffff);
        colorString = String.format("#%06x", mStringColor & 0xffffff);
        colorNumeric = String.format("#%06x", mNumericColor & 0xffffff);

        colKeywords = "abstract, package, private, implements, protected, public, import, " +
                "extends, interface, static, class, return, var, new, switch, case, if, else, " +
                "const, for, in, super, final, break, try, catch, finally, await, async";
        colDataTypes = "byte, short, int, long, float, double, boolean, char, string, null, dynamic, false, true, this, void, integer";
        colNotations = "override, Override, NonNull, Nullable";

        colMethods = "CupertinoApp, CupertinoThemeData, CupertinoTextThemeData, CupertinoButton, CupertinoTabScaffold, " +
                        "CupertinoTabBar, BottomNavigationBarItem, CupertinoTabView, CupertinoPageScaffold, CupertinoNavigationBar, CupertinoPageRoute, " +
                        "MaterialApp, ThemeData, DateTime, Card, RoundedRectangleBorder, BorderRadius, Border, TextStyle, " +
                        "Material, InkWell, Padding, Row, SizedBox, Expanded, MaterialPageRoute, " +
                        "AlertDialog, Text, FlatButton, CircularProgressIndicator, Container, Center, Scaffold, AppBar, Flexible, " +
                        "TimeOfDay, SingleChildScrollView, TextField, DropDown, Flexible, Align, TextEditingController, " +
                        "TextInputType, MediaQuery, Icon, Column, Fluttertoast, Positioned, " +
                        "IconButton, Expanded, BoxDecoration, FutureBuilder, ListView, GridView, FloatingActionButton, Stack";

        a.recycle();

        prepareView();
        parseCode();
        changeText();
    }

    private void prepareView(){
        Context context = getContext();

        HorizontalScrollView.LayoutParams scrolLayoutParams = new HorizontalScrollView.LayoutParams(
                HorizontalScrollView.LayoutParams.MATCH_PARENT,
                HorizontalScrollView.LayoutParams.MATCH_PARENT);

        LinearLayout.LayoutParams linLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        LinearLayout.LayoutParams textNumberLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout.LayoutParams textCodeLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        this.removeAllViews();

        LinearLayout linCode = new LinearLayout(context);
        linCode.setLayoutParams(linLayoutParams);
        linCode.setOrientation(LinearLayout.HORIZONTAL);
        //this.setLayoutParams(linLayoutParams);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/consola.ttf");

        if (textViewNumber == null) textViewNumber = new TextView(context);
        textViewNumber.setGravity(Gravity.END);
        textViewNumber.setText(Html.fromHtml(""));
        textViewNumber.setLayoutParams(textNumberLayoutParams);
        textViewNumber.setPadding(convertDpToPixel(mNumberPaddingLeft), convertDpToPixel(mNumberPaddingTop),
                convertDpToPixel(mNumberPaddingRight), convertDpToPixel(mNumberPaddingBottom));
        textViewNumber.setTypeface(font);
        textViewNumber.setTextColor(mNumberColor);

        linCode.addView(textViewNumber);

        if (textViewCode == null) textViewCode = new TextView(context);
        textViewCode.setText(Html.fromHtml(""));
        textViewCode.setPadding(convertDpToPixel(mCodePaddingLeft), convertDpToPixel(mCodePaddingTop),
                convertDpToPixel(mCodePaddingRight), convertDpToPixel(mCodePaddingBottom));
        textViewCode.setLayoutParams(textCodeLayoutParams);
        textViewCode.setTypeface(font);
        textViewCode.setTextColor(mCodeColor);

        HorizontalScrollView scrollCode = new HorizontalScrollView(context);
        scrollCode.setLayoutParams(scrolLayoutParams);

        scrollCode.addView(textViewCode);
        linCode.addView(scrollCode);

        this.addView(linCode);

        invalidateMeasurements();
    }

    private void invalidateMeasurements() {
        if (textViewNumber != null){
            textViewNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textViewNumber.setTextColor(mNumberColor);
            textViewNumber.setBackgroundColor(mNumberBackground);
        }
        if (textViewCode != null){
            textViewCode.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textViewCode.setTextColor(mCodeColor);
            textViewCode.setBackgroundColor(mCodeBackground);
        }
    }

    private void invalidateCode() {
        parseCode();
        changeText();
    }

    private String normalizeString(String text){
        text = text
                .replace("▼", "</")
                .replace("▲", "/>")
                .replace("♦", "?>")
                .replace("♠", "<?php")
                .replace("♣", "<?xml")
                .replace("‹", "<!--")
                .replace("›", "-->")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace(" ", "&nbsp;")
                .replace("■", "//")
                .replace("►", "/*")
                .replace("◄", "*/")
                .replace("—", "__")
                .replace("u2022u", "•")
                .replace("u25A0u", "■")
                .replace("u25BAu", "►")
                .replace("u25C4u", "◄")
                .replace("u25BCu", "▼")
                .replace("u25B2u", "▲")
                .replace("u2666u", "♦")
                .replace("u2660u", "♠")
                .replace("u2663u", "♣")
                .replace("u2039u", "‹")
                .replace("u203Au", "›")
                .replace("u2014u", "—")
                .replace("[●]", "</font>")
                .replace("[●", "<font color=\"")
                .replace("●]", "\">")
        ;
        return text;
    }

    private String unnormalizeString(String text){
        text = text
                .replace("♠", "u2660u")
                .replace("♣", "u2663u")
                .replace("•", "u2022u")
                .replace("▼", "u25BCu")
                .replace("▲", "u25B2u")
                .replace("■", "u25A0u")
                .replace("►", "u25BAu")
                .replace("◄", "u25C4u")
                .replace("‹", "u2039u")
                .replace("›", "u203Au")
                .replace("—", "u2014u")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("</", "▼")
                .replace("/>", "▲")
                .replace("?>", "♦")
                .replace("<?php", "♠")
                .replace("<?xml", "♣")
                .replace("<!--", "‹")
                .replace("-->", "›")
                .replace("\t", "    ")
                .replace(" ", "&nbsp;")
                .replace("&nbsp;", " ")
                .replace("//", "■")
                .replace("/*", "►")
                .replace("*/", "◄")
                .replace("__", "—")
        ;
        return text;
    }

    private boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private ArrayList<String> split(String s, Pattern pattern) {
        assert s != null;
        assert pattern != null;
        Matcher m = pattern.matcher(s);
        ArrayList<String> ret = new ArrayList<>();
        int start = 0;
        while (m.find()) {
            String text = s.substring(start, m.start());
            if (text.trim().isEmpty()){
                ret.add(text);
            } else {
                ret.add("•" + text + "•");
            }
            ret.add(m.group());
            start = m.end();
        }
        ret.add(start >= s.length() ? "" : s.substring(start));
        return ret;
    }

    private void parseCode(){
        if (mLanguage.equals(Language.KOTLIN)){
            colKeywords += ", fun, it";
        }

        if (mLanguage.equals(Language.PHP)){
            colKeywords += ", elseif, foreach, function, construct, continue, echo";
            colDataTypes += ", this, TRUE, FALSE, NULL";
            colMethods += ", function_exists, fclose, fopen, chmod, unlink, is_dir, is_file, is_writable, rtrim, trim, array, "+
                    "strtolower, strtoupper, file_exists, isset, empty, array_merge, include, defined, is_numeric, header, ini_get, " +
                    "error_reporting, error_get_last, preg_replace, array_keys, htmlspecialchars, explode, extension_loaded, " +
                    "substr, strpos, basename, str_replace, str_ireplace, parent, require_once, require, class_exists, " +
                    "method_exists, array_slice, is_callable, sscanf, exit, end, strlen, readfile, mkdir, getimagesize, imagecreatefromjpeg, " +
                    "imagecreatefrompng, imagecreatetruecolor, imagecolorallocatealpha, imagecolorallocate, imagesavealpha, imagealphablending, " +
                    "imagefill, imagecopyresampled, imagefontwidth, imagefontheight, imagettfbbox, imagettftext, imagepng, imagedestroy, is_dir" ;
        }

        String content = mPlainText;
        content = unnormalizeString(content);

        String[] texts = content.split("\n");
        StringBuilder newContent = new StringBuilder();

        String[] keywords = colKeywords.split(",");
        String[] dataTypes = colDataTypes.split(",");
        String[] methods = colMethods.split(",");
        String[] notations = colNotations.split(",");

        mCodeLines = texts.length;

        boolean isCommentBlock = false;
        boolean isCommentHTMLBlock = false;
        boolean isPHPBlock = false;
        boolean isStringDoubleBlock = false;
        boolean isStringSingleBlock = false;

        boolean isHtmlOpeningTag = false;
        boolean isHtmlClosingTag = false;
        boolean isTagOpen = false;
        boolean isSettingOpen = false;

        int commentIndex = 0;
        int commentHTMLIndex = 0;

        for (int t = 0; t < texts.length; t++){
            boolean isCommentLine = false;

            String text = texts[t];

            Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
            ArrayList<String> listWord = split(text + " ", pattern);
            StringBuilder newText = new StringBuilder();

            for (int w = 0; w < listWord.size(); w++){
                String word = listWord.get(w);
                String newWord = word;
                String prevWord = "";
                String nextWord = "";

                if (w > 0){
                    prevWord = listWord.get(w - 1);
                }
                if (w < listWord.size() - 1){
                    nextWord = listWord.get(w + 1);
                }
                // Command here
                if (isCommentBlock){
                    newWord = newWord.replace("•", "");
                    commentIndex++;
                    if (newWord.equals("◄")){
                        isCommentBlock = false;
                    }
                } else if (isCommentHTMLBlock){
                    newWord = newWord.replace("•", "");
                    commentHTMLIndex++;
                    if (newWord.equals("›")){
                        isCommentHTMLBlock = false;
                    }
                } else {
                    if (isCommentLine){
                        newWord = newWord.replace("•", "");
                    } else {
                        if (word.startsWith("•") && word.endsWith("•")) {
                            newWord = newWord.replace("•", "");

                            boolean isCantEdit = false;
                            boolean isWord = false;

                            if (!(isStringDoubleBlock || isStringSingleBlock)) {
                                isCantEdit = true;
                            }

                            if (word.contains("function")){
                                Log.i("word", newWord);
                            }

                            if (isCantEdit || (mLanguage.equals(Language.PHP) && isPHPBlock && isHtmlOpeningTag)) {
                                if (!(mLanguage.equals(Language.HTML) || mLanguage.equals(Language.XML))) {
                                    for (String key : keywords) {
                                        if (mLanguage.equals(Language.VB)){
                                            if (newWord.trim().toLowerCase().equals(key.trim().toLowerCase())) {
                                                newWord = String.format("[●%s●]%s[●]", colorKeyword, newWord);
                                                isWord = true;
                                                break;
                                            }
                                        } else {
                                            if (word.contains("function") && key.trim().equals("function")){
                                                Log.i("word", newWord);
                                            }
                                            if (newWord.trim().equals(key.trim())) {
                                                newWord = String.format("[●%s●]%s[●]", colorKeyword, newWord);
                                                isWord = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!isWord) {
                                        for (String key : dataTypes) {
                                            if (mLanguage.equals(Language.VB)){
                                                if (newWord.trim().toLowerCase().equals(key.trim().toLowerCase())) {
                                                    newWord = String.format("[●%s●]%s[●]", colorDataType, newWord);
                                                    isWord = true;
                                                    break;
                                                }
                                            } else {
                                                if (newWord.trim().equals(key.trim())) {
                                                    newWord = String.format("[●%s●]%s[●]", colorDataType, newWord);
                                                    isWord = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (!isWord) {
                                        for (String key : methods) {
                                            if (mLanguage.equals(Language.VB)){
                                                if (newWord.trim().toLowerCase().equals(key.trim().toLowerCase())) {
                                                    newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                                                    isWord = true;
                                                    break;
                                                }
                                            } else {
                                                if (newWord.trim().equals(key.trim())) {
                                                    newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                                                    isWord = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (!isWord) {
                                        if (prevWord.equals("@")){
                                            for (String key : notations) {
                                                if (mLanguage.equals(Language.VB)){
                                                    if (newWord.trim().toLowerCase().equals(key.trim().toLowerCase())) {
                                                        newWord = String.format("[●%s●]%s[●]", colorNotation, newWord);
                                                        break;
                                                    }
                                                } else {
                                                    if (newWord.trim().equals(key.trim())) {
                                                        newWord = String.format("[●%s●]%s[●]", colorNotation, newWord);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (isNumeric(newWord)) {
                                        newWord = String.format("[●%s●]%s[●]", colorNumeric, newWord);
                                    }
                                }
                            } else {
                                newWord = String.format("[●%s●]%s[●]", colorString, newWord);
                            }

                            if (mLanguage.equals(Language.HTML)
                                    || mLanguage.equals(Language.XML)
                                    || mLanguage.equals(Language.PHP)) {
                                if (nextWord.equals(":")){
                                    if (isCantEdit && (isHtmlOpeningTag || isHtmlClosingTag)){
                                        newWord = String.format("[●%s●]%s[●]", colorAttribute, newWord);
                                    }
                                }
                                if (isTagOpen){
                                    newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                                }
                            }

                            if (isSettingOpen){
                                newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                            }
                        } else if (newWord.equals("\"")) {
                            if (!isStringDoubleBlock) {
                                isStringDoubleBlock = true;
                                newWord = String.format("[●%s●]%s[●]", colorString, newWord);
                            } else {
                                isStringDoubleBlock = false;
                                newWord = String.format("[●%s●]%s[●]", colorString, newWord);
                            }
                        } else if (newWord.equals("'")) {
                            if (!mLanguage.equals(Language.VB)) {
                                if (!isStringSingleBlock) {
                                    isStringSingleBlock = true;
                                    newWord = String.format("[●%s●]%s[●]", colorString, newWord);
                                } else {
                                    isStringSingleBlock = false;
                                    newWord = String.format("[●%s●]%s[●]", colorString, newWord);
                                }
                            }
                        } else if (newWord.equals("♠")) {
                            newWord = String.format("[●%s●]%s[●]", colorAttribute, newWord);
                            isPHPBlock = true;
                        } else if (newWord.equals("♦")) {
                            newWord = String.format("[●%s●]%s[●]", colorAttribute, newWord);
                            isPHPBlock = false;
                        } else if (newWord.equals("<")) {
                            if (mLanguage.equals(Language.HTML)
                                    || mLanguage.equals(Language.XML)
                                    || mLanguage.equals(Language.PHP)) {
                                isTagOpen = true;
                                isHtmlOpeningTag = true;
                                newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                            }
                        } else if (newWord.equals("▼")){ // </
                            if (mLanguage.equals(Language.HTML)
                                    || mLanguage.equals(Language.XML)
                                    || mLanguage.equals(Language.PHP)) {
                                isTagOpen = true;
                                isHtmlClosingTag = true;
                                newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                            }
                        } else if (newWord.equals("▲")){ // />
                            if (mLanguage.equals(Language.HTML)
                                    || mLanguage.equals(Language.XML)
                                    || mLanguage.equals(Language.PHP)) {
                                if (isHtmlOpeningTag){
                                    isHtmlOpeningTag = false;
                                }
                                if (isHtmlClosingTag){
                                    isHtmlClosingTag = false;
                                }
                                newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                            }

                        } else if (newWord.equals(">")){
                            if (mLanguage.equals(Language.HTML)
                                    || mLanguage.equals(Language.XML)
                                    || mLanguage.equals(Language.PHP)){
                                if (isHtmlOpeningTag){
                                    newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                                    isHtmlOpeningTag = false;
                                } else if (isHtmlClosingTag){
                                    newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                                    isHtmlClosingTag = false;
                                }
                            }
                        } else if (newWord.equals(" ")){
                            if (mLanguage.equals(Language.HTML)
                                    || mLanguage.equals(Language.XML)
                                    || mLanguage.equals(Language.PHP)){
                                if (isTagOpen){
                                    isTagOpen = false;
                                }
                            }
                        } else if (newWord.equals("[")){
                            if (mLanguage.equals(Language.INI)
                                    || mLanguage.equals(Language.INF)){
                                isSettingOpen = true;
                                newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                            }
                        } else if (newWord.equals("]")){
                            if (mLanguage.equals(Language.INI)
                                    || mLanguage.equals(Language.INF)){
                                isSettingOpen = false;
                                newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                            }
                        } else if (newWord.equals("—")){
                            if (mLanguage.equals(Language.PHP)){
                                if (nextWord.replace("•", "").equals("construct")){
                                    newWord = String.format("[●%s●]%s[●]", colorKeyword, newWord);
                                }
                            }
                        } else if (newWord.equals("$")){
                            if (mLanguage.equals(Language.PHP)){
                                if (nextWord.replace("•", "").equals("this")){
                                    newWord = String.format("[●%s●]%s[●]", colorDataType, newWord);
                                }
                            }
                        } else {
                            if ((isStringDoubleBlock || isStringSingleBlock)) {
                                if (!(mLanguage.equals(Language.PHP) && isPHPBlock && isHtmlOpeningTag)){
                                    newWord = String.format("[●%s●]%s[●]", colorString, newWord);
                                }
                            }
                            if (isTagOpen){
                                newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                            }
                            if (isSettingOpen){
                                newWord = String.format("[●%s●]%s[●]", colorMethod, newWord);
                            }
                        }
                    }

                    if (mLanguage.equals(Language.INI)
                            || mLanguage.equals(Language.INF)){
                        if (!isCommentLine)
                        if (newWord.equals("#")){
                            newWord = String.format("[●%s●]%s", colorComment, newWord);
                            isCommentLine = true;
                        }
                    } else if (mLanguage.equals(Language.VB)) {
                        if (!isCommentLine)
                            if (newWord.equals("'")){
                                newWord = String.format("[●%s●]%s", colorComment, newWord);
                                isCommentLine = true;
                            }
                    } else {
                        //is comment line
                        if (!isCommentLine)
                        if (newWord.equals("■")){
                            newWord = String.format("[●%s●]%s", colorComment, newWord);
                            isCommentLine = true;
                        }
                    }
                }

                //is comment block
                if (!isCommentBlock)
                if (newWord.equals("►")){
                    newWord = String.format("[●%s●]%s", colorComment, newWord);
                    isCommentBlock = true;
                    commentIndex++;
                }

                if (!isCommentHTMLBlock)
                if (newWord.equals("‹")){
                    newWord = String.format("[●%s●]%s", colorComment, newWord);
                    isCommentHTMLBlock = true;
                    commentHTMLIndex++;
                }

                newWord = normalizeString(newWord); //Must end of process
                newText.append(newWord);
            }

            newContent.append(newText);

            if (!isCommentBlock && commentIndex > 0){
                newContent.append("</font>");
                commentIndex = 0;
            }

            if (!isCommentHTMLBlock && commentHTMLIndex > 0){
                newContent.append("</font>");
                commentHTMLIndex = 0;
            }

            if (isCommentLine){
                newContent.append("</font>");
            }

            newContent.append("<br/>");
        }

        mCodeText = newContent.toString();
    }

    private void changeText(){
        if (textViewCode != null){
            textViewCode.setText(Html.fromHtml(mCodeText));
        }
        if (textViewNumber != null){

            StringBuilder number = new StringBuilder();

            if (mCodeLines > 0){
                for (int i = 0; i < mCodeLines; i++){
                    String text = String.valueOf(i + 1);
                    number.append(text).append("<br/>");
                }
            } else {
                String text = String.valueOf(1);
                number.append(text).append("<br/>");
            }

            textViewNumber.setText(Html.fromHtml(number.toString()));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public int getMethodColor() {
        return mMethodColor;
    }

    public void setMethodColor(int mMethodColor) {
        this.mMethodColor = mMethodColor;
        parseCode();
        changeText();
    }

    public int getNotationColor() {
        return mNotationColor;
    }

    public void setNotationColor(int mNotationColor) {
        this.mNotationColor = mNotationColor;
        invalidateCode();
    }

    private int getAttributeColor() {
        return mAttributeColor;
    }

    private void setAttributeColor(int mAttributeColor) {
        this.mAttributeColor = mAttributeColor;
        invalidateCode();
    }

    public int getCommentColor() {
        return mCommentColor;
    }

    public void setCommentColor(int mCommentColor) {
        this.mCommentColor = mCommentColor;
        invalidateCode();
    }

    public int getKeywordColor() {
        return mKeywordColor;
    }

    public void setKeywordColor(int mKeywordColor) {
        this.mKeywordColor = mKeywordColor;
        invalidateCode();
    }

    public int getDataTypeColor() {
        return mDataTypeColor;
    }

    public void setDataTypeColor(int mDataTypeColor) {
        this.mDataTypeColor = mDataTypeColor;
        invalidateCode();
    }

    public int getNumericColor() {
        return mNumericColor;
    }

    public void setNumericColor(int mNumericColor) {
        this.mNumericColor = mNumericColor;
        invalidateCode();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        invalidateMeasurements();
    }

    public String getText() {
        return mPlainText;
    }

    public void setText(String mPlainText) {
        this.mPlainText = mPlainText;
        invalidateCode();
    }

    public String getCode() {
        return mCodeText;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
        invalidateCode();
    }

    public int getCodeColor() {
        return mCodeColor;
    }

    public void setCodeColor(int mCodeColor) {
        this.mCodeColor = mCodeColor;
        invalidateMeasurements();
        invalidateCode();
    }

    public int getStringColor() {
        return mStringColor;
    }

    public void setStringColor(int mStringColor) {
        this.mStringColor = mStringColor;
        invalidateCode();
    }

    protected TextView getTextViewNumber() {
        return textViewNumber;
    }

    public void setTextViewNumber(TextView textViewNumber) {
        this.textViewNumber = textViewNumber;
    }

    protected TextView getTextViewCode() {
        return textViewCode;
    }

    public void setTextViewCode(TextView textViewCode) {
        this.textViewCode = textViewCode;
    }

    public int getNumberColor() {
        return mNumberColor;
    }

    public void setNumberColor(int mNumberColor) {
        this.mNumberColor = mNumberColor;
        invalidateMeasurements();
        invalidateCode();
    }

    public int getCodeLines() {
        return mCodeLines;
    }

    protected void setCodeLines(int mCodeLines) {
        this.mCodeLines = mCodeLines;
    }

    public int getNumberBackgroundColor() {
        return mNumberBackground;
    }

    public void setNumberBackgroundColor(int mNumberBackgroundColor) {
        this.mNumberBackground = mNumberBackgroundColor;
        invalidateMeasurements();
    }

    public int getCodeBackgroundColor() {
        return mCodeBackground;
    }

    public void setCodeBackgroundColor(int mCodeBackgroundColor) {
        this.mCodeBackground = mCodeBackgroundColor;
        invalidateMeasurements();
    }

    public float getNumberPaddingLeft() {
        return mNumberPaddingLeft;
    }

    public void setNumberPaddingLeft(float mNumberPaddingLeft) {
        this.mNumberPaddingLeft = mNumberPaddingLeft;
        invalidateMeasurements();
    }

    public float getNumberPaddingTop() {
        return mNumberPaddingTop;
    }

    public void setNumberPaddingTop(float mNumberPaddingTop) {
        this.mNumberPaddingTop = mNumberPaddingTop;
        invalidateMeasurements();
    }

    public float getNumberPaddingRight() {
        return mNumberPaddingRight;
    }

    public void setNumberPaddingRight(float mNumberPaddingRight) {
        this.mNumberPaddingRight = mNumberPaddingRight;
        invalidateMeasurements();
    }

    public float getNumberPaddingBottom() {
        return mNumberPaddingBottom;
    }

    public void setNumberPaddingBottom(float mNumberPaddingBottom) {
        this.mNumberPaddingBottom = mNumberPaddingBottom;
        invalidateMeasurements();
    }

    public float getCodePaddingLeft() {
        return mCodePaddingLeft;
    }

    public void setCodePaddingLeft(float mCodePaddingLeft) {
        this.mCodePaddingLeft = mCodePaddingLeft;
        invalidateMeasurements();
    }

    public float getCodePaddingTop() {
        return mCodePaddingTop;
    }

    public void setCodePaddingTop(float mCodePaddingTop) {
        this.mCodePaddingTop = mCodePaddingTop;
        invalidateMeasurements();
    }

    public float getCodePaddingRight() {
        return mCodePaddingRight;
    }

    public void setCodePaddingRight(float mCodePaddingRight) {
        this.mCodePaddingRight = mCodePaddingRight;
        invalidateMeasurements();
    }

    public float getCodePaddingBottom() {
        return mCodePaddingBottom;
    }

    public void setCodePaddingBottom(float mCodePaddingBottom) {
        this.mCodePaddingBottom = mCodePaddingBottom;
        invalidateMeasurements();
    }
}
