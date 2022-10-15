package com.zekitez.brixsgcalculator;

// import android.util.Log;

public class FromToBrixSg {
    
    private final String TAG = "FtBrixSg";
    private static final int SG = 0;
    private static final int BRIX = 1;

    private static final int EU_BRIX = 0;
    private static final int EU_SG = 1;
    private static final int EU_SUGAR = 2;
    private static final int EU_ALCOHOL = 3;

    private static final float[][] brixSgSkrAlcEu = new float[][]{
            // https://eur-lex.europa.eu/legal-content/en/ALL/?uri=CELEX%3A31990R2676
            // Table from http://shop.gabsystem.com/data/descargas/Brix-Alcohol%20table%20II.pdf
            // ºBrix Sacarose % (m/m), Volumic Mass @ 20 ºC, Sugar in gr/l, "Alcohol % vol @ 20 ºC"
            {0.0f, 1.0f, 0.0f, 0.0f},  // Added line to get the full range in Brix and SG.{14.90f, 1.0595f, 134.90f, 8.01f},
        {15.00f, 1.0599f, 136.00f, 8.08f},
        {15.10f, 1.0603f, 137.10f, 8.15f},
        {15.20f, 1.0608f, 138.20f, 8.21f},
        {15.30f, 1.0612f, 139.30f, 8.27f},
        {15.40f, 1.0616f, 140.40f, 8.34f},
        {15.50f, 1.0621f, 141.50f, 8.41f},
        {15.60f, 1.0625f, 142.60f, 8.47f},
        {15.70f, 1.0629f, 143.70f, 8.54f},
        {15.80f, 1.0633f, 144.80f, 8.60f},
        {15.90f, 1.0638f, 145.90f, 8.67f},
        {16.00f, 1.0642f, 147.00f, 8.73f},
        {16.10f, 1.0646f, 148.10f, 8.80f},
        {16.20f, 1.0651f, 149.20f, 8.86f},
        {16.30f, 1.0655f, 150.30f, 8.93f},
        {16.40f, 1.0660f, 151.50f, 9.00f},
        {16.50f, 1.0664f, 152.60f, 9.06f},
        {16.60f, 1.0668f, 153.70f, 9.13f},
        {16.70f, 1.0672f, 154.80f, 9.20f},
        {16.80f, 1.0677f, 155.90f, 9.26f},
        {16.90f, 1.0681f, 157.00f, 9.33f},
        {17.00f, 1.0685f, 158.10f, 9.39f},
        {17.10f, 1.0690f, 159.30f, 9.46f},
        {17.20f, 1.0694f, 160.40f, 9.53f},
        {17.30f, 1.0699f, 161.50f, 9.59f},
        {17.40f, 1.0703f, 162.60f, 9.66f},
        {17.50f, 1.0707f, 163.70f, 9.73f},
        {17.60f, 1.0711f, 164.80f, 9.79f},
        {17.70f, 1.0716f, 165.90f, 9.86f},
        {17.80f, 1.0720f, 167.00f, 9.92f},
        {17.90f, 1.0724f, 168.10f, 9.99f},
        {18.00f, 1.0729f, 169.30f, 10.06f},
        {18.10f, 1.0733f, 170.40f, 10.12f},
        {18.20f, 1.0738f, 171.50f, 10.19f},
        {18.30f, 1.0742f, 172.60f, 10.25f},
        {18.40f, 1.0746f, 173.70f, 10.32f},
        {18.50f, 1.0751f, 174.90f, 10.39f},
        {18.60f, 1.0755f, 176.00f, 10.46f},
        {18.70f, 1.0760f, 177.20f, 10.53f},
        {18.80f, 1.0764f, 178.30f, 10.59f},
        {18.90f, 1.0768f, 179.40f, 10.66f},
        {19.00f, 1.0773f, 180.50f, 10.72f},
        {19.10f, 1.0777f, 181.70f, 10.80f},
        {19.20f, 1.0782f, 182.80f, 10.86f},
        {19.30f, 1.0786f, 183.90f, 10.93f},
        {19.40f, 1.0791f, 185.10f, 11.00f},
        {19.50f, 1.0795f, 186.30f, 11.07f},
        {19.60f, 1.0800f, 187.40f, 11.13f},
        {19.70f, 1.0804f, 188.60f, 11.21f},
        {19.80f, 1.0809f, 189.70f, 11.27f},
        {19.90f, 1.0813f, 190.80f, 11.34f},
        {20.00f, 1.0817f, 191.90f, 11.40f},
        {20.10f, 1.0822f, 193.10f, 11.47f},
        {20.20f, 1.0826f, 194.20f, 11.54f},
        {20.30f, 1.0831f, 195.30f, 11.60f},
        {20.40f, 1.0835f, 196.50f, 11.67f},
        {20.50f, 1.0840f, 197.70f, 11.75f},
        {20.60f, 1.0844f, 198.80f, 11.81f},
        {20.70f, 1.0849f, 200.00f, 11.88f},
        {20.80f, 1.0853f, 201.10f, 11.96f},
        {20.90f, 1.0857f, 202.20f, 12.01f},
        {21.00f, 1.0862f, 203.30f, 12.08f},
        {21.10f, 1.0866f, 204.50f, 12.15f},
        {21.20f, 1.0871f, 205.70f, 12.22f},
        {21.30f, 1.0875f, 206.80f, 12.29f},
        {21.40f, 1.0880f, 207.90f, 12.35f},
        {21.50f, 1.0884f, 209.10f, 12.42f},
        {21.60f, 1.0889f, 210.30f, 12.49f},
        {21.70f, 1.0893f, 211.40f, 12.56f},
        {21.80f, 1.0897f, 212.50f, 12.63f},
        {21.90f, 1.0902f, 213.60f, 12.69f},
        {22.00f, 1.0906f, 214.80f, 12.76f},
        {22.10f, 1.0911f, 216.00f, 12.83f},
        {22.20f, 1.0916f, 217.20f, 12.90f},
        {22.30f, 1.0920f, 218.30f, 12.97f},
        {22.40f, 1.0925f, 219.50f, 13.04f},
        {22.50f, 1.0929f, 220.60f, 13.11f},
        {22.60f, 1.0933f, 221.70f, 13.17f},
        {22.70f, 1.0938f, 222.90f, 13.24f},
        {22.80f, 1.0943f, 224.10f, 13.31f},
        {22.90f, 1.0947f, 225.20f, 13.38f},
        {23.00f, 1.0952f, 226.40f, 13.45f},
        {23.10f, 1.0956f, 227.60f, 13.52f},
        {23.20f, 1.0961f, 228.70f, 13.59f},
        {23.30f, 1.0965f, 229.90f, 13.66f},
        {23.40f, 1.0970f, 231.10f, 13.73f},
        {23.50f, 1.0975f, 232.30f, 13.80f},
        {23.60f, 1.0979f, 233.40f, 13.87f},
        {23.70f, 1.0984f, 234.60f, 13.94f},
        {23.80f, 1.0988f, 235.80f, 14.01f},
        {23.90f, 1.0993f, 237.00f, 14.08f},
        {24.00f, 1.0998f, 238.20f, 14.15f},
        {24.10f, 1.1007f, 239.30f, 14.22f},
        {24.20f, 1.1011f, 240.30f, 14.28f},
        {24.30f, 1.1016f, 241.60f, 14.35f},
        {24.40f, 1.1022f, 243.30f, 14.44f},
        {24.50f, 1.1026f, 244.00f, 14.50f},
        {24.60f, 1.1030f, 245.00f, 14.56f},
        {24.70f, 1.1035f, 246.40f, 14.64f},
        {24.80f, 1.1041f, 247.70f, 14.72f},
        {24.90f, 1.1045f, 248.70f, 14.78f},
        {25.00f, 1.1049f, 249.70f, 14.84f},
        {25.10f, 1.1053f, 250.70f, 14.90f},
        {25.20f, 1.1057f, 251.70f, 14.96f},
        {25.30f, 1.1062f, 253.00f, 15.03f},
        {25.40f, 1.1068f, 254.40f, 15.11f},
        {25.50f, 1.1072f, 255.40f, 15.17f},
        {25.60f, 1.1076f, 256.40f, 15.23f},
        {25.70f, 1.1081f, 257.80f, 15.32f},
        {25.80f, 1.1087f, 259.10f, 15.39f},
        {25.90f, 1.1091f, 260.10f, 15.45f},
        {26.00f, 1.1095f, 261.10f, 15.51f},
        {26.10f, 1.1100f, 262.50f, 15.60f},
        {26.20f, 1.1106f, 263.80f, 15.67f},
        {26.30f, 1.1110f, 264.80f, 15.73f},
        {26.40f, 1.1114f, 265.80f, 15.79f},
        {26.50f, 1.1119f, 267.20f, 15.88f},
        {26.60f, 1.1125f, 268.50f, 15.95f}
    };

