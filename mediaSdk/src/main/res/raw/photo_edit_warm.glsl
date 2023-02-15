varying highp vec2 textureCoordinate;
precision highp float;

uniform sampler2D inputImageTexture;
uniform sampler2D curve;
uniform sampler2D greyFrame;
uniform sampler2D layerImage;
uniform float strength;
uniform float warmValue;

void main()
{
    lowp vec4 textureColor;
    lowp vec4 originalTextureColor;
    vec4 greyColor;
    vec4 layerColor;

    float xCoordinate = textureCoordinate.x;
    float yCoordinate = textureCoordinate.y;

    highp float redCurveValue;
    highp float greenCurveValue;
    highp float blueCurveValue;

	originalTextureColor = textureColor = texture2D(inputImageTexture, vec2(xCoordinate, yCoordinate));
    float resultAlpha=originalTextureColor.a*(1.0/(originalTextureColor.a+0.00001));
    greyColor = texture2D(greyFrame, vec2(xCoordinate, yCoordinate));
    layerColor = texture2D(layerImage, vec2(xCoordinate, yCoordinate));

    redCurveValue = texture2D(curve, vec2(textureColor.r, 0.0)).r;
    greenCurveValue = texture2D(curve, vec2(textureColor.g, 0.0)).g;
    blueCurveValue = texture2D(curve, vec2(textureColor.b, 0.0)).b;

    textureColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);

    redCurveValue = texture2D(curve, vec2(textureColor.r, 0.0)).a;
    greenCurveValue = texture2D(curve, vec2(textureColor.g, 0.0)).a;
    blueCurveValue = texture2D(curve, vec2(textureColor.b, 0.0)).a;

    lowp vec4 textureColor2 = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);

    lowp vec4 base = vec4(mix(textureColor.rgb, textureColor2.rgb, 1.0 - greyColor.r), textureColor.a);
    lowp vec4 overlayer = vec4(layerColor.r, layerColor.g, layerColor.b, 1.0);

    textureColor = 1.0 - ((1.0 - base) * (1.0 - overlayer));
    textureColor = (textureColor - base) * warmValue + base;

    redCurveValue = texture2D(curve, vec2(textureColor.r, 1.0)).r;
    greenCurveValue = texture2D(curve, vec2(textureColor.g, 1.0)).g;
    blueCurveValue = texture2D(curve, vec2(textureColor.b, 1.0)).b;
    textureColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, 1.0);

    gl_FragColor = mix(originalTextureColor, vec4(textureColor.r, textureColor.g, textureColor.b, 1.0)*resultAlpha, strength);
}