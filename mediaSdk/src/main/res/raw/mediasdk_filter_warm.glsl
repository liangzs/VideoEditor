varying highp vec2 textureCoordinate;
precision highp float;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;
uniform sampler2D inputImageTexture3;
uniform sampler2D inputImageTexture4;
uniform float strength;
void main()
{
    lowp vec4 textureColor;
    lowp vec4 textureOri;
    vec4 greyColor;
    vec4 layerColor;

    float xCoordinate = textureCoordinate.x;
    float yCoordinate = textureCoordinate.y;

    highp float redCurveValue;
    highp float greenCurveValue;
    highp float blueCurveValue;

    textureColor = texture2D(inputImageTexture, vec2(xCoordinate, yCoordinate));
    textureOri=textureColor;
    float resultAlpha=textureOri.a*(1.0/(textureOri.a+0.000001));
    greyColor = texture2D(inputImageTexture3, vec2(xCoordinate, yCoordinate));
    layerColor = texture2D(inputImageTexture4, vec2(xCoordinate, yCoordinate));

    // step1 curve  inputImageTexture2
    redCurveValue = texture2D(inputImageTexture2, vec2(textureColor.r, 0.0)).r;
    greenCurveValue = texture2D(inputImageTexture2, vec2(textureColor.g, 0.0)).g;
    blueCurveValue = texture2D(inputImageTexture2, vec2(textureColor.b, 0.0)).b;

    // step2 curve with mask 
    textureColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);

    redCurveValue = texture2D(inputImageTexture2, vec2(textureColor.r, 0.0)).a;
    greenCurveValue = texture2D(inputImageTexture2, vec2(textureColor.g, 0.0)).a;
    blueCurveValue = texture2D(inputImageTexture2, vec2(textureColor.b, 0.0)).a;

    lowp vec4 textureColor2 = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);

    // step3 screen with 60%
    lowp vec4 base = vec4(mix(textureColor.rgb, textureColor2.rgb, 1.0 - greyColor.r), textureColor.a);
    lowp vec4 overlayer = vec4(layerColor.r, layerColor.g, layerColor.b, 1.0);

    // screen blending
    textureColor = 1.0 - ((1.0 - base) * (1.0 - overlayer));
    textureColor = (textureColor - base) * 0.6 + base;

    redCurveValue = texture2D(inputImageTexture2, vec2(textureColor.r, 1.0)).r;
    greenCurveValue = texture2D(inputImageTexture2, vec2(textureColor.g, 1.0)).g;
    blueCurveValue = texture2D(inputImageTexture2, vec2(textureColor.b, 1.0)).b;
    textureColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0)*resultAlpha;

    gl_FragColor = mix(textureOri, textureColor, strength);
}