    // https://winning-homebrew.com/specific-gravity-to-brix.html
    private static final float[][] sg_brix = new float[][]{
            {0.990f, 	0.00f},
            {0.991f, 	0.00f},
            {0.992f,	0.00f},
            {0.993f, 	0.00f},
            {0.994f, 	0.00f},
            {0.995f,    0.00f},
            {0.996f, 	0.00f},
            {0.997f, 	0.00f},
            {0.998f, 	0.00f},
            {0.999f, 	0.00f},
            {1.000f, 	0.00f},
            {1.001f, 	0.26f},
            {1.002f, 	0.51f},
            {1.003f, 	0.77f},
            {1.004f, 	1.03f},
            {1.005f, 	1.28f},
            {1.006f, 	1.54f},
            {1.007f, 	1.80f},
            {1.008f, 	2.05f},
            {1.009f, 	2.31f},
            {1.010f, 	2.56f},
            {1.011f, 	2.81f},
            {1.012f, 	3.07f},
            {1.013f, 	3.32f},
            {1.014f, 	3.57f},
            {1.015f, 	3.82f},
            {1.016f, 	4.08f},
            {1.017f, 	4.33f},
            {1.018f, 	4.58f},
            {1.019f, 	4.83f},
            {1.020f, 	5.08f},
            {1.021f, 	5.33f},
            {1.022f, 	5.57f},
            {1.023f, 	5.82f},
            {1.024f, 	6.07f},
            {1.025f, 	6.32f},
            {1.026f, 	6.57f},
            {1.027f, 	6.81f},
            {1.028f, 	7.06f},
            {1.029f, 	7.30f},
            {1.030f, 	7.55f},
            {1.031f, 	7.80f},
            {1.032f, 	8.04f},
            {1.033f, 	8.28f},
            {1.034f, 	8.53f},
            {1.035f, 	8.77f},
            {1.036f, 	9.01f},
            {1.037f, 	9.26f},
            {1.038f, 	9.50f},
            {1.039f, 	9.74f},
            {1.040f, 	9.98f},
            {1.041f, 	10.22f},
            {1.042f, 	10.46f},
            {1.043f, 	10.70f},
            {1.044f, 	10.94f},
            {1.045f, 	11.18f},
            {1.046f, 	11.42f},
            {1.047f, 	11.66f},
            {1.048f, 	11.90f},
            {1.049f, 	12.14f},
            {1.050f, 	12.37f},
            {1.051f, 	12.61f},
            {1.052f, 	12.85f},
            {1.053f, 	13.08f},
            {1.054f, 	13.32f},
            {1.055f, 	13.55f},
            {1.056f, 	13.79f},
            {1.057f, 	14.02f},
            {1.058f, 	14.26f},
            {1.059f, 	14.49f},
            {1.060f, 	14.72f},
            {1.061f, 	14.96f},
            {1.062f, 	15.19f},
            {1.063f, 	15.42f},
            {1.064f, 	15.65f},
            {1.065f, 	15.88f},
            {1.066f, 	16.11f},
            {1.067f, 	16.34f},
            {1.068f, 	16.57f},
            {1.069f, 	16.80f},
            {1.070f, 	17.03f},
            {1.071f, 	17.26f},
            {1.072f, 	17.49f},
            {1.073f, 	17.72f},
            {1.074f, 	17.95f},
            {1.075f, 	18.18f},
            {1.076f, 	18.40f},
            {1.077f, 	18.63f},
            {1.078f, 	18.86f},
            {1.079f, 	19.08f},
            {1.080f, 	19.31f},
            {1.081f, 	19.53f},
            {1.082f, 	19.76f},
            {1.083f, 	19.98f},
            {1.084f, 	20.21f},
            {1.085f, 	20.43f},
            {1.086f, 	20.65f},
            {1.087f, 	20.88f},
            {1.088f, 	21.10f},
            {1.089f, 	21.32f},
            {1.090f, 	21.54f},
            {1.091f, 	21.77f},
            {1.092f, 	21.99f},
            {1.093f, 	22.21f},
            {1.094f, 	22.43f},
            {1.095f, 	22.65f},
            {1.096f, 	22.87f},
            {1.097f, 	23.09f},
            {1.098f, 	23.31f},
            {1.099f, 	23.53f},
            {1.100f, 	23.75f},
            {1.101f, 	23.96f},
            {1.102f, 	24.18f},
            {1.103f, 	24.40f},
            {1.104f, 	24.62f},
            {1.105f, 	24.83f},
            {1.106f, 	25.05f},
            {1.107f, 	25.27f},
            {1.108f, 	25.48f},
            {1.109f, 	25.70f},
            {1.110f, 	25.91f},
            {1.111f, 	26.13f},
            {1.112f, 	26.34f},
            {1.113f, 	26.56f},
            {1.114f, 	26.77f},
            {1.115f, 	26.98f},
            {1.116f, 	27.20f},
            {1.117f, 	27.41f},
            {1.118f, 	27.62f},
            {1.119f, 	27.83f},
            {1.120f, 	28.05f},
            {1.121f, 	28.26f},
            {1.122f, 	28.47f},
            {1.123f, 	28.68f},
            {1.124f, 	28.89f},
            {1.125f, 	29.10f},
            {1.126f, 	29.31f},
            {1.127f, 	29.52f},
            {1.128f, 	29.73f},
            {1.129f, 	29.94f},
            {1.130f, 	30.15f, 0.00f}
    };

