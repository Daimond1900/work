package com.yifeng.face;


import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.yifeng.ChifCloud12345update.R;

/**
 * A class for annotating a CharSequence with spans to convert textual emoticons
 * to graphical ones.
 */
public class SmileyParser {
    // Singleton stuff

    private static SmileyParser sInstance;

	public static SmileyParser getInstance() {
		return sInstance; }
    public static void init(Context context) {
    	if
    	(sInstance==null)
        sInstance = new SmileyParser( context);
    }

    private   Context mContext;
    private static   String[] mSmileyTexts;
    private final Pattern mPattern;
    private final HashMap<String, Integer> mSmileyToRes;

    private  SmileyParser(Context context ) {
    	this.mContext=context;
    	if(mSmileyTexts==null)
        mSmileyTexts = mContext.getResources().getStringArray(DEFAULT_SMILEY_TEXTS);
        mSmileyToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }
    
 
     //表情图片集合
        public static final int[] sIconIds = {
            R.drawable.face3,
            R.drawable.face8,
            R.drawable.face9,
            R.drawable.face10,
            R.drawable.face11,
            R.drawable.face15,
            R.drawable.face21,
            R.drawable.face24,
            R.drawable.face26,
            R.drawable.face28,
            R.drawable.face31,   
            R.drawable.face32,
            R.drawable.face33,
            R.drawable.face34,
            R.drawable.face35,
            R.drawable.face36,
            R.drawable.face37,
            R.drawable.face39,
            R.drawable.face40,
            R.drawable.face41,
            R.drawable.face42,
            R.drawable.face43,
            R.drawable.face44,
            R.drawable.face45,
            R.drawable.face46,
            R.drawable.face47,
            R.drawable.face48,
            R.drawable.face49,
            R.drawable.face50,
            R.drawable.face52,
            R.drawable.face53,
            R.drawable.face54,
            R.drawable.face55,
            R.drawable.face56,
            R.drawable.face57,
            R.drawable.face58,
            R.drawable.face59,
            R.drawable.face60,
            R.drawable.face61,
            R.drawable.face62,
            R.drawable.face63,
            R.drawable.face64,
            R.drawable.face65,
            R.drawable.face66,
            R.drawable.face67,
            R.drawable.face68,
            R.drawable.face69,
            R.drawable.face70,
            R.drawable.face71,
            R.drawable.face72,
            R.drawable.face73,
            R.drawable.face77,
            R.drawable.face78,
            R.drawable.face79,
        };
        //将图片映射为 文字
        public static int aini = 0;
        public static int aoteman = 1;
        public static int baibai = 2;
        public static int baobao = 3;
        public static int beiju = 4;
        public static int beishang = 5;
        public static int bianbian = 6;
        public static int bishi = 7;
        public static int bizui = 8;
        public static int buyao = 9;
        public static int chanzui = 10;
 
 
        //得到图片表情 根据id
        public static int getSmileyResource(int which) {
            return sIconIds[which];
        }
   

    // NOTE: if you change anything about this array, you must make the corresponding change

    // to the string arrays: default_smiley_texts and default_smiley_names in res/values/arrays.xml

//    public static final int[] DEFAULT_SMILEY_RES_IDS = {
//       getSmileyResource( aini), // 0
//        getSmileyResource(aoteman), // 1
//        getSmileyResource(baibai), // 2
//        getSmileyResource(baobao), // 3
//        getSmileyResource(beiju), // 4
//        getSmileyResource(beishang), // 5
//        getSmileyResource(bianbian), // 6
//        getSmileyResource(bishi), // 7
//        getSmileyResource(bizui), // 8
//        getSmileyResource(buyao), // 9
//        getSmileyResource(chanzui), // 10
//
//
//    };

    public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;
//    public static final int DEFAULT_SMILEY_NAMES = R.array.default_smiley_names;

    /**
     * Builds the hashtable we use for mapping the string version
     * of a smiley (e.g. ":-)") to a resource ID for the icon version.
     */
    private HashMap<String, Integer> buildSmileyToRes() {
//        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
//            // Throw an exception if someone updated DEFAULT_SMILEY_RES_IDS
//
//            // and failed to update arrays.xml
//
//            throw new IllegalStateException("Smiley resource ID/text mismatch");
//        }

        HashMap<String, Integer> smileyToRes =
                            new HashMap<String, Integer>(mSmileyTexts.length);
        for (int i = 0; i < sIconIds.length; i++) {
            smileyToRes.put(mSmileyTexts[i], sIconIds[i]);
        }

        return smileyToRes;
    }

    
    /**
     * Builds the regular expression we use to find smileys in {@link #addSmileySpans}.
     */
    //构建正则表达式
    private Pattern buildPattern() {
        // Set the StringBuilder capacity with the assumption that the average

        // smiley is 3 characters long.

        StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);

        // Build a regex that looks like (:-)|:-(|...), but escaping the smilies

        // properly so they will be interpreted literally by the regex matcher.

        patternString.append('('); 
        for (String s : mSmileyTexts) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        // Replace the extra '|' with a ')'

        patternString.replace(patternString.length() - 1, patternString.length(), ")");

        return Pattern.compile(patternString.toString());
    }


    /**
     * Adds ImageSpans to a CharSequence that replace textual emoticons such
     * as :-) with a graphical version.
     *
     * @param text A CharSequence possibly containing emoticons
     * @return A CharSequence annotated with ImageSpans covering any
     * recognized emoticons.
     */
    //根据文本替换成图片
    public CharSequence addSmileySpans(CharSequence text) {
    	if(text==null)return null;
        SpannableStringBuilder builder = null;
		try {
			builder = new SpannableStringBuilder(text);
			Matcher matcher = mPattern.matcher(text);
			while (matcher.find()) {
			    int resId = mSmileyToRes.get(matcher.group());
			    builder.setSpan(new ImageSpan(mContext, resId),
			                    matcher.start(), matcher.end(),
			                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return builder;
    }
}
