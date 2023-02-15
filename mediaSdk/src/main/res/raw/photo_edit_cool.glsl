varying highp vec2 textureCoordinate;
precision highp float;

uniform sampler2D inputImageTexture;
uniform sampler2D curve;
uniform float strength;

void main()
{
    lowp vec4 textureColor;
    lowp vec4 textureColorOri;

    highp float redCurveValue;
    highp float greenCurveValue;
    highp float blueCurveValue;

    textureColor = texture2D(inputImageTexture, textureCoordinate);
    textureColorOri = textureColor;
    float resultAlpha=textureColor.a*(1.0/(textureColor.a+0.00001));
    redCurveValue = texture2D(curve, vec2(textureColor.r, 0.0)).r;
    greenCurveValue = texture2D(curve, vec2(textureColor.g, 0.0)).g;
    blueCurveValue = texture2D(curve, vec2(textureColor.b, 0.0)).b;

    redCurveValue = texture2D(curve, vec2(redCurveValue, 0.0)).a;
    greenCurveValue = texture2D(curve, vec2(greenCurveValue, 0.0)).a;
    blueCurveValue = texture2D(curve, vec2(blueCurveValue, 0.0)).a;

    redCurveValue = redCurveValue * 1.25 - 0.12549;
    greenCurveValue = greenCurveValue * 1.25 - 0.12549;
    blueCurveValue = blueCurveValue * 1.25 - 0.12549;

    textureColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);
    textureColor = (textureColorOri - textureColor) * 0.549 + textureColor;

    gl_FragColor = mix(textureColorOri, textureColor, strength)*resultAlpha;
} 
  