    //-----------------------------------------------

    public float findSgByBrix(float brix){
        for (float[] sgBrix : sg_brix) {
            if (brix < sgBrix[BRIX]) {
                return sgBrix[SG];
            }
        }
        return 0.0f;
    }

    public float findBrixBySg(float sg){
        for (float[] sgBrix : sg_brix) {
            if (sg <= sgBrix[SG]) {
                return sgBrix[BRIX];
            }
        }
        return 0.0f;
    }

    //---------------------------------------------------------

    //---------------------------------------------------------

    private float interpolate(int index, int fromColumn, int toColumn, float fromValue){
        float result = brixSgSkrAlcEu[index][toColumn];
        if (index > 0){
            float correction = Math.abs(( (brixSgSkrAlcEu[index][toColumn] - brixSgSkrAlcEu[index - 1][toColumn] ) /
                    (brixSgSkrAlcEu[index][fromColumn] - brixSgSkrAlcEu[index - 1][fromColumn] ) ) * (brixSgSkrAlcEu[index][fromColumn] - fromValue));
//            Log.d(TAG, index + " f:"+fromColumn + " to:" + toColumn + " value:" + fromValue + " result:" + result + " cor:" + correction + " total:" + (result - correction));
            result = result - correction;
        }
        return Math.max(result, 0.0f);
    }

