varying highp vec2 textureCoordinate;
precision highp float;

uniform sampler2D inputImageTexture;
uniform sampler2D curve;

uniform sampler2D grey1Frame;
uniform sampler2D grey2Frame;
uniform sampler2D grey3Frame;
uniform float strength;

void main()
{
    lowp vec4 textureColor;
    lowp vec4 originalTextureColor;
    lowp vec4 textureColorOri;
    float xCoordinate = textureCoordinate.x;
    float yCoordinate = textureCoordinate.y;

    highp float redCurveValue;
    highp float greenCurveValue;
    highp float blueCurveValue;

    vec4 grey1Color;
    vec4 grey2Color;
    vec4 grey3Color;

    originalTextureColor = textureColor = texture2D(inputImageTexture, vec2(xCoordinate, yCoordinate));
    float resultAlpha=originalTextureColor.a*(1.0/(originalTextureColor.a+0.00001));

    grey1Color = texture2D(grey1Frame, vec2(xCoordinate, yCoordinate));
    grey2Color = texture2D(grey2Frame, vec2(xCoordinate, yCoordinate));
    grey3Color = texture2D(grey3Frame, vec2(xCoordinate, yCoordinate));

    mediump vec4 overlay = vec4(0, 0, 0, 1.0);
    mediump vec4 base = textureColor;

    mediump float ra;
    if (base.r < 0.5)
    {
        ra = overlay.r * base.r * 2.0;
    }
    else
    {
        ra = 1.0 - ((1.0 - base.r) * (1.0 - overlay.r) * 2.0);
    }

    mediump float ga;
    if (base.g < 0.5)
    {
        ga = overlay.g * base.g * 2.0;
    }
    else
    {
        ga = 1.0 - ((1.0 - base.g) * (1.0 - overlay.g) * 2.0);
    }

    mediump float ba;
    if (base.b < 0.5)
    {
        ba = overlay.b * base.b * 2.0;
    }
    else
    {
        ba = 1.0 - ((1.0 - base.b) * (1.0 - overlay.b) * 2.0);
    }

    textureColor = vec4(ra, ga, ba, 1.0);
    base = (textureColor - base) * (grey1Color.r*0.1019) + base;

    textureColor = vec4(base.r, base.g, base.b, 1.0);
    mediump vec4 textureColor2 = vec4(0.098, 0.0, 0.1843, 1.0);
    textureColor2 = textureColor + textureColor2 - (2.0 * textureColor2 * textureColor);

    textureColor = (textureColor2 - textureColor) * 0.6 + textureColor;

    redCurveValue = texture2D(curve, vec2(textureColor.r, 0.0)).r;
    greenCurveValue = texture2D(curve, vec2(textureColor.g, 0.0)).g;
    blueCurveValue = texture2D(curve, vec2(textureColor.b, 0.0)).b;

    textureColorOri = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);
    textureColor = (textureColorOri - textureColor) * grey2Color.r + textureColor;

    redCurveValue = texture2D(curve, vec2(textureColor.r, 1.0)).r;
    greenCurveValue = texture2D(curve, vec2(textureColor.g, 1.0)).g;
    blueCurveValue = texture2D(curve, vec2(textureColor.b, 1.0)).b;

    textureColorOri = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);
    textureColor = (textureColorOri - textureColor) * (grey3Color.r) * 1.0 + textureColor;


    overlay = vec4(0.6117, 0.6117, 0.6117, 1.0);
    base = textureColor;
    if (base.r < 0.5)
    {
        ra = overlay.r * base.r * 2.0;
    }
    else
    {
        ra = 1.0 - ((1.0 - base.r) * (1.0 - overlay.r) * 2.0);
    }

    if (base.g < 0.5)
    {
        ga = overlay.g * base.g * 2.0;
    }
    else
    {
        ga = 1.0 - ((1.0 - base.g) * (1.0 - overlay.g) * 2.0);
    }

    if (base.b < 0.5)
    {
        ba = overlay.b * base.b * 2.0;
    }
    else
    {
        ba = 1.0 - ((1.0 - base.b) * (1.0 - overlay.b) * 2.0);
    }

    textureColor = vec4(ra, ga, ba, 1.0);
    base = (textureColor - base) + base;

    textureColor = vec4(base.r, base.g, base.b, 1.0);
    textureColor2 = vec4(0.113725, 0.0039, 0.0, 1.0);
    textureColor2 = textureColor + textureColor2 - (2.0 * textureColor2 * textureColor);

    base = (textureColor2 - textureColor) * 0.3 + textureColor;
    redCurveValue = texture2D(curve, vec2(base.r, 1.0)).a;
    greenCurveValue = texture2D(curve, vec2(base.g, 1.0)).a;
    blueCurveValue = texture2D(curve, vec2(base.b, 1.0)).a;

    base = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);
    overlay = vec4(1.0, 1.0, 1.0, 1.0);

    textureColor = 1.0 - ((1.0 - base) * (1.0 - overlay));
    textureColor = (textureColor - base) * 0.05098 + base;

    gl_FragColor = mix(originalTextureColor, vec4(textureColor.r, textureColor.g, textureColor.b, 1.0)*resultAlpha, strength);
} 