    private float findEuColumnByColumn(float value, int findColumn, int byColumn){
        if (value >= brixSgSkrAlcEu[0][byColumn]) {
            for (int i = 0; i < brixSgSkrAlcEu.length; i++) {
                if (value <= brixSgSkrAlcEu[i][byColumn]) {
                    return interpolate(i, byColumn, findColumn, value);
                    //return brixSgSkrAlcEu[i][byColumn];
                }
            }
        }
        return 0.0f;
    }

    //-----------------------------------------------

    public float findEuBrixBySg(float sg){
        return findEuColumnByColumn(sg, EU_BRIX, EU_SG);
    }

    public float findEuBrixByAlc(float alcohol){
        return findEuColumnByColumn(alcohol, EU_BRIX, EU_ALCOHOL);
    }

    public float findEuSgByBrix(float brix){
        return findEuColumnByColumn(brix, EU_SG, EU_BRIX);
    }

    public float findEuSgByAlc(float alcohol){
        return findEuColumnByColumn(alcohol, EU_SG, EU_ALCOHOL);
    }

    //---------------------------------------------------------

    public float findEuSugarByBrix(float brix){
        return findEuColumnByColumn(brix, EU_SUGAR, EU_BRIX);
    }

    public float findEuSugarByAlc(float alcohol){
        return findEuColumnByColumn(alcohol, EU_SUGAR, EU_ALCOHOL);
    }

    public float findEuSugarBySg(float sg){
        return findEuColumnByColumn(sg, EU_SUGAR, EU_SG);
    }

    //---------------------------------------------------------

    public float findEuAlcByBrix(float brix){
        return findEuColumnByColumn(brix, EU_ALCOHOL, EU_BRIX);
    }

    public float findEuAlcBySg(float sg){
        return findEuColumnByColumn(sg, EU_ALCOHOL, EU_SG);
    }